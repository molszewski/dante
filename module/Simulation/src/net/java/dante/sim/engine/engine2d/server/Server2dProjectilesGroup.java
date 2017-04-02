/*
 * Created on 2006-08-09
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.engine2d.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.java.dante.sim.data.object.projectile.ServerProjectileState;
import net.java.dante.sim.data.object.state.Changeable;
import net.java.dante.sim.data.object.state.ObjectState;
import net.java.dante.sim.engine.EngineObjectsGroup;
import net.java.dante.sim.engine.collision.CollisionDetector;
import net.java.dante.sim.engine.collision.CollisionModule;


/**
 * Class representing group of projectiles used in
 * {@link net.java.dante.sim.engine.engine2d.server.ServerEngine2d} class.
 *
 * @author M.Olszewski
 */
class Server2dProjectilesGroup implements EngineObjectsGroup
{
  /** Map containing association between projectiles identifiers and projectiles. */
  private Map<Integer, Server2dProjectile> projectiles =
      new HashMap<Integer, Server2dProjectile>(100);
  /** List of projectiles to remove. */
  private List<Integer> projectilesToRemove = new ArrayList<Integer>(10);
  /** All projectiles as unmodifiable collection. */
  private Collection<Server2dProjectile> projectilesCollection;
  /** Collision detector used by this {@link Server2dProjectilesGroup}. */
  private CollisionDetector detector;
  /** Collision module used by this {@link Server2dProjectilesGroup}. */
  private CollisionModule collisionModule;
  /** Visibility manager used by this group to update visibility records. */
  private VisibilityManager visibilityManager;


  /**
   * Creates instance of {@link Server2dProjectilesGroup} with specified
   * parameters.
   *
   * @param collisionDetector - collisions detector used by this
   *        {@link Server2dProjectilesGroup}.
   * @param defaultCollisionModule - collision module used by this
   *        {@link Server2dProjectilesGroup}.
   * @param visibilityMan - visibility manager.
   */
  Server2dProjectilesGroup(CollisionDetector collisionDetector,
      CollisionModule defaultCollisionModule,
      VisibilityManager visibilityMan)
  {
    if (collisionDetector == null)
    {
      throw new NullPointerException("Specified collisionDetector is null!");
    }
    if (defaultCollisionModule == null)
    {
      throw new NullPointerException("Specified defaultCollisionModule is null!");
    }
    if (visibilityMan == null)
    {
      throw new NullPointerException("Specified visibilityMan is null!");
    }

    projectilesCollection = Collections.unmodifiableCollection(projectiles.values());
    detector          = collisionDetector;
    collisionModule   = defaultCollisionModule;
    visibilityManager = visibilityMan;
  }


  /**
   * @see net.java.dante.sim.engine.EngineObjectsGroup#update(long)
   */
  public void update(long delta)
  {
    prepareProjectiles();

    updateActiveProjectiles(delta);

    removeMarkedProjectiles();
  }

  /**
   * Removes all projectiles marked as 'to remove'.
   */
  void removeMarkedProjectiles()
  {
    // Remove projectiles marked as 'to remove'
    for (Integer projectileId : projectilesToRemove)
    {
      Server2dProjectile projectile = projectiles.get(projectileId);
      detector.removeObject(projectile);
      projectiles.remove(projectileId);

      // Generate proper events
      visibilityManager.generateProjectileDestroyedEvents(projectile);
    }
    // And clear list - do not remove again
    projectilesToRemove.clear();
  }

  /**
   * Prepares all projectiles.
   */
  private void prepareProjectiles()
  {
    for (Integer projectileId : projectiles.keySet())
    {
      Server2dProjectile projectile = projectiles.get(projectileId);
      // Mark projectile state as updated.
      ObjectState agentState = projectile.getProjectile().getData();
      if (agentState instanceof Changeable)
      {
        ((Changeable)agentState).changesUpdated();
      }
    }
  }

  /**
   * Updates state of all active projectiles.
   *
   * @param delta - time elapsed since last update.
   */
  private void updateActiveProjectiles(long delta)
  {
    // Projectiles update
    for (Integer projectileId : projectiles.keySet())
    {
      Server2dProjectile projectile = projectiles.get(projectileId);
      // Only active projectiles are moving
      if (projectile.isActive())
      {
        projectile.update(delta);
        ServerProjectileState projectileState = (ServerProjectileState)projectile.getProjectile().getData();
        if (projectileState.isPositionChanged())
        {
          visibilityManager.updateRecordsByMovedProjectile(projectile);
          detector.checkCollisions(collisionModule, projectile);
        }
      }
    }
  }

  /**
   * @see net.java.dante.sim.engine.EngineObjectsGroup#render()
   */
  public void render()
  {
    for (Integer projectileId : projectiles.keySet())
    {
      projectiles.get(projectileId).render();
    }
  }

  /**
   * Removes projectile with specified identifier from this group.
   * Projectile is added to special list containing identifiers of projectiles
   * to remove. Projectiles from this list will be removed during next
   * {@link #update(long)} method's invocation.
   * If projectile with specified identifier does not belong to this group,
   * this method returns immediately.
   *
   * @param projectileId - projectile's identifier which should be removed.
   */
  void removeProjectile(int projectileId)
  {
    if (projectileId < 0)
    {
      throw new IllegalArgumentException("Invalid argument projectileId - it must be an integer greater or equal to zero!");
    }

    Integer projectileIdObj = Integer.valueOf(projectileId);
    Server2dProjectile projectile = projectiles.get(projectileIdObj);

    if (projectile != null)
    {
      projectile.setActive(false);
      if (!projectilesToRemove.contains(projectileIdObj))
      {
        projectilesToRemove.add(projectileIdObj);
      }
    }
  }

  /**
   * Adds specified projectile with specified identifier to this group immediately.
   *
   * @param projectileId - identifier of projectile which will be added.
   * @param projectile - projectile which will be added.
   */
  void addProjectile(int projectileId, Server2dProjectile projectile)
  {
    if (projectile == null)
    {
      throw new NullPointerException("Specified projectile is null!");
    }
    if (projectileId < 0)
    {
      throw new IllegalArgumentException("Invalid argument projectileId - it must be an integer greater or equal to zero!");
    }

    projectiles.put(Integer.valueOf(projectileId), projectile);
    detector.addObject(projectile);
  }

  /**
   * Gets all projectiles as unmodifiable collection.
   *
   * @return Returns all projectiles as unmodifiable collection.
   */
  Collection<Server2dProjectile> getProjectiles()
  {
    return projectilesCollection;
  }
}