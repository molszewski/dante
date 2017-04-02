/*
 * Created on 2006-08-06
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.agent;

import net.java.dante.sim.data.common.InitData;

/**
 * Initialization data for {@link ClientEnemyAgent} agents.
 *
 * @author M.Olszewski
 */
public class ClientEnemyAgentInitData implements InitData
{
  /** Enemy agent's identifier. */
  private int id;
  /** Current simulation's time. */
  private long currentTime;

  
  /**
   * Creates object of {@link ClientEnemyAgentInitData} with specified starting position.
   * 
   * @param enemyId - enemy agent's identifier.
   * @param currentSimTime - the current simulation's time.
   */
  public ClientEnemyAgentInitData(int enemyId, long currentSimTime)
  {
    if (enemyId < 0)
    {
      throw new IllegalArgumentException("Invalid argument enemyId - it must be positive integer or zero!");
    }
    if (currentSimTime < 0)
    {
      throw new IllegalArgumentException("Invalid argument currentSimTime - it must be positive long integer or zero!");
    }
    
    id          = enemyId;
    currentTime = currentSimTime;
  }
  
  
  /**
   * Gets enemy agent's identifier.
   * 
   * @return Returns enemy agent's identifier.
   */
  public int getAgentId()
  {
    return id;
  }
  
  /**
   * Gets the current simulation's time.
   * 
   * @return Returns the current simulation's time.
   */
  public long getCurrentTime()
  {
    return currentTime;
  }
}