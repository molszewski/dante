/*
 * Created on 2006-08-12
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d.server;

/**
 * Class holding current time.
 *
 * @author M.Olszewski
 */
final class CurrentTimeHolder
{
  /** Time of current update used to generate events. */
  private long currentUpdateTime = 0;

  
  /**
   * Creates instance of {@link CurrentTimeHolder} class.
   */
  CurrentTimeHolder()
  {
    // Intentionally left empty.
  }
  
  
  /**
   * Updates current time.
   * 
   * @param delta - time elapsed since last update.
   */
  void update(long delta)
  {
    currentUpdateTime += delta;
  }
  
  /**
   * Gets current time.
   * 
   * @return Returns current time.
   */
  long getCurrentTime()
  {
    return currentUpdateTime;
  }
}