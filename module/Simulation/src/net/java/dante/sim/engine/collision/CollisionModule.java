/*
 * Created on 2006-07-21
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.collision;

import net.java.dante.sim.engine.EngineObject;

/**
 * Collsion module for {@link CollisionDetector}, checking whether 
 * collision occured between two instances of {@link EngineObject} 
 * implementations. 
 * Also containing associated {@link CollisionListener} objects that can be
 * obtained via {@link #getListeners()} method.
 * 
 * @author M.Olszewski
 */
public interface CollisionModule
{
  /**
   * Checks whether specified instances of {@link EngineObject} implemntations
   * collides with each other or not.
   * 
   * @param o1 - first object to check.
   * @param o2 - second object to check.
   * 
   * @return Returns <code>true</code> if objects collides, 
   *         <code>false</code> otherwise.
   */
  boolean checkCollision(EngineObject o1, EngineObject o2);
  
  /**
   * Retrieves list of listeners associated with this {@link CollisionModule}.
   * 
   * @return Returns list of listeners associated with this {@link CollisionModule}.
   */
  CollisionListener[] getListeners();
}
