/*
 * Created on 2006-09-10
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.java.dante.sim.io.ClientInputData;

/**
 * Updates queue with blocking and non-blocking implementations.
 * Both implementations are thread safe.
 *
 * @author M.Olszewski
 */
public abstract class UpdatesQueue
{
  /**
   * Creates instance of {@link UpdatesQueue} class.
   */
  protected UpdatesQueue()
  {
    // Intentionally left empty.
  }


  /**
   * Puts the specified input data to updates queue.
   *
   * @param inputData the specified input data.
   */
  protected abstract void put(ClientInputData inputData);

  /**
   * Takes input data from the head of updates queue.
   *
   * @return Returns input data obtained from the head of updates queue
   *         or <code>null</code> if no data was available.
   */
  protected abstract ClientInputData take();

  /**
   * Clears updates queue.
   */
  protected abstract void clear();


  /**
   * Creates instance of {@link UpdatesQueue} class that waits for updates
   * to become available in {@link #take()} method.
   *
   * @return Returns created instance of {@link UpdatesQueue} class
   *         that waits for updates.
   */
  public static UpdatesQueue createBlockingQueue()
  {
    return new BlockingUpdatesQueue();
  }

  /**
   * Creates instance of {@link UpdatesQueue} class that does not wait for updates
   * to become available in {@link #take()} method - if no updates are available,
   * {@link #take()} will return immediately.
   *
   * @return Returns created instance of {@link UpdatesQueue} class
   *         that does not wait for updates.
   */
  public static UpdatesQueue createNonBlockingQueue()
  {
    return new NonBlockingUpdatesQueue();
  }


  /**
   * Blocking queue - each call of {@link UpdatesQueue#take()} method will
   * wait for update data become available.
   *
   * @author M.Olszewski
   */
  static class BlockingUpdatesQueue extends UpdatesQueue
  {
    private BlockingQueue<ClientInputData> queue =
        new LinkedBlockingQueue<ClientInputData>();

    /**
     * @see net.java.dante.algorithms.UpdatesQueue#clear()
     */
    @Override
    protected void clear()
    {
      queue.clear();
    }

    /**
     * @see net.java.dante.algorithms.UpdatesQueue#put(net.java.dante.sim.io.ClientInputData)
     */
    @Override
    protected void put(ClientInputData inputData)
    {
      try
      {
        queue.put(inputData);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }

    /**
     * @see net.java.dante.algorithms.UpdatesQueue#take()
     */
    @Override
    protected ClientInputData take()
    {
      ClientInputData data = null;
      try
      {
        data = queue.take();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }

      return data;
    }
  }

  /**
   * Non-blocking queue - calls to {@link UpdatesQueue#take()} method will not
   * wait for update data become available.
   *
   * @author M.Olszewski
   */
  static class NonBlockingUpdatesQueue extends UpdatesQueue
  {
    private Queue<ClientInputData> queue =
        new ConcurrentLinkedQueue<ClientInputData>();

    /**
     * @see net.java.dante.algorithms.UpdatesQueue#clear()
     */
    @Override
    protected void clear()
    {
      queue.clear();
    }

    /**
     * @see net.java.dante.algorithms.UpdatesQueue#put(net.java.dante.sim.io.ClientInputData)
     */
    @Override
    protected void put(ClientInputData inputData)
    {
      queue.offer(inputData);
    }

    /**
     * @see net.java.dante.algorithms.UpdatesQueue#take()
     */
    @Override
    protected ClientInputData take()
    {
      return queue.poll();
    }
  }
}