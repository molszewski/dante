/*
 * Created on 2006-08-13
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d;

import java.util.ArrayList;
import java.util.List;

import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.engine.EngineObjectsGroup;
import net.java.dante.sim.engine.graphics.java2d.Java2dSprite;


/**
 * Class managing groups of explosions.
 *
 * @author M.Olszewski
 */
public class Engine2dExplosionsGroup implements EngineObjectsGroup
{
  /** List with {@link Engine2dExplosion} objects */
  private List<Engine2dExplosion> explosions =
      new ArrayList<Engine2dExplosion>(50);
  /** Sprites repository. */
  private SpritesRepository sprites;
  
  
  /**
   * Creates instance of {@link Engine2dExplosionsGroup} class.
   * 
   * @param spritesRepository - sprites repository.
   */
  public Engine2dExplosionsGroup(SpritesRepository spritesRepository)
  {
    if (spritesRepository == null)
    {
      throw new NullPointerException("Specified spritesRepository is null!");
    }
    
    sprites = spritesRepository;
  }
  

  /** 
   * @see net.java.dante.sim.engine.EngineObjectsGroup#update(long)
   */
  public void update(long delta)
  {
    for (int i = 0, size = explosions.size(); i < size; i++)
    {
      Engine2dExplosion explosion = explosions.get(i);
      explosion.update(delta);
      
      if (!explosion.isActive())
      {
        explosions.remove(explosion);
        size = explosions.size();
      }
    }
  }

  /** 
   * @see net.java.dante.sim.engine.EngineObjectsGroup#render()
   */
  public void render()
  {
    for (Engine2dExplosion explosion : explosions)
    {
      explosion.render();
    }
  }
  
  /**
   * Creates new agent explosion object using the specified 
   * exploded agent's coordinates and size and explosion range.
   * Created explosion object is added to this group. 
   * 
   * @param agentX
   * @param agentY
   * @param agentSize
   * @param explosionRange
   */
  public void addAgentExplosion(double agentX, double agentY, 
      ObjectSize agentSize, double explosionRange)
  {
    addExplosion(sprites.getAgentExplosionSprites(), 
        sprites.getAgentExplosionDelay(), 
        agentX, agentY, agentSize, explosionRange);
  }
  
  /**
   * Creates new projectile explosion object using the specified 
   * exploded projectile's coordinates and size and explosion range.
   * Created explosion object is added to this group. 
   * 
   * @param projectileX
   * @param projectileY
   * @param projectileSize
   * @param explosionRange
   */
  public void addProjectileExplosion(double projectileX, double projectileY, 
      ObjectSize projectileSize, double explosionRange)
  {
    addExplosion(sprites.getProjectileExplosionSprites(), 
        sprites.getProjectileExplosionDelay(), 
        projectileX, projectileY, projectileSize, explosionRange);
  }
  
  private void addExplosion(Java2dSprite[] sprites2, long spritesDelay,
      double explodedX, double explodedY, 
      ObjectSize explodedSize, double explosionRange)
  {
    double explosionX = (explodedX + (explodedSize.getWidth() >> 1) - explosionRange);
    double explosionY = (explodedY + (explodedSize.getHeight() >> 1) - explosionRange);
    ObjectSize explosionSize = new ObjectSize( (((int)explosionRange) << 1), (((int)explosionRange) << 1));
    
    Engine2dExplosion explo = new Engine2dExplosion(
        sprites2, spritesDelay, explosionX, explosionY, explosionSize);
    explosions.add(explo);
  }
  
  /**
   * Removes all explosions from this group.
   */
  void clear()
  {
    explosions.clear();
  }
}