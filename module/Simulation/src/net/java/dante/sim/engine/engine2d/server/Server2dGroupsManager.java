/*
 * Created on 2006-08-10
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.engine2d.server;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.java.dante.sim.command.CommandsRepository;
import net.java.dante.sim.common.Dbg;
import net.java.dante.sim.data.ObjectStateChangedListener;
import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.SimulationData;
import net.java.dante.sim.data.map.MapTileType;
import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.agent.ServerAgent;
import net.java.dante.sim.data.object.agent.ServerAgentState;
import net.java.dante.sim.data.object.projectile.ServerProjectile;
import net.java.dante.sim.data.object.projectile.ServerProjectileState;
import net.java.dante.sim.engine.EngineObject;
import net.java.dante.sim.engine.collision.CollisionDetector;
import net.java.dante.sim.engine.collision.CollisionListener;
import net.java.dante.sim.engine.engine2d.Engine2dExplosionsGroup;
import net.java.dante.sim.engine.engine2d.Engine2dInitData;
import net.java.dante.sim.engine.engine2d.Engine2dStillObject;
import net.java.dante.sim.engine.engine2d.Engine2dTilesGroup;
import net.java.dante.sim.engine.engine2d.SpritesRepository;
import net.java.dante.sim.engine.graphics.java2d.Java2dContext;
import net.java.dante.sim.event.EventsRepository;
import net.java.dante.sim.event.types.EventTypesUtils;
import net.java.dante.sim.io.CommandsData;
import net.java.dante.sim.io.GroupAbandonSimulationData;
import net.java.dante.sim.util.Colors;



/**
 * Groups manager, managing all objects groups from
 * {@link net.java.dante.sim.engine.engine2d.server.ServerEngine2d}.
 *
 * @author M.Olszewski
 */
class Server2dGroupsManager
{
  /** Collision detector. */
  private CollisionDetector collisionDetector = new CollisionDetector();
  /** Default collision detector. */
  private Server2dCollisionModule collisionModule = new Server2dCollisionModule();
  /** Visibility manager.  */
  VisibilityManager visibilityManager;
  /** Groups of agents. */
  Map<Integer, Server2dAgentsGroup> agentsGroups =
    new HashMap<Integer, Server2dAgentsGroup>();
  /** Projectiles group. */
  Server2dProjectilesGroup projectiles;
  /** Explosions group. */
  Engine2dExplosionsGroup explosions;
  /** Tiles group. */
  private Engine2dTilesGroup tiles;
  /** Graphics context. */
  private Java2dContext context;
  /** Sprites repository. */
  SpritesRepository sprites;
  /** Current time holder. */
  CurrentTimeHolder timeHolder;


  /**
   * Creates instance of {@link Server2dGroupsManager} class with specified parameters.
   *
   * @param spritesRepository - sprites repository.
   * @param graphicsContext - graphics context.
   * @param currentTimeHolder - current time holder.
   */
  Server2dGroupsManager(Java2dContext graphicsContext,
      SpritesRepository spritesRepository,
      CurrentTimeHolder currentTimeHolder)
  {
    if (graphicsContext == null)
    {
      throw new NullPointerException("Specified graphicsContext is null!");
    }
    if (spritesRepository == null)
    {
      throw new NullPointerException("Specified spritesRepository is null!");
    }
    if (currentTimeHolder == null)
    {
      throw new NullPointerException("Specified currentTimeHolder is null!");
    }

    sprites = spritesRepository;
    context = graphicsContext;
    timeHolder = currentTimeHolder;

    visibilityManager = new VisibilityManager(currentTimeHolder);
  }


  /**
   * Initializes all groups fetching data from the specified
   * {@link SimulationData} object.
   *
   * @param simData - the specified {@link SimulationData} object.
   * @param engineInitData - initialization data for engine.
   */
  void init(SimulationData simData, Engine2dInitData engineInitData)
  {
    if (simData == null)
    {
      throw new NullPointerException("Specified simData is null!");
    }
    if (engineInitData == null)
    {
      throw new NullPointerException("Specified engineInitData is null!");
    }

    // Initialize collision detector
    initCollisionDetector(simData.getMediator());

    // Initialize obstacle tiles
    initializeTiles(simData.getMap(), engineInitData.getGlobalData().getMapTileSize());

    // Initialize group of agents
    initAgentsGroups(simData.getRoot());

    // Initialize projectiles group
    projectiles = new Server2dProjectilesGroup(
        collisionDetector, collisionModule, visibilityManager);


    // Initialize explosions group
    explosions = new Engine2dExplosionsGroup(sprites);

    // Initialize objects state listener
    simData.addObjectAddedListener(new Engine2dObjectStateListener());

    // Initialize all visibility records - first check required to detect
    // still enemy agents at the very start of simulation
    initVisibilityRecords();
  }

