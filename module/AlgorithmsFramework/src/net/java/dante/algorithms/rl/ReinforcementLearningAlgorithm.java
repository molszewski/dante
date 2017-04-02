/*
 * Created on 2006-09-10
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.rl;

import java.util.HashMap;
import java.util.Map;

import net.java.dante.algorithms.AlgorithmUtils;
import net.java.dante.algorithms.BaseAlgorithmImpl;
import net.java.dante.algorithms.common.Dbg;
import net.java.dante.algorithms.data.AlgorithmData;
import net.java.dante.algorithms.data.ControlledAgentData;
import net.java.dante.algorithms.data.TileData;
import net.java.dante.sim.command.CommandUtils;
import net.java.dante.sim.command.CommandsRepositoryBuilder;
import net.java.dante.sim.engine.time.SystemTimer;
import net.java.dante.sim.engine.time.TimeStopper;
import net.java.dante.sim.event.Event;
import net.java.dante.sim.event.EventsRepository;
import net.java.dante.sim.event.types.FriendlyAgentBlockedEvent;
import net.java.dante.sim.event.types.FriendlyAgentDestroyedEvent;
import net.java.dante.sim.event.types.FriendlyAgentMoveEvent;
import net.java.dante.sim.event.types.FriendlyAgentMoveFinishedEvent;
import net.java.dante.sim.io.UpdateData;


/**
 * Reinforcement learning algorithm using
 * "Connectionist Q-learning Java Framework".
 *
 * @author M.Olszewski
 */
public class ReinforcementLearningAlgorithm extends BaseAlgorithmImpl
{
  private static final int TIME_STOPPER_INTERVAL = 50;
  private CommandsRepositoryBuilder builder;
  private Map<Integer, RFAgent> agents;

  /**
   * Creates instance of {@link ReinforcementLearningAlgorithm} class.
   */
  public ReinforcementLearningAlgorithm()
  {
    // We'll not wait for updates
    super(false);
  }


  /**
   * @see net.java.dante.algorithms.BaseAlgorithmImpl#runAlgorithm()
   */
  @Override
  protected void runAlgorithm()
  {
    Dbg.enableDateTimeLog("RL__log__", ".log");

    initialize();
    startAgents();

    SystemTimer timer = new SystemTimer();
    TimeStopper stopper = new TimeStopper(timer, TIME_STOPPER_INTERVAL);
    timer.start();

    long delta = 0;
    long lastTime = 0;

    sendCommands(builder.build());

    while (isRunning())
    {
      delta = timer.getTime() - lastTime;
      lastTime = timer.getTime();

      stopper.startPoint();
      {
        // Make decisions
        for (Integer agentId : agents.keySet())
        {
          RFAgent agent = agents.get(agentId);

          agent.update(delta);
        }

        updateAgents(refreshAndObtain());

        sendCommands(builder.build());
      }
      stopper.endPoint();

      // No point in running
      if (agents.size() <= 0)
      {
        break;
      }
    }

    store();

    Dbg.disableLogging();
  }

  private void initialize()
  {
    AlgorithmData data = getAlgorithmData();

    builder = CommandUtils.createDefaultBuilder(data.getGroupId());
    TileData[] tiles = AlgorithmUtils.createTilesData(data.getMap(), data.getTileSize());

    ControlledAgentData[] agentsData =  data.getControlledData().getAgentsData();
    agents = new HashMap<Integer, RFAgent>(agentsData.length);

    for (int i = 0; i < agentsData.length; i++)
    {
      ControlledAgentData agentData = agentsData[i];
      agents.put(Integer.valueOf(agentData.getId()),
                 new RFAgent(data, agentData, builder, tiles, i));
    }
  }

  private void startAgents()
  {
    for (Integer agentId : agents.keySet())
    {
      agents.get(agentId).update(0);
    }
  }

  private void updateAgents(UpdateData update)
  {
    if (update != null)
    {
      EventsRepository repository = update.getRepository();
      for (int i = 0, size = repository.getEventsCount(); i < size; i++)
      {
        Event event = repository.getEvent(i);
        if (event instanceof FriendlyAgentDestroyedEvent)
        {
          FriendlyAgentDestroyedEvent destroyedEvt = (FriendlyAgentDestroyedEvent)event;
          Integer agentId = Integer.valueOf(destroyedEvt.getFriendlyAgentId());
          RFAgent agent = agents.get(agentId);
          if (agent != null)
          {
            agent.storeBrain();
          }
          agents.remove(agentId);
        }
        else if (event instanceof FriendlyAgentMoveEvent)
        {
          FriendlyAgentMoveEvent moveEvt = (FriendlyAgentMoveEvent)event;

          RFAgent agent = agents.get(Integer.valueOf(moveEvt.getFriendlyAgentId()));
          if (agent != null)
          {
            agent.moved(moveEvt.getDestinationX(), moveEvt.getDestinationY(),
                        moveEvt.getSpeedX(), moveEvt.getSpeedY());
          }
        }
        else if (event instanceof FriendlyAgentMoveFinishedEvent)
        {
          FriendlyAgentMoveFinishedEvent finishedEvt = (FriendlyAgentMoveFinishedEvent)event;

          RFAgent agent = agents.get(Integer.valueOf(finishedEvt.getFriendlyAgentId()));
          if (agent != null)
          {
            agent.moveFinished();
          }
        }
        else if (event instanceof FriendlyAgentBlockedEvent)
        {
          FriendlyAgentBlockedEvent blockedEvt = (FriendlyAgentBlockedEvent)event;

          Integer agentId = Integer.valueOf(blockedEvt.getFriendlyAgentId());
          RFAgent agent = agents.get(agentId);
          if (agent != null)
          {
            agent.blocked(blockedEvt.getBlockedX(),
                          blockedEvt.getBlockedY());
          }
        }

      }
    }
  }

  private void store()
  {
    for (Integer agentId : agents.keySet())
    {
      agents.get(agentId).storeBrain();
    }
  }


  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return "Reinforcement Learning Algorithm [Q-learning]";
  }
}