/*
 * Created on 2006-09-01
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.java.dante.algorithms.data.ProjectileData;
import net.java.dante.algorithms.data.ProjectilesData;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.event.Event;
import net.java.dante.sim.event.EventsRepository;
import net.java.dante.sim.event.types.ObjectMoveEventParams;
import net.java.dante.sim.event.types.ProjectileDestroyedEvent;
import net.java.dante.sim.event.types.ProjectileEvent;
import net.java.dante.sim.event.types.ProjectileGoneEvent;
import net.java.dante.sim.event.types.ProjectileMoveEvent;
import net.java.dante.sim.event.types.ProjectileSeenEvent;
import net.java.dante.sim.event.types.ProjectileShotEvent;
import net.java.dante.sim.event.types.ProjectileTypeEvent;

/**
 * Implementation of {@link ProjectilesData} interface.
 *
 * @author M.Olszewski
 */
class ProjectilesDataImpl implements ProjectilesData, EventsUpdateable
{
  /** Default size of list with projectiles. */
  private static final int DEFAULT_PROJECTILES_SIZE = 200;

  private ObjectSize size;
  private double maxSpeed;

  /** List with projectiles. */
  private List<ProjectileDataImpl> projectiles =
      new ArrayList<ProjectileDataImpl>(DEFAULT_PROJECTILES_SIZE);
  /** Map between projectiles identifiers and projectiles data. */
  private Map<Integer, ProjectileDataImpl> projectilesMap =
      new HashMap<Integer, ProjectileDataImpl>(DEFAULT_PROJECTILES_SIZE);


  /**
   * Creates instance of {@link ProjectilesDataImpl} class.
   *
   * @param projectilesSize all projectiles size.
   * @param projectileMaxSpeed all projectiles maximum speed.
   */
  ProjectilesDataImpl(ObjectSize projectilesSize, double projectileMaxSpeed)
  {
    if (projectilesSize == null)
    {
      throw new NullPointerException("Specified projectilesSize is null!");
    }


    size     = projectilesSize;
    maxSpeed = projectileMaxSpeed;
  }


  /**
   * @see net.java.dante.algorithms.data.ProjectilesData#getProjectilesCount()
   */
  public int getProjectilesCount()
  {
    return projectiles.size();
  }

  /**
   * @see net.java.dante.algorithms.data.ProjectilesData#getProjectilesData()
   */
  public ProjectileData[] getProjectilesData()
  {
    return projectiles.toArray(new ProjectileData[projectiles.size()]);
  }

  /**
   * @see net.java.dante.algorithms.EventsUpdateable#update(net.java.dante.sim.event.EventsRepository)
   */
  public void update(EventsRepository repository)
  {
    if (repository == null)
    {
      throw new NullPointerException("Specified repository is null!");
    }

    for (int i = 0, eventsCount = repository.getEventsCount(); i < eventsCount; i++)
    {
      Event event = repository.getEvent(i);
      if (event instanceof ProjectileEvent)
      {
        processProjectileEvent((ProjectileEvent)event);
      }
    }
  }

  /**
   * Processes the specified projectile.
   *
   * @param event the specified projectile.
   */
  private void processProjectileEvent(ProjectileEvent event)
  {
    ProjectileDataImpl projectileData = projectilesMap.get(Integer.valueOf(event.getProjectileId()));

    if (projectileData != null)
    {
      if ((event instanceof ProjectileGoneEvent) ||
          (event instanceof ProjectileDestroyedEvent))
      {
        removeProjectile(projectileData);
      }
      else if (event instanceof ProjectileMoveEvent)
      {
        setMovementParameters(projectileData, (ObjectMoveEventParams)event);
      }
      else
      {
        throw new IllegalStateException("Illegal state of projectiles - missed PROJECTILE_GONE/PROJECTILE_DESTROYED event!");
      }
    }
    else
    {
      if ((event instanceof ProjectileShotEvent) ||
          (event instanceof ProjectileSeenEvent))
      {
        ProjectileTypeEvent typeEvent = (ProjectileTypeEvent)event;
        ObjectMoveEventParams movedParams = (ObjectMoveEventParams)event;

        ProjectileDataImpl newProjectileData = new ProjectileDataImpl(typeEvent.getProjectileType(),
                                                                      event.getProjectileId(),
                                                                      size,
                                                                      maxSpeed);
        setMovementParameters(newProjectileData, movedParams);
        addProjectile(newProjectileData);
      }
      else
      {
        throw new IllegalStateException("Illegal state of projectiles - missed PROJECTILE_SEEN/PROJECTILE_SHOT event!");
      }
    }
  }

  /**
   * Sets the specified movement parameters of the specified projectile's data.
   *
   * @param projectileData the specified projectile's data.
   * @param moveParams the specified movement parameters.
   */
  private void setMovementParameters(ProjectileDataImpl projectileData,
                                     ObjectMoveEventParams moveParams)
  {
    projectileData.setX(moveParams.getDestinationX());
    projectileData.setY(moveParams.getDestinationY());
    projectileData.setSpeedX(moveParams.getSpeedX());
    projectileData.setSpeedY(moveParams.getSpeedY());
  }

  /**
   * Adds the specified projectile's data to this projectiles data storage.
   *
   * @param projectileData the specified projectile.
   */
  private void addProjectile(ProjectileDataImpl projectileData)
  {
    projectiles.add(projectileData);
    projectilesMap.put(Integer.valueOf(projectileData.getId()), projectileData);
  }

  /**
   * Removes the specified projectile's data from this projectiles data storage.
   *
   * @param projectileData the specified projectile.
   */
  private void removeProjectile(ProjectileDataImpl projectileData)
  {
    projectiles.remove(projectileData);
    projectilesMap.remove(Integer.valueOf(projectileData.getId()));
  }
}