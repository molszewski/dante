/*
 * Created on 2006-07-16
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.io;



/**
 * Class defining input for client's simulation.
 *
 * @author M.Olszewski
 */
public class ClientInput extends QueuedSimulationInput
{
  /**
   * Creates instance of {@link ClientInput} class.
   */
  public ClientInput()
  {
    // Intentionally left empty.
  }


  /** 
   * @see net.java.dante.sim.io.QueuedSimulationInput#filterInputData(net.java.dante.sim.io.InputData)
   */
  @Override
  protected boolean filterInputData(InputData inputData)
  {
    return ((inputData instanceof TimeSyncData) ||
            (inputData instanceof UpdateData) ||
            (inputData instanceof StatisticsData));
  }
}