/*
 * Created on 2006-08-09
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.engine2d.server;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.java.dante.sim.command.AgentCommands;
import net.java.dante.sim.command.CommandsRepository;
import net.java.dante.sim.command.types.Command;
import net.java.dante.sim.data.object.agent.ServerAgent;
import net.java.dante.sim.data.object.agent.ServerAgentState;
import net.java.dante.sim.data.object.state.Changeable;
import net.java.dante.sim.data.object.state.ObjectState;
import net.java.dante.sim.engine.EngineObjectsGroup;
import net.java.dante.sim.engine.collision.CollisionDetector;
import net.java.dante.sim.engine.collision.CollisionModule;
import net.java.dante.sim.engine.graphics.java2d.Java2dContext;
import net.java.dante.sim.event.EventUtils;
import net.java.dante.sim.event.EventsRepositoryBuilder;
import net.java.dante.sim.event.types.EventTypesUtils;


/**
 * Class managing group of agents.
 *
 * @author M.Olszewski
 */
class Server2dAgentsGroup implements EngineObjectsGroup
{
  /** Group identifier. */
  private int groupId;
  /** Indicates whether this group is active. */
  private boolean active;
  /** Map containing association between agents identifiers and agents. */
  private Map<Integer, Server2dAgent> agents;
  /** List with agents to remove. */
  private List<Integer> agentsToRemove = new ArrayList<Integer>();
  /** Unmodifiable collection of agents, returned by {@link #getAgents()} method. */
  private Collection<Server2dAgent> agentsCollection;
  /** Events repository builder for this group. */
  private EventsRepositoryBuilder groupEventsBuilder;
  /** Collision detector used by this group to check collisions. */
  private CollisionDetector collisionDetector;
  /** Collision module used by this {@link Server2dAgentsGroup}. */
  private CollisionModule collisionModule;
  /** Visibility manager used by this group to update visibility records. */
  private VisibilityManager visibilityManager;
  /** Visibility record. */
  private VisibilityRecord visibilityRecord;
  /** Group's statistics. */
  private GroupStatistics statistics;
  /** Current time holder. */
  private CurrentTimeHolder timeHolder;
  /** Group's color. */
  private Color color;


  /**
   * Creates instance of {@link Server2dAgentsGroup} class with specified
   * parameters.
   *
   * @param agentsGroupId - identifier of this group.
   * @param agentsGroup - map containing all agents and their identifiers.
   */
  Server2dAgentsGroup(int agentsGroupId, Map<Integer, Server2dAgent> agentsGroup)
  {
    if (agentsGroup == null)
    {
      throw new NullPointerException("Specified agentsGroup is null!");
    }
    if (agentsGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsGroupId - it must be an integer greater or equal to zero!");
    }

    groupId = agentsGroupId;
    agents  = agentsGroup;

    agentsCollection   = Collections.unmodifiableCollection(agents.values());
    groupEventsBuilder = EventUtils.createDefaultBuilder(groupId);
    visibilityRecord   = new VisibilityRecord(this);
    statistics         = new GroupStatistics(groupId);
  }


  /**
   * Initializes this agents group with specified modules.
   *
   * @param groupColor - group's color.
   * @param detector - collision detector used by this group.
   * @param defaultCollisionModule - collision module used by this
   *        {@link Server2dProjectilesGroup}.
   * @param visibilityMan - visibility manager.
   * @param currentTimeHolder - current time holder.
   */
  void initDependencies(Color groupColor,
      CollisionDetector detector, CollisionModule defaultCollisionModule,
      VisibilityManager visibilityMan, CurrentTimeHolder currentTimeHolder)
  {
    if (groupColor == null)
    {
      throw new NullPointerException("Specified groupColor is null!");
    }
    if (detector == null)
    {
      throw new NullPointerException("Specified detector is null!");
    }
    if (defaultCollisionModule == null)
    {
      throw new NullPointerException("Specified defaultCollisionModule is null!");
    }
    if (visibilityMan == null)
    {
      throw new NullPointerException("Specified visibilityMan is null!");
    }
    if (currentTimeHolder == null)
    {
      throw new NullPointerException("Specified currentTimeHolder is null!");
    }

    color             = groupColor;
    collisionDetector = detector;
    collisionModule   = defaultCollisionModule;
    visibilityManager = visibilityMan;
    timeHolder        = currentTimeHolder;

    // Add agents to collision detector
    for (Integer agentId : agents.keySet())
    {
      detector.addObject(agents.get(agentId));
    }
  }

  /**
   * @see net.java.dante.sim.engine.EngineObjectsGroup#update(long)
   */
  public void update(long delta)
  {
    removeMarkedAgents();

    prepareAgents();
    updateActiveAgents(delta);

    removeMarkedAgents();
  }

  /**
   * Removes all agents marked as 'to remove'.
   */
  private void removeMarkedAgents()
  {
    // Remove agents marked as 'to remove'
    for (Integer agentId : agentsToRemove)
    {
      collisionDetector.removeObject(agents.get(agentId));
      agents.remove(agentId);
    }
    // And clear list - do not remove again
    agentsToRemove.clear();
  }

