/*
 * Created on 2006-08-06
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.agent;

import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.data.common.PositionData;

/**
 * Initialization data for {@link ClientFriendlyAgent} agents.
 *
 * @author M.Olszewski
 */
public class ClientFriendlyAgentInitData implements InitData
{
  /** Client agent's position. */
  private PositionData position;
  /** Client agent's identifier. */
  private int id;
  /** Current simulation's time. */
  private long currentTime;

  
  /**
   * Creates object of {@link ClientFriendlyAgentInitData} with specified starting position.
   * 
   * @param agentId - agent's identifier.
   * @param currentSimTime - the current simulation's time.
   * @param startX - 'x' coordinate of start position.
   * @param startY - 'y' coordinate of start position.
   */
  public ClientFriendlyAgentInitData(int agentId, long currentSimTime,
      double startX, double startY)
  {
    if (agentId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentId - it must be positive integer or zero!");
    }
    if (currentSimTime < 0)
    {
      throw new IllegalArgumentException("Invalid argument currentSimTime - it must be positive long integer or zero!");
    }
    
    id          = agentId;
    currentTime = currentSimTime;
    position    = new PositionData(startX, startY);
  }
  
  
  /**
   * Gets agent's identifier.
   * 
   * @return Returns agent's identifier.
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
  
  /**
   * Gets 'x' coordinate of start position.
   * 
   * @return Returns 'x' coordinate of start position.
   */
  public double getStartX()
  {
    return position.getStartX();
  }

  /**
   * Gets 'y' coordinate of start position.
   * 
   * @return Returns 'y' coordinate of start position.
   */
  public double getStartY()
  {
    return position.getStartY();
  }
}