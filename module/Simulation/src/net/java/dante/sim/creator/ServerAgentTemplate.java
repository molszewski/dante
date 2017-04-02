/*
 * Created on 2006-07-11
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.creator;

import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.data.object.ServerWeaponSystem;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.agent.ServerAgent;
import net.java.dante.sim.data.object.agent.ServerAgentInitData;
import net.java.dante.sim.data.object.agent.ServerAgentState;
import net.java.dante.sim.data.template.ClassTypesDataStorage;
import net.java.dante.sim.data.template.TemplateTypeData;
import net.java.dante.sim.data.template.TemplatesTypesStorage;
import net.java.dante.sim.data.template.types.AgentTemplateData;

/**
 * Class creating instances of {@link ServerAgent} class with appropriate
 * {@link ServerWeaponSystem} objects using template data obtained from
 * {@link net.java.dante.sim.data.template.TemplatesTypesStorage}.
 *
 * @author M.Olszewski
 */
class ServerAgentTemplate implements SimulationObjectTemplate
{
  /** Class storage with agents templates types data. */
  private ClassTypesDataStorage agentsClassStorage;
  /** Weapon system template used by this {@link ServerAgentTemplate}. */
  private ServerWeaponSystemTemplate weaponTemplate;

  /**
   * Constructs instance of {@link ServerAgentTemplate} class with specified
   * templates types data storage.
   *
   * @param templatesStorage - templates types data storage.
   * @param weaponSystemTemplate - weapon system template used by
   *        {@link ServerAgent} objects created by this template.
   */
  ServerAgentTemplate(TemplatesTypesStorage templatesStorage,
      ServerWeaponSystemTemplate weaponSystemTemplate)
  {
    if (templatesStorage == null)
    {
      throw new NullPointerException("Specified templatesStorage is null!");
    }
    if (weaponSystemTemplate == null)
    {
      throw new NullPointerException("Specified weaponSystemTemplate is null!");
    }

    weaponTemplate = weaponSystemTemplate;

    agentsClassStorage = templatesStorage.getClassStorage(AgentTemplateData.class);
    if (agentsClassStorage == null)
    {
      throw new IllegalArgumentException("Invalid argument templatesStorage - it must contain templates for Agent class!");
    }
  }


  /**
   * @see net.java.dante.sim.creator.SimulationObjectTemplate#createObject(java.lang.String, net.java.dante.sim.data.ObjectsMediator, net.java.dante.sim.data.common.InitData)
   */
  public SimulationObject createObject(String typeName,
      ObjectsMediator objectsMediator, InitData initData) throws ObjectCreationFailedException
  {
    if (initData == null)
    {
      throw new NullPointerException("Specified initData is null!");
    }
    if (!(initData instanceof ServerAgentInitData))
    {
      throw new IllegalArgumentException("Invalid argument initData - not an instance of ServerAgentInitData class!");
    }

    TemplateTypeData agentTypeData = agentsClassStorage.getTemplateTypeData(typeName);
    if (agentTypeData == null)
    {
      throw new ObjectCreationFailedException("Cannot find template type data for '" + typeName + "' type!");
    }

    AgentTemplateData agentData = (AgentTemplateData)agentTypeData.getData();

    ServerWeaponSystem weapon =
        (ServerWeaponSystem)weaponTemplate.createWeaponSystem(agentData.getWeaponSystemType());
    if (weapon == null)
    {
      throw new ObjectCreationFailedException("Cannot find template type data for '" + agentData.getWeaponSystemType() + "' weapon system type!");
    }

    return createAgent(agentTypeData.getType(), objectsMediator,
        agentData, weapon, (ServerAgentInitData)initData);
  }

  /**
   * Creates instance of {@link ServerAgent} class, using template data, initialization
   * data and previously constructed object of {@link ServerWeaponSystem}.
   *
   * @param type - agent's type.
   * @param objectsMediator - objects mediator.
   * @param templateData - agent's template data.
   * @param weapon - weapon system used by this {@link ServerAgent}.
   * @param initData - agent's initialization data.
   *
   * @return Returns created instance of {@link ServerAgent} class.
   */
  private ServerAgent createAgent(String type,
      ObjectsMediator objectsMediator, AgentTemplateData templateData,
      ServerWeaponSystem weapon, ServerAgentInitData initData)
  {
    ServerAgentState state = new ServerAgentState(type,
        templateData.getSightRange(), templateData.getMaxHealth(),
        templateData.getMaxSpeed(), templateData.getAgentSize(), weapon);

    state.setX(initData.getStartX());
    state.setY(initData.getStartY());
    state.setSpeedX(0.0);
    state.setSpeedY(0.0);
    state.setCurrentHitPoints(state.getMaxHitPoints());
    state.changesUpdated();

    return new ServerAgent(state, initData.getAgentQueueSize());
  }

  /**
   * @see net.java.dante.sim.creator.SimulationObjectTemplate#isSupported(java.lang.String)
   */
  public boolean isSupported(String typeName)
  {
    boolean supported = false;

    TemplateTypeData agentTypeData = agentsClassStorage.getTemplateTypeData(typeName);
    if (agentTypeData != null)
    {
      AgentTemplateData agentData = (AgentTemplateData)agentTypeData.getData();

      if (weaponTemplate.isSupported(agentData.getWeaponSystemType()))
      {
        supported = true;
      }
    }

    return supported;
  }
}