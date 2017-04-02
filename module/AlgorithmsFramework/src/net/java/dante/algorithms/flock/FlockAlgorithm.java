/*
 * Created on 2006-09-11
 *
 * @author M.Olszewski
 */


package net.java.dante.algorithms.flock;

import java.util.HashMap;
import java.util.Map;

import net.java.dante.algorithms.AlgorithmUtils;
import net.java.dante.algorithms.BaseAlgorithmImpl;
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
import net.java.dante.sim.util.math.Point2d;


/**
 * Flock algorithm class.
 *
 * @author M.Olszewski
 */
public class FlockAlgorithm extends BaseAlgorithmImpl
{
  private static final int TIME_STOPPER_INTERVAL = 50;
  private Map<Integer, FlockMember> members;
  private CommandsRepositoryBuilder builder;
  DestinationChooser cruiser;

  /**
   * Creates instance of {@link FlockAlgorithm} class.
   */
  public FlockAlgorithm()
  {
    super(false);
  }

  /**
   * @see net.java.dante.algorithms.BaseAlgorithmImpl#runAlgorithm()
   */
  protected void runAlgorithm()
  {
    AlgorithmData data = getAlgorithmData();
    initialize(data);

    SystemTimer timer = new SystemTimer();
    TimeStopper stopper = new TimeStopper(timer, TIME_STOPPER_INTERVAL);
    long delta        = 0;
    long lastLoopTime = 0;

    while (isRunning())
    {
      delta = timer.getTime() - lastLoopTime;
      lastLoopTime = timer.getTime();

      stopper.startPoint();
      {
        cruiser.update(delta);
        for (Integer memberId : members.keySet())
        {
          FlockMember member = members.get(memberId);
          member.update(delta);
        }

        updateFlockMembers(refreshAndObtain());

        sendCommands(builder.build());
      }
      stopper.endPoint();

      // No point in running
      if (members.size() <= 0)
      {
        break;
      }
    }
  }

  private void initialize(AlgorithmData algorithmData)
  {
    if (algorithmData == null)
    {
      throw new NullPointerException("Specified algorithmData is null!");
    }

    builder = CommandUtils.createDefaultBuilder(algorithmData.getGroupId());
    initMembers(algorithmData);
  }

  private void initMembers(AlgorithmData algorithmData)
  {
    ControlledAgentData[] agents = algorithmData.getControlledData().getAgentsData();
    members = new HashMap<Integer, FlockMember>(agents.length);

    TileData[] obstacles = AlgorithmUtils.createTilesData(algorithmData.getMap(),
                                                          algorithmData.getTileSize());

    for (int i = 0; i < agents.length; i++)
    {
      ControlledAgentData agent = agents[i];
      members.put(Integer.valueOf(agent.getId()),
                                  new FlockMember(agent, algorithmData, builder,
                                                  new Point2d(400, 304), obstacles));
    }

    cruiser = new DestinationChooser(members, algorithmData);

    for (Integer memberId : members.keySet())
    {
      members.get(memberId).setFlockRepository(members);
    }


  }

  private void updateFlockMembers(UpdateData update)
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
          members.remove(Integer.valueOf(destroyedEvt.getFriendlyAgentId()));
        }
        else if (event instanceof FriendlyAgentMoveEvent)
        {
          FriendlyAgentMoveEvent moveEvt = (FriendlyAgentMoveEvent)event;

          FlockMember member = members.get(Integer.valueOf(moveEvt.getFriendlyAgentId()));
          if (member != null)
          {
            member.moved(moveEvt.getDestinationX(), moveEvt.getDestinationY(),
                         moveEvt.getSpeedX(), moveEvt.getSpeedY());
          }
        }
        else if (event instanceof FriendlyAgentMoveFinishedEvent)
        {
          FriendlyAgentMoveFinishedEvent finishedEvt = (FriendlyAgentMoveFinishedEvent)event;

          FlockMember member = members.get(Integer.valueOf(finishedEvt.getFriendlyAgentId()));
          if (member != null)
          {
            member.moveFinished();
          }
        }
        else if (event instanceof FriendlyAgentBlockedEvent)
        {
          FriendlyAgentBlockedEvent blockedEvt = (FriendlyAgentBlockedEvent)event;

          FlockMember member = members.get(Integer.valueOf(blockedEvt.getFriendlyAgentId()));
          if (member != null)
          {
            member.blocked(blockedEvt.getBlockedX(),
                           blockedEvt.getBlockedY());
          }
        }
      }
    }
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return "Flock algorithm";
  }
}