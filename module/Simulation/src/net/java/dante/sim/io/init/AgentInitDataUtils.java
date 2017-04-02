/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io.init;

import java.util.Arrays;

/**
 * Utility class capable of creating instances of
 * {@link net.java.dante.sim.io.init.AgentsInitData}, {@link net.java.dante.sim.io.init.FriendlyAgentInitData},
 * {@link net.java.dante.sim.io.init.EnemyAgentsGroupInitData} and
 * {@link net.java.dante.sim.io.init.EnemyAgentInitData} classes.
 * This class can be used externally.
 *
 * @author M.Olszewski
 */
public final class AgentInitDataUtils
{
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private AgentInitDataUtils()
  {
    // Intentionally left empty.
  }


  /**
   * Creates instance of {@link FriendlyAgentInitData} class with specified
   * parameters.
   *
   * @param typeName name of agent's type.
   * @param agentId agent's identifier.
   * @param startX agent's start 'x' coordinate.
   * @param startY agent's start 'y' coordinate.
   *
   * @return Returns instance of {@link FriendlyAgentInitData} class with specified
   *         parameters.
   */
  public static FriendlyAgentInitData createFriendlyAgentInitData(
      String typeName, int agentId, double startX, double startY)
  {
    return new FriendlyAgentInitDataImpl(typeName, agentId, startX, startY);
  }

  /**
   * Creates instance of {@link EnemyAgentInitData} class with specified
   * parameters.
   *
   * @param typeName name of agent's type.
   * @param agentId agent's identifier.
   *
   * @return Returns instance of {@link EnemyAgentInitData} class with specified
   *         parameters.
   */
  public static EnemyAgentInitData createEnemyAgentInitData(String typeName, int agentId)
  {
    return new EnemyAgentInitDataImpl(typeName, agentId);
  }

  /**
   * Creates instance of {@link EnemyAgentsGroupInitData} class with specified
   * parameters.
   *
   * @param enemyAgentsInitData array with initialization data for enemy agents.
   *
   * @return Returns instance of {@link EnemyAgentsGroupInitData} class
   *         with specified parameters.
   */
  public static EnemyAgentsGroupInitData createEnemyAgentsGroupInitData(
      EnemyAgentInitData[] enemyAgentsInitData)
  {
    return new EnemyAgentsGroupInitDataImpl(Arrays.asList(enemyAgentsInitData));
  }

  /**
   * Creates instance of {@link AgentsInitData} class with specified
   * parameters.
   *
   * @param friendlyAgents array with initialization data for friendly
   *        agents groups.
   * @param enemyAgentsGroups array with initialization data for
   *        enemy agents groups.
   *
   * @return Returns instance of {@link AgentsInitData} class with specified
   *         parameters.
   */
  public static AgentsInitData createAgentsGroupsInitData(
      FriendlyAgentInitData[] friendlyAgents,
      EnemyAgentsGroupInitData[] enemyAgentsGroups)
  {
    return new AgentsInitDataImpl(Arrays.asList(friendlyAgents),
        Arrays.asList(enemyAgentsGroups));
  }
}