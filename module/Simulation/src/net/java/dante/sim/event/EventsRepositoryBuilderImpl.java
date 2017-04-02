/*
 * Created on 2006-08-17
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.java.dante.sim.event.types.EnemyAgentEvent;
import net.java.dante.sim.event.types.EnemyAgentMoveEvent;
import net.java.dante.sim.event.types.EventTypesUtils;
import net.java.dante.sim.event.types.FriendlyAgentEvent;
import net.java.dante.sim.event.types.FriendlyAgentMoveEvent;
import net.java.dante.sim.event.types.ProjectileEvent;
import net.java.dante.sim.event.types.ProjectileMoveEvent;


/**
 * Implementation of {@link EventsRepositoryBuilder} interface.
 *
 * @author M.Olszewski
 */
class EventsRepositoryBuilderImpl implements EventsRepositoryBuilder
{
  /** Agents group identifier. */
  int groupId;
  /** List with all events regarding this agents group. */
  private List<Event> events = new ArrayList<Event>();
  /** Movement events cache for this group. */
  private MovementEventsCache movementCache = new MovementEventsCache();


  /**
   * Creates instance of {@link EventsRepositoryBuilderImpl} with
   * the specified parameters.
   *
   * @param agentsGroupId - agents group identifier.
   */
  EventsRepositoryBuilderImpl(int agentsGroupId)
  {
    if (agentsGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsGroupId - it must be positive integer or zero!");
    }

    groupId = agentsGroupId;
  }


  /**
   * @see net.java.dante.sim.event.EventsRepositoryBuilder#getGroupId()
   */
  public int getGroupId()
  {
    return groupId;
  }

  /**
   * @see net.java.dante.sim.event.EventsRepositoryBuilder#addEvent(net.java.dante.sim.event.Event)
   */
  public void addEvent(Event event)
  {
    if (event == null)
    {
      throw new NullPointerException("Specified event is null!");
    }

    if (!movementCache.processEvent(event))
    {
      addEventDirectly(event);
    }
  }

  /**
   * Adds event directly, without processing it in the cache.
   *
   * @param event - event to add.
   */
  void addEventDirectly(Event event)
  {
    if (!events.contains(event))
    {
      events.add(event);
    }
  }

  /**
   * Adds event directly at the specified index, without processing it
   * in the cache.
   *
   * @param index - the specified index.
   * @param event - event to add.
   */
  void addEventDirectly(int index, Event event)
  {
    if (!events.contains(event))
    {
      events.add(index, event);
    }
  }

  /**
   * Gets index for event that will be inserted. It takes into consideration
   * all events stored in the cache.
   *
   * @return Returns index for event that will be inserted.
   */
  int getNextEventIndex()
  {
    return events.size() + movementCache.size();
  }

  /**
   * @see net.java.dante.sim.event.EventsRepositoryBuilder#build()
   */
  public EventsRepository build()
  {
    EventsRepository eventsRepository = null;

    // Try to generate cached events first.
    movementCache.generateEvents();

    if (events.size() > 0)
    {
      eventsRepository = new EventsRepositoryImpl(groupId, events);

      events = new ArrayList<Event>();
      movementCache.clear();
    }

    return eventsRepository;
  }

  /**
   * Movement events cache storing
   *
   * @author M.Olszewski
   */
  private class MovementEventsCache
  {
    /** Index event to event map. */
    Map<Integer, Event> indexToEvent = new TreeMap<Integer, Event>();
    /** Cache with movement data of friendly agents. */
    Map<Integer, FriendlyAgentMoveData> friendlyMovementEvent =
        new HashMap<Integer, FriendlyAgentMoveData>();
    /** Cache with movement data of enemy agents. */
    Map<Integer, EnemyAgentMoveData> enemyMovementEvent =
        new HashMap<Integer, EnemyAgentMoveData>();
    /** Cache with movement data of projectiles. */
    Map<Integer, ProjectileMoveData> projectileMovementEvent =
        new HashMap<Integer, ProjectileMoveData>();

    /**
     * Creates instance of {@link MovementEventsCache} class.
     */
    MovementEventsCache()
    {
      // Intentionally left empty.
    }

