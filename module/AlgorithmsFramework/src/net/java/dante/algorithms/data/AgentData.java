/*
 * Created on 2006-08-31
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.data;

/**
 * Interface defining common agent's data.
 *
 * @author M.Olszewski
 */
public interface AgentData extends ObjectData
{
  /**
   * Gets maximum number of agent's hit points.
   *
   * @return Returns maximum number of agent's hit points.
   */
  int getMaxHitPoints();

  /**
   * Gets agent's sight range.
   *
   * @return Returns agent's sight range.
   */
  double getSightRange();

  /**
   * Gets agent's weapon data.
   *
   * @return Returns agent's weapon system.
   */
  WeaponData getWeapon();
}