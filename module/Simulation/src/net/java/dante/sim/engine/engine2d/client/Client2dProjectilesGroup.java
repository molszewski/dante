/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.engine2d.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.java.dante.sim.common.Dbg;
import net.java.dante.sim.creator.ObjectsCreator;
import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.object.projectile.ClientProjectile;
import net.java.dante.sim.data.object.projectile.ClientProjectileInitData;
import net.java.dante.sim.engine.EngineObjectsGroup;
import net.java.dante.sim.event.types.ObjectMoveEventParams;
import net.java.dante.sim.event.types.ProjectileDestroyedEvent;
import net.java.dante.sim.event.types.ProjectileEvent;
import net.java.dante.sim.event.types.ProjectileGoneEvent;
import net.java.dante.sim.event.types.ProjectileMoveEvent;
import net.java.dante.sim.event.types.ProjectileSeenEvent;
import net.java.dante.sim.event.types.ProjectileShotEvent;
import net.java.dante.sim.event.types.ProjectileTypeEvent;


/**
 * Class representing group of projectiles used in
 * {@link net.java.dante.sim.engine.engine2d.client.ClientEngine2d} class.
 *
 * @author M.Olszewski
 */
class Client2dProjectilesGroup implements EngineObjectsGroup
{
  /** Map containing association between projectiles identifiers and projectiles. */
  private Map<Integer, Client2dProjectile> projectiles =
      new HashMap<Integer, Client2dProjectile>(100);
  /** List of projectiles to remove. */
  private List<Integer> projectilesToRemove = new ArrayList<Integer>(10);
  /** Projectiles events cache. */
  private List<ProjectileEvent> eventsCache = new ArrayList<ProjectileEvent>(100);
  /**
   * Objects creator creating
   * {@link net.java.dante.sim.data.object.projectile.ClientProjectile} objects.
   */
  private ObjectsCreator creator;
  /**
   * Objects mediator used by projectiles group to notify about newly created
   * projectiles.
   */
  private ObjectsMediator mediator;


  /**
   * Creates instance of {@link Client2dProjectilesGroup} with the
   * specified parameters.
   *
   * @param objectsCreator - objects creator.
   * @param objectsMediator - objects mediator.
   */
  Client2dProjectilesGroup(ObjectsCreator objectsCreator,
      ObjectsMediator objectsMediator)
  {
    if (objectsCreator == null)
    {
      throw new NullPointerException("Specified objectsCreator is null!");
    }
    if (objectsMediator == null)
    {
      throw new NullPointerException("Specified objectsMediator is null!");
    }

    creator  = objectsCreator;
    mediator = objectsMediator;
  }

  /**
   * @see net.java.dante.sim.engine.EngineObjectsGroup#update(long)
   */
  public void update(long delta)
  {
    processEventsCache(delta);

    // Projectiles update
    for (Integer projectileId : projectiles.keySet())
    {
      Client2dProjectile projectile = projectiles.get(projectileId);
      // Only active projectiles are moving
      if (projectile.isActive())
      {
        projectile.update(delta);
      }
    }
  }

  /** Total time that has passed. */
  private long totalTime = 0;

  /**
   * @param delta
   */
  private void processEventsCache(long delta)
  {
    totalTime += delta;

    for (Iterator<ProjectileEvent> it = eventsCache.iterator(); it.hasNext(); )
    {
      ProjectileEvent event = it.next();

      if (event.getTime() <= totalTime)
      {
        Client2dProjectile projectile = projectiles.get(Integer.valueOf(event.getProjectileId()));

        if (projectile != null)
        {
          if (event instanceof ProjectileGoneEvent)
          {
            projectile.projectileGone((ProjectileGoneEvent)event);
          }
          else if (event instanceof ProjectileDestroyedEvent)
          {
            projectile.projectileDestroyed((ProjectileDestroyedEvent)event);
          }
          else if (event instanceof ProjectileMoveEvent)
          {
            projectile.addEvent(event);
          }
          else
          {
            Dbg.error("projectile=" + projectile);
            Dbg.error("projectiles=" + projectiles);
            Dbg.error("projectilesToRemove=" + projectilesToRemove);
            Dbg.error("event=" + event);
            throw new IllegalStateException("Illegal state of projectiles group - missed PROJECTILE_GONE/PROJECTILE_DESTROYED event!");
          }
        }
        else
        {
          if ((event instanceof ProjectileShotEvent) ||
              (event instanceof ProjectileSeenEvent))
          {
            ProjectileTypeEvent typeEvent = (ProjectileTypeEvent)event;
            ObjectMoveEventParams movedParams = (ObjectMoveEventParams)event;



            ClientProjectile clientProjectile = (ClientProjectile)creator.createObject(
                ClientProjectile.class, typeEvent.getProjectileType(),
                new ClientProjectileInitData(
                    typeEvent.getProjectileId(), (typeEvent.getTime() - delta), // Subtract delta - it will be added soon
                    movedParams.getDestinationX(), movedParams.getDestinationY(),
                    movedParams.getSpeedX(), movedParams.getSpeedY()));
            mediator.objectCreated(clientProjectile);
          }
          else
          {
            Dbg.error("projectile=" + projectile);
            Dbg.error("projectiles=" + projectiles);
            Dbg.error("projectilesToRemove=" + projectilesToRemove);
            Dbg.error("event=" + event);
            throw new IllegalStateException("Illegal state of projectiles group - missed PROJECTILE_SEEN/PROJECTILE_SHOT event!");
          }
        }
        it.remove();
      }
      else
      {
        break;
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

    projectiles.remove(Integer.valueOf(projectileId));
  }

  /**
   * Adds specified projectile with specified identifier to this group immediately.
   *
   * @param projectileId - identifier of projectile which will be added.
   * @param projectile - projectile which will be added.
   */
  void addProjectile(int projectileId, Client2dProjectile projectile)
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
  }

  /**
   * Processes the specified {@link ProjectileEvent} event.
   *
   * @param event - the specified {@link ProjectileEvent} event.
   */
  void processEvents(ProjectileEvent event)
  {
    eventsCache.add(event);
  }

  /**
   * Shifts time of this group by specified amount of time.
   *
   * @param time - amount of time by which time of group will be shifted.
   */
  void timeShift(long time)
  {
    for (Integer projectileId : projectiles.keySet())
    {
      projectiles.get(projectileId).timeShift(time);
    }
  }
}