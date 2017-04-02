/*
 * Created on 2006-08-28
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io;

/**
 * Base class for simulation data classes that are representing data
 * for agents group with the specified identifier.
 * 
 * @author M.Olszewski
 */
public abstract class GroupSimulationData
{
  /** Identifier of an agents group for which this data was generated. */
  private int groupId;


  /**
   * Creates instance of {@link GroupSimulationData} class with the
   * specified parameters.
   *
   * @param agentsGroupId - identifier of an agents group for which this data
   *        was generated.
   */
  public GroupSimulationData(int agentsGroupId)
  {
    if (agentsGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsGroupId - it must be zero or positive integer!");
    }

    groupId = agentsGroupId;
  }


  /**
   * Gets agents group's identifier.
   *
   * @return Returns agents group's identifier.
   */
  public int getGroupId()
  {
    return groupId;
  }
}