  /**
   * Initializes collision detector and default collision module.
   *
   * @param mediator - objects mediator used by collision listener.
   */
  private void initCollisionDetector(ObjectsMediator mediator)
  {
    collisionModule.addListener(new Engine2dCollisionListener(mediator));
    collisionDetector.registerModule(collisionModule);
  }

  /**
   * Initializes agents - it is searching for instances of {@link ServerAgent}
   * class in simulation objects tree and if one is found it is added to
   * its group with proper sprite, obtained from {@link SpritesRepository}
   * object.
   *
   * @param root - root of simulation objects tree.
   */
  private void initAgentsGroups(SimulationObject root)
  {
    Server2dAgentsGroup[] groups =
        Server2dAgentsGroupsBuilder.getInstance().build(root, sprites);

    for (int i = 0; i < groups.length; i++)
    {
      Server2dAgentsGroup group = groups[i];
      group.initDependencies(ColorsRepository.getColor(i),
          collisionDetector, collisionModule, visibilityManager, timeHolder);
      agentsGroups.put(Integer.valueOf(group.getGroupId()), group);
      visibilityManager.registerRecord(group.getVisibilityRecord());
    }
  }

  /**
   * Initializes {@link Engine2dTilesGroup} object containing obstacle tiles
   * from the specified {@link SimulationMap} object. Each tile is
   * created with the specified size.
   * All created obstacle tiles are added to the {@link CollisionDetector}
   * object.
   *
   * @param map - the specified {@link SimulationMap} object.
   * @param tileSize - size of each tile.
   */
  private void initializeTiles(SimulationMap map, ObjectSize tileSize)
  {
    List<Engine2dStillObject> tilesList = new ArrayList<Engine2dStillObject>();

    for (int i = 0, rows = map.getRows(), columns = map.getColumns(); i < rows; i++)
    {
      for (int j = 0; j < columns; j++)
      {
        MapTileType tileType = map.getTile(i, j);
        if (tileType == MapTileType.OBSTACLE)
        {
          Engine2dStillObject tile = new Engine2dStillObject(
              sprites.getTileSprite(), (tileSize.getWidth() * j),
              (tileSize.getHeight() * i), tileSize);
          tilesList.add(tile);
          collisionDetector.addObject(tile);
        }
      }
    }
    tiles = new Engine2dTilesGroup(tilesList);
  }

  private void initVisibilityRecords()
  {
    for (Integer groupId : agentsGroups.keySet())
    {
      visibilityManager.initVisibilityRecords(agentsGroups.get(groupId));
    }
  }

  /**
   * Process the specified {@link CommandsData} containing input data
   * for one of agents groups.
   *
   * @param commandsData - the specified {@link CommandsData}
   *        containing input data for one of agents groups.
   */
  void processCommands(CommandsData commandsData)
  {
    CommandsRepository repository = commandsData.getRepository();
    Server2dAgentsGroup group = agentsGroups.get(Integer.valueOf(repository.getGroupId()));
    if (group != null)
    {
      group.processCommands(repository);
    }
    else
    {
      if (Dbg.DBGW) Dbg.warning("Invalid group identifier detected in CommandsData: " + repository.getGroupId());
    }
  }

  /**
   * Method invoked when some group abandons simulation.
   *
   * @param abandonedGroupData - data of group which abandoned simulation.
   */
  void groupAbandoned(GroupAbandonSimulationData abandonedGroupData)
  {
    Integer abandonedGroupId = Integer.valueOf(abandonedGroupData.getGroupId());
    Server2dAgentsGroup group = agentsGroups.get(abandonedGroupId);
    if (group != null)
    {
      // Mark group as abandoned, create proper events
      group.groupAbandonded();

      // Remove group
      agentsGroups.remove(abandonedGroupId);
    }
    else
    {
      if (Dbg.DBGW) Dbg.warning("Invalid group identifier detected in CommandsData: " + abandonedGroupData.getGroupId());
    }
  }

