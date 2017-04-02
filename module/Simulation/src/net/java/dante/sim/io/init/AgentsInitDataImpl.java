/*
 * Created on 2006-08-16
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io.init;

import java.util.List;

/**
 * Implementation of {@link AgentsInitData} interface.
 *
 * @author M.Olszewski
 */
class AgentsInitDataImpl implements AgentsInitData
{
  /** List with {@link FriendlyAgentInitData} objects. */
  private List<FriendlyAgentInitData> friendlyAgentsData;
  /** List with {@link EnemyAgentsGroupInitData} objects. */
  private List<EnemyAgentsGroupInitData> enemyGroupsData;


  /**
   * Creates instance of {@link EnemyAgentsGroupInitDataImpl} class.
   *
   * @param friendlyAgentsInitData - list with {@link FriendlyAgentInitData} objects.
   * @param enemyGroupsInitData - list with {@link EnemyAgentsGroupInitData} objects.
   */
  AgentsInitDataImpl(List<FriendlyAgentInitData> friendlyAgentsInitData,
      List<EnemyAgentsGroupInitData> enemyGroupsInitData)
  {
    if (friendlyAgentsInitData == null)
    {
      throw new NullPointerException("Specified friendlyAgentsInitData is null!");
    }
    if (friendlyAgentsInitData.size() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument friendlyAgentsInitData - it must contain at least 1 element!");
    }
    if (enemyGroupsInitData == null)
    {
      throw new NullPointerException("Specified enemyGroupsInitData is null!");
    }
    if (enemyGroupsInitData.size() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument enemyGroupsInitData - it must contain at least 1 element!");
    }

    friendlyAgentsData = friendlyAgentsInitData;
    enemyGroupsData    = enemyGroupsInitData;
  }


  /**
   * @see net.java.dante.sim.io.init.AgentsInitData#getEnemyGroupsCount()
   */
  public int getEnemyGroupsCount()
  {
    return enemyGroupsData.size();
  }

  /**
   * @see net.java.dante.sim.io.init.AgentsInitData#getEnemyGroup(int)
   */
  public EnemyAgentsGroupInitData getEnemyGroup(int index)
  {
    return enemyGroupsData.get(index);
  }

  /**
   * @see net.java.dante.sim.io.init.AgentsInitData#getFriendlyAgentsCount()
   */
  public int getFriendlyAgentsCount()
  {
    return friendlyAgentsData.size();
  }

  /**
   * @see net.java.dante.sim.io.init.AgentsInitData#getFriendlyAgentInitData(int)
   */
  public FriendlyAgentInitData getFriendlyAgentInitData(int index)
  {
    return friendlyAgentsData.get(index);
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + enemyGroupsData.hashCode();
    result = PRIME * result + friendlyAgentsData.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof AgentsInitDataImpl))
    {
      final AgentsInitDataImpl other = (AgentsInitDataImpl) object;
      equal = ((enemyGroupsData.equals(other.enemyGroupsData)) &&
               (friendlyAgentsData.equals(other.friendlyAgentsData)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[enemyGroupsData=" + enemyGroupsData +
        "; friendlyAgentsData=" + friendlyAgentsData + "]");
  }
}