/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.creator;

import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.data.object.ClientWeaponSystem;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.agent.ClientFriendlyAgent;
import net.java.dante.sim.data.object.agent.ClientFriendlyAgentInitData;
import net.java.dante.sim.data.object.agent.ClientFriendlyAgentState;
import net.java.dante.sim.data.template.ClassTypesDataStorage;
import net.java.dante.sim.data.template.TemplateTypeData;
import net.java.dante.sim.data.template.TemplatesTypesStorage;
import net.java.dante.sim.data.template.types.AgentTemplateData;

/**
 * Class creating {@link ClientFriendlyAgent} objects at the client side.
 *
 * @author M.Olszewski
 */
class ClientFriendlyAgentTemplate implements SimulationObjectTemplate
{
  /** Class storage with controlled agents templates types data. */
  private ClassTypesDataStorage agentsClassStorage;
  /** Weapon system template used by this {@link ClientFriendlyAgent}. */
  private ClientWeaponSystemTemplate weaponTemplate;

  
  /**
   * Constructs instance of {@link ClientFriendlyAgentTemplate} class with specified
   * templates types data storage.
   * 
   * @param templatesStorage - templates types data storage.
   * @param weaponSystemTemplate - weapon system template used by 
   *        {@link ClientFriendlyAgent} objects created by this template.
   */
  ClientFriendlyAgentTemplate(TemplatesTypesStorage templatesStorage, 
      ClientWeaponSystemTemplate weaponSystemTemplate)
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
      throw new IllegalArgumentException("Invalid argument templatesStorage - it must contain templates for ClientControlledAgent class!");
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
    if (!(initData instanceof ClientFriendlyAgentInitData))
    {
      throw new IllegalArgumentException("Invalid argument initData - not an instance of ClientFriendlyAgentInitData class!");
    }
    
    TemplateTypeData agentTypeData = agentsClassStorage.getTemplateTypeData(typeName);
    if (agentTypeData == null)
    {
      throw new ObjectCreationFailedException("Cannot find template type data for '" + typeName + "' type!");
    }
    
    AgentTemplateData agentData = (AgentTemplateData)agentTypeData.getData();
    
    ClientWeaponSystem weapon = (ClientWeaponSystem)weaponTemplate.createWeaponSystem(agentData.getWeaponSystemType());
    if (weapon == null)
    {
      throw new ObjectCreationFailedException("Cannot find template type data for '" + agentData.getWeaponSystemType() + "' weapon system type!");
    }
    
    return createAgent(agentTypeData.getType(), objectsMediator, 
        agentData, weapon, (ClientFriendlyAgentInitData)initData);
  }

  /**
   * Creates instance of {@link ClientFriendlyAgent} class, using template data,
   * initialization data and previously constructed object of 
   * {@link ClientWeaponSystem}.
   * 
   * @param type - agent's type.
   * @param objectsMediator - objects mediator.
   * @param templateData - agent's template data.
   * @param weapon - weapon system used by this {@link ClientFriendlyAgent}.
   * @param initData - agent's initialization data.
   * 
   * @return Returns created instance of {@link ClientFriendlyAgent} class.
   */
  private ClientFriendlyAgent createAgent(String type, 
      ObjectsMediator objectsMediator, AgentTemplateData templateData,
      ClientWeaponSystem weapon, ClientFriendlyAgentInitData initData)
  {
    ClientFriendlyAgentState state = new ClientFriendlyAgentState(type, 
        templateData.getSightRange(), templateData.getMaxHealth(),
        templateData.getMaxSpeed(), templateData.getAgentSize(), weapon);
    
    state.setX(initData.getStartX());
    state.setY(initData.getStartY());
    state.setSpeedX(0.0);
    state.setSpeedY(0.0);
    state.setCurrentHitPoints(state.getMaxHitPoints());
    
    return new ClientFriendlyAgent(initData.getAgentId(), state, 
        objectsMediator, initData.getCurrentTime());
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