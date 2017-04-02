/*
 * Created on 2006-07-17
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.time;

/**
 * Interface for classes representing systems delivering constant number
 * of updates per second.
 *
 * @author M.Olszewski
 */
public interface UPS
{
  /**
   * Sets desired updates per second rate.
   * 
   * @param UPS - desired updates per second rate.
   */
  void setUPS(int UPS);
  
  /**
   * Gets desired updates per second rate.
   * 
   * @return Returns desired updates per second rate.
   */
  int getUPS();
  
  /**
   * Gets real number of updates per second rate.
   * 
   * @return Returns real number of updates per second rate.
   */
  int getCurrentUPS();
  
  /**
   * Gets average number of updates per second rate. 
   * This operation must at least return the same value as 
   * {@link #getCurrentUPS()} method.
   * 
   * @return Returns average number of updates per second rate.
   */
  double getAvgUPS();
  
  /**
   * Starts this {@link UPS}.
   */
  void start();
  
  /**
   * Stops this {@link UPS}.
   */
  void stop();

  /**
   * Refreshes this {@link UPS}. This method should be called before
   * each update and return delta of time between recent and this updates.
   * 
   * @return Returns delta between current and recent updates.
   */
  long refresh();
  
  /**
   * Sleeps for number of milliseconds that will allow to maintain 
   * specified UPS. This method should be called after each update.
   */
  void sleep();
  
  /**
   * Gets current time of this UPS.
   * 
   * @return Returns current time of this UPS.
   */
  long getTime();
}