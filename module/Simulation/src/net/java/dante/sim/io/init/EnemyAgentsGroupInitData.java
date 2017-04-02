/*
 * Created on 2006-08-16
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.io.init;

/**
 * Interface defining enemy agents group's initialization data. 
 *
 * @author M.Olszewski
 */
public interface EnemyAgentsGroupInitData
{
  /**
   * Gets number of enemy agents in this group.
   * 
   * @return Returns number of enemy agents in this group.
   */
  int getEnemyAgentsCount();
  
  /**
   * Gets initialization data for friendly agent from the specified index.
   * 
   * @param index - the specified index.
   * 
   * @return Returns initialization data for friendly agent from the specified 
   *         index.
   */
  EnemyAgentInitData getEnemyAgentInitData(int index);
}