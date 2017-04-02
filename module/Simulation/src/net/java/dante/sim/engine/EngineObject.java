/*
 * Created on 2006-07-21
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine;

/**
 * Interface for objects that belongs to engine. 
 * 
 * @author M.Olszewski
 */
public interface EngineObject
{
  /**
   * Updates state of this {@link EngineObject} regarding time that has 
   * passed since last update.
   * 
   * @param delta - time that has passed since last update.
   */
  void update(long delta);
  
  /**
   * Renders this {@link EngineObject} using internal graphics method.
   */
  void render();
  
  /**
   * Determines whether this {@link EngineObject} is active.
   * 
   * @return Returns <code>true</code> if object is active, <code>false</code>
   *         otherwise.
   */
  boolean isActive();
}