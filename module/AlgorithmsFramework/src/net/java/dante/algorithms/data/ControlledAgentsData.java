/*
 * Created on 2006-08-31
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.data;

/**
 * Interface defining data for agents controlled by algorithm.
 *
 * @author M.Olszewski
 */
public interface ControlledAgentsData
{
  /**
   * Gets total number of controlled agents (including destroyed ones).
   *
   * @return Returns total number of controlled agents.
   */
  int getAgentsCount();

  /**
   * Gets data for all controlled agents.
   *
   * @return Returns array with data for all controlled agents.
   */
  ControlledAgentData[] getAgentsData();

  /**
   * Gets data for controlled agent with the specified identifier.
   *
   * @param agentId the specified agent's identifier.
   *
   * @return Returns data for controlled agent with the specified identifier.
   */
  ControlledAgentData getAgentData(int agentId);

  /**
   * Gets number of active controlled agents.
   *
   * @return Returns number of active controlled agents.
   */
  int getActiveAgentsCount();

  /**
   * Gets data for active controlled agents.
   *
   * @return Returns array with data for active controlled agents.
   */
  ControlledAgentData[] getActiveAgentsData();

  /**
   * Gets data for active agent from the specified index.
   *
   * @param index the specified active agent's index.
   *
   * @return Returns data for visible active agent from the specified index.
   */
  ControlledAgentData getActiveAgentData(int index);

  /**
   * Gets number of blocked and active controlled agents.
   *
   * @return Returns number of blocked and active controlled agents.
   */
  int getBlockedAgentsCount();

  /**
   * Gets data for blocked and active controlled agents.
   *
   * @return Returns array with data for blocked and active controlled agents.
   */
  ControlledAgentData[] getBlockedAgentsData();
}