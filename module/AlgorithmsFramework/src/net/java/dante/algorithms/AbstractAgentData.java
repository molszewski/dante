/*
 * Created on 2006-09-01
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms;


import net.java.dante.algorithms.data.AgentData;
import net.java.dante.algorithms.data.WeaponData;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.object.state.AgentStateHolder;

/**
 * Implementation of {@link net.java.dante.algorithms.data.AgentData} interface.
 *
 * @author M.Olszewski
 */
abstract class AbstractAgentData extends AbstractObjectData implements AgentData
{
  /** Agent's weapon system */
  private WeaponData weapon;
  /** Agent's maximum hit points and sight range. */
  private AgentStateHolder agentHolder;


  /**
   * Creates instance of {@link AbstractAgentData} class.
   *
   * @param objectTypeName name of object's type.
   * @param objectIdentifier object's identifier.
   * @param objectSize object's size.
   * @param maxObjectSpeed maximum object's speed.
   * @param maxHitPoints agent's max hit points.
   * @param maxSightRange agent's sight range.
   * @param weaponSystem agent's weapon system.
   */
  AbstractAgentData(String objectTypeName,
                    int objectIdentifier,
                    ObjectSize objectSize,
                    double maxObjectSpeed,
                    int maxHitPoints,
                    double maxSightRange,
                    WeaponData weaponSystem)
  {
    super(objectTypeName, objectIdentifier,
          objectSize, maxObjectSpeed);

    agentHolder = new AgentStateHolder(maxSightRange, maxHitPoints, maxObjectSpeed);
    weapon      = weaponSystem;
  }


  /**
   * @see net.java.dante.algorithms.data.AgentData#getMaxHitPoints()
   */
  public int getMaxHitPoints()
  {
    return agentHolder.getMaxHitPoints();
  }

  /**
   * @see net.java.dante.algorithms.data.AgentData#getSightRange()
   */
  public double getSightRange()
  {
    return agentHolder.getSightRange();
  }

  /**
   * @see net.java.dante.algorithms.data.AgentData#getWeapon()
   */
  public WeaponData getWeapon()
  {
    return weapon;
  }

  /**
   * @see net.java.dante.algorithms.AbstractObjectData#toString()
   */
  @Override
  public String toString()
  {
    final String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) +
        "; maxHitPoints=" + agentHolder.getMaxHitPoints() +
        "; sightRange=" + agentHolder.getSightRange() +
        "; weapon=" + weapon + "]");
  }
}