    /**
     * Gets total size of cache.
     *
     * @return Returns total size of cache.
     */
    int size()
    {
      return (friendlyMovementEvent.size() + enemyMovementEvent.size() +
              projectileMovementEvent.size() + indexToEvent.size());
    }

    /**
     * Processes specified event, updating this cache. Returns value indicating
     * whether event was cached by this cache.
     *
     * @param event - event to process.
     *
     * @return Returns <code>true</code> if event was cached by this cache,
     *         <code>false</code> otherwise.
     */
    boolean processEvent(Event event)
    {
      boolean eventCached = false;

      if (event instanceof FriendlyAgentEvent)
      {
        eventCached = processFriendlyAgentEvent((FriendlyAgentEvent)event);
      }
      else if (event instanceof EnemyAgentEvent)
      {
        eventCached = processEnemyAgentEvent((EnemyAgentEvent)event);
      }
      else if (event instanceof ProjectileEvent)
      {
        eventCached = processProjectileEvent((ProjectileEvent)event);
      }

      return eventCached;
    }

    private boolean processFriendlyAgentEvent(FriendlyAgentEvent event)
    {
      boolean eventCached = false;

      Integer friendlyAgentId = Integer.valueOf(event.getFriendlyAgentId());
      FriendlyAgentMoveData data = friendlyMovementEvent.get(friendlyAgentId);

      switch (event.getEventType())
      {
        case FRIENDLY_AGENT_MOVE:
        {
          FriendlyAgentMoveEvent moveEvent = (FriendlyAgentMoveEvent)event;
          if (data != null)
          {
            if (data.checkSpeed(moveEvent.getSpeedX(), moveEvent.getSpeedY()))
            {
              data.update(moveEvent.getDestinationX(), moveEvent.getDestinationY());
            }
            else
            {
              indexToEvent.put(Integer.valueOf(data.getEventIndex()),
                  data.createFriendlyMoveEvent(friendlyAgentId.intValue()));
              data.set((getNextEventIndex() - 1), moveEvent);
            }
          }
          else
          {
            friendlyMovementEvent.put(friendlyAgentId,
                new FriendlyAgentMoveData(getNextEventIndex(), moveEvent));
          }

          eventCached = true;
          break;
        }
        case FRIENDLY_AGENT_BLOCKED:
        {
          if (data != null)
          {
            indexToEvent.put(Integer.valueOf(data.getEventIndex()),
                data.createFriendlyMoveEvent(friendlyAgentId.intValue()));
            friendlyMovementEvent.remove(friendlyAgentId);
          }
          break;
        }
        default:
        {
          // Intentionally left empty.
        }
      }

      return eventCached;
    }

    private boolean processEnemyAgentEvent(EnemyAgentEvent event)
    {
      boolean eventCached = false;

      Integer enemyAgentId = Integer.valueOf(event.getEnemyAgentId());
      EnemyAgentMoveData data = enemyMovementEvent.get(enemyAgentId);

      switch (event.getEventType())
      {
        case ENEMY_AGENT_MOVE:
        {
          EnemyAgentMoveEvent moveEvent = (EnemyAgentMoveEvent)event;
          if (data != null)
          {
            if (data.checkSpeed(moveEvent.getSpeedX(), moveEvent.getSpeedY()))
            {
              data.update(moveEvent.getDestinationX(), moveEvent.getDestinationY());
            }
            else
            {
              indexToEvent.put(Integer.valueOf(data.getEventIndex()),
                  data.createEnemyMoveEvent(enemyAgentId.intValue()));
              data.set((getNextEventIndex() - 1), moveEvent);
            }
          }
          else
          {
            enemyMovementEvent.put(enemyAgentId,
                new EnemyAgentMoveData(getNextEventIndex(), moveEvent));
          }

          eventCached = true;
          break;
        }
        case ENEMY_AGENT_GONE:
        {
          if (data != null)
          {
            indexToEvent.put(Integer.valueOf(data.getEventIndex()),
                data.createEnemyMoveEvent(enemyAgentId.intValue()));
            enemyMovementEvent.remove(enemyAgentId);
          }
          break;
        }
        default:
        {
          // Intentionally left empty.
        }
      }

      return eventCached;
    }

