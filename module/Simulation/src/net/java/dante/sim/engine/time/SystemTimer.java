/*
 * Created on 2006-07-16
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.time;

/**
 * System timer, using JDK 1.5 method {@link System#nanoTime()} for best 
 * precision and results.
 *
 * @author M.Olszewski
 */
public final class SystemTimer implements Timer
{
  /** Constant value used in conversion. */
  private static final long NANO_TO_MS = 1000000L;
  
  /** Reference time (in nanoseconds). */
  private long referenceTime = 0;
  /** Variable indicating whether timer was started. */
  private boolean started = false;

  
  /** 
   * @see net.java.dante.sim.engine.time.Timer#start()
   */
  public void start()
  {
    if (!started)
    {
      referenceTime = System.nanoTime();
      started = true;
    }
  }

  /** 
   * @see net.java.dante.sim.engine.time.Timer#stop()
   */
  public void stop()
  {
    if (started)
    {
      referenceTime = 0;
      started = false;
    }
  }
  
  /** 
   * @see net.java.dante.sim.engine.time.Timer#isRunning()
   */
  public boolean isRunning()
  {
    return started;
  }

  /** 
   * @see net.java.dante.sim.engine.time.Timer#sleep(long)
   */
  public void sleep(long duration)
  {
    if (started && (duration > 0))
    {
      // More accurate method than Thread.sleep(duration):
      long sleepTime = System.nanoTime() + duration * NANO_TO_MS;
      while (System.nanoTime() <= sleepTime)
      {
        Thread.yield();
      }
    }
  }

  /** 
   * @see net.java.dante.sim.engine.time.Timer#getTime()
   */
  public long getTime()
  {
    long time = 0;
    if (started)
    {
      time = (System.nanoTime() - referenceTime)/NANO_TO_MS;
    }
    return time;
  }
}