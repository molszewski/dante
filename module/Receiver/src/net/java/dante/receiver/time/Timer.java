/*
 * Created on 2006-09-02
 *
 * @author M.Olszewski
 */

package net.java.dante.receiver.time;

/**
 * Timer interface.
 *
 * @author M.Olszewski
 */
public interface Timer
{
  /**
   * Schedules this timer to generate message after the specified
   * amount of milliseconds.
   *
   * @param delay - the specified amount of milliseconds.
   */
  void scheduleAfter(long delay);

  /**
   * Schedules this timer to generate message at the specified date
   *
   * @param date - the specified amount of milliseconds.
   */
  void scheduleAt(long date);

  /**
   * Cancels this timer. If this timer was not scheduled or it was scheduled
   * and fired, this method must return immediately.
   */
  void cancel();
}