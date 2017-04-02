/*
 * Created on 2006-08-16
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.java.dante.sim.data.ActionListener;
import net.java.dante.sim.data.GlobalData;
import net.java.dante.sim.data.ObjectsTraverser;
import net.java.dante.sim.data.SimulationData;
import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.agent.ServerAgent;
import net.java.dante.sim.data.object.agent.ServerAgentState;
import net.java.dante.sim.data.template.TemplateTypeData;
import net.java.dante.sim.data.template.TemplatesTypesStorage;
import net.java.dante.sim.data.template.types.AgentTemplateData;
import net.java.dante.sim.data.template.types.WeaponTemplateData;
import net.java.dante.sim.io.ClientInputData;
import net.java.dante.sim.io.ServerOutputData;


/**
 * Initialization data for agents group with specified identifier.
 * Contains group's identifier, map's data, global data, templates storage,
 * data specific for friendly and enemy agents.
 *
 * @author M.Olszewski
 */
public class InitializationData implements ClientInputData, ServerOutputData
{
  /** This group's identifier. */
  private int groupId;
  /** Simulation's global settings. */
  private GlobalData globalData;
  /** Simulation's map. */
  private SimulationMap map;
  /** Agent's type template data. */
  private TemplateTypeData agentTypeData;
  /** Weapon's type template data. */
  private TemplateTypeData weaponTypeData;
  /** Agents initialization data. */
  private AgentsInitData agentsData;


  /**
   * Private constructor creating instance of {@link InitializationData} from
   * specified arguments.
   *
   * @param agentsGroupId identifier of this group.
   * @param simMap simulation's map.
   * @param agentTemplateTypeData agent's type template data.
   * @param weaponTemplateTypeData weapon's type template data.
   * @param simGlobalData simulation's global data.
   * @param agentsInitData agents initialization data.
   */
  private InitializationData(int                   agentsGroupId,
                             SimulationMap         simMap,
                             TemplateTypeData      agentTemplateTypeData,
                             TemplateTypeData      weaponTemplateTypeData,
                             GlobalData            simGlobalData,
                             AgentsInitData        agentsInitData)
  {
    if (simMap == null)
    {
      throw new NullPointerException("Specified simMap is null!");
    }
    if (agentTemplateTypeData == null)
    {
      throw new NullPointerException("Specified agentTypeData is null!");
    }
    if (weaponTemplateTypeData == null)
    {
      throw new NullPointerException("Specified weaponTypeData is null!");
    }
    if (simGlobalData == null)
    {
      throw new NullPointerException("Specified simGlobalData is null!");
    }
    if (agentsInitData == null)
    {
      throw new NullPointerException("Specified agentsInitData is null!");
    }
    if (agentsGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsGroupId - it must be an integer greater or equal to zero!");
    }

    groupId        = agentsGroupId;
    map            = simMap;
    agentTypeData  = agentTemplateTypeData;
    weaponTypeData = weaponTemplateTypeData;
    globalData     = simGlobalData;
    agentsData     = agentsInitData;
  }


  /**
   * Gets identifier of this agents group.
   *
   * @return Returns identifier of this agents group.
   */
  public int getGroupId()
  {
    return groupId;
  }

  /**
   * Gets simulation's map.
   *
   * @return Returns simulation's map.
   */
  public SimulationMap getMap()
  {
    return map;
  }

  /**
   * Gets {@link TemplateTypeData} object representing template data for
   * default agent's type.
   *
   * @return Returns {@link TemplateTypeData} object representing template data for
   *         default agent's type.
   */
  public TemplateTypeData getAgentTemplateTypeData()
  {
    return agentTypeData;
  }

  /**
   * Gets {@link TemplateTypeData} object representing template data for
   * default weapon's type.
   *
   * @return Returns {@link TemplateTypeData} object representing template data for
   *         default weapon's type.
   */
  public TemplateTypeData getWeaponTemplateTypeData()
  {
    return weaponTypeData;
  }

  /**
   * Gets {@link GlobalData} object with global simulation data.
   *
   * @return Returns {@link GlobalData} object with global simulation data.
   */
  public GlobalData getGlobalData()
  {
    return globalData;
  }

  /**
   * Gets {@link AgentsInitData} object with agents initialization data.
   *
   * @return Returns {@link AgentsInitData} object with agents initialization data.
   */
  public AgentsInitData getAgentsInitData()
  {
    return agentsData;
  }

