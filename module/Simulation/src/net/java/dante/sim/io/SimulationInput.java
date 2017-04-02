/*
 * Created on 2006-07-04
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.io;

/**
 * Interface representing entry point for {@link net.java.dante.sim.Simulation} object.
 * Its method is invoked when data is ready to be received and processed
 * by {@link net.java.dante.sim.Simulation} object.
 *
 * @author M.Olszewski
 */
public interface SimulationInput
{
  /**
   * Invoked externally when data for {@link net.java.dante.sim.Simulation} was received and is 
   * ready to be processed.
   * 
   * @param data - received data.
   */
  public void dataReceived(InputData data);
}