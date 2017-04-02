/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io;

/**
 * Class representing special data denoting situation where some group
 * of agents was eliminated completely. This data is generated by
 * server and can be provided as input for client.
 *
 * @author M.Olszewski
 */
public class GroupEliminatedSimulationData extends GroupSimulationData implements ServerOutputData, ClientInputData
{
  /**
   * Creates instance of {@link GroupEliminatedSimulationData} class with the
   * specified parameters.
   *
   * @param agentsGroupId - identifier of an agents group which was eliminated.
   */
  public GroupEliminatedSimulationData(int agentsGroupId)
  {
    super(agentsGroupId);
  }
}