  /**
   * Creates array with instances of {@link InitializationData} class
   * with data for each agents group obtained from specified
   * {@link SimulationData} implementation.
   *
   * @param simData source of groups initialization data.
   *
   * @return Returns array with instances of {@link InitializationData} class
   *         with data for each agents group obtained from specified
   *         {@link SimulationData} implementation.
   * @throws NullPointerException if specified <code>data</code> is <code>null</code>.
   */
  public static InitializationData[] createInitializationData(SimulationData simData)
  {
    if (simData == null)
    {
      throw new NullPointerException("Specified data is null!");
    }

    Map<Integer, AgentsInitData> agentsInitDataArray = obtainObjectsData(simData.getRoot());
    InitializationData[] groupInitData =
        new InitializationData[agentsInitDataArray.size()];

    int i = 0;
    for (Integer groupId : agentsInitDataArray.keySet())
    {
      AgentsInitData agentsInitData = agentsInitDataArray.get(groupId);
      groupInitData[i] = new InitializationData(groupId.intValue(),
                                                simData.getMap(),
                                                obtainAgentTemplate(simData.getTemplatesStorage()),
                                                obtainWeaponTemplate(simData.getTemplatesStorage()),
                                                simData.getGlobalData(),
                                                agentsInitData);
      i++;
    }

    return groupInitData;
  }

  /**
   * Obtains default agent's template type data from the specified storage.
   *
   * @param storage the specified storage.
   *
   * @return Returns obtained default agent's template type data
   *         from the specified storage.
   */
  private static TemplateTypeData obtainAgentTemplate(TemplatesTypesStorage storage)
  {
    return storage.getClassStorage(AgentTemplateData.class).getDefault();
  }

  /**
   * Obtains default weapon's template type data from the specified storage.
   *
   * @param storage the specified storage.
   *
   * @return Returns obtained default weapon's template type data
   *         from the specified storage.
   */
  private static TemplateTypeData obtainWeaponTemplate(TemplatesTypesStorage storage)
  {
    return storage.getClassStorage(WeaponTemplateData.class).getDefault();
  }

  /**
   * Creates instance of {@link InitializationData} with data obtained from
   * specified arguments.
   *
   * @param groupId identifier of this group.
   * @param simMap simulation's map.
   * @param agentTemplateTypeData agent's type template data.
   * @param weaponTemplateTypeData weapon's type template data.
   * @param globalData simulation's global data.
   * @param agentsInitData agents initialization data.
   *
   * @return Returns instance of {@link InitializationData} containing specified
   *         data.
   * @throws NullPointerException if any of specified arguments is <code>null</code>.
   */
  public static InitializationData createInitializationData(
      int groupId,
      SimulationMap simMap,
      TemplateTypeData agentTemplateTypeData,
      TemplateTypeData weaponTemplateTypeData,
      AgentsInitData agentsInitData,
      GlobalData globalData)
  {
    return new InitializationData(groupId, simMap,
                                  agentTemplateTypeData, weaponTemplateTypeData,
                                  globalData, agentsInitData);
  }

  /**
   * Obtains data from tree of {@link SimulationObject} objects and stores
   * them in appropriate structures.
   *
   * @param root root object of {@link SimulationObject} objects tree.
   *
   * @return Returns array with instances of {@link AgentsInitData}
   *         containing all fetched initialization data for groups of agents.
   */
  private static Map<Integer, AgentsInitData> obtainObjectsData(SimulationObject root)
  {
    FetchInitDataListener fetcher = new FetchInitDataListener();
    ObjectsTraverser.getDefault().traverse(root, fetcher);

    return fetcher.createGroupsInitData();
  }


  /**
   * Class responsible for fetching initialization data from all objects stored
   * in {@link SimulationData} object.
   *
   * @author M.Olszewski
   */
  private static class FetchInitDataListener implements ActionListener
  {
    /**
     * Map where all fetched data regarding {@link FriendlyAgentInitData}
     * objects will be stored.
     */
    private Map<Integer, List<FriendlyAgentInitData>> friendlyGroupsInitData =
        new HashMap<Integer, List<FriendlyAgentInitData>>();
    /**
     * Map where all fetched data regarding {@link EnemyAgentInitData}
     * objects will be stored.
     */
    private Map<Integer, List<EnemyAgentInitData>> enemyGroupsInitData =
        new HashMap<Integer, List<EnemyAgentInitData>>();


