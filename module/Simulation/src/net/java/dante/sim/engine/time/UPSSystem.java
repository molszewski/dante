/*
 * Created on 2006-07-17
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.time;

/**
 * Implementation of {@link UPS} interface using {@link Timer} for obtaining 
 * time and calculating proper number of milliseconds to sleep after each
 * update. 
 * Current UPS is refreshed once per second and average UPS is always 
 * equal to current UPS.
 *
 * @author M.Olszewski
 */
public final class UPSSystem implements UPS
{
  /** One second in milliseconds. */
  private static final long ONE_SECOND_MS = 1000;
  
  /** Desired number of updates per second. */
  private int desiredUPS;
  /** Update counter, reset after one second. */
  private int updateCount = 0;
  /** Real number of updates, refreshed every one second. */
  private int currentUPS;
  /** One second counter used to refresh {@link #currentUPS} variable. */
  private TimeCounter counter = new TimeCounter(ONE_SECOND_MS);
  
  /** Recent refresh time. */
  private long lastRefreshTime;
  /** Maximum allowed time for single update. */
  private long maxUpdateTime;
  
  /** Timer used by this {@link UPSSystem} object.  */
  private Timer timer;
  
 
  /**
   * Creates instance of {@link UPSSystem} using specified timer.
   * 
   * @param engineTimer - timer that will be used by this {@link UPSSystem} object.
   * 
   * @throws NullPointerException if <code>engineTimer</code> is <code>null</code>.
   */
  public UPSSystem(Timer engineTimer)
  {
    if (engineTimer == null)
    {
      throw new NullPointerException("Specified engineTimer is null!");
    }
    
    timer = engineTimer;
  }

  /** 
   * @see net.java.dante.sim.engine.time.UPS#getCurrentUPS()
   */
  public int getCurrentUPS()
  {
    return currentUPS;
  }
  
  /** 
   * @see net.java.dante.sim.engine.time.UPS#getAvgUPS()
   */
  public double getAvgUPS()
  {
    return getCurrentUPS();
  }

  /** 
   * @see net.java.dante.sim.engine.time.UPS#getUPS()
   */
  public int getUPS()
  {
    return desiredUPS;
  }

  /** 
   * @see net.java.dante.sim.engine.time.UPS#setUPS(int)
   */
  public void setUPS(int UPS)
  {
    if (UPS != desiredUPS)
    {
      desiredUPS = UPS;
      currentUPS = UPS;
      maxUpdateTime = 1000L / UPS;
      
      counter.reset();
      updateCount = 0;
      
      // restart timer
      if (timer.isRunning())
      {
        timer.stop();
      }
      timer.start();
    }
  }
  
  /** 
   * @see net.java.dante.sim.engine.time.UPS#refresh()
   */
  public long refresh()
  {
    long delta = timer.getTime() - lastRefreshTime;
    delta = (delta == 0)? 1 : delta;
        
    lastRefreshTime = timer.getTime();
    
    if (counter.isActionTime(delta))
    {
      currentUPS = updateCount;
      updateCount = 0;
      counter.reset();
    }
    
    return delta;
  }

  /** 
   * @see net.java.dante.sim.engine.time.UPS#sleep()
   */
  public void sleep()
  {
    timer.sleep(lastRefreshTime + maxUpdateTime - timer.getTime());
    updateCount++;
  }

  /** 
   * @see net.java.dante.sim.engine.time.UPS#start()
   */
  public void start()
  {
    timer.start();
  }

  /** 
   * @see net.java.dante.sim.engine.time.UPS#stop()
   */
  public void stop()
  {
    timer.stop();
  }
  
  /**
   * @see net.java.dante.sim.engine.time.UPS#getTime()
   */
  public long getTime()
  {
    return timer.getTime();
  }
}