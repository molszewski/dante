/*
 * Created on 2006-07-17
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.time;


/**
 * Class simplifying time management in {@link net.java.dante.sim.engine.Engine} subclasses.
 * Objects of this class are created with value of time delay which is counted
 * by enabled (check {@link #isEnabled()} method) time counters in method
 * {@link #isActionTime(long)}. This method also returns boolean value
 * indicating whether desired time delay has passed or not.
 * <p>Each timer is reusable - it can be reset (e.g. check {@link #reset()} method)
 * with current or new time delay.
 *
 * @author M.Olszewski
 */
public class TimeCounter
{
  /** Time counted by this {@link TimeCounter}. */
  private long elapsedTime;
  /** Time delay of this {@link TimeCounter}. */
  private long delay;
  /** Determines whether it is action time for this {@link TimeCounter}. */
  private boolean actionTime;
  /** Determines whether this {@link TimeCounter} is enabled or not. */
  private boolean enabled = true;

  /**
   * Creates instance of {@link TimeCounter} with specified time delay.
   *
   * @param timeDelay - time delay.
   */
  public TimeCounter(long timeDelay)
  {
    if (timeDelay < 0)
    {
      throw new IllegalArgumentException("Invalid argument timeDelay - it must be positive long integer or zero!");
    }

    delay = timeDelay;
  }


  /**
   * Checks whether time delay of this {@link TimeCounter} has passed and
   * it is time for some action. This method will always return <code>false</code>
   * for disabled {@link TimeCounter}.
   *
   * @param delta - delta time between last and current call to this method.
   *
   * @return Returns <code>true</code> if time delay has passed.
   */
  public boolean isActionTime(long delta)
  {
    if (enabled && !actionTime)
    {
      elapsedTime += delta;
      actionTime = (elapsedTime >= delay);
    }

    return (enabled && actionTime);
  }

  /**
   * Checks whether time delay of this {@link TimeCounter} has passed and
   * it is time for some action. This method will always return <code>false</code>
   * for disabled {@link TimeCounter}.
   *
   * @return Returns <code>true</code> if time delay has passed.
   */
  public boolean isActionTime()
  {
    return (enabled && actionTime);
  }

  /**
   * Refreshes time of this time counter by the specified amount of elapsed
   * time. Disabled time counters cannot be refreshed - this method will return
   * immediately. This method does not return value indicating whether
   * it is 'action time' - you must call {@link #isActionTime(long)} method
   * to check this condition.
   *
   * @param delta the specified elapsed time.
   */
  public void refresh(long delta)
  {
    if (enabled && !actionTime)
    {
      elapsedTime += delta;
      actionTime = (elapsedTime >= delay);
    }
  }

  /**
   * Resets this {@link TimeCounter} with set time delay.
   */
  public void reset()
  {
    actionTime = false;
    elapsedTime = 0;
  }

  /**
   * Resets this {@link TimeCounter} and sets specified time delay.
   *
   * @param newActionDelay - new time delay for this {@link TimeCounter}.
   */
  public void reset(long newActionDelay)
  {
    delay = newActionDelay;
    reset();
  }

  /**
   * Enables or disables this {@link TimeCounter}.
   *
   * @param enabledTimer - determines whether this {@link TimeCounter} should
   *        be enabled or disabled.
   */
  public void enable(boolean enabledTimer)
  {
    enabled = enabledTimer;
  }

  /**
   * Checks whether this {@link TimeCounter} is enabled or disabled.
   *
   * @return Returns <code>true</code> if this {@link TimeCounter} is enabled,
   *         <code>false</code> otherwise.
   */
  public boolean isEnabled()
  {
    return enabled;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[enabled=" + enabled +
        "; elapsedTime=" + elapsedTime + "; delay=" + delay +
        "; actionTime=" + actionTime +"]");
  }
}