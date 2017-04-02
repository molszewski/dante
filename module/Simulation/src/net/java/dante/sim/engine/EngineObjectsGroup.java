/*
 * Created on 2006-08-09
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine;

/**
 * Interface for group of objects that belongs to engine. 
 * 
 * @author M.Olszewski
 */
public interface EngineObjectsGroup
{
  /**
   * Updates state of this {@link EngineObjectsGroup} regarding time that has 
   * passed since last update.
   * 
   * @param delta - time that has passed since last update.
   */
  void update(long delta);
  
  /**
   * Renders this {@link EngineObjectsGroup} using internal graphics method.
   */
  void render();
}