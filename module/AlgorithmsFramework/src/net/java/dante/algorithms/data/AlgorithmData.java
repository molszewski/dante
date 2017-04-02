/*
 * Created on 2006-08-31
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.data;

import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.object.ObjectSize;

/**
 * Interface defining data for algorithms.
 *
 * @author M.Olszewski
 */
public interface AlgorithmData
{
  /**
   * Gets agents group identifier, identifying group in the simulation
   * environment.
   *
   * @return Returns agents group identifier.
   */
  int getGroupId();

  /**
   * Gets simulation's map for this algorithm.
   *
   * @return Returns simulation's map for this algorithm.
   */
  SimulationMap getMap();

  /**
   * Gets size of each tile on the map.
   *
   * @return Returns size of each tile on the map.
   */
  ObjectSize getTileSize();

  /**
   * Gets data for enemy agents.
   *
   * @return Returns data for enemy agents.
   */
  EnemyAgentsData getEnemiesData();

  /**
   * Gets data for controlled agents.
   *
   * @return Returns data for controlled agents.
   */
  ControlledAgentsData getControlledData();

  /**
   * Gets data for visible projectiles.
   *
   * @return Returns data for visible projectiles.
   */
  ProjectilesData getProjectilesData();

  /**
   * Gets time received most recently.
   *
   * @return Returns time received most recently.
   */
  long getMostRecentTime();

  /**
   * Refreshes this algorithm's data using data obtained externally.
   * This interface does not denote how should external source of data
   * look like and how refresh process should be performed.
   *
   * @return This method should return <code>true</code> if refresh process
   *         altered algorithm's data in any way or <code>false</code>
   *         otherwise.
   */
  boolean refresh();

  /**
   * Gets the best refresh rate, in milliseconds.
   *
   * @return Returns the best refresh rate, in milliseconds.
   */
  long getBestRefreshRate();
}