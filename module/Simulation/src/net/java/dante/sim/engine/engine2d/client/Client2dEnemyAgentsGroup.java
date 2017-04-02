/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.engine2d.client;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.java.dante.sim.engine.EngineObjectsGroup;
import net.java.dante.sim.engine.graphics.java2d.Java2dContext;
import net.java.dante.sim.event.types.EnemyAgentEvent;
import net.java.dante.sim.util.Colors;


/**
 * Class managing group of enemy agents.
 *
 * @author M.Olszewski
 */
class Client2dEnemyAgentsGroup implements EngineObjectsGroup
{
  /** Map containing association between agents identifiers and agents. */
  private Map<Integer, Client2dEnemyAgent> agents;
  /** List with agents to remove. */
  private List<Integer> agentsToRemove = new ArrayList<Integer>();
  /** Group color. */
  private Color color = new Color(Colors.RED);


  /**
   * Creates instance of {@link Client2dEnemyAgentsGroup} class with specified
   * parameters.
   *
   * @param agentsGroup - map containing all agents and their identifiers.
   */
  Client2dEnemyAgentsGroup(Map<Integer, Client2dEnemyAgent> agentsGroup)
  {
    if (agentsGroup == null)
    {
      throw new NullPointerException("Specified agentsGroup is null!");
    }

    agents  = agentsGroup;
  }

  /**
   * @see net.java.dante.sim.engine.EngineObjectsGroup#update(long)
   */
  public void update(long delta)
  {
    removeMarkedAgents();
    prepareAgents();
    updateActiveAgents(delta);
  }

  /**
   * Removes all agents marked as 'to remove'.
   */
  private void removeMarkedAgents()
  {
    // Remove agents marked as 'to remove'
    for (Integer agentId : agentsToRemove)
    {
      agents.remove(agentId);
    }
    // And clear list - do not remove again
    agentsToRemove.clear();
  }

  /**
   * Prepares all enemy agents.
   */
  private void prepareAgents()
  {
    for (Integer agentId : agents.keySet())
    {
      Client2dEnemyAgent enemyAgent = agents.get(agentId);
      enemyAgent.changesUpdated();
    }
  }

  /**
   * Updates state of all active agents.
   *
   * @param delta - time elapsed since last update.
   */
  private void updateActiveAgents(long delta)
  {
    // Agents update
    for (Integer agentId : agents.keySet())
    {
      Client2dEnemyAgent agent = agents.get(agentId);
      if (agent.isActive())
      {
        agent.update(delta);
      }
    }
  }

  /**
   * @see net.java.dante.sim.engine.EngineObjectsGroup#render()
   */
  public void render()
  {
    for (Integer agentId : agents.keySet())
    {
      Client2dEnemyAgent agent = agents.get(agentId);
      if (agent.isActive() && agent.isVisible())
      {
        agent.render();
      }
    }
  }

  /**
   * Renders agent's identifiers.
   *
   * @param context - Java 2D graphics context.
   */
  void renderIDs(Java2dContext context)
  {
    for (Integer agentId : agents.keySet())
    {
      Client2dEnemyAgent agent = agents.get(agentId);
      if (agent.isActive() && agent.isVisible())
      {
        agent.renderID(context.getAcceleratedGraphics(), color);
      }
    }
  }

  /**
   * Processes the specified {@link EnemyAgentEvent} event.
   *
   * @param event - the specified {@link EnemyAgentEvent} event.
   */
  void processEvents(EnemyAgentEvent event)
  {
    Client2dEnemyAgent agent = agents.get(Integer.valueOf(event.getEnemyAgentId()));
    if (agent != null)
    {
      agent.addEvent(event);
    }
  }

  /**
   * Shifts time of this group by specified amount of time.
   *
   * @param time - amount of time by which time of group will be shifted.
   */
  void timeShift(long time)
  {
    for (Integer agentId : agents.keySet())
    {
      agents.get(agentId).timeShift(time);
    }
  }

  /**
   * Removes agent with specified identifier from this group.
   * Agent is added to special list containing identifiers of agents
   * to remove. Agents from this list will be removed during next
   * {@link #update(long)} method's invocation.
   * If agent with specified identifier does not belong to this group,
   * this method returns immediately.
   *
   * @param agentId - agent's identifier which should be removed.
   */
  void removeAgent(int agentId)
  {
    if (agentId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentId - it must be an integer greater or equal to zero!");
    }

    Integer agentIdObj = Integer.valueOf(agentId);
    Client2dEnemyAgent agent = agents.get(agentIdObj);
    if (agent != null)
    {
      agent.setActive(false);
      if (!agentsToRemove.contains(agentIdObj))
      {
        agentsToRemove.add(agentIdObj);
      }
    }
  }
}