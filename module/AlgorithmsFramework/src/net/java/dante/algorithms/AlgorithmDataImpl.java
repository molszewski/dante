/*
 * Created on 2006-09-01
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms;

import java.util.ArrayList;
import java.util.List;

import net.java.dante.algorithms.data.AlgorithmData;
import net.java.dante.algorithms.data.ControlledAgentsData;
import net.java.dante.algorithms.data.EnemyAgentsData;
import net.java.dante.algorithms.data.ProjectilesData;
import net.java.dante.algorithms.data.WeaponData;
import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.template.types.AgentTemplateData;
import net.java.dante.sim.data.template.types.WeaponTemplateData;
import net.java.dante.sim.event.EventsRepository;
import net.java.dante.sim.io.ClientInputData;
import net.java.dante.sim.io.TimeSyncData;
import net.java.dante.sim.io.UpdateData;
import net.java.dante.sim.io.init.AgentsInitData;
import net.java.dante.sim.io.init.EnemyAgentInitData;
import net.java.dante.sim.io.init.EnemyAgentsGroupInitData;
import net.java.dante.sim.io.init.FriendlyAgentInitData;
import net.java.dante.sim.io.init.InitializationData;

/**
 * Implementation of {@link AlgorithmData} interface.
 *
 * @author M.Olszewski
 */
class AlgorithmDataImpl implements AlgorithmData
{
  /** Controlled group's identifier. */
  private int groupId;
  /** Simulation's map. */
  private SimulationMap map;
  /** Size of each tile. */
  private ObjectSize tileSize;
  /** Enemies data data storage. */
  private EnemiesDataImpl enemies;
  /** Controlled agents data storage. */
  private ControlledAgentsDataImpl agents;
  /** Projectiles data storage. */
  private ProjectilesDataImpl projectiles;
  /** Best refresh rate. */
  private long refreshRate;
  /** Most recently received time. */
  private long time = 0;
  /** Updates queue. */
  private UpdatesQueue queue;


  /**
   * Creates instance of {@link AlgorithmDataImpl} class using
   * the specified updates queue.
   *
   * @param updatesQueue updates queue.
   */
  AlgorithmDataImpl(UpdatesQueue updatesQueue)
  {
    if (updatesQueue == null)
    {
      throw new NullPointerException("Specified updatesQueue is null!");
    }

    queue = updatesQueue;
  }


  /**
   * @see net.java.dante.algorithms.data.AlgorithmData#getGroupId()
   */
  public int getGroupId()
  {
    return groupId;
  }

  /**
   * @see net.java.dante.algorithms.data.AlgorithmData#getMap()
   */
  public SimulationMap getMap()
  {
    return map;
  }

  /**
   * @see net.java.dante.algorithms.data.AlgorithmData#getTileSize()
   */
  public ObjectSize getTileSize()
  {
    return tileSize;
  }

  /**
   * @see net.java.dante.algorithms.data.AlgorithmData#getControlledData()
   */
  public ControlledAgentsData getControlledData()
  {
    return agents;
  }

  /**
   * @see net.java.dante.algorithms.data.AlgorithmData#getEnemiesData()
   */
  public EnemyAgentsData getEnemiesData()
  {
    return enemies;
  }

  /**
   * @see net.java.dante.algorithms.data.AlgorithmData#getProjectilesData()
   */
  public ProjectilesData getProjectilesData()
  {
    return projectiles;
  }

  /**
   * Rebuilds this {@link AlgorithmData} data by using next events repository
   * from the queue. This method returns value indicating whether algorithm's
   * data was indeed rebuilt.
   *
   * @return Returns <code>true</code> if algorithm's data was rebuilt,
   *         <code>false</code> otherwise.
   *
   * @see net.java.dante.algorithms.data.AlgorithmData#refresh()
   */
  public boolean refresh()
  {
    return (refresh0() != null);
  }

  /**
   * Rebuilds this {@link AlgorithmData} data by using next events repository
   * from the queue and returns instance of {@link UpdateData} object
   * which was used during rebuilt process. This method may return
   * <code>null</code> if no update was performed.
   *
   * @return Returns instance of {@link UpdateData} object which was used
   *         during rebuilt process.
   */
  UpdateData refreshAndObtain()
  {
    UpdateData update = null;

    ClientInputData data = refresh0();
    if (data instanceof UpdateData)
    {
      update = (UpdateData)data;
    }

    return update;
  }

  /**
   * Rebuilds this {@link AlgorithmData} data by using next events repository
   * from the queue and returns instance of {@link ClientInputData} object
   * which was used during rebuilt process. This method may return
   * <code>null</code> if no update was performed.
   *
   * @return Returns instance of {@link UpdateData} object which was used
   *         during rebuilt process.
   */
  private ClientInputData refresh0()
  {
    ClientInputData clientData = queue.take();
    if (clientData instanceof UpdateData)
    {
      UpdateData updateData = (UpdateData)clientData;
      EventsRepository repository = updateData.getRepository();

      enemies.update(repository);
      agents.update(repository);
      projectiles.update(repository);

      time = updateData.getTime();
    }
    else if (clientData instanceof TimeSyncData)
    {
      TimeSyncData timeData = (TimeSyncData)clientData;

      time = timeData.getTime();
    }

    return clientData;
  }