  /**
   * Prepares all agents.
   */
  private void prepareAgents()
  {
    for (Integer agentId : agents.keySet())
    {
      // Update old positions
      Server2dAgent agent = agents.get(agentId);
      agent.updateOldPosition();
      // Mark agent state as updated.
      ObjectState agentState = agent.getAgent().getData();
      if (agentState instanceof Changeable)
      {
        ((Changeable)agentState).changesUpdated();
      }
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
      Server2dAgent agent = agents.get(agentId);
      if (agent.isActive())
      {
        agent.update(delta);

        if (agent.isPositionChanged())
        {
          collisionDetector.checkCollisions(collisionModule, agent);

          // If position is not changed anymore, it means that agent was blocked
          // so no visibility updates should be performed
          if (agent.isPositionChanged())
          {
            // Update other visibility records
            visibilityManager.updateRecordsByMovedAgent(groupId, agent);

            ServerAgentState state = (ServerAgentState)agent.getAgent().getData();
            groupEventsBuilder.addEvent(
                EventTypesUtils.createFriendlyAgentMoveEvent(
                    timeHolder.getCurrentTime(), agentId.intValue(),
                    state.getX(), state.getY(),
                    state.getSpeedX(), state.getSpeedY()));

            if (agent.getAgent().isCommandDone())
            {
              groupEventsBuilder.addEvent(
                  EventTypesUtils.createFriendlyAgentMoveFinishedEvent(
                      timeHolder.getCurrentTime(), agentId.intValue()));
            }
          }
        }
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
      Server2dAgent agent = agents.get(agentId);
      if (agent.isActive())
      {
        agent.render();
      }
    }
  }

  /**
   * Special rendering method, using Java 2D graphics context to draw
   * e.g. sight range circles.
   *
   * @param context - Java 2D graphics context.
   */
  void render(Java2dContext context)
  {
    for (Integer agentId : agents.keySet())
    {
      Server2dAgent agent = agents.get(agentId);
      if (agent.isActive())
      {
        agent.render(context);
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
      Server2dAgent agent = agents.get(agentId);
      if (agent.isActive())
      {
        agent.renderID(context.getAcceleratedGraphics(), color);
      }
    }
  }

  /**
   * Processes commands from specified commands repository.
   *
   * @param repository - repository with commands.
   */
  void processCommands(CommandsRepository repository)
  {
    int agentsGroupId = repository.getGroupId();
    for (int i = 0, groupCount = repository.getAgentCommandsCount(); i < groupCount; i++)
    {
      AgentCommands commands = repository.getAgentCommands(i);
      int agentId = commands.getAgentId();
      Server2dAgent agent = agents.get(Integer.valueOf(agentId));
      // Commands can be added only to active agents
      if ((agent != null) && (agent.isActive()))
      {
        ServerAgent simAgent = agent.getAgent();
        if (simAgent.getGroupId() == agentsGroupId)
        {
          for (int j = 0, commandsCount = commands.getCommandsCount(); j < commandsCount; j++)
          {
            Command command = repository.getCommand(commands.getCommandId(j));

            // If any command was not added, do not proceed - performance hit here.
            if (!simAgent.addCommand(command))
            {
              break;
            }
          }
        }
      }
    }
  }

  /**
   * This method should be invoked if this group abandoned simulation.
   */
  void groupAbandonded()
  {
    // Generate proper events for other groups only - this group abandoned us
    for (Integer agentId : agents.keySet())
    {
      Server2dAgent agent = agents.get(agentId);
      visibilityManager.generateEnemyAgentKilledEvents(groupId, agent, 0);
      markAsToRemove(agentId);
    }

    removeMarkedAgents();
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

    Integer agentIdObj  = Integer.valueOf(agentId);
    Server2dAgent agent = agents.get(agentIdObj);

    // Generate proper events
    groupEventsBuilder.addEvent(
        EventTypesUtils.createFriendlyAgentDestroyedEvent(
            timeHolder.getCurrentTime(), agentId));

    if (agent != null)
    {
      agent.setActive(false);
      markAsToRemove(agentIdObj);
    }
  }

  /**
   * Marks agent with the specified identifier as 'to remove'.
   *
   * @param agentId the specified agent's identifier.
   */
  private void markAsToRemove(Integer agentId)
  {
    if (!agentsToRemove.contains(agentId))
    {
      agentsToRemove.add(agentId);
    }
  }

  /**
   * Updates group's statistics.
   */
  void updateStatistics()
  {
    statistics.updateCompleted();
  }

  /**
   * Gets identifier of this agents group.
   *
   * @return Returns identifier of this agents group.
   */
  int getGroupId()
  {
    return groupId;
  }

  /**
   * Gets all agents as unmodifiable collection.
   *
   * @return Returns all agents as unmodifiable collection.
   */
  Collection<Server2dAgent> getAgents()
  {
    return agentsCollection;
  }

  /**
   * Gets number of active agents in the group. Assumption: all agents
   * marked as 'to remove' are also inactive.
   *
   * @return Returns number of active agents in the group.
   */
  int getActiveAgentsCount()
  {
    return (agents.size() - agentsToRemove.size());
  }

  /**
   * Gets value indicating whether this group is active.
   *
   * @return Returns <code>true</code> if this group is active,
   *         <code>false</code> otherwise.
   */
  boolean isActive()
  {
    return active;
  }

  /**
   * Marks this group as not active.
   */
  void notActive()
  {
    active = false;
  }

  /**
   * Gets visibility record connected with this group.
   *
   * @return Returns visibility record connected with this group.
   */
  VisibilityRecord getVisibilityRecord()
  {
    return visibilityRecord;
  }

  /**
   * Gets events repository builder for this group.
   *
   * @return Returns events repository builder for this group.
   */
  EventsRepositoryBuilder getEventsBuilder()
  {
    return groupEventsBuilder;
  }

  /**
   * Gets this group's statistics.
   *
   * @return Returns this group's statistics.
   */
  GroupStatistics getStatistics()
  {
    return statistics;
  }
}