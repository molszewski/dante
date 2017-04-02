/*
 * Created on 2006-09-01
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms;


import net.java.dante.algorithms.data.EnemyAgentData;
import net.java.dante.algorithms.data.WeaponData;
import net.java.dante.sim.data.object.ObjectSize;

/**
 * Implementation of {@link net.java.dante.algorithms.data.EnemyAgentData} interface.
 *
 * @author M.Olszewski
 */
class EnemyAgentDataImpl extends AbstractAgentData implements EnemyAgentData
{
  /** Visibility status of observed object. */
  private boolean inRange;
  /** Number of hits received by enemy agent. */
  private int hitsCount = 0;


  /**
   * Creates instance of {@link EnemyAgentDataImpl} class.
   *
   * @param objectTypeName name of object's type.
   * @param objectIdentifier object's identifier.
   * @param objectSize object's size.
   * @param maxObjectSpeed maximum object's speed.
   * @param maxHitPoints agent's max hit points.
   * @param maxSightRange agent's sight range.
   * @param weaponSystem agent's weapon system.
   */
  EnemyAgentDataImpl(String objectTypeName, int objectIdentifier,
                     ObjectSize objectSize, double maxObjectSpeed,
                     int maxHitPoints, double maxSightRange,
                     WeaponData weaponSystem)
  {
    super(objectTypeName, objectIdentifier, objectSize, maxObjectSpeed,
          maxHitPoints, maxSightRange, weaponSystem);
  }


  /**
   * @see net.java.dante.algorithms.data.EnemyAgentData#getHitsCount()
   */
  public int getHitsCount()
  {
    return hitsCount;
  }

  /**
   * Adds specified number of hits that were received by enemy agent.
   *
   * @param hitsNumber - additional number of hits received by enemy agent.
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
   * @see net.java.dante.algorithms.data.EnemyAgentData#isVisible()
   */
  public boolean isVisible()
  {
    return inRange;
  }

  /**
   * Sets status of specified object to 'in sight range' of some
   * agents group.
   */
  void inSightRange()
  {
    inRange = true;
  }

  /**
   * Sets status of specified object to 'out of sight range' of some
   * agents group.
   */
  void outOfSightRange()
  {
    inRange = false;
  }

  /**
   * @see net.java.dante.algorithms.AbstractAgentData#toString()
   */
  @Override
  public String toString()
  {
    final String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) +
        "; hitsCount=" + hitsCount + "; isVisibile=" + inRange + "]");
  }
}