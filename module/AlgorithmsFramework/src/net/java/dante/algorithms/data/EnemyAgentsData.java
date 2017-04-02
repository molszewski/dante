/*
 * Created on 2006-08-31
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.data;

/**
 * Interface defining enemy agents data.
 *
 * @author M.Olszewski
 */
public interface EnemyAgentsData
{
  /**
   * Gets total number of enemies (including destroyed, visible and
   * not visible).
   *
   * @return Returns total number of enemies.
   */
  int getEnemiesCount();

  /**
   * Gets data for all enemy agents.
   *
   * @return Returns array with data for all enemy agents.
   */
  EnemyAgentData[] getEnemiesData();

  /**
   * Gets number of visible enemies.
   *
   * @return Returns number of visible enemies.
   */
  int getVisibleEnemiesCount();

  /**
   * Gets data for all visible enemy agents.
   *
   * @return Returns array with data for all visible enemy agents.
   */
  EnemyAgentData[] getVisibleEnemiesData();

  /**
   * Gets data for visible enemy agent from the specified index.
   *
   * @param index the specified enemy agent's index.
   *
   * @return Returns data for visible enemy agent from the specified index.
   */
  EnemyAgentData getVisibleEnemyData(int index);

  /**
   * Gets number of not visible enemies.
   *
   * @return Returns number of not visible enemies.
   */
  int getNotVisibleEnemiesCount();

  /**
   * Gets data for all not visible, but probably not destroyed enemy agents.
   *
   * @return Returns array with data for all not visible, but probably
   *         not destroyed enemy agents.
   */
  EnemyAgentData[] getNotVisibleEnemiesData();

  /**
   * Gets number of destroyed enemies.
   *
   * @return Returns number of destroyed enemies.
   */
  int getDestroyedEnemiesCount();
}