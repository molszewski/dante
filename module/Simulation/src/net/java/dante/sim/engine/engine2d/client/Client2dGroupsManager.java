/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.engine2d.client;

import java.util.ArrayList;
import java.util.List;

import net.java.dante.sim.common.Dbg;
import net.java.dante.sim.data.ObjectStateChangedListener;
import net.java.dante.sim.data.SimulationData;
import net.java.dante.sim.data.map.MapTileType;
import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.agent.ClientEnemyAgent;
import net.java.dante.sim.data.object.agent.ClientEnemyAgentState;
import net.java.dante.sim.data.object.agent.ClientFriendlyAgent;
import net.java.dante.sim.data.object.agent.ClientFriendlyAgentState;
import net.java.dante.sim.data.object.projectile.ClientProjectile;
import net.java.dante.sim.data.object.projectile.ClientProjectileState;
import net.java.dante.sim.engine.collision.CollisionDetector;
import net.java.dante.sim.engine.engine2d.Engine2dExplosionsGroup;
import net.java.dante.sim.engine.engine2d.Engine2dStillObject;
import net.java.dante.sim.engine.engine2d.Engine2dTilesGroup;
import net.java.dante.sim.engine.engine2d.SpritesRepository;
import net.java.dante.sim.engine.graphics.java2d.Java2dContext;
import net.java.dante.sim.event.Event;
import net.java.dante.sim.event.EventsRepository;
import net.java.dante.sim.event.types.EnemyAgentEvent;
import net.java.dante.sim.event.types.FriendlyAgentEvent;
import net.java.dante.sim.event.types.ProjectileEvent;
import net.java.dante.sim.io.UpdateData;


/**
 * Groups manager, managing all objects groups from
 * {@link net.java.dante.sim.engine.engine2d.client.ClientEngine2d}.
 *
 * @author M.Olszewski
 */
class Client2dGroupsManager
{
  /** Graphics context. */
  private Java2dContext context;
  /** Sprites repository. */
  SpritesRepository sprites;
  /** Friendly agents group. */
  Client2dFriendlyAgentsGroup friendlyGroup;
  /** Enemy agents group. */
  Client2dEnemyAgentsGroup enemyGroup;
  /** Projectiles group. */
  Client2dProjectilesGroup projectiles;
  /** Explosions group. */
  Engine2dExplosionsGroup explosions;
  /** Tiles group. */
  private Engine2dTilesGroup tiles;


  /**
   * Creates instance of {@link Client2dGroupsManager} class with specified parameters.
   *
   * @param spritesRepository - sprites repository.
   * @param graphicsContext - graphics context.
   */
  Client2dGroupsManager(Java2dContext graphicsContext,
      SpritesRepository spritesRepository)
  {
    if (graphicsContext == null)
    {
      throw new NullPointerException("Specified graphicsContext is null!");
    }
    if (spritesRepository == null)
    {
      throw new NullPointerException("Specified spritesRepository is null!");
    }

    sprites = spritesRepository;
    context = graphicsContext;
  }


  /**
   * Initializes all groups fetching data from the specified
   * {@link SimulationData} object.
   *
   * @param simData - the specified {@link SimulationData} object.
   * @param engineInitData - initialization data for engine.
   */
  void init(SimulationData simData, ClientEngine2dInitData engineInitData)
  {
    if (simData == null)
    {
      throw new NullPointerException("Specified simData is null!");
    }
    if (engineInitData == null)
    {
      throw new NullPointerException("Specified engineInitData is null!");
    }

    // Initialize agents group (enemy and friendly)
    initAgentsGroups(engineInitData.getGroupId(), simData.getRoot());

    // Initialize projectiles group
    projectiles = new Client2dProjectilesGroup(engineInitData.getCreator(),
        simData.getMediator());

    // Initialize explosions group
    explosions = new Engine2dExplosionsGroup(sprites);

    // Initialize obstacle tiles
    initializeTiles(simData.getMap(), engineInitData.getGlobalData().getMapTileSize());

    // Initialize objects state listener
    simData.addObjectAddedListener(new ClientObjectStateListener());
  }

