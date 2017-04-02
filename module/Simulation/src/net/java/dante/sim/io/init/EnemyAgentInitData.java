/*
 * Created on 2006-08-16
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.io.init;

/**
 * Interface defining enemy agent's initialization data. 
 *
 * @author M.Olszewski
 */
public interface EnemyAgentInitData
{
  /**
   * Gets name of enemy agent's type.
   * 
   * @return Returns name of enemy agent's type.
   */
  String getType();
  
  /**
   * Gets enemy agent's identifier.
   * 
   * @return Returns enemy agent's identifier.
   */
  int getAgentId();
}