/*
 * Created on 2006-07-17
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.time;

import junit.framework.TestCase;

/**
 * Test case for {@link UPSSystem} class.
 *
 * @author M.Olszewski
 */
public class UPSSystemTest extends TestCase
{

  /**
   * Test method for {@link net.java.dante.sim.engine.time.UPSSystem#getCurrentUPS()}.
   */
  public void testGetCurrentUPS()
  {
    final int DESIRED_UPS = 100;
    Timer t = new SystemTimer();
    UPS ups = new UPSSystem(t);
    ups.setUPS(DESIRED_UPS);
    long time = -t.getTime();
    for (int i = 0; i < 2000; i++)
    {
      ups.refresh();
      
      if (ups.getCurrentUPS() > ups.getUPS())
      {
        fail("Current UPS cannot be greater than desired UPS!");
      }
      
      ups.sleep();
    }
    time += t.getTime();
    System.err.println("time=" + time);
  }

}