  /**
   * Updates all groups.
   *
   * @param delta - time that has passed since last update.
   */
  void update(long delta)
  {
    projectiles.update(delta);

    for (Integer groupId : agentsGroups.keySet())
    {
      agentsGroups.get(groupId).update(delta);
    }

    // Remove marked projectiles _now_ so 'destroyed' events will be generated
    // with correct time
    projectiles.removeMarkedProjectiles();

    updateVisibilityRecords();

    explosions.update(delta);

    for (Integer groupId : agentsGroups.keySet())
    {
      agentsGroups.get(groupId).updateStatistics();
    }
  }

  /**
   * Updates all visibility records for all agents and projectiles once more.
   */
  private void updateVisibilityRecords()
  {
    for (Integer groupId : agentsGroups.keySet())
    {
      Server2dAgentsGroup group = agentsGroups.get(groupId);
      visibilityManager.updateRecordsForAgentsGroups(group);
    }

    visibilityManager.updateRecordsForProjectilesGroups(projectiles);
  }

  /**
   * Renders all groups.
   */
  void render()
  {
    for (Integer groupId : agentsGroups.keySet())
    {
      agentsGroups.get(groupId).render(context);
    }

    projectiles.render();
    tiles.render();
    explosions.render();

    for (Integer groupId : agentsGroups.keySet())
    {
      agentsGroups.get(groupId).renderIDs(context);
    }
  }

  /**
   * Gets number of active groups - active group contains at least one
   * active agent.
   *
   * @return Returns number of active groups.
   */
  int getActiveGroupsCount()
  {
    int activeGroupsCnt = 0;
    for (Integer groupId : agentsGroups.keySet())
    {
      Server2dAgentsGroup group = agentsGroups.get(groupId);
      if (group.getActiveAgentsCount() > 0)
      {
        activeGroupsCnt++;
      }
    }

    return activeGroupsCnt;
  }

  /**
   * Checks whether agents group with the specified identifier is active.
   *
   * @param groupId the specified identifier.
   *
   * @return Returns <code>true</code> if group with the specified identifier
   *         is active, <code>false</code> otherwise.
   */
  boolean isGroupActive(Integer groupId)
  {
    return (agentsGroups.get(groupId).getActiveAgentsCount() > 0);
  }

  /**
   * Builds all events repositories. This method returns map with agents groups
   * identifiers and agents events repositories. Returned map
   * can contain <code>null</code> values, if no events repository was
   * created for specified group's identifier.
   *
   * @return Returns map with agents groups identifiers and
   *         agents events repositories.
   */
  Map<Integer, EventsRepository> buildEventsRepositories()
  {
    Map<Integer, EventsRepository> repositories = new HashMap<Integer, EventsRepository>(agentsGroups.size());

    for (Integer groupId : agentsGroups.keySet())
    {
      EventsRepository repository = agentsGroups.get(groupId).getEventsBuilder().build();
      repositories.put(groupId, repository);
    }

    return repositories;
  }

  /**
   * Builds events repositories for groups marked as not active after this
   * update. This method returns map with agents groups
   * identifiers and agents events repositories. Returned map
   * can contain <code>null</code> values, if no events repository was
   * created for specified group's identifier. This method may also return
   * <code>null</code> if no events repository was generated.
   *
   * @return Returns map with agents groups identifiers and
   *         built agents events repositories or <code>null</code> if
   *         no repository was created.
   */
  Map<Integer, EventsRepository> buildEventsRepositoriesForEliminatedGroups()
  {
    Map<Integer, EventsRepository> repositories = null;

    for (Integer groupId : agentsGroups.keySet())
    {
      Server2dAgentsGroup group = agentsGroups.get(groupId);
      if (group.isActive() && (group.getActiveAgentsCount() <= 0))
      {
        EventsRepository repository = group.getEventsBuilder().build();

        if (repositories == null)
        {
          repositories = new HashMap<Integer, EventsRepository>(agentsGroups.size());
        }

        repositories.put(groupId, repository);

        group.notActive();
      }
    }

    return repositories;
  }