  /**
   * @see net.java.dante.algorithms.data.AlgorithmData#getBestRefreshRate()
   */
  public long getBestRefreshRate()
  {
    return refreshRate;
  }

  /**
   * @see net.java.dante.algorithms.data.AlgorithmData#getMostRecentTime()
   */
  public long getMostRecentTime()
  {
    return time;
  }

  /**
   * Initializes data for this algorithm using specified initialization data.
   *
   * @param initData the specified initialization data.
   */
  void initialize(InitializationData initData)
  {
    if (initData == null)
    {
      throw new NullPointerException("Specified initData is null!");
    }

    queue.clear();
    time        = 0;

    groupId     = initData.getGroupId();
    map         = initData.getMap();
    tileSize    = initData.getGlobalData().getMapTileSize();
    refreshRate = initData.getGlobalData().getSendUpdateMessagesInterval();

    AgentTemplateData agentTemplate = (AgentTemplateData)initData.getAgentTemplateTypeData().getData();

    WeaponTemplateData weaponTemplate = (WeaponTemplateData)initData.getWeaponTemplateTypeData().getData();
    WeaponData weaponData = new WeaponDataImpl(weaponTemplate);

    enemies     = initializeEnemiesData(initData.getAgentsInitData(), agentTemplate, weaponData);
    agents      = initializeControlledData(initData.getAgentsInitData(), agentTemplate, weaponData);
    projectiles = new ProjectilesDataImpl(weaponTemplate.getProjectileSize(),
                                          weaponTemplate.getMaxSpeed());
  }

  /**
   * Initializes enemy agents data storage.
   *
   * @param agentsInitData all agents initialization data.
   * @param agentTemplate agent's template.
   * @param weaponData weapon data used by all agents.
   *
   * @return Returns initialized enemy agents data storage.
   */
  private EnemiesDataImpl initializeEnemiesData(AgentsInitData agentsInitData,
                                                AgentTemplateData agentTemplate,
                                                WeaponData weaponData)
  {
    List<EnemyAgentDataImpl> enemiesData = new ArrayList<EnemyAgentDataImpl>();

    for (int i = 0, groupsCount = agentsInitData.getEnemyGroupsCount(); i < groupsCount; i++)
    {
      EnemyAgentsGroupInitData group = agentsInitData.getEnemyGroup(i);

      for (int j = 0, agentsCount = group.getEnemyAgentsCount(); j < agentsCount; j++)
      {
        EnemyAgentInitData enemy = group.getEnemyAgentInitData(j);
        EnemyAgentDataImpl enemyData = new EnemyAgentDataImpl(enemy.getType(),
                                                              enemy.getAgentId(),
                                                              agentTemplate.getAgentSize(),
                                                              agentTemplate.getMaxSpeed(),
                                                              agentTemplate.getMaxHealth(),
                                                              agentTemplate.getSightRange(),
                                                              weaponData);
        enemiesData.add(enemyData);
      }
    }

    return new EnemiesDataImpl(enemiesData);
  }

  /**
   * Initializes controlled agents data storage.
   *
   * @param agentsInitData all agents initialization data.
   * @param agentTemplate agent's template.
   * @param weaponData weapon data used by all agents.
   *
   * @return Returns initialized controlled agents data storage.
   */
  private ControlledAgentsDataImpl initializeControlledData(AgentsInitData agentsInitData,
                                                           AgentTemplateData agentTemplate,
                                                           WeaponData weaponData)
  {
    int friendlyCount = agentsInitData.getFriendlyAgentsCount();
    List<ControlledAgentDataImpl> agentsData = new ArrayList<ControlledAgentDataImpl>(friendlyCount);

    for (int i = 0; i < friendlyCount; i++)
    {
      FriendlyAgentInitData agent = agentsInitData.getFriendlyAgentInitData(i);

      ControlledAgentDataImpl agentData = new ControlledAgentDataImpl(agent.getType(),
                                                                      agent.getAgentId(),
                                                                      agentTemplate.getAgentSize(),
                                                                      agentTemplate.getMaxSpeed(),
                                                                      agentTemplate.getMaxHealth(),
                                                                      agentTemplate.getSightRange(),
                                                                      weaponData);
      agentData.setX(agent.getStartX());
      agentData.setY(agent.getStartY());

      agentsData.add(agentData);
    }

    return new ControlledAgentsDataImpl(agentsData);
  }

  /**
   * Adds the specified update data to queue from which it will be
   * taken when {@link #refresh()} will be called.
   *
   * @param updateData the specified update data.
   */
  void addUpdateData(UpdateData updateData)
  {
    if (updateData == null)
    {
      throw new NullPointerException("Specified updateData is null!");
    }

    queue.put(updateData);
  }

  /**
   * Adds the specified time data to queue from which it will be
   * taken when {@link #refresh()} will be called.
   *
   * @param timeData the specified time data.
   */
  void addTimeData(TimeSyncData timeData)
  {
    if (timeData == null)
    {
      throw new NullPointerException("Specified timeData is null!");
    }

    queue.put(timeData);
  }
}