    private boolean processProjectileEvent(ProjectileEvent event)
    {
      boolean eventCached = false;

      Integer projectileId = Integer.valueOf(event.getProjectileId());
      ProjectileMoveData data = projectileMovementEvent.get(projectileId);

      switch (event.getEventType())
      {
        case PROJECTILE_MOVE:
        {
          ProjectileMoveEvent moveEvent = (ProjectileMoveEvent)event;
          if (data != null)
          {
            if (data.checkSpeed(moveEvent.getSpeedX(), moveEvent.getSpeedY()))
            {
              data.update(moveEvent.getDestinationX(), moveEvent.getDestinationY());
            }
            else
            {
              indexToEvent.put(Integer.valueOf(data.getEventIndex()),
                  data.createProjectileMoveEvent(projectileId.intValue()));
              data.set((getNextEventIndex() - 1), moveEvent);
            }
          }
          else
          {
            projectileMovementEvent.put(projectileId,
                new ProjectileMoveData(getNextEventIndex(), moveEvent));
          }

          eventCached = true;
          break;
        }
        case PROJECTILE_GONE:
        {
          if (data != null)
          {
            indexToEvent.put(Integer.valueOf(data.getEventIndex()),
                data.createProjectileMoveEvent(projectileId.intValue()));
            projectileMovementEvent.remove(projectileId);
          }
          break;
        }
        default:
        {
          // Intentionally left empty.
        }
      }

      return eventCached;
    }

    /**
     * Generates all possible events now and adds them to events repository.
     */
    void generateEvents()
    {
      for (Integer friendlyId : friendlyMovementEvent.keySet())
      {
        FriendlyAgentMoveData data = friendlyMovementEvent.get(friendlyId);
        indexToEvent.put(Integer.valueOf(data.getEventIndex()),
                         data.createFriendlyMoveEvent(friendlyId.intValue()));
      }

      for (Integer enemyId : enemyMovementEvent.keySet())
      {
        EnemyAgentMoveData data = enemyMovementEvent.get(enemyId);
        indexToEvent.put(Integer.valueOf(data.getEventIndex()),
                         data.createEnemyMoveEvent(enemyId.intValue()));
      }

      for (Integer projectileId : projectileMovementEvent.keySet())
      {
        ProjectileMoveData data = projectileMovementEvent.get(projectileId);
        indexToEvent.put(Integer.valueOf(data.getEventIndex()),
                         data.createProjectileMoveEvent(projectileId.intValue()));
      }

      for (Integer index : indexToEvent.keySet())
      {
        addEventDirectly(index.intValue(), indexToEvent.get(index));
      }
    }

    /**
     * Clears this cache.
     */
    void clear()
    {
      projectileMovementEvent.clear();
      enemyMovementEvent.clear();
      friendlyMovementEvent.clear();
      indexToEvent.clear();
    }
  }

  /**
   * Base class for all movement data stored by {@link MovementEventsCache}.
   *
   * @author M.Olszewski
   */
  private abstract class MoveData
  {
    /** Movement event index. */
    private int eventIndex;
    /** Movement event start time. */
    protected long startTime;
    /** Movement destination's 'x' coordinate. */
    protected double x;
    /** Movement destination's 'y' coordinate. */
    protected double y;
    /** Moving object horizontal speed. */
    protected double speedX;
    /** Moving object vertical speed. */
    protected double speedY;


    /**
     * Constructor of {@link MoveData} class.
     *
     * @param moveEventIndex - movement event index.
     * @param moveStartTime - movement event start time.
     * @param objectDestinationX - movement destination's 'x' coordinate.
     * @param objectDestinationY - movement destination's 'y' coordinate.
     * @param objectSpeedX - moving object horizontal speed.
     * @param objectSpeedY - moving object vertical speed.
     */
    MoveData(int moveEventIndex, long moveStartTime,
        double objectDestinationX, double objectDestinationY,
        double objectSpeedX, double objectSpeedY)
    {
      set(moveEventIndex, moveStartTime, objectDestinationX, objectDestinationY,
          objectSpeedX, objectSpeedY);
    }


    /**
     * Checks whether horizontal and vertical speed are the same as specified
     * as parameters.
     *
     * @param objectSpeedX - moving object horizontal speed.
     * @param objectSpeedY - moving object vertical speed.
     *
     * @return Returns <code>true</code> if speeds are the same,
     *         <code>false</code> otherwise.
     */
    final boolean checkSpeed(double objectSpeedX, double objectSpeedY)
    {
      return ((speedX == objectSpeedX) && (speedY == objectSpeedY));
    }

