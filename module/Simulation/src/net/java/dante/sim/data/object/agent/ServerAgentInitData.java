/*
 * Created on 2006-07-11
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.object.agent;

import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.data.common.PositionData;

/**
 * Initialization data for {@link ServerAgent} agents.
 *
 * @author M.Olszewski
 */
public class ServerAgentInitData implements InitData
{
  /** Server agent's position. */
  private PositionData position;
  /** Size of agent's commands queue. */
  private int queueSize;


  /**
   * Creates object of {@link ServerAgentInitData} with specified starting position.
   *
   * @param startX - 'x' coordinate of start position.
   * @param startY - 'y' coordinate of start position.
   * @param agentQueueSize - the agent's commands queue size.
   */
  public ServerAgentInitData(double startX, double startY, int agentQueueSize)
  {
    position = new PositionData(startX, startY);

    if (agentQueueSize <= 0)
    {
      throw new IllegalArgumentException("Invalid argument agentQueueSize - it must be positive integer!");
    }

    queueSize = agentQueueSize;
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

  /**
   * Gets size of agent's commands queue.
   *
   * @return Returns size of agent's commands queue.
   */
  public int getAgentQueueSize()
  {
    return queueSize;
  }
}