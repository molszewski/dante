/*
 * Created on 2006-07-31
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.Event;


/**
 * Generic interface defining set of parameters for events regarding
 * friendly agents.
 * 
 * @author M.Olszewski
 */
public interface FriendlyAgentEvent extends Event
{
  /**
   * Gets friendly agent's identifier.
   * 
   * @return Returns friendly agent's identifier.
   */
  int getFriendlyAgentId();
}