    /**
     * Updates object's destination coordinates by overwriting ones
     * stored in this {@link MoveData} object.
     *
     * @param objectDestinationX
     * @param objectDestinationY
     */
    final void update(double objectDestinationX, double objectDestinationY)
    {
      x = objectDestinationX;
      y = objectDestinationY;
    }

    /**
     * Sets parameters of this {@link MoveData} object to the specified ones.
     *
     * @param moveEventIndex - movement event index.
     * @param moveStartTime - movement event start time.
     * @param objectDestinationX - movement destination's 'x' coordinate.
     * @param objectDestinationY - movement destination's 'y' coordinate.
     * @param objectSpeedX - moving object horizontal speed.
     * @param objectSpeedY - moving object vertical speed.
     */
    final void set(int moveEventIndex, long moveStartTime,
        double objectDestinationX, double objectDestinationY,
        double objectSpeedX, double objectSpeedY)
    {
      eventIndex = moveEventIndex;
      startTime  = moveStartTime;
      x          = objectDestinationX;
      y          = objectDestinationY;
      speedX     = objectSpeedX;
      speedY     = objectSpeedY;
    }

    /**
     * Gets event index (where event should be inserted).
     *
     * @return Returns event index (where event should be inserted).
     */
    final int getEventIndex()
    {
      return eventIndex;
    }
  }

  /**
   * Class storing data for events regarding movement of friendly agents.
   *
   * @author M.Olszewski
   */
  private class FriendlyAgentMoveData extends MoveData
  {
    /**
     * Creates instance of {@link FriendlyAgentMoveData} class using data
     * from specified event.
     *
     * @param moveEventIndex - movement event index.
     * @param moveEvent - the specified {@link FriendlyAgentMoveEvent}.
     */
    FriendlyAgentMoveData(int moveEventIndex, FriendlyAgentMoveEvent moveEvent)
    {
      super(moveEventIndex, moveEvent.getTime(),
          moveEvent.getDestinationX(), moveEvent.getDestinationY(),
          moveEvent.getSpeedX(), moveEvent.getSpeedY());
    }

    /**
     * Sets data of this {@link FriendlyAgentMoveData} object to one
     * obtained from specified event.
     *
     * @param moveEventIndex - movement event index.
     * @param moveEvent - the specified {@link FriendlyAgentMoveEvent}.
     */
    void set(int moveEventIndex, FriendlyAgentMoveEvent moveEvent)
    {
      set(moveEventIndex, moveEvent.getTime(),
          moveEvent.getDestinationX(), moveEvent.getDestinationY(),
          moveEvent.getSpeedX(), moveEvent.getSpeedY());
    }

    /**
     * Creates instance of {@link FriendlyAgentMoveEvent} class using
     * data stored inside this {@link FriendlyAgentMoveData} object.
     *
     * @param friendlyId - identifier of friendly agent for which event
     *        is created.
     *
     * @return Returns created instance of {@link FriendlyAgentMoveEvent} class.
     */
    FriendlyAgentMoveEvent createFriendlyMoveEvent(int friendlyId)
    {
      return EventTypesUtils.createFriendlyAgentMoveEvent(startTime, friendlyId,
          x, y, speedX, speedY);
    }
  }

  /**
   * Class storing data for events regarding movement of enemy agents.
   *
   * @author M.Olszewski
   */
  private final class EnemyAgentMoveData extends MoveData
  {
    /**
     * Creates instance of {@link EnemyAgentMoveData} class using data
     * from specified event.
     *
     * @param moveEventIndex - movement event index.
     * @param moveEvent - the specified {@link EnemyAgentMoveEvent}.
     */
    EnemyAgentMoveData(int moveEventIndex, EnemyAgentMoveEvent moveEvent)
    {
      super(moveEventIndex, moveEvent.getTime(),
          moveEvent.getDestinationX(), moveEvent.getDestinationY(),
          moveEvent.getSpeedX(), moveEvent.getSpeedY());
    }

