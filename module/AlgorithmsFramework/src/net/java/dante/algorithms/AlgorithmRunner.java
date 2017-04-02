/*
 * Created on 2006-09-01
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class intended to run descendants of {@link BaseAlgorithmImpl} class
 * in separate threads.
 *
 * @author M.Olszewski
 */
public class AlgorithmRunner extends Thread
{
  /** Number of algorithms runners running. */
  private static final AtomicInteger RUNNERS_COUNT = new AtomicInteger();
  /** Prefix for runner's name. */
  private static final String RUNNER_NAME_PREFIX = AlgorithmRunner.class.getSimpleName() + "#";

  /** Algorithm to run. */
  private BaseAlgorithmImpl algorithm;


  /**
   * Creates instance of {@link AlgorithmRunner} class.
   *
   * @param algorithmToRun - algorithm to run.
   */
  public AlgorithmRunner(BaseAlgorithmImpl algorithmToRun)
  {
    super(RUNNER_NAME_PREFIX + RUNNERS_COUNT);

    RUNNERS_COUNT.incrementAndGet();

    if (algorithmToRun == null)
    {
      throw new NullPointerException("Specified algorithmToRun is null!");
    }

    algorithm = algorithmToRun;
  }


  /**
   * @see java.lang.Thread#run()
   */
  @Override
  public void run()
  {
    algorithm.startRunning();

    try
    {
      algorithm.runAlgorithm();

      // Algorithm finished normally
      algorithm.finished();
    }
    catch (Throwable e)
    {
      // Exception caught while running algorithm
      algorithm.exceptionCaught(e);
    }

    RUNNERS_COUNT.decrementAndGet();
  }
}