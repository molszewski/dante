/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.time;

import junit.framework.TestCase;

/**
 * Test case for {@link TimeStopper} class.
 *
 * @author M.Olszewski
 */
public class TimeStopperTest extends TestCase
{

  /**
   * Test method for 
   * {@link net.java.dante.sim.engine.time.TimeStopper#TimeStopper(Timer, long)}.
   */
  public final void testTimeStopper()
  {
    // Correct cases
    for (int i = 1; i < 10000; i++)
    {
      new TimeStopper(new SystemTimer(), i);
    }
    
    SystemTimer timer = new SystemTimer();
    for (int i = 1; i < 10000; i++)
    {
      new TimeStopper(timer, i);
    }
    
    // Invalid cases
    try
    {
      new TimeStopper(null, -6);
      fail("NullPointerException expected!");
    }
    catch (NullPointerException e)
    {
      // Intentionally left empty.
    }
    
    try
    {
      new TimeStopper(null, 100);
      fail("NullPointerException expected!");
    }
    catch (NullPointerException e)
    {
      // Intentionally left empty.
    }
    
    try
    {
      new TimeStopper(new SystemTimer(), 0);
      fail("IllegalArgumentException expected!");
    }
    catch (IllegalArgumentException e)
    {
      // Intentionally left empty.
    }
    
    try
    {
      new TimeStopper(new SystemTimer(), -6);
      fail("IllegalArgumentException expected!");
    }
    catch (IllegalArgumentException e)
    {
      // Intentionally left empty.
    }
  }

  /**
   * Test method for {@link net.java.dante.sim.engine.time.TimeStopper#startPoint()}.
   */
  public final void testStartPoint()
  {
    TimeStopper stopper = new TimeStopper(new SystemTimer(), 100);
    // Just mark 'start point'
    for (int i = 0; i < 10000; i++)
    {
      stopper.startPoint();
    }
  }

  /**
   * Test method for {@link net.java.dante.sim.engine.time.TimeStopper#endPoint()}.
   */
  public final void testEndPoint()
  {
    TimeStopper stopper = new TimeStopper(new SystemTimer(), 100);
    // Just mark 'end point'
    for (int i = 0; i < 10000; i++)
    {
      stopper.endPoint();
    }
  }
  
  /**
   * Tests whole functionality of {@link TimeStopper} class for different
   * time intervals.
   */
  public final void testTimeStopperClass()
  {
    final int[] intervals = {1,       2,    5,  10,  15,  20,  50, 100, 1000};
    final int[] loops =     {1000, 1000, 1000, 500, 500, 500, 100, 100,   50};
    
    
    SystemTimer timer = new SystemTimer();
    for (int i = 0; i < intervals.length; i++)
    {
      TimeStopper stopper = new TimeStopper(timer, intervals[i]);
      timer.start();
      for (int j = 0, count = loops[i]; j < count; j++)
      {
        long t = -timer.getTime();
        stopper.startPoint();
        {
          // Intentionally left empty.
        }
        stopper.endPoint();
        t += timer.getTime();
        if (t < intervals[i])
        {
          fail("Interval is lesser than requested! Requested=" + intervals[i]);
        }
      }
      System.err.println("Interval " + intervals[i] + " - passed.");
    }
  }
}