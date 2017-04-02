/*
 * Created on 2006-08-02
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data;

import net.java.dante.sim.creator.ObjectsCreator;
import net.java.dante.sim.creator.ObjectsCreatorFactory;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.SimulationObjectsGroup;
import net.java.dante.sim.data.object.agent.ClientEnemyAgent;
import net.java.dante.sim.data.object.agent.ClientEnemyAgentInitData;
import net.java.dante.sim.data.object.agent.ClientFriendlyAgent;
import net.java.dante.sim.data.object.agent.ClientFriendlyAgentInitData;
import net.java.dante.sim.data.object.projectile.ClientProjectile;
import net.java.dante.sim.data.template.TemplatesTypesStorage;
import net.java.dante.sim.io.init.AgentsInitData;
import net.java.dante.sim.io.init.EnemyAgentInitData;
import net.java.dante.sim.io.init.EnemyAgentsGroupInitData;
import net.java.dante.sim.io.init.FriendlyAgentInitData;

/**
 * Subclass of {@link AbstractSimulationData} class created by 
 * {@link net.java.dante.sim.data.MainInitializationDataLoader} on client side.
 *
 * @author M.Olszewski
 */
class ClientSimulationDataImpl extends AbstractSimulationData
{
  /** Projectiles group. */
  SimulationObjectsGroup projectilesGroup = new SimulationObjectsGroup();
  /** Object holding global data. */
  private GlobalData global;
  /** Objects mediator */
  private ObjectsMediator mediator = new ObjectsMediator() {

    /**
     * @see net.java.dante.sim.data.ObjectsMediator#objectCreated(net.java.dante.sim.data.object.SimulationObject)
     */
    public void objectCreated(SimulationObject object)
    {
      if (object instanceof ClientProjectile)
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
   * Creates instance of {@link ClientSimulationDataImpl} with specified
   * parameters.
   * 
   * @param simMap - simulation's map.
   * @param templatesStorage - templates storage.
   * @param globalData - object holding global data.
   */
  public ClientSimulationDataImpl(SimulationMap simMap, 
      TemplatesTypesStorage templatesStorage, GlobalData globalData)
  {
    super(simMap, templatesStorage);
    
    if (globalData == null)
    {
      throw new NullPointerException("Specified globalData is null!");
    }
    
    global = globalData;
    creator = ObjectsCreatorFactory.getInstance().createObjectsCreator(this);
  }

  
  /** 
   * @see net.java.dante.sim.data.SimulationData#init(net.java.dante.sim.data.common.InitData)
   */
  public void init(InitData initData)
  {
    // Intentionally left empty.
  }
  
  /**
   * Init simulation objects tree.
   * 
   * @param agentsInitData - initialization data for simulation objects tree.
   */
  void initObjects(AgentsInitData agentsInitData)
  {
    SimulationObjectsGroup root = getRoot();
    addObject(projectilesGroup, root);
    
    SimulationObjectsGroup friendlyGroup = new SimulationObjectsGroup();
    addObject(friendlyGroup, root);
    
    for (int i = 0, size = agentsInitData.getFriendlyAgentsCount(); i < size; i++)
    {
      FriendlyAgentInitData initData = agentsInitData.getFriendlyAgentInitData(i);
      
      SimulationObject agent = creator.createObject(
          ClientFriendlyAgent.class, initData.getType(), 
          new ClientFriendlyAgentInitData(initData.getAgentId(), 0, 
              initData.getStartX(), initData.getStartY()));
      
      addObject(agent, friendlyGroup);
    }
    
    SimulationObjectsGroup enemyGroup = new SimulationObjectsGroup();
    addObject(enemyGroup, root);
    
    for (int i = 0, groupsCount = agentsInitData.getEnemyGroupsCount(); i < groupsCount; i++)
    {
      EnemyAgentsGroupInitData groupInitData = agentsInitData.getEnemyGroup(i);
      
      for (int j = 0, agentsCount = groupInitData.getEnemyAgentsCount(); j < agentsCount; j++)
      {
        EnemyAgentInitData enemyInitData = groupInitData.getEnemyAgentInitData(j);
      
        SimulationObject agent = creator.createObject(
            ClientEnemyAgent.class, enemyInitData.getType(), 
            new ClientEnemyAgentInitData(enemyInitData.getAgentId(), 0)); 
        
        addObject(agent, enemyGroup);
      }
    }
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
    return global;
  }

  /** 
   * @see net.java.dante.sim.data.SimulationData#getType()
   */
  public SimulationDataType getType()
  {
    return SimulationDataType.SIMPLE;
  }
}