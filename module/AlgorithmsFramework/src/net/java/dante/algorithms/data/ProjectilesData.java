/*
 * Created on 2006-08-31
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.data;

/**
 * Interface defining data only for projectiles that are
 * in range of agents controlled by algorithm.
 *
 * @author M.Olszewski
 */
public interface ProjectilesData
{

  /**
   * Gets total number of projectiles.
   *
   * @return Returns total number of projectiles.
   */
  int getProjectilesCount();

  /**
   * Gets data of all projectiles.
   *
   * @return Returns array with data for all projectiles.
   */
  ProjectileData[] getProjectilesData();
}