  /**
   * Initializes {@link Client2dFriendlyAgentsGroup} and
   * {@link Client2dEnemyAgentsGroup} objects by data fetched from
   * simulation objects tree.
   *
   * @param friendlyGroupId - friendly agents group's identifier.
   * @param root - root of simulation objects tree.
   */
  private void initAgentsGroups(int friendlyGroupId, SimulationObject root)
  {
    Client2dAgentsGroupsBuilder builder =
        new Client2dAgentsGroupsBuilder(friendlyGroupId, sprites);

    // Build
    builder.build(root);

    // Assign proper groups
    enemyGroup = builder.getEnemyGroup();
    friendlyGroup = builder.getFriendlyGroup();
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
        }
      }
    }
    tiles = new Engine2dTilesGroup(tilesList);
  }

  /**
   * Process the specified {@link UpdateData} containing events
   * for agents group.
   *
   * @param updateData - the specified {@link UpdateData}
   *        containing events for agents group.
   */
  void processInput(UpdateData updateData)
  {
    EventsRepository repository = updateData.getRepository();

    if (repository.getGroupId() == friendlyGroup.getGroupId())
    {
      for (int i = 0, eventsCount = repository.getEventsCount(); i < eventsCount; i++)
      {
        Event event = repository.getEvent(i);

        if (event instanceof FriendlyAgentEvent)
        {
          friendlyGroup.processEvents((FriendlyAgentEvent)event);
        }
        else if (event instanceof EnemyAgentEvent)
        {
          enemyGroup.processEvents((EnemyAgentEvent)event);
        }
        else if (event instanceof ProjectileEvent)
        {
          projectiles.processEvents((ProjectileEvent)event);
        }
      }
    }
    else
    {
      if (Dbg.DBGW) Dbg.warning("Invalid group identifier detected in UpdateData: " + repository.getGroupId());
    }
  }

  /**
   * Updates all groups.
   *
   * @param delta - time that has passed since last update.
   */
  void update(long delta)
  {
    friendlyGroup.update(delta);
    enemyGroup.update(delta);
    projectiles.update(delta);
    explosions.update(delta);
  }

  /**
   * Renders all groups.
   */
  void render()
  {
    friendlyGroup.render();
    enemyGroup.render();
    projectiles.render();
    tiles.render();
    explosions.render();

    enemyGroup.renderIDs(context);
    friendlyGroup.renderIDs(context);
  }

  /**
   * Shifts time of all groups which requires this by specified amount of time.
   *
   * @param time - amount of time by which time of groups will be shifted.
   */
  void timeShift(long time)
  {
    friendlyGroup.timeShift(time);
    enemyGroup.timeShift(time);
    projectiles.timeShift(time);
  }

  /**
   * Listener notified about objects added/removed to/from simulation data.
   *
   * @author M.Olszewski
   */
  final class ClientObjectStateListener implements ObjectStateChangedListener
  {
    /**
     * @see net.java.dante.sim.data.ObjectStateChangedListener#objectAdded(net.java.dante.sim.data.object.SimulationObject)
     */
    public void objectAdded(SimulationObject object)
    {
      if (object instanceof ClientProjectile)
      {
        ClientProjectile clientProjectile = (ClientProjectile)object;
        Client2dProjectile client2dProjectile =
            new Client2dProjectile(clientProjectile,
                sprites.getProjectileMoveSprites(),
                sprites.getProjectileMoveDelay());

        projectiles.addProjectile(object.getId(), client2dProjectile);
      }
    }

    /**
     * @see net.java.dante.sim.data.ObjectStateChangedListener#objectRemoved(net.java.dante.sim.data.object.SimulationObject)
     */
    public void objectRemoved(SimulationObject object)
    {
      if (object instanceof ClientProjectile)
      {
        int projectileId = object.getId();
        ClientProjectileState projectileState = (ClientProjectileState)object.getData();

        if (!projectileState.isAlive())
        {
          explosions.addProjectileExplosion(
              projectileState.getX(), projectileState.getY(),
              projectileState.getSize(), projectileState.getExplosionRange());
        }

        projectiles.removeProjectile(projectileId);
      }
      else if (object instanceof ClientFriendlyAgent)
      {
        ClientFriendlyAgent agent = (ClientFriendlyAgent)object;
        friendlyGroup.removeAgent(agent.getId());

        ClientFriendlyAgentState agentState = (ClientFriendlyAgentState)agent.getData();
        explosions.addAgentExplosion(agentState.getX(), agentState.getY(),
            agentState.getSize(), agentState.getSize().getWidth());
      }
      else if (object instanceof ClientEnemyAgent)
      {
        ClientEnemyAgent agent = (ClientEnemyAgent)object;
        enemyGroup.removeAgent(agent.getId());

        ClientEnemyAgentState agentState = (ClientEnemyAgentState)agent.getData();
        explosions.addAgentExplosion(agentState.getX(), agentState.getY(),
            agentState.getSize(), agentState.getSize().getWidth());
      }
    }
  }
}