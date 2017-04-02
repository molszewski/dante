/*
 * Created on 2006-07-21
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.collision;

import net.java.dante.sim.engine.EngineObject;

/**
 * Interface for classes that are meant to be notified about detected collisions.
 * Method {@link #collisionDetected(EngineObject, EngineObject)} is invoked
 * when any collision is detected by associated {@link net.java.dante.sim.engine.collision.CollisionModule} 
 * in {@link net.java.dante.sim.engine.collision.CollisionDetector}.
 * 
 * @author M.Olszewski
 */
public interface CollisionListener
{
  /**
   * Callback method invoked when collision between two objects was
   * detected by by associated {@link net.java.dante.sim.engine.collision.CollisionModule} in
   * {@link net.java.dante.sim.engine.collision.CollisionDetector}.<p>
   * {@link net.java.dante.sim.engine.collision.CollisionDetector} assures that collided objects
   * are not equal. 
   * 
   * @param o1 - first object that collided.
   * @param o2 - second object that collided.
   */
  void collisionDetected(EngineObject o1, EngineObject o2);
}