  /**
   * Creates array with objects holding statistics for agents groups.
   *
   * @return Returns array with objects holding statistics for agents groups.
   */
  GroupStatistics[] buildGroupsStatistics()
  {
    List<GroupStatistics> statistics = new ArrayList<GroupStatistics>(agentsGroups.size());

    for (Integer groupId : agentsGroups.keySet())
    {
      GroupStatistics stats = agentsGroups.get(groupId).getStatistics();

      stats.calculateTotalPoints();

      statistics.add(stats);
    }

    return statistics.toArray(new GroupStatistics[statistics.size()]);
  }

  /**
   * Collision listener used by this engine implementation.
   *
   * @author M.Olszewski
   */
  private class Engine2dCollisionListener implements CollisionListener
  {
    /** Objects mediator. */
    private ObjectsMediator mediator;


    /**
     * Creates instance of {@link Engine2dCollisionListener} class.
     *
     * @param objectsMediator - objects mediator.
     */
    Engine2dCollisionListener(ObjectsMediator objectsMediator)
    {
      if (objectsMediator == null)
      {
        throw new NullPointerException("Specified objectsMediator is null!");
      }

      mediator = objectsMediator;
    }


    /**
     * @see net.java.dante.sim.engine.collision.CollisionListener#collisionDetected(net.java.dante.sim.engine.EngineObject, net.java.dante.sim.engine.EngineObject)
     */
    public void collisionDetected(EngineObject o1, EngineObject o2)
    {
      // agent-to-agent collision
      if ((o1 instanceof Server2dAgent) && (o2 instanceof Server2dAgent))
      {
        agentToAgentCollision((Server2dAgent)o1, (Server2dAgent)o2);
      }
      // agent-to-wall collision
      else if ((o1 instanceof Server2dAgent) && (o2 instanceof Engine2dStillObject))
      {
        agentToWallCollision((Server2dAgent)o1, (Engine2dStillObject)o2);
      }
      else if ((o2 instanceof Server2dAgent) && (o1 instanceof Engine2dStillObject))
      {
        agentToWallCollision((Server2dAgent)o2, (Engine2dStillObject)o1);
      }
      // agent-to-projectile collision
      else if ((o1 instanceof Server2dAgent) && (o2 instanceof Server2dProjectile))
      {
        agentToProjectileCollision((Server2dAgent)o1, (Server2dProjectile)o2);
      }
      else if ((o2 instanceof Server2dAgent) && (o1 instanceof Server2dProjectile))
      {
        agentToProjectileCollision((Server2dAgent)o2, (Server2dProjectile)o1);
      }
      // projectile-to-wall collision
      else if ((o1 instanceof Server2dProjectile) && (o2 instanceof Engine2dStillObject))
      {
        projectileToWallCollision((Server2dProjectile)o1, (Engine2dStillObject)o2);
      }
      else if ((o2 instanceof Server2dProjectile) && (o1 instanceof Engine2dStillObject))
      {
        projectileToWallCollision((Server2dProjectile)o2, (Engine2dStillObject)o1);
      }
      // projectile-to-projectile collision
      else if ((o1 instanceof Server2dProjectile) && (o2 instanceof Server2dProjectile))
      {
        projectileToProjectileCollision((Server2dProjectile)o1, (Server2dProjectile)o2);
      }
    }

    /**
     * Performs action for situation where two agents are colliding.
     *
     * @param movingAgent - first colliding agent (moving one).
     * @param collidedAgent - second colliding agent (not moving one).
     */
    private void agentToAgentCollision(Server2dAgent movingAgent,
        Server2dAgent collidedAgent)
    {
      if (Dbg.DBG1)
      {
        Dbg.write("agent to agent collision!");
        Dbg.write("movingAgent=" + movingAgent);
        Dbg.write("collidedAgent=" + collidedAgent);
      }

      ServerAgent serverAgent1 = movingAgent.getAgent();
      ServerAgentState agentState1 = (ServerAgentState)serverAgent1.getData();

      movingAgent.reversePosition();
      agentsGroups.get(Integer.valueOf(serverAgent1.getGroupId())).getEventsBuilder().addEvent(
          EventTypesUtils.createFriendlyAgentBlockedEvent(
              timeHolder.getCurrentTime(), serverAgent1.getId(),
              agentState1.getX(), agentState1.getY()));
    }

