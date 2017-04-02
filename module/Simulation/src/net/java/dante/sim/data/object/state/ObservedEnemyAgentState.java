/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

import net.java.dante.sim.data.object.ClientWeaponSystem;

/**
 * Interface defining set of enemy agent attributes: hits count, visible
 * status (in sight range or not) and operational status (alive or destroyed).
 * 
 * @author M.Olszewski
 */
public interface ObservedEnemyAgentState extends AgentState, ObservedObjectState
{
  /**
   * Gets controlled agent's weapon system.
   * 
   * @return Returns controlled agent's weapon system.
   */
  ClientWeaponSystem getWeapon();
  
  /**
   * Gets number of hits that specified enemy agent received.
   * 
   * @return Returns number of hits that specified enemy agent received.
   */
  int getHitsCount();
  
  /**
   * Adds specified number of hits that were received by enemy agent.
   * 
   * @param hitsNumber - additional number of hits received by enemy agent.
   */
  void addHitsCount(int hitsNumber);
}