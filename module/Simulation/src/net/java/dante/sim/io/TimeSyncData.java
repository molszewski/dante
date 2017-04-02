/*
 * Created on 2006-07-15
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io;

/**
 * Class containing data with current simulation's time. It is used to
 * synchronize time between two connected simulations.
 *
 * @author M.Olszewski
 */
public class TimeSyncData implements ClientInputData, ServerOutputData
{
  /** Agents group identifier. */
  private int groupId;
  /** Simulation's time. */
  private long time;


  /**
   * Creates object of {@link TimeSyncData} class with specified time.
   *
   * @param agentsGroupId - agents group's identifier.
   * @param simTime - simulation's time.
   */
  public TimeSyncData(int agentsGroupId, long simTime)
  {
    if (agentsGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsGroupId - it must be positive integer or zero!");
    }

    groupId = agentsGroupId;
    time    = simTime;
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

  /**
   * Gets simulation's time.
   *
   * @return Returns simulation's time.
   */
  public long getTime()
  {
    return time;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + groupId;
    result = PRIME * result + (int) (time ^ (time >>> 32));

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof TimeSyncData))
    {
      final TimeSyncData other = (TimeSyncData) object;
      equal = ((groupId == other.groupId) &&
               (time == other.time));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[groupId=" + groupId + "; time=" + time + "]");
  }
}