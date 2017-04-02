/*
 * Created on 2006-07-05
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.java.dante.sim.creator.ObjectsCreator;
import net.java.dante.sim.creator.ObjectsCreatorFactory;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.map.StartLocation;
import net.java.dante.sim.data.map.StartLocationGroup;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.SimulationObjectsGroup;
import net.java.dante.sim.data.object.agent.ServerAgent;
import net.java.dante.sim.data.object.agent.ServerAgentInitData;
import net.java.dante.sim.data.object.projectile.ServerProjectile;
import net.java.dante.sim.data.settings.SimulationSettings;
import net.java.dante.sim.data.template.TemplatesTypesStorage;
import net.java.dante.sim.data.template.types.AgentTemplateData;


/**
 * Subclass of {@link AbstractSimulationData} class created by
 * {@link net.java.dante.sim.data.MainFileLoader} on server side.
 *
 * @author M.Olszewski
 */
class ServerSimulationDataImpl extends AbstractSimulationData implements SimulationData
{
  /** Simulation settings containing part of global data. */
  private SimulationSettings settings;
  /** Reference to object with simulation global data. */
  private GlobalData globalData;
  /** Projectiles group. */
  SimulationObjectsGroup projectilesGroup = new SimulationObjectsGroup();

  /** Objects mediator */
  private ObjectsMediator mediator = new ObjectsMediator() {

    /**
     * @see net.java.dante.sim.data.ObjectsMediator#objectCreated(net.java.dante.sim.data.object.SimulationObject)
     */
    public void objectCreated(SimulationObject object)
    {
      if (object instanceof ServerProjectile)
      {
        addObject(object, projectilesGroup);
      }
    }

    /**
     * @see net.java.dante.sim.data.ObjectsMediator#objectDestroyed(net.java.dante.sim.data.object.SimulationObject)
     */
    public void objectDestroyed(SimulationObject object)
    {
      removeObject(object);
    }
  };
  /** Objects creator. */
  private ObjectsCreator creator;


  /**
   * Creates instance of {@link ServerSimulationDataImpl} class.
   *
   * @param simMap - object representing simulation map.
   * @param templatesStorage - templates types data storage.
   * @param simSettings - object with part of global simulation settings.
   *
   * @throws NullPointerException if any of arguments is <code>null</code>.
   * @throws IllegalArgumentException if <code>agentTypeName</code> is empty
   *         string.
   */
  ServerSimulationDataImpl(SimulationMap simMap, TemplatesTypesStorage templatesStorage,
      SimulationSettings simSettings)
  {
    super(simMap, templatesStorage);

    if (simSettings == null)
    {
      throw new NullPointerException("Specified simSettings is null!");
    }

    settings = simSettings;
    creator = ObjectsCreatorFactory.getInstance().createObjectsCreator(this);
  }


  /**
   * @see net.java.dante.sim.data.SimulationData#init(net.java.dante.sim.data.common.InitData)
   */
  public void init(InitData initData)
  {
    if (initData == null)
    {
      throw new NullPointerException("Specified initData is null!");
    }
    if (!(initData instanceof ServerSimulationInitData))
    {
      throw new IllegalArgumentException("Invalid argument initData - not an instance of ServerSimulationInitData class!");
    }

    ServerSimulationInitData data = (ServerSimulationInitData)initData;
    SimulationMap map = getMap();

    int maxGroupsCnt = data.getGroupsCount();
    if (maxGroupsCnt > map.locationGroupsCount())
    {
      throw new IllegalArgumentException("Invalid argument initData - requested number of agents groups is greater than number of start location groups on map!");
    }

    // init global data
    initGlobalData(maxGroupsCnt);

    // init objects
    SimulationObjectsGroup root = getRoot();
    addObject(projectilesGroup, root);

    ObjectSize tileSize = settings.getMapTileSize();
    String agentType = getTemplatesStorage().getClassStorage(AgentTemplateData.class).getDefault().getType();

    // Prepare list of start location groups
    List<StartLocationGroup> locationGroups = new ArrayList<StartLocationGroup>(map.locationGroupsCount());
    for (int i = 0, size = map.locationGroupsCount(); i < size; i++)
    {
      locationGroups.add(map.getStartLocationGroup(i));
    }

    // Shuffle the list so random location groups will be chosen each time
    Collections.shuffle(locationGroups);

    for (int i = 0; i < maxGroupsCnt; i++)
    {
      SimulationObjectsGroup objGroup = new SimulationObjectsGroup();
      addObject(objGroup, root);

      StartLocationGroup group = locationGroups.get(i);

      for (int j = 0, size = group.locationsCount(); j < size; j++)
      {
        StartLocation loc = group.getLocation(j);
        double x = tileSize.getWidth()  * loc.getColumn();
        double y = tileSize.getHeight() * loc.getRow();

        SimulationObject agent = creator.createObject(ServerAgent.class,
                                                      agentType,
                                                      new ServerAgentInitData(x,
                                                                              y,
                                                                              globalData.getAgentQueueMaxSize()));
        addObject(agent, objGroup);

        ((ServerAgent)agent).setGroupId(objGroup.getId());
      }
    }
  }

  /**
   * Initializes global data object.
   *
   * @param maxGroupsCount - maximum groups count.
   */
  private void initGlobalData(int maxGroupsCount)
  {
    globalData = new GlobalData(settings, maxGroupsCount);
  }

  /**
   * @see net.java.dante.sim.data.SimulationData#getMediator()
   */
  public ObjectsMediator getMediator()
  {
    return mediator;
  }

  /**
   * @see net.java.dante.sim.data.SimulationData#getCreator()
   */
  public ObjectsCreator getCreator()
  {
    return creator;
  }

  /**
   * @see net.java.dante.sim.data.SimulationData#getGlobalData()
   */
  public GlobalData getGlobalData()
  {
    return globalData;
  }

  /**
   * @see net.java.dante.sim.data.SimulationData#getType()
   */
  public SimulationDataType getType()
  {
    return SimulationDataType.FULL;
  }
}