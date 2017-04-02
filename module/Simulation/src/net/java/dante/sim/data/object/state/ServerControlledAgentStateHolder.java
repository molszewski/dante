/*
 * Created on 2006-08-07
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

import net.java.dante.sim.data.object.ServerWeaponSystem;

/**
 * Class holding data defined in {@link ServerControlledAgentState} interface.
 * 
 * @author M.Olszewski
 */
public class ServerControlledAgentStateHolder extends ControlledAgentStateHolder implements ServerControlledAgentState
{
  /** Agent's weapon system. */
  private ServerWeaponSystem weapon;
  
  
  /**
   * Creates instance of {@link ServerControlledAgentStateHolder} class holding data 
   * specified in {@link ServerControlledAgentState} interface.
   *
   * @param agentSightRange - agent's sight range.
   * @param agentMaxHitPoints - agent's max hit points.
   * @param agentMaxSpeed - agent's max speed.
   * @param weaponSystem - agent's weapon system.
   */
  public ServerControlledAgentStateHolder(double agentSightRange, 
      int agentMaxHitPoints, double agentMaxSpeed, 
      ServerWeaponSystem weaponSystem)
  {
    super(agentSightRange, agentMaxHitPoints, agentMaxSpeed);
    
    if (weaponSystem == null)
    {
      throw new NullPointerException("Specified weaponSystem is null!");
    }
    
    weapon = weaponSystem;
  }

  
  /**
   * @see net.java.dante.sim.data.object.state.ServerControlledAgentState#getWeapon()
   */
  public ServerWeaponSystem getWeapon()
  {
    return weapon;
  }
}