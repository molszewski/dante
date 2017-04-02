/*
 * Created on 2006-08-07
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

import net.java.dante.sim.data.object.ServerWeaponSystem;

/**
 * Interface defining set of controlled agent attributes at the server side,
 * containing weapon system specific for this agent.
 * 
 * @author M.Olszewski
 */
public interface ServerControlledAgentState extends ControlledAgentState
{
  /**
   * Gets server agent's weapon system.
   * 
   * @return Returns controlled agent's weapon system.
   */
  ServerWeaponSystem getWeapon();
}