    /**
     * Sets data of this {@link EnemyAgentMoveData} object to one
     * obtained from specified event.
     *
     * @param moveEventIndex - movement event index.
     * @param moveEvent - the specified {@link EnemyAgentMoveEvent}.
     */
    void set(int moveEventIndex, EnemyAgentMoveEvent moveEvent)
    {
      set(moveEventIndex, moveEvent.getTime(),
          moveEvent.getDestinationX(), moveEvent.getDestinationY(),
          moveEvent.getSpeedX(), moveEvent.getSpeedY());
    }

    /**
     * Creates instance of {@link EnemyAgentMoveEvent} class using
     * data stored inside this {@link EnemyAgentMoveData} object.
     *
     * @param enemyId - identifier of enemy agent for which event
     *        is created.
     *
     * @return Returns created instance of {@link EnemyAgentMoveEvent} class.
     */
    EnemyAgentMoveEvent createEnemyMoveEvent(int enemyId)
    {
      return EventTypesUtils.createEnemyAgentMoveEvent(startTime, enemyId,
          x, y, speedX, speedY);
    }
  }

  /**
   * Class storing data for events regarding movement of projectile.
   *
   * @author M.Olszewski
   */
  private class ProjectileMoveData extends MoveData
  {
    /**
     * Creates instance of {@link ProjectileMoveData} class using data
     * from specified event.
     *
     * @param moveEventIndex - movement event index.
     * @param moveEvent - the specified {@link ProjectileMoveEvent}.
     */
    ProjectileMoveData(int moveEventIndex, ProjectileMoveEvent moveEvent)
    {
      super(moveEventIndex, moveEvent.getTime(),
          moveEvent.getDestinationX(), moveEvent.getDestinationY(),
          moveEvent.getSpeedX(), moveEvent.getSpeedY());
    }

    /**
     * Sets data of this {@link ProjectileMoveData} object to one
     * obtained from specified event.
     *
     * @param moveEventIndex - movement event index.
     * @param moveEvent - the specified {@link ProjectileMoveEvent}.
     */
    void set(int moveEventIndex, ProjectileMoveEvent moveEvent)
    {
      set(moveEventIndex, moveEvent.getTime(),
          moveEvent.getDestinationX(), moveEvent.getDestinationY(),
          moveEvent.getSpeedX(), moveEvent.getSpeedY());
    }

    /**
     * Creates instance of {@link ProjectileMoveEvent} class using
     * data stored inside this {@link ProjectileMoveData} object.
     *
     * @param projectileId - identifier of projectile for which event
     *        is created.
     *
     * @return Returns created instance of {@link ProjectileMoveEvent} class.
     */
    ProjectileMoveEvent createProjectileMoveEvent(int projectileId)
    {
      return EventTypesUtils.createProjectileMoveEvent(startTime,
          projectileId, x, y, speedX, speedY);
    }
  }

