/*
 * Created on 2006-09-01
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms;


import net.java.dante.algorithms.data.ControlledAgentData;
import net.java.dante.algorithms.data.WeaponData;
import net.java.dante.sim.data.object.ObjectSize;

/**
 * Implementation of {@link net.java.dante.algorithms.data.ControlledAgentData} interface.
 *
 * @author M.Olszewski
 */
class ControlledAgentDataImpl extends AbstractAgentData implements ControlledAgentData
{
  /** Agent's hit points. */
  private int hitPoints;
  /** Agent's blocked status. */
  private boolean blocked;


  /**
   * Creates instance of {@link ControlledAgentDataImpl} class.
   *
   * @param objectTypeName name of object's type.
   * @param objectIdentifier object's identifier.
   * @param objectSize object's size.
   * @param maxObjectSpeed maximum object's speed.
   * @param maxHitPoints agent's max hit points.
   * @param maxSightRange agent's sight range.
   * @param weaponSystem agent's weapon system.
   */
  ControlledAgentDataImpl(String objectTypeName, int objectIdentifier,
                          ObjectSize objectSize, double maxObjectSpeed,
                          int maxHitPoints, double maxSightRange,
                          WeaponData weaponSystem)
  {
    super(objectTypeName, objectIdentifier, objectSize, maxObjectSpeed,
          maxHitPoints, maxSightRange, weaponSystem);

    hitPoints = maxHitPoints;
  }


  /**
   * @see net.java.dante.algorithms.data.ControlledAgentData#getHitPoints()
   */
  public int getHitPoints()
  {
    return hitPoints;
  }

  /**
   * Sets number of agent's current hit points. If specified value exceeds
   * agent's maximum hit points (which can be obtained by call to
   * {@link #getMaxHitPoints()} method), agent's current hit points
   * must be set to value of maximum hit points.
   *
   * @param currentHitPoints - new number of agent's current hit points to set.
   */
  public void setCurrentHitPoints(int currentHitPoints)
  {
    if (currentHitPoints <= getMaxHitPoints())
    {
      hitPoints = currentHitPoints;
    }
    else
    {
      hitPoints = getMaxHitPoints();
    }
  }

  /**
   * @see net.java.dante.algorithms.data.ControlledAgentData#isBlocked()
   */
  public boolean isBlocked()
  {
    return blocked;
  }

  /**
   * This method should be invoked if agent is considered as blocked.
   */
  void blocked()
  {
    blocked = true;
  }

  /**
   * This method should be invoked if agent is considered as not blocked.
   */
  void unblocked()
  {
    blocked = false;
  }

  /**
   * @see net.java.dante.algorithms.AbstractAgentData#toString()
   */
  @Override
  public String toString()
  {
    final String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) +
        "; hitPoints=" + hitPoints + "; blocked=" + blocked + "]");
  }
}