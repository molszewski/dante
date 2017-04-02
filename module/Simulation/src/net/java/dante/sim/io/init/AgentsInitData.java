/*
 * Created on 2006-08-16
 *
 * @author M.Olszewski 
 */


package net.java.dante.sim.io.init;

/**
 * Initialization data for friendly agents group and all enemies agents groups.
 * 
 * @author M.Olszewski
 */
public interface AgentsInitData
{
  /**
   * Gets number of enemy groups.
   * 
   * @return Returns number of enemy groups.
   */
  int getEnemyGroupsCount();
  
  /**
   * Gets initialization data for enemy group from the specified index.
   * 
   * @param index - the specified index.
   * 
   * @return Returns initialization data for enemy group from the specified index.
   */
  EnemyAgentsGroupInitData getEnemyGroup(int index);
  
  /**
   * Gets number of friendly agents.
   * 
   * @return Returns number of friendly agents.
   */
  int getFriendlyAgentsCount();
  
  /**
   * Gets initialization data for friendly agent from the specified index.
   * 
   * @param index - the specified index.
   * 
   * @return Returns initialization data for friendly agent from the specified 
   *         index.
   */
  FriendlyAgentInitData getFriendlyAgentInitData(int index);
}