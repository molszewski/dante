/*
 * Created on 2006-07-17
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Abstract class implementing {@link net.java.dante.sim.io.CachedSimulationInput} interface,
 * using queue as a cache for objects of {@link InputData} implementations.
 * It also delivers convenience method ({@link #filterInputData(InputData)})
 * for checking input data before adding it to queue.
 *
 * @author M.Olszewski
 */
public abstract class QueuedSimulationInput implements CachedSimulationInput
{
  /** Queue caching input data. */
  private Queue<InputData> inputQueue = new ConcurrentLinkedQueue<InputData>();

  /**
   * @see net.java.dante.sim.io.SimulationInput#dataReceived(net.java.dante.sim.io.InputData)
   */
  public void dataReceived(InputData inputData)
  {
    if (inputData == null)
    {
      throw new NullPointerException("Specified inputData is null!");
    }

    if (filterInputData(inputData))
    {
      inputQueue.add(inputData);
    }
  }

  /**
   * @see net.java.dante.sim.io.CachedSimulationInput#retrieveInput()
   */
  public InputData retrieveInput()
  {
    return inputQueue.poll();
  }

  /**
   * @see net.java.dante.sim.io.CachedSimulationInput#dispose()
   */
  public void dispose()
  {
    inputQueue.clear();
  }

  /**
   * Filters received input data. This method should return <code>true</code>
   * if specified object of {@link InputData} implementation should be
   * added to this {@link QueuedSimulationInput}.
   *
   * @param inputData - object of {@link InputData} implementation to filter.
   *
   * @return Returns <code>true</code> if specified {@link InputData} should
   *         be processed, <code>false</code> otherwise.
   */
  protected abstract boolean filterInputData(InputData inputData);
}