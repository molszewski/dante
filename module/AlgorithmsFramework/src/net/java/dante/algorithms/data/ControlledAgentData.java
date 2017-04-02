/*
 * Created on 2006-08-31
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.data;


/**
 * Interface defining data of agent controlled by algorithm.
 *
 * @author M.Olszewski
 */
public interface ControlledAgentData extends AgentData
{
  /**
   * Gets current number of controlled agent's hit points.
   *
   * @return Returns current number of controlled agent's hit points.
   */
  int getHitPoints();

  /**
   * Determines whether this agent is blocked or not by some obstacle.
   *
   * @return Returns <code>true</code> if this agent is blocked,
   *         <code>false</code> otherwise.
   */
  boolean isBlocked();
}