  /**
   * Test.
   *
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    {
      EventsRepositoryBuilder b1 = new EventsRepositoryBuilderImpl(1);
      b1.addEvent(EventTypesUtils.createEnemyAgentHitEvent(90, 3, 1));
      b1.addEvent(EventTypesUtils.createProjectileDestroyedEvent(95L, 4, 80.0, 95.0));
      b1.addEvent(EventTypesUtils.createFriendlyAgentDestroyedEvent(100,1));
      b1.addEvent(EventTypesUtils.createFriendlyAgentHitEvent(110, 2, 20));
      b1.addEvent(EventTypesUtils.createProjectileDestroyedEvent(120L, 5, 100.0, 95.0));
      b1.addEvent(EventTypesUtils.createEnemyAgentDestroyedEvent(130L, 3, 1));

      EventsRepositoryBuilder b2 = new EventsRepositoryBuilderImpl(2);
      b2.addEvent(EventTypesUtils.createFriendlyAgentHitEvent(90, 3, 15));
      b2.addEvent(EventTypesUtils.createProjectileDestroyedEvent(95, 6, 80.0, 95.0));
      b2.addEvent(EventTypesUtils.createEnemyAgentDestroyedEvent(100L, 1, 22));
      b2.addEvent(EventTypesUtils.createEnemyAgentHitEvent(110, 2, 22));
      b2.addEvent(EventTypesUtils.createProjectileDestroyedEvent(120, 8, 100.0, 95.0));
      b2.addEvent(EventTypesUtils.createFriendlyAgentDestroyedEvent(130, 3));

      EventsRepository repository1 = b1.build();
      System.out.println("ev.count=" + repository1.getEventsCount());
      for (int i = 0; i < repository1.getEventsCount(); i++)
      {
        System.out.println(repository1.getEvent(i));
      }

      EventsRepository repository2 = b2.build();
      System.out.println("ev.count=" + repository2.getEventsCount());
      for (int i = 0; i < repository2.getEventsCount(); i++)
      {
        System.out.println(repository2.getEvent(i));
      }
    }
    {
      EventsRepositoryBuilder b1 = new EventsRepositoryBuilderImpl(1);

      b1.addEvent(EventTypesUtils.createProjectileShotEvent(90, "BABA", 101, 85.2, 110.31, 20.0, 21.0, 22));
      b1.addEvent(EventTypesUtils.createFriendlyAgentMoveEvent(100, 1, 5.2, 10.31, 10.0, 11.0));
      b1.addEvent(EventTypesUtils.createProjectileMoveEvent(100, 101, 115.2, 110.31, 20.0, 21.0));
      b1.addEvent(EventTypesUtils.createFriendlyAgentMoveEvent(110, 1, 5.3, 10.34, 10.0, 11.0));
      b1.addEvent(EventTypesUtils.createProjectileMoveEvent(110, 101, 115.2, 110.31, 20.0, 21.0));
      b1.addEvent(EventTypesUtils.createFriendlyAgentBlockedEvent(115, 1, 5.58, 11.37));
      b1.addEvent(EventTypesUtils.createFriendlyAgentMoveEvent(120, 1, 5.43, 10.97, 11.0, 14.0));
      b1.addEvent(EventTypesUtils.createProjectileMoveEvent(120, 101, 115.2, 110.31, 20.0, 21.0));
      b1.addEvent(EventTypesUtils.createFriendlyAgentMoveEvent(130, 1, 5.58, 11.37, 11.0, 14.0));
      b1.addEvent(EventTypesUtils.createProjectileMoveEvent(130, 101, 115.2, 110.31, 20.0, 21.0));
      b1.addEvent(EventTypesUtils.createFriendlyAgentBlockedEvent(135, 1, 5.58, 11.37));
//      b1.addEvent(EventTypesUtils.createProjectileGoneEvent(135, 101, 115.2, 110.31, 20.0, 21.0));

      EventsRepositoryBuilder b2 = new EventsRepositoryBuilderImpl(2);

      b2.addEvent(EventTypesUtils.createEnemyAgentMoveEvent(100, 1, 5.2, 10.31, 10.0, 11.0));
      b2.addEvent(EventTypesUtils.createEnemyAgentGoneEvent(120, 1, 5.58, 11.37, 11.0, 14.0));
      b2.addEvent(EventTypesUtils.createEnemyAgentSeenEvent(130, 1, 5.58, 11.37, 11.0, 14.0));
      b2.addEvent(EventTypesUtils.createEnemyAgentMoveEvent(140, 1, 5.3, 10.34, 10.0, 11.0));
      b2.addEvent(EventTypesUtils.createEnemyAgentMoveEvent(142, 1, 5.3, 11.34, 10.0, 11.0));
      b2.addEvent(EventTypesUtils.createEnemyAgentMoveEvent(150, 1, 5.43, 11.97, 11.0, 11.0));
      b2.addEvent(EventTypesUtils.createEnemyAgentMoveEvent(152, 1, 5.43, 12.97, 11.0, 11.0));
      b2.addEvent(EventTypesUtils.createEnemyAgentMoveEvent(160, 1, 5.57, 13.97, 12.0, 11.0));
      b2.addEvent(EventTypesUtils.createEnemyAgentMoveEvent(162, 1, 5.57, 13.97, 12.0, 11.0));

      EventsRepository repository1 = b1.build();
      System.out.println("events count=" + repository1.getEventsCount());
      for (int i = 0; i < repository1.getEventsCount(); i++)
      {
        System.out.println(repository1.getEvent(i));
      }

      EventsRepository repository2 = b2.build();
      System.out.println("events count=" + repository2.getEventsCount());
      for (int i = 0; i < repository2.getEventsCount(); i++)
      {
        System.out.println(repository2.getEvent(i));
      }
    }
  }
}