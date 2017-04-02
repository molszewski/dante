/*
 * Created on 2006-07-22
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io;


/**
 * Class defining input for server's simulation.
 *
 * @author M.Olszewski
 */
public class ServerInput extends QueuedSimulationInput
{
  /**
   * Creates instance of {@link ServerInput} class.
   */
  public ServerInput()
  {
    // Intentionally left empty.
  }


  /**
   * @see net.java.dante.sim.io.QueuedSimulationInput#filterInputData(net.java.dante.sim.io.InputData)
   */
  @Override
  protected boolean filterInputData(InputData inputData)
  {
    return ((inputData instanceof GroupAbandonSimulationData) ||
            (inputData instanceof CommandsData));
  }
}