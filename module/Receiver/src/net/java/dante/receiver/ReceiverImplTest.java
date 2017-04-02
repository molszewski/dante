/*
 * Created on 2006-08-22
 *
 * @author M.Olszewski
 */

package net.java.dante.receiver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

/**
 * Test case for {@link RunnableReceiver} class.
 *
 * @author M.Olszewski
 */
public class ReceiverImplTest extends TestCase
{
  /**
   * Messages processor for {@link #testPostMessage()} method.
   *
   * @author M.Olszewski
   */
  final class PostMessageTestProcessor implements MessagesProcessor
  {
    /** Time in which test message is processed. */
    static final int TEST_MESSAGE_PROCESSING_TIME = 50;
    /** Counts total number of messages processed by this processor. */
    AtomicInteger msgsCnt = new AtomicInteger();
    /** Counts number of messages being processed at the moment by this processor. */
    AtomicInteger processingMsgsCnt = new AtomicInteger();

    /**
     * @see net.java.dante.receiver.MessagesProcessor#processMessage(net.java.dante.receiver.ReceiverMessage)
     */
    public void processMessage(ReceiverMessage message)
    {
      processingMsgsCnt.incrementAndGet();

      if (message instanceof TestMessage)
      {
        try
        {
          Thread.sleep(TEST_MESSAGE_PROCESSING_TIME);
        }
        catch (InterruptedException e)
        {
          fail("Thread was interrupted - it cannot be!");
        }

        msgsCnt.incrementAndGet();
      }

      processingMsgsCnt.decrementAndGet();
    }

    /**
     * Gets number of messages being processed at this time.
     *
     * @return Returns number of messages being processed at this time.
     */
    int getMessagesProcessing()
    {
      return processingMsgsCnt.get();
    }

    /**
     * Gets number of all processed messages so far.
     *
     * @return Returns number of all processed messages so far.
     */
    int getProcessedMessagesCount()
    {
      return msgsCnt.get();
    }
  }

  /**
   * For tests purposes.
   *
   * @author M.Olszewski
   */
  final class TestMessage implements ReceiverMessage
  {
    // Intentionally left empty.
  }

  /**
   * Test method for {@link net.java.dante.receiver.RunnableReceiver#postMessage(ReceiverMessage)}.
   */
  public void testPostMessage()
  {
    final int messagesCount = 500;

    final PostMessageTestProcessor example = new PostMessageTestProcessor();
    final Receiver receiver = new RunnableReceiver();
    receiver.start(example);

    // Post messages in other thread
    new Thread() {

      public void run()
      {
        for (int i = 0; i < messagesCount; i++)
        {
          receiver.postMessage(new TestMessage());
          int messagesCnt = example.getMessagesProcessing();
          assertTrue((messagesCnt == 1) || (messagesCnt == 0));
        }
      }
    }.start();

    for (int i = 0, count = (messagesCount * 5) / 2; i < count; i++)
    {
      try
      {
        Thread.sleep(PostMessageTestProcessor.TEST_MESSAGE_PROCESSING_TIME/2);
      }
      catch (InterruptedException e)
      {
        fail("Thread was interrupted - it cannot be!");
      }

      int messagesCnt = example.getMessagesProcessing();
      assertTrue((messagesCnt == 1) || (messagesCnt == 0));
    }

    receiver.dispose(true);

    assertEquals(example.getProcessedMessagesCount(), messagesCount);
  }

  /**
   * Messages processor for {@link #testDispose()} method.
   *
   * @author M.Olszewski
   */
  final class DisposeMessageTestProcessor implements MessagesProcessor
  {
    /** Indicates whether processing of messages is allowed. */
    private AtomicBoolean messagesAllowed = new AtomicBoolean(true);
    /** Indicates whether {@link FinalTestMessage} message was received. */
    private AtomicBoolean finalMsgReceived = new AtomicBoolean();

    /**
     * @see net.java.dante.receiver.MessagesProcessor#processMessage(net.java.dante.receiver.ReceiverMessage)
     */
    public void processMessage(ReceiverMessage message)
    {
      if (!messagesAllowed.get())
      {
        fail("Messages processing is not allowed! Test failed!");
      }
      else
      {
        if (message instanceof FinalTestMessage)
        {
          finalMsgReceived.set(true);
        }
      }
    }

    /**
     * Allows messages processing.
     */
    void allowProcessing()
    {
      messagesAllowed.set(true);
    }

    /**
     * Disallows messages processing.
     */
    void disallowProcessing()
    {
      messagesAllowed.set(false);
    }

    /**
     * Checks whether {@link FinalTestMessage} message was received.
     *
     * @return Returns <code>true</code> if {@link FinalTestMessage}
     *         message was received, <code>false</code> otherwise.
     */
    boolean isFinalMessageReceived()
    {
      return finalMsgReceived.get();
    }
  }

  /**
   * For tests purposes.
   *
   * @author M.Olszewski
   */
  final class FinalTestMessage implements ReceiverMessage
  {
    // Intentionally left empty.
  }

  /**
   * Test method for {@link net.java.dante.receiver.RunnableReceiver#dispose(boolean)}.
   */
  public void testDispose()
  {
    final int messagesCount = 1000;

    final DisposeMessageTestProcessor processor = new DisposeMessageTestProcessor();
    final Receiver receiver = new RunnableReceiver();
    receiver.start(processor);

    // Post messages in other thread
    new Thread() {

      public void run()
      {
        for (int i = 0; i < messagesCount; i++)
        {
          receiver.postMessage(new TestMessage());
        }
        // send 'final message'..
        receiver.postMessage(new FinalTestMessage());
      }
    }.start();

    // ..and wait for it
    while (!processor.isFinalMessageReceived())
    {
      try
      {
        Thread.sleep(50);
      }
      catch (InterruptedException e)
      {
        fail("Thread was interrupted - it cannot be!");
      }
    }

    // Disallow any processing!
    processor.disallowProcessing();

    // Call dispose many times
    for (int i = 0; i < messagesCount; i++)
    {
      receiver.dispose(true);
    }

    for (int i = 0; i < messagesCount; i++)
    {
      receiver.postMessage(new TestMessage());
    }
  }
}