    /**
     * Performs action for situation where agent is colliding with wall.
     *
     * @param agent - agent colliding with wall.
     * @param wall - wall colliding with agent.
     */
    private void agentToWallCollision(Server2dAgent agent, Engine2dStillObject wall)
    {
      if (Dbg.DBG1)
      {
        Dbg.write("agent to wall collision!");
        Dbg.write("agent=" + agent);
        Dbg.write("wall=" + wall);
      }

      ServerAgent serverAgent = agent.getAgent();
      ServerAgentState agentState = (ServerAgentState)agent.getAgent().getData();

      agent.reversePosition();
      agentsGroups.get(Integer.valueOf(serverAgent.getGroupId())).getEventsBuilder().addEvent(
          EventTypesUtils.createFriendlyAgentBlockedEvent(
              timeHolder.getCurrentTime(), serverAgent.getId(),
              agentState.getX(), agentState.getY()));
    }

    /**
     * Performs action for situation where agent is colliding with projectile.
     *
     * @param agent - agent colliding with projectile.
     * @param projectile - projectile colliding with agent.
     */
    private void agentToProjectileCollision(Server2dAgent agent, Server2dProjectile projectile)
    {
      if (Dbg.DBG1)
      {
        Dbg.write("agent to projectile collision!");
        Dbg.write("agent=" + agent);
        Dbg.write("projectile=" + projectile);
      }

      // Agent
      ServerAgent      serverAgent      = agent.getAgent();
      ServerProjectile serverProjectile = projectile.getProjectile();

      ServerAgentState      agentState      = (ServerAgentState)agent.getAgent().getData();
      ServerProjectileState projectileState = (ServerProjectileState)serverProjectile.getData();
      int damage = projectileState.getDamage();

      agentState.setCurrentHitPoints(agentState.getCurrentHitPoints() - damage);

      Server2dAgentsGroup hitGroup = agentsGroups.get(Integer.valueOf(serverAgent.getGroupId()));
      hitGroup.getEventsBuilder().addEvent(
          EventTypesUtils.createFriendlyAgentHitEvent(
              timeHolder.getCurrentTime(), serverAgent.getId(), damage));
      visibilityManager.generateEnemyAgentHitEvents(serverAgent.getGroupId(),
                                                    agent,
                                                    projectileState.getOwnerId());

      Server2dAgentsGroup shootingGroup = agentsGroups.get(Integer.valueOf(projectileState.getOwnerGroupId()));

      if (shootingGroup.getGroupId() != hitGroup.getGroupId())
      {
        // We've hit enemy
        shootingGroup.getStatistics().enemyAgentHit();
        // Enemy received damage
        hitGroup.getStatistics().damageReceived(damage);
      }
      else
      {
        // Oops, we've hit friendly agent
        shootingGroup.getStatistics().friendlyFireHit();
        // We've received damage
        shootingGroup.getStatistics().damageReceived(damage);
      }

      if (agentState.getCurrentHitPoints() <= 0)
      {
        visibilityManager.generateEnemyAgentKilledEvents(serverAgent.getGroupId(),
                                                         agent,
                                                         projectileState.getOwnerId());

        if (shootingGroup.getGroupId() != hitGroup.getGroupId())
        {
          // We've destroyed enemy!
          shootingGroup.getStatistics().enemyAgentDestroyed();
          // Enemy is destroyed
          hitGroup.getStatistics().friendlyAgentDestroyed();
        }
        else
        {
          // Greatest OOOooops, we've destroyed friendly agent
          shootingGroup.getStatistics().friendlyFireDestroyed();
        }

        mediator.objectDestroyed(serverAgent);
      }

      // Projectile
      mediator.objectDestroyed(serverProjectile);
    }

    /**
     * Performs action for situation where projectile is colliding with wall.
     *
     * @param projectile - projectile colliding with wall.
     * @param wall - wall colliding with projectile.
     */
    private void projectileToWallCollision(Server2dProjectile projectile, Engine2dStillObject wall)
    {
      if (Dbg.DBG1)
      {
        Dbg.write("projectile to wall collision!");
        Dbg.write("projectile=" + projectile);
        Dbg.write("wall=" + wall);
      }

      mediator.objectDestroyed(projectile.getProjectile());
    }

