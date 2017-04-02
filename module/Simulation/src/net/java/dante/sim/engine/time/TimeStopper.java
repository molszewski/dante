/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.time;

/**
 * Time stopper class assures that time between two places in source code
 * will be at least equal to value specified in constructor of this class.
 * Please remember, that each call to {@link #startPoint()} method
 * must be followed by call to {@link #endPoint()} method. Otherwise
 * time stopper will not work properly.
 *
 * @author M.Olszewski
 */
public class TimeStopper
{
  /** Timer used by this {@link TimeStopper} object. */
  private Timer timer;
  /** Time at start point. */
  private long startPointTime;
  /** Minimum time interval between start and end points. */
  private final long interval;


  /**
   * Creates instance of {@link TimeStopper} with the specified parameters
   * and starts the specified timer.
   *
   * @param timeInterval desired minimum interval between start point and
   *        end point.
   * @param engineTimer timer that will be used by this {@link TimeStopper}
   *        object.
   */
  public TimeStopper(Timer engineTimer, long timeInterval)
  {
    this(engineTimer, timeInterval, true);
  }

  /**
   * Creates instance of {@link TimeStopper} with the specified parameters.
   *
   * @param engineTimer timer that will be used by this {@link TimeStopper}
   *        object.
   * @param timeInterval desired minimum interval between start point and
   *        end point.
   * @param startTimer determines whether the specified timer should be started.
   */
  public TimeStopper(Timer engineTimer, long timeInterval, boolean startTimer)
  {
    if (engineTimer == null)
    {
      throw new NullPointerException("Specified engineTimer is null!");
    }
    if (timeInterval <= 0)
    {
      throw new IllegalArgumentException("Invalid argument timeInterval - it must be a positive long integer!");
    }

    timer = engineTimer;
    interval = timeInterval;

    if (startTimer)
    {
      timer.start();
    }
  }


  /**
   * Marks start point. Current time at this point is stored.
   */
  public void startPoint()
  {
    startPointTime = timer.getTime();
  }

  /**
   * Marks end point. Difference between current time and start point's time
   * is calculated and if it is lesser than desired minimum time interval
   * between start and end points, current thread is put into sleep state
   * for calculated difference.
   */
  public void endPoint()
  {
    long difference = timer.getTime() - startPointTime;
    if (difference < interval)
    {
      timer.sleep(interval - difference);
    }
  }
}