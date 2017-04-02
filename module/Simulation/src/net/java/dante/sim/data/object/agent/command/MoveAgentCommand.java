/*
 * Created on 2006-05-02
 *
 * @author M.Olszewski
 */
package net.java.dante.sim.data.object.agent.command;

import net.java.dante.sim.data.object.DistanceTracker;
import net.java.dante.sim.data.object.agent.ServerAgentState;
import net.java.dante.sim.util.math.MathUtils;
import net.java.dante.sim.util.math.Vector2d;

/**
 * Movement command - issues agent to move from its current location to
 * location specified as parameters in constructor. Moving agent cannot
 * attack.
 *
 * @author M.Olszewski
 */
final class MoveAgentCommand extends AbstractAgentCommand
{
  /** Destination's 'x' coordinate. */
  double targetX;
  /** Destination's 'y' coordinate. */
  double targetY;
  /**
   * Most recent delta passed as argument in
   * {@link MoveExecutingState#execute(int, int, ServerAgentState, long)} method.
   */
  long lastDelta = 0;
  /** Tracks whether destination point was reached. */
  DistanceTracker tracker;


  /**
   * Creates instance of {@link MoveAgentCommand} with specified destination
   * coordinates.
   *
   * @param destinationX - destination's 'x' coordinate.
   * @param destinationY - destination's 'y' coordinate.
   */
  MoveAgentCommand(double destinationX, double destinationY)
  {
    targetX = destinationX;
    targetY = destinationY;

    stateMachine = new CommandStateMachine(new MoveReadyState(),
        new MoveExecutingState(), new BaseStoppedState(), new MoveDoneState());
  }


  /**
   * Implementation of {@link ReadyState} specific for 'move' command.
   *
   * @author M.Olszewski
   */
  final class MoveReadyState extends ReadyState
  {
    /**
     * @see net.java.dante.sim.data.object.agent.command.CommandState#execute(int, int, net.java.dante.sim.data.object.agent.ServerAgentState, long)
     */
    @Override
    void execute(int agentId, int groupId, ServerAgentState agentState, long elapsedTime)
    {
      int halfWidth = (agentState.getSize().getWidth() >> 1);
      int halfHeight = (agentState.getSize().getHeight() >> 1);

      double startX = agentState.getX() + halfWidth;
      double startY = agentState.getY() + halfHeight;
      Vector2d velocity = MathUtils.calculateVelocity(agentState.getMaxSpeed(),
                                                      startX, startY,
                                                      targetX, targetY);
      agentState.setSpeedX(velocity.getX());
      agentState.setSpeedY(velocity.getY());

      tracker = new DistanceTracker(agentState.getMaxSpeed(),
                                    startX, startY,
                                    targetX, targetY);

      // Subtract halves - at last we're moving first point of agent
      targetX -= halfWidth;
      targetY -= halfHeight;

      // Set to executing and execute.
      stateMachine.setExecutingState();
      stateMachine.execute(agentId, groupId, agentState, elapsedTime);
    }
  }

  /**
   * Implementation of {@link ExecutingState} specific for 'move' command.
   *
   * @author M.Olszewski
   */
  final class MoveExecutingState extends ExecutingState
  {
    /**
     * @see net.java.dante.sim.data.object.agent.command.CommandState#execute(int, int, net.java.dante.sim.data.object.agent.ServerAgentState, long)
     */
    @Override
    void execute(int agentId, int groupId, ServerAgentState agentState, long elapsedTime)
    {
      lastDelta = elapsedTime;

      double dx = ((elapsedTime * agentState.getSpeedX()) / 1000);
      double dy = ((elapsedTime * agentState.getSpeedY()) / 1000);
      agentState.setX(agentState.getX() + dx);
      agentState.setY(agentState.getY() + dy);

      if (tracker.isDestinationReached(elapsedTime))
      {
        // correct position if necessary
        if (agentState.getX() != targetX)
        {
          agentState.setX(targetX);
        }
        if (agentState.getY() != targetY)
        {
          agentState.setY(targetY);
        }

        stateMachine.setDoneState();
      }
    }

    /**
     * @see net.java.dante.sim.data.object.agent.command.CommandState#stop()
     */
    @Override
    void stop()
    {
      stateMachine.setStoppedState();
    }

    /**
     * @see net.java.dante.sim.data.object.agent.command.CommandState#reverse(net.java.dante.sim.data.object.agent.ServerAgentState)
     */
    @Override
    void reverse(ServerAgentState agentState)
    {
      agentState.reversePosition();
      tracker.reverse(lastDelta);
    }
  }

  /**
   * Implementation of {@link DoneState} specific for 'move' command.
   *
   * @author M.Olszewski
   */
  final class MoveDoneState extends DoneState
  {
    /**
     * @see net.java.dante.sim.data.object.agent.command.CommandState#reverse(net.java.dante.sim.data.object.agent.ServerAgentState)
     */
    @Override
    void reverse(ServerAgentState agentState)
    {
      agentState.setX(agentState.getOldX());
      agentState.setY(agentState.getOldY());
      if (!tracker.reverse(lastDelta))
      {
        stateMachine.setExecutingState();
      }
    }
  }
}