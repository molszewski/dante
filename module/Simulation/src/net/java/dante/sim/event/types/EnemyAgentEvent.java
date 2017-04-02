/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.Event;


/**
 * Generic interface defining set of parameters for events regarding
 * enemy agents.
 *
 * @author M.Olszewski
 */
public interface EnemyAgentEvent extends Event
{
  /**
   * Gets enemy agent's identifier.
   * 
   * @return Returns enemy agent's identifier.
   */
  int getEnemyAgentId();
}