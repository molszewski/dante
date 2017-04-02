/*
 * Created on 2006-08-07
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

import net.java.dante.sim.data.object.ClientWeaponSystem;

/**
 * Interface defining set of controlled agent attributes at the client side,
 * containing weapon system specific for this agent.
 * 
 * @author M.Olszewski
 */
public interface FriendlyAgentState extends ControlledAgentState
{
  /**
   * Gets controlled agent's weapon system.
   * 
   * @return Returns controlled agent's weapon system.
   */
  ClientWeaponSystem getWeapon();
}