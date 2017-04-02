/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

import net.java.dante.sim.data.object.ClientWeaponSystem;

/**
 * Class holding data defined in {@link ObservedEnemyAgentState} interface, allowing
 * to modify number of enemy agent's hits number, visibility and operational 
 * statuses.
 * 
 * @author M.Olszewski
 */
public class ObservedEnemyAgentStateHolder extends AgentStateHolder implements ObservedEnemyAgentState
{
  /** Agent's weapon system. */
  private ClientWeaponSystem weapon;
  /** Observed agent state holder. */
  private ObservedObjectStateHolder observedObjectHolder;
  /** Number of hits received by enemy agent. */
  private int hitsCount = 0;

  
  /**
   * Creates instance of {@link ObservedEnemyAgentStateHolder} class holding data 
   * specified in {@link ObservedEnemyAgentState} interface.
   * 
   * @param agentSightRange - agent's sight range.
   * @param agentMaxHitPoints - agent's max hit points.
   * @param agentMaxSpeed - agent's max speed.
   * @param weaponSystem - agent's weapon system.
   */
  public ObservedEnemyAgentStateHolder(double agentSightRange, 
      int agentMaxHitPoints, double agentMaxSpeed,
      ClientWeaponSystem weaponSystem)
  {
    super(agentSightRange, agentMaxHitPoints, agentMaxSpeed);
    
    if (weaponSystem == null)
    {
      throw new NullPointerException("Specified weaponSystem is null!");
    }
    
    weapon = weaponSystem;
    observedObjectHolder = new ObservedObjectStateHolder(false);
  }
  

  /**
   * @see net.java.dante.sim.data.object.state.ObservedEnemyAgentState#getWeapon()
   */
  public ClientWeaponSystem getWeapon()
  {
    return weapon;
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObservedEnemyAgentState#getHitsCount()
   */
  public int getHitsCount()
  {
    return hitsCount;
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ObservedEnemyAgentState#addHitsCount(int)
   */
  public void addHitsCount(int hitsNumber)
  {
    if (hitsNumber <= 0)
    {
      throw new IllegalArgumentException("Invalid argument hitsNumber - it must be positive integer!");
    }
    
    hitsCount += hitsNumber;
  }
  
  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#inSightRange()
   */
  public void inSightRange()
  {
    observedObjectHolder.inSightRange();
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#outOfSightRange()
   */
  public void outOfSightRange()
  {
    observedObjectHolder.outOfSightRange();
  }
  
  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#isVisible()
   */
  public boolean isVisible()
  {
    return observedObjectHolder.isVisible();
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#isAlive()
   */
  public boolean isAlive()
  {
    return observedObjectHolder.isAlive();
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#destroyed()
   */
  public void destroyed()
  {
    observedObjectHolder.destroyed();
  }

  /** 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) +
        "; weaponSystem=" + weapon + 
        "; observedState=" + observedObjectHolder + 
        "; hitsCount=" + hitsCount + "]");
  }
}