    /**
     * Creates instance of {@link FetchInitDataListener}.
     */
    FetchInitDataListener()
    {
      // Intentionally left empty.
    }


    /**
     * Creates map with association between friendly group identifiers and
     * instances of {@link AgentsInitData} containing all
     * fetched initialization data for groups of agents.
     *
     * @return Returns map with association between friendly group identifiers and
     *         instances of {@link AgentsInitData} containing all
     *         fetched initialization data for groups of agents.
     */
    Map<Integer, AgentsInitData> createGroupsInitData()
    {
      Map<Integer, AgentsInitData> agentsInitDataMap =
          new HashMap<Integer, AgentsInitData>(friendlyGroupsInitData.size());

      for (Integer friendlyGroupId : friendlyGroupsInitData.keySet())
      {
        List<FriendlyAgentInitData> friendlyGroup = friendlyGroupsInitData.get(friendlyGroupId);
        List<EnemyAgentsGroupInitData> enemyGroups =
            new ArrayList<EnemyAgentsGroupInitData>(enemyGroupsInitData.size());

        for (Integer enemyGroupId : enemyGroupsInitData.keySet())
        {
          if (enemyGroupId.intValue() != friendlyGroupId.intValue())
          {
            enemyGroups.add(
                new EnemyAgentsGroupInitDataImpl(enemyGroupsInitData.get(enemyGroupId)));
          }
        }

        agentsInitDataMap.put(friendlyGroupId, new AgentsInitDataImpl(friendlyGroup, enemyGroups));
      }

      return agentsInitDataMap;
    }


    /**
     * @see net.java.dante.sim.data.ActionListener#entryAction(net.java.dante.sim.data.object.SimulationObject)
     */
    public void entryAction(SimulationObject object)
    {
      if (object instanceof ServerAgent)
      {
        ServerAgent agent = (ServerAgent)object;
        addFriendlyAgent(agent);
        addEnemyAgent(agent);
      }
    }

    /**
     * Adds data regarding friendly agent.
     *
     * @param agent the {@link ServerAgent} object.
     */
    private void addFriendlyAgent(ServerAgent agent)
    {
      ServerAgentState state = (ServerAgentState)agent.getData();
      int groupId = agent.getParent().getId();

      Integer groupIdObj = Integer.valueOf(groupId);
      List<FriendlyAgentInitData> groupList = friendlyGroupsInitData.get(groupIdObj);
      if (groupList == null)
      {
        groupList = new ArrayList<FriendlyAgentInitData>();
        friendlyGroupsInitData.put(groupIdObj, groupList);
      }

      groupList.add(new FriendlyAgentInitDataImpl(state.getType(), agent.getId(),
          state.getX(), state.getY()));
    }

    /**
     * Adds data with enemy agent.
     *
     * @param agent the {@link ServerAgent} object.
     */
    private void addEnemyAgent(ServerAgent agent)
    {
      ServerAgentState state = (ServerAgentState)agent.getData();
      int groupId = agent.getParent().getId();

      Integer groupIdObj = Integer.valueOf(groupId);
      List<EnemyAgentInitData> groupList = enemyGroupsInitData.get(groupIdObj);
      if (groupList == null)
      {
        groupList = new ArrayList<EnemyAgentInitData>();
        enemyGroupsInitData.put(groupIdObj, groupList);
      }

      groupList.add(new EnemyAgentInitDataImpl(state.getType(), agent.getId()));
    }

    /**
     * @see net.java.dante.sim.data.ActionListener#exitAction(net.java.dante.sim.data.object.SimulationObject)
     */
    public final void exitAction(SimulationObject object)
    {
      // Intentionally left empty.
    }
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + groupId;
    result = PRIME * result + globalData.hashCode();
    result = PRIME * result + map.hashCode();
    result = PRIME * result + agentTypeData.hashCode();
    result = PRIME * result + weaponTypeData.hashCode();
    result = PRIME * result + agentsData.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof InitializationData))
    {
      final InitializationData other = (InitializationData) object;
      equal = ((groupId == other.groupId) &&
               (globalData.equals(other.globalData)) &&
               (map.equals(other.map)) &&
               (agentTypeData.equals(other.agentTypeData)) &&
               (weaponTypeData.equals(other.weaponTypeData)) &&
               (agentsData.equals(other.agentsData)));
    }
    return equal;
  }
}