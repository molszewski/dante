/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io;


/**
 * Class representing special input data for each type of simulation denoting
 * situation where some group of agents abandoned simulation for some reason.
 *
 * @author M.Olszewski
 */
public class GroupAbandonSimulationData extends GroupSimulationData implements ClientInputData, ServerInputData
{
  /**
   * Creates instance of {@link GroupAbandonSimulationData} class with the
   * specified parameters.
   *
   * @param agentsGroupId - identifier of an agents group which abandoned simulation.
   */
  public GroupAbandonSimulationData(int agentsGroupId)
  {
    super(agentsGroupId);
  }
}