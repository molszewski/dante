/*
 * Created on 2006-09-01
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms;


import net.java.dante.algorithms.data.ProjectileData;
import net.java.dante.sim.data.object.ObjectSize;

/**
 * Implementation of {@link net.java.dante.algorithms.data.ProjectileData} interface.
 *
 * @author M.Olszewski
 */
class ProjectileDataImpl extends AbstractObjectData implements ProjectileData
{
  /**
   * Creates instance of {@link AbstractObjectData} class.
   *
   * @param objectTypeName name of object's type.
   * @param objectIdentifier object's identifier.
   * @param objectSize object's size.
   * @param maxObjectSpeed maximum object's speed.
   */
  public ProjectileDataImpl(String objectTypeName, int objectIdentifier,
                            ObjectSize objectSize, double maxObjectSpeed)
  {
    super(objectTypeName, objectIdentifier, objectSize, maxObjectSpeed);
  }
}
