/*
 * Created on 2006-08-07
 *
 * @author M.Olszewski 
 */


package net.java.dante.sim.data.object.state;

import net.java.dante.sim.data.object.ClientWeaponSystem;

/**
 * Class holding data defined in {@link FriendlyAgentState} interface.
 * 
 * @author M.Olszewski
 */
public class FriendlyAgentStateHolder extends ControlledAgentStateHolder implements FriendlyAgentState
{
  /** Agent's weapon system. */
  private ClientWeaponSystem weapon;
  
  
  /**
   * Creates instance of {@link ControlledAgentStateHolder} class holding data 
   * specified in {@link ControlledAgentState} interface.
   *
   * @param agentSightRange - agent's sight range.
   * @param agentMaxHitPoints - agent's max hit points.
   * @param agentMaxSpeed - agent's max speed.
   * @param weaponSystem - agent's weapon system.
   */
  public FriendlyAgentStateHolder(double agentSightRange, 
      int agentMaxHitPoints, double agentMaxSpeed, 
      ClientWeaponSystem weaponSystem)
  {
    super(agentSightRange, agentMaxHitPoints, agentMaxSpeed);
    
    if (weaponSystem == null)
    {
      throw new NullPointerException("Specified weaponSystem is null!");
    }
    
    weapon = weaponSystem;
  }

  
  /**
   * @see net.java.dante.sim.data.object.state.FriendlyAgentState#getWeapon()
   */
  public ClientWeaponSystem getWeapon()
  {
    return weapon;
  }

  /**
   * @see net.java.dante.sim.data.object.state.ControlledAgentStateHolder#toString()
   */
  @Override
  public String toString()
  {
    String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) +
        "; weaponSystem=" + weapon + "]");
  }
}