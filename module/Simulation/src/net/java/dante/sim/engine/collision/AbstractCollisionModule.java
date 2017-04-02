/*
 * Created on 2006-07-21
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.collision;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class supporting operations of adding and removing listeners
 * associated with {@link CollisionModule}. Listeners can be fetched by 
 * calling {@link #getListeners()} method. 
 * All concrete subclasses must override 
 * {@link net.java.dante.sim.engine.collision.CollisionModule#checkCollision(net.java.dante.sim.engine.EngineObject, net.java.dante.sim.engine.EngineObject)}
 * method from {@link CollisionModule} interface. 
 * 
 * @author M.Olszewski
 */
public abstract class AbstractCollisionModule implements CollisionModule
{
  /** Listeners associated with this {@link AbstractCollisionModule}. */
  private Set<CollisionListener> listeners = new HashSet<CollisionListener>();
  
  
  /**
   * Protected constructor - this class is only for inheritance.
   */
  protected AbstractCollisionModule()
  {
    // Intentionally left empty.
  }
  

  /**
   * Adds collision listener to this {@link AbstractCollisionModule}.
   * Listeners cannot be added more than once.
   * 
   * @param listener - listener to add.
   */
  public void addListener(CollisionListener listener)
  {
    listeners.add(listener);
  }
  
  /**
   * Removes collision listener from this {@link AbstractCollisionModule}.
   * If listener is not associated with this {@link AbstractCollisionModule},
   * this method returns immediately.
   * 
   * @param listener - listener to remove.
   */
  public void removeListener(CollisionListener listener)
  {
    listeners.remove(listener);
  }
  
  /**
   * @see net.java.dante.sim.engine.collision.CollisionModule#getListeners()
   */
  public CollisionListener[] getListeners()
  {
    return listeners.toArray(new CollisionListener[listeners.size()]);
  }
}