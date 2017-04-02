/*
 * Created on 2006-08-22
 *
 * @author M.Olszewski
 */

package net.java.dante.receiver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of {@link Receiver} interface delivering messages for
 * {@link MessagesProcessor} object specified in this class constructor.
 * All posted messages are stored in blocking messages queue and
 * processed in separate thread.
 * Thread processing messages is started in constructor of this class
 * and can be stopped by call to {@link #dispose(boolean)} method.
 *
 * @author M.Olszewski
 */
public class RunnableReceiver implements Receiver, Runnable
{
  /** Indicates whether receiver thread is running. */
  private AtomicBoolean running = new AtomicBoolean();
  /** Messages queue. */
  private BlockingQueue<ReceiverMessage> messagesQueue =
      new LinkedBlockingQueue<ReceiverMessage>();
  /** Custom receiver messages processor. */
  private MessagesProcessor processor;
  /** Receiver thread. */
  private Thread receiverThread;


  /**
   * @see net.java.dante.receiver.Receiver#start(net.java.dante.receiver.MessagesProcessor)
   */
  public void start(MessagesProcessor messagesProcessor)
  {
    if (messagesProcessor == null)
    {
      throw new NullPointerException("Specified messagesProcessor is null!");
    }

    // Set messages processor
    processor = messagesProcessor;
    // Create and start thread
    receiverThread = new Thread(this);
    receiverThread.start();
  }

  /**
   * @see net.java.dante.receiver.Receiver#postMessage(net.java.dante.receiver.ReceiverMessage)
   */
  public void postMessage(ReceiverMessage message)
  {
    put(message);
  }

  /**
   * Puts specified message into messages queue.
   *
   * @param message - message to put.
   */
  private void put(ReceiverMessage message)
  {
    if (running.get())
    {
      try
      {
        messagesQueue.put(message);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }


  /**
   * Stops this thread and clears messages queue.
   *
   * @see net.java.dante.receiver.Receiver#dispose(boolean)
   */
  public void dispose(boolean wait)
  {
    // Perform disposal only once
    if (running.get())
    {
      // Clear queue - ThreadShutDownMessage must be the first one
      messagesQueue.clear();
      // Put 'queue shutdown' message
      put(new ThreadShutdownMessage());

      if (wait)
      {
        // Wait for thread to finish
        try
        {
          receiverThread.join();
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * @see java.lang.Runnable#run()
   */
  public void run()
  {
    running.set(true);

    while (true)
    {
      ReceiverMessage message = take();
      if (message != null)
      {
        // Special message? Quit thread and clear messages queue
        if (message instanceof ThreadShutdownMessage)
        {
          running.set(false);
          messagesQueue.clear();
          break;
        }

        try
        {
          processor.processMessage(message);
        }
        catch (Throwable e1)
        {
          try
          {
            // Send notification about exception to messages processor
            processor.processMessage(new MessageProcessingExceptionMessage(e1));
          }
          catch (Throwable e2)
          {
            // Intentionally left empty.
          }
        }
      }
    }
  }

  /**
   * Takes message from messages queue. This method blocks current thread
   * until message is taken from the queue.
   *
   * @return Returns taken message or <code>null</code> if taking procedure
   *         was interrupted.
   */
  private ReceiverMessage take()
  {
    ReceiverMessage message = null;
    try
    {
      message = messagesQueue.take();
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }

    return message;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass().toString());
  }


  /**
   * Special message indicating shutdown of thread receiving messages .
   *
   * @author M.Olszewski
   */
  final class ThreadShutdownMessage implements ReceiverMessage
  {
    // Intentionally left empty.
  }
}