    /**
     * Performs action for situation where projectile is colliding with projectile.
     *
     * @param projectile1 - first colliding projectile.
     * @param projectile2 - second colliding projectile.
     */
    private void projectileToProjectileCollision(Server2dProjectile projectile1, Server2dProjectile projectile2)
    {
      if (Dbg.DBG1)
      {
        Dbg.write("projectile to projectile collision!");
        Dbg.write("projectile1=" + projectile1);
        Dbg.write("projectile2=" + projectile2);
      }

      mediator.objectDestroyed(projectile1.getProjectile());
      mediator.objectDestroyed(projectile2.getProjectile());
    }
  }

  /**
   * Listener notified about new objects added to simulation data.
   *
   * @author M.Olszewski
   */
  class Engine2dObjectStateListener implements ObjectStateChangedListener
  {
    /**
     * @see net.java.dante.sim.data.ObjectStateChangedListener#objectAdded(net.java.dante.sim.data.object.SimulationObject)
     */
    public void objectAdded(SimulationObject object)
    {
      if (object instanceof ServerProjectile)
      {
        ServerProjectile serverProjectile = (ServerProjectile)object;
        Server2dProjectile server2dProjectile =
            new Server2dProjectile(serverProjectile,
                sprites.getProjectileMoveSprites(),
                sprites.getProjectileMoveDelay());

        projectiles.addProjectile(object.getId(), server2dProjectile);

        ServerProjectileState state = (ServerProjectileState)serverProjectile.getData();

        Server2dAgentsGroup agentsGroup = agentsGroups.get(Integer.valueOf(state.getOwnerGroupId()));

        agentsGroup.getVisibilityRecord().update(server2dProjectile);
        agentsGroup.getStatistics().projectileShot();
        agentsGroup.getEventsBuilder().addEvent(
            EventTypesUtils.createProjectileShotEvent(
                timeHolder.getCurrentTime(), state.getType(), object.getId(),
                state.getX(), state.getY(), state.getSpeedX(), state.getSpeedY(),
                state.getOwnerId()));
      }
    }

    /**
     * @see net.java.dante.sim.data.ObjectStateChangedListener#objectRemoved(net.java.dante.sim.data.object.SimulationObject)
     */
    public void objectRemoved(SimulationObject object)
    {
      if (object instanceof ServerProjectile)
      {
        projectiles.removeProjectile(object.getId());

        ServerProjectileState state = (ServerProjectileState)object.getData();
        explosions.addProjectileExplosion(state.getX(), state.getY(), state.getSize(),
            state.getExplosionRange());
      }
      else if (object instanceof ServerAgent)
      {
        ServerAgent agent = (ServerAgent)object;
        Server2dAgentsGroup agentGroup = agentsGroups.get(Integer.valueOf(agent.getGroupId()));
        agentGroup.removeAgent(agent.getId());

        ServerAgentState agentState = (ServerAgentState)agent.getData();
        explosions.addAgentExplosion(agentState.getX(), agentState.getY(),
            agentState.getSize(), agentState.getSize().getWidth());
      }
    }
  }

  /**
   * Repository with colors for agents groups.
   *
   * @author M.Olszewski
   */
  private static class ColorsRepository
  {
    /** Colors for groups. */
    private static final Color[] COLORS = {
      new Color(Colors.SANDY_BROWN),
      new Color(Colors.CYAN),
      new Color(Colors.MAGENTA),
      new Color(Colors.RED),
      new Color(Colors.KHAKI),
      new Color(Colors.STEEL_BLUE),
      new Color(Colors.GHOST_WHITE),
      new Color(Colors.FIRE_BRICK),
      new Color(Colors.BLANCHED_ALMOND),
      new Color(Colors.CHARTREUSE),
      new Color(Colors.DARK_SLATE_BLUE),
      new Color(Colors.HOT_PINK),
      new Color(Colors.AQUAMARINE),
      new Color(Colors.TAN),
      new Color(Colors.DARK_ORANGE),
      new Color(Colors.TEAL),
    };

    /**
     * Gets color from the specified index. If index exceeds length of
     * array with colors, valid index is obtained by calculating modulo
     * of index and length of array with colors.
     *
     * @param index - the specified index.
     *
     * @return Returns color from the specified index.
     */
    static Color getColor(int index)
    {
      return COLORS[index % COLORS.length];
    }
  }
}