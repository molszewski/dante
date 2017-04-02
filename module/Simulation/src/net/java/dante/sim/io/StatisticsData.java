/*
 * Created on 2006-08-14
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io;

import net.java.dante.sim.engine.engine2d.server.GroupStatistics;

/**
 * Class containing statistics for agents group.
 *
 * @author M.Olszewski
 */
public class StatisticsData implements ClientInputData, ServerOutputData
{
  /** Statistics for agents group. */
  private GroupStatistics groupStatistics;


  /**
   * Creates instance of {@link StatisticsData} class with the specified
   * parameters.
   *
   * @param agentsGroupStatistics - statistics for agents group.
   */
  public StatisticsData(GroupStatistics agentsGroupStatistics)
  {
    if (agentsGroupStatistics == null)
    {
      throw new NullPointerException("Specified agentsGroupStatistics is null!");
    }

    groupStatistics = agentsGroupStatistics;
  }

  /**
   * Gets statistics for the specified group.
   *
   * @return Returns statistics for the specified group.
   */
  public GroupStatistics getGroupStatistics()
  {
    return groupStatistics;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + groupStatistics.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof StatisticsData))
    {
      final StatisticsData other = (StatisticsData) object;
      equal = (groupStatistics.equals(other.groupStatistics));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[groupStatistics=" + groupStatistics + "]");
  }
}