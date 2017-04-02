/*
 * Created on 2006-08-16
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io.init;

import java.util.List;

/**
 * Implementation of {@link EnemyAgentsGroupInitData} interface.
 *
 * @author M.Olszewski
 */
class EnemyAgentsGroupInitDataImpl implements EnemyAgentsGroupInitData
{
  /** List with {@link EnemyAgentInitData} objects. */
  private List<EnemyAgentInitData> enemyAgentsData;


  /**
   * Creates instance of {@link EnemyAgentsGroupInitDataImpl} class.
   *
   * @param enemyAgentsInitData - list with {@link EnemyAgentInitData} objects.
   */
  EnemyAgentsGroupInitDataImpl(List<EnemyAgentInitData> enemyAgentsInitData)
  {
    if (enemyAgentsInitData == null)
    {
      throw new NullPointerException("Specified enemyAgentsInitData is null!");
    }
    if (enemyAgentsInitData.size() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument enemyAgentsInitData - it must contain at least 1 element!");
    }

    enemyAgentsData = enemyAgentsInitData;
  }


  /**
   * @see net.java.dante.sim.io.init.EnemyAgentsGroupInitData#getEnemyAgentsCount()
   */
  public int getEnemyAgentsCount()
  {
    return enemyAgentsData.size();
  }

  /**
   * @see net.java.dante.sim.io.init.EnemyAgentsGroupInitData#getEnemyAgentInitData(int)
   */
  public EnemyAgentInitData getEnemyAgentInitData(int index)
  {
    return enemyAgentsData.get(index);
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + enemyAgentsData.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof EnemyAgentsGroupInitDataImpl))
    {
      final EnemyAgentsGroupInitDataImpl other = (EnemyAgentsGroupInitDataImpl) object;
      equal = (enemyAgentsData.equals(other.enemyAgentsData));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[enemyAgentsData=" + enemyAgentsData + "]");
  }
}