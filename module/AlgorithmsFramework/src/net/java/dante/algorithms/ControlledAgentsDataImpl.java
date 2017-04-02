/*
 * Created on 2006-09-01
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.java.dante.algorithms.data.ControlledAgentData;
import net.java.dante.algorithms.data.ControlledAgentsData;
import net.java.dante.sim.event.Event;
import net.java.dante.sim.event.EventsRepository;
import net.java.dante.sim.event.types.FriendlyAgentBlockedEvent;
import net.java.dante.sim.event.types.FriendlyAgentDestroyedEvent;
import net.java.dante.sim.event.types.FriendlyAgentEvent;
import net.java.dante.sim.event.types.FriendlyAgentHitEvent;
import net.java.dante.sim.event.types.FriendlyAgentMoveEvent;
import net.java.dante.sim.event.types.ObjectMoveEventParams;

/**
 * Implementation of {@link ControlledAgentsData} interface.
 *
 * @author M.Olszewski
 */
class ControlledAgentsDataImpl implements ControlledAgentsData, EventsUpdateable
{
  /** Map between agents identifiers and agents data. */
  private Map<Integer, ControlledAgentDataImpl> agentsMap;
  /** List with all controlled agents. */
  private List<ControlledAgentDataImpl> agents;
  /** List with active controlled agents. */
  private List<ControlledAgentDataImpl> activeAgents;
  /** List with blocked and active controlled agents. */
  private List<ControlledAgentDataImpl> blockedAgents;


  /**
   * Creates instance of {@link ControlledAgentsDataImpl} class.
   *
   * @param agentsList - list with data of all controlled agents.
   */
  ControlledAgentsDataImpl(List<ControlledAgentDataImpl> agentsList)
  {
    if (agentsList == null)
    {
      throw new NullPointerException("Specified agentsList is null!");
    }
    if (agentsList.size() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsList - it must contain at least 1 element!");
    }

    agents        = agentsList;
    activeAgents  = new ArrayList<ControlledAgentDataImpl>(agentsList.size());
    activeAgents.addAll(agentsList);

    blockedAgents = new ArrayList<ControlledAgentDataImpl>(agentsList.size());

    // Initialize agents map
    agentsMap        = new HashMap<Integer, ControlledAgentDataImpl>(agentsList.size());
    for (ControlledAgentDataImpl data : agentsList)
    {
      agentsMap.put(Integer.valueOf(data.getId()), data);
    }
  }


  /**
   * @see net.java.dante.algorithms.data.ControlledAgentsData#getActiveAgentsCount()
   */
  public int getActiveAgentsCount()
  {
    return activeAgents.size();
  }

  /**
   * @see net.java.dante.algorithms.data.ControlledAgentsData#getActiveAgentsData()
   */
  public ControlledAgentData[] getActiveAgentsData()
  {
    return activeAgents.toArray(new ControlledAgentData[activeAgents.size()]);
  }

  /**
   * @see net.java.dante.algorithms.data.ControlledAgentsData#getActiveAgentData(int)
   */
  public ControlledAgentData getActiveAgentData(int index)
  {
    return activeAgents.get(index);
  }

  /**
   * @see net.java.dante.algorithms.data.ControlledAgentsData#getAgentsCount()
   */
  public int getAgentsCount()
  {
    return agents.size();
  }

  /**
   * @see net.java.dante.algorithms.data.ControlledAgentsData#getAgentsData()
   */
  public ControlledAgentData[] getAgentsData()
  {
    return agents.toArray(new ControlledAgentData[agents.size()]);
  }

  /**
   * @see net.java.dante.algorithms.data.ControlledAgentsData#getAgentData(int)
   */
  public ControlledAgentData getAgentData(int agentId)
  {
    return agentsMap.get(Integer.valueOf(agentId));
  }

  /**
   * @see net.java.dante.algorithms.data.ControlledAgentsData#getBlockedAgentsCount()
   */
  public int getBlockedAgentsCount()
  {
    return blockedAgents.size();
  }

  /**
   * @see net.java.dante.algorithms.data.ControlledAgentsData#getBlockedAgentsData()
   */
  public ControlledAgentData[] getBlockedAgentsData()
  {
    return blockedAgents.toArray(new ControlledAgentData[blockedAgents.size()]);
  }

  /**
   * @see net.java.dante.algorithms.EventsUpdateable#update(net.java.dante.sim.event.EventsRepository)
   */
  public void update(EventsRepository repository)
  {
    if (repository == null)
    {
      throw new NullPointerException("Specified repository is null!");
    }

    for (int i = 0, eventsCount = repository.getEventsCount(); i < eventsCount; i++)
    {
      Event event = repository.getEvent(i);
      if (event instanceof FriendlyAgentEvent)
      {
        processFriendlyEvent((FriendlyAgentEvent)event);
      }
    }

    blockedAgents.retainAll(activeAgents);
  }

  /**
   * Processes the specified controlled agent's event.
   *
   * @param event the specified controlled agent's event.
   */
  private void processFriendlyEvent(FriendlyAgentEvent event)
  {
    ControlledAgentDataImpl agentData = agentsMap.get(Integer.valueOf(event.getFriendlyAgentId()));

    if (agentData != null)
    {
      if (event instanceof FriendlyAgentMoveEvent)
      {
        agentData.unblocked();
        blockedAgents.remove(agentData);
        setMovementParameters(agentData, (ObjectMoveEventParams)event);
      }
      else if (event instanceof FriendlyAgentBlockedEvent)
      {
        agentData.blocked();
        blockedAgents.add(agentData);
      }
      else if (event instanceof FriendlyAgentHitEvent)
      {
        FriendlyAgentHitEvent hitEvent = (FriendlyAgentHitEvent)event;
        agentData.setCurrentHitPoints(agentData.getHitPoints() - hitEvent.getDamage());
      }
      else if (event instanceof FriendlyAgentDestroyedEvent)
      {
        agentData.destroyed();
        activeAgents.remove(agentData);
      }
    }
  }

  /**
   * Sets the specified movement parameters of the specified agent's data.
   *
   * @param agentData the specified agent's data.
   * @param moveParams the specified movement parameters.
   */
  private void setMovementParameters(ControlledAgentDataImpl agentData,
                                     ObjectMoveEventParams moveParams)
  {
    agentData.setX(moveParams.getDestinationX());
    agentData.setY(moveParams.getDestinationY());
    agentData.setSpeedX(moveParams.getSpeedX());
    agentData.setSpeedY(moveParams.getSpeedY());
  }
}