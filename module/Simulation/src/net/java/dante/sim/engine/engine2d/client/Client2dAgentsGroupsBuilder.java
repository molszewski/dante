/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.engine2d.client;

import java.util.HashMap;
import java.util.Map;

import net.java.dante.sim.data.ActionListener;
import net.java.dante.sim.data.ObjectsTraverser;
import net.java.dante.sim.data.SimulationData;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.agent.ClientEnemyAgent;
import net.java.dante.sim.data.object.agent.ClientFriendlyAgent;
import net.java.dante.sim.engine.engine2d.SpritesRepository;


/**
 * Helper class building enemy group of agents and friendly group of agents
 * from the specified root of simulation objects tree.
 *
 * @author M.Olszewski
 */
class Client2dAgentsGroupsBuilder
{
  /** Sprites repository with agents sprites. */
  SpritesRepository sprites;
  /** Friendly group's identifier. */
  private int groupId;
  /** Friendly agents group. */
  private Client2dFriendlyAgentsGroup friendlyGroup;
  /** Enemy agents group. */
  private Client2dEnemyAgentsGroup enemyGroup;


  /**
   * Creates instance of {@link Client2dAgentsGroupsBuilder} with specified
   * parameters.
   *
   * @param agentsGroupId - friendly agents group's identifier.
   * @param spritesRepository - sprites repository.
   */
  Client2dAgentsGroupsBuilder(int agentsGroupId,
      SpritesRepository spritesRepository)
  {
    if (spritesRepository == null)
    {
      throw new NullPointerException("Specified spritesRepository is null!");
    }
    if (agentsGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsGroupId - it must be an integer greater or equal to zero!");
    }

    groupId = agentsGroupId;
    sprites  = spritesRepository;
  }

  /**
   * Fetches data for {@link Client2dEnemyAgentsGroup} and
   * {@link Client2dFriendlyAgentsGroup} objects from specified simulation
   * objects tree. Proper
   *
   * @param root - root of simulation objects tree.
   */
  void build(SimulationObject root)
  {
    if (root == null)
    {
      throw new NullPointerException("Specified root is null!");
    }

    AgentsGroupFetcher fetcher = new AgentsGroupFetcher();
    ObjectsTraverser.getDefault().traverse(root, fetcher);
    enemyGroup    = fetcher.createEnemyGroup();
    friendlyGroup = fetcher.createFriendlyGroup(groupId);
  }

  /**
   * Gets created {@link Client2dEnemyAgentsGroup} object or <code>null</code>
   * if {@link #build(SimulationObject)} method was not called.
   *
   * @return Returns created {@link Client2dEnemyAgentsGroup} object
   *         or <code>null</code> if {@link #build(SimulationObject)}
   *         method was not called.
   */
  Client2dEnemyAgentsGroup getEnemyGroup()
  {
    return enemyGroup;
  }

  /**
   * Gets created {@link Client2dFriendlyAgentsGroup} object or <code>null</code>
   * if {@link #build(SimulationObject)} method was not called.
   *
   * @return Returns created {@link Client2dFriendlyAgentsGroup} object
   *         or <code>null</code> if {@link #build(SimulationObject)}
   *         method was not called.
   */
  Client2dFriendlyAgentsGroup getFriendlyGroup()
  {
    return friendlyGroup;
  }

  /**
   * Implementation of {@link ActionListener} fetching agents from
   * {@link SimulationData} and creating instances of
   * {@link Client2dEnemyAgent} and {@link Client2dFriendlyAgent} classes
   * used by this engine.
   *
   * @author M.Olszewski
   */
  private class AgentsGroupFetcher implements ActionListener
  {
    /** Map  with friendly agents group and agents identifiers. */
    private Map<Integer, Client2dFriendlyAgent> friends =
        new HashMap<Integer, Client2dFriendlyAgent>();
    /** Map with enemy agents group and enemies identifiers. */
    private Map<Integer, Client2dEnemyAgent> enemies =
        new HashMap<Integer, Client2dEnemyAgent>();


    /**
     * Creates instance of {@link AgentsGroupFetcher} class.
     */
    AgentsGroupFetcher()
    {
      // Intentionally left empty.
    }


    /**
     * @see net.java.dante.sim.data.ActionListener#entryAction(net.java.dante.sim.data.object.SimulationObject)
     */
    public void entryAction(SimulationObject object)
    {
      if (object instanceof ClientEnemyAgent)
      {
        Client2dEnemyAgent enemyAgent = new Client2dEnemyAgent(
            (ClientEnemyAgent)object, sprites.getAgentMoveSprites(),
            sprites.getAgentMoveDelay());
        enemies.put(Integer.valueOf(object.getId()), enemyAgent);
      }
      else if (object instanceof ClientFriendlyAgent)
      {
        Client2dFriendlyAgent friendlyAgent = new Client2dFriendlyAgent(
            (ClientFriendlyAgent)object, sprites.getAgentMoveSprites(),
            sprites.getAgentMoveDelay());
        friends.put(Integer.valueOf(object.getId()), friendlyAgent);
      }
    }

    /**
     * @see net.java.dante.sim.data.ActionListener#exitAction(net.java.dante.sim.data.object.SimulationObject)
     */
    public void exitAction(SimulationObject object)
    {
      // Intentionally left empty.
    }

    /**
     * Create {@link Client2dEnemyAgentsGroup} object from fetched data.
     *
     * @return Returns created {@link Client2dEnemyAgentsGroup} object.
     */
    Client2dEnemyAgentsGroup createEnemyGroup()
    {
      return new Client2dEnemyAgentsGroup(enemies);
    }

    /**
     * Create {@link Client2dFriendlyAgentsGroup} object from fetched data.
     *
     * @param agentsGroupId - friendly agents group's identifier.
     *
     * @return Returns created {@link Client2dFriendlyAgentsGroup} object.
     */
    Client2dFriendlyAgentsGroup createFriendlyGroup(int agentsGroupId)
    {
      return new Client2dFriendlyAgentsGroup(agentsGroupId, friends);
    }
  }
}