/*
 * Created on 2006-07-16
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.time;

/**
 * Interface for all classes that counts passing time.
 *
 * @author M.Olszewski
 */
public interface Timer
{
  /**
   * Starts timer. If timer was started, this method should return immediately.
   */
  void start();
  
  /**
   * Stops started timer. If timer was already stopped or never started, 
   * this method should return immediately.
   */
  void stop();
  
  /**
   * Checks whether this {@link Timer} is currently running.
   * 
   * @return Returns <code>true</code> if timer is running, <code>false</code>
   *         otherwise.
   */
  boolean isRunning();
  
  /**
   * Sleeps current thread for specified duration of time (in milliseconds).
   * Implementations must work only if timer was started.
   * 
   * @param duration - duration of sleep. If this parameter is negative long
   *        integer, this method should immediately return.
   */
  void sleep(long duration);
  
  /**
   * Gets current time (in milliseconds) that passed since last 
   * call to {@link #start()} method. If timer was stopped or never started,
   * this method must return zero.
   * 
   * @return Returns current time (in milliseconds) that passed since last 
   *         call to {@link #start()} method.
   */
  long getTime();
}