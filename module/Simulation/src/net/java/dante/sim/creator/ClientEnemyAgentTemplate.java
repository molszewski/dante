/*
 * Created on 2006-08-06
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.creator;

import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.data.object.ClientWeaponSystem;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.agent.ClientEnemyAgent;
import net.java.dante.sim.data.object.agent.ClientEnemyAgentInitData;
import net.java.dante.sim.data.object.agent.ClientEnemyAgentState;
import net.java.dante.sim.data.template.ClassTypesDataStorage;
import net.java.dante.sim.data.template.TemplateTypeData;
import net.java.dante.sim.data.template.TemplatesTypesStorage;
import net.java.dante.sim.data.template.types.AgentTemplateData;

/**
 * Class creating {@link ClientEnemyAgent} objects at the client side.
 *
 * @author M.Olszewski
 */
class ClientEnemyAgentTemplate implements SimulationObjectTemplate
{
  /** Class storage with enemy agents templates types data. */
  private ClassTypesDataStorage enemyAgentsClassStorage;
  /** Weapon system template used by this {@link ClientEnemyAgent}. */
  private ClientWeaponSystemTemplate weaponTemplate;
  
  
  /**
   * Constructs instance of {@link ClientEnemyAgentTemplate} class with specified
   * templates types data storage.
   * 
   * @param templatesStorage - templates types data storage.
   * @param weaponSystemTemplate - weapon system template used by  
   *        {@link ClientEnemyAgent} objects created by this template.
   */
  ClientEnemyAgentTemplate(TemplatesTypesStorage templatesStorage, 
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
    
    enemyAgentsClassStorage = templatesStorage.getClassStorage(AgentTemplateData.class);
    if (enemyAgentsClassStorage == null)
    {
      throw new IllegalArgumentException("Invalid argument templatesStorage - it must contain templates for ClientEnemyAgent class!");
    }
  }
  

  /** 
   * @see net.java.dante.sim.creator.SimulationObjectTemplate#createObject(java.lang.String, net.java.dante.sim.data.ObjectsMediator, net.java.dante.sim.data.common.InitData)
   */
  public SimulationObject createObject(String typeName,
      ObjectsMediator objectsMediator, InitData initData)
      throws ObjectCreationFailedException
  {
    if (initData == null)
    {
      throw new NullPointerException("Specified initData is null!");
    }
    if (!(initData instanceof ClientEnemyAgentInitData))
    {
      throw new IllegalArgumentException("Invalid argument initData - not an instance of ClientEnemyAgentInitData class!");
    }
    
    TemplateTypeData agentTypeData = enemyAgentsClassStorage.getTemplateTypeData(typeName);
    if (agentTypeData == null)
    {
      throw new ObjectCreationFailedException("Cannot find template type data for '" + typeName + "' type!");
    }
    
    AgentTemplateData agentData = (AgentTemplateData)agentTypeData.getData();
    ClientWeaponSystem weapon = 
      (ClientWeaponSystem)weaponTemplate.createWeaponSystem(agentData.getWeaponSystemType());
    if (weapon == null)
    {
      throw new ObjectCreationFailedException("Cannot find template type data for '" + agentData.getWeaponSystemType() + "' weapon system type!");
    }

    return createAgent(agentTypeData.getType(), objectsMediator, 
        agentData, weapon, (ClientEnemyAgentInitData)initData);
  }

  /**
   * Creates instance of {@link ClientEnemyAgent} class, using template data,
   * initialization data and previously constructed object of 
   * {@link ClientWeaponSystem}.
   * 
   * @param type - agent's type.
   * @param objectsMediator - objects mediator.
   * @param templateData - agent's template data.
   * @param weapon - weapon system used by this {@link ClientEnemyAgent}.
   * @param initData - agent's initialization data.
   * 
   * @return Returns created instance of {@link ClientEnemyAgent} class.
   */
  private ClientEnemyAgent createAgent(String type, 
      ObjectsMediator objectsMediator, AgentTemplateData templateData, 
      ClientWeaponSystem weapon, ClientEnemyAgentInitData initData)
  {
    ClientEnemyAgentState state = new ClientEnemyAgentState(type, 
        templateData.getSightRange(), templateData.getMaxHealth(), 
        templateData.getMaxSpeed(), templateData.getAgentSize(), weapon);
    
    // Not matter
    state.setX(0.0);
    state.setY(0.0);
    state.setSpeedX(0.0);
    state.setSpeedY(0.0);
    
    return new ClientEnemyAgent(initData.getAgentId(), state, 
        objectsMediator, initData.getCurrentTime());
  }
  
  /** 
   * @see net.java.dante.sim.creator.SimulationObjectTemplate#isSupported(java.lang.String)
   */
  public boolean isSupported(String typeName)
  {
    boolean supported = false;
    
    TemplateTypeData agentTypeData = enemyAgentsClassStorage.getTemplateTypeData(typeName);
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