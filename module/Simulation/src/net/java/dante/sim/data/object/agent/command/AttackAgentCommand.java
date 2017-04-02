/*
 * Created on 2006-07-28
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.object.agent.command;

import net.java.dante.sim.common.Dbg;
import net.java.dante.sim.data.object.agent.ServerAgentState;
import net.java.dante.sim.util.math.Line2d;
import net.java.dante.sim.util.math.MathUtils;
import net.java.dante.sim.util.math.Point2d;
import net.java.dante.sim.util.math.Rect2d;

/**
 * Attack command - issues agent to attack specified location (x, y) using
 * agent's weapon system. If projectile cannot be generated by weapon system,
 * agent will try to do it until projectile is generated.
 *
 * @author M.Olszewski
 */
final class AttackAgentCommand extends AbstractAgentCommand
{
  /** Projectile destination's 'x' coordinate. */
  double targetX;
  /** Projectile destination's 'y' coordinate. */
  double targetY;


  /**
   * Creates instance of {@link AttackAgentCommand} with specified destination
   * coordinates.
   *
   * @param destinationX - projectile destination's 'x' coordinate.
   * @param destinationY - projectile destination's 'y' coordinate.
   */
  AttackAgentCommand(double destinationX, double destinationY)
  {
    targetX = destinationX;
    targetY = destinationY;

    stateMachine = new CommandStateMachine(new AttackReadyState(),
        new AttackExecutingState(), new StoppedState(), new DoneState());
  }


  /**
   * Implementation of {@link ReadyState} specific for 'attack' command.
   *
   * @author M.Olszewski
   */
  class AttackReadyState extends ReadyState
  {
    /**
     * @see net.java.dante.sim.data.object.agent.command.CommandState#execute(int, int, net.java.dante.sim.data.object.agent.ServerAgentState, long)
     */
    @Override
    void execute(int agentId, int groupId, ServerAgentState agentState, long elapsedTime)
    {
      // Set to executing and execute.
      stateMachine.setExecutingState();
      stateMachine.execute(agentId, groupId, agentState, elapsedTime);
    }
  }

  /**
   * Implementation of {@link ExecutingState} specific for 'attack' command.
   *
   * @author M.Olszewski
   */
  class AttackExecutingState extends ExecutingState
  {
    /**
     * @see net.java.dante.sim.data.object.agent.command.CommandState#execute(int, int, net.java.dante.sim.data.object.agent.ServerAgentState, long)
     */
    @Override
    void execute(int agentId, int groupId, ServerAgentState agentState, long elapsedTime)
    {
      double x = agentState.getX();
      double y = agentState.getY();
      int width = agentState.getSize().getWidth();
      int height = agentState.getSize().getHeight();
      double centerX = x + (width >> 1);
      double centerY = y + (height >> 1);

      int widthDx = agentState.getWeapon().getProjectileSize().getWidth();
      int heightDx = agentState.getWeapon().getProjectileSize().getHeight();

      Point2d point = MathUtils.calculateFirstIntersection(new Line2d(centerX, centerY, targetX, targetY),
          new Rect2d(x - widthDx, y - heightDx, width + widthDx, height + heightDx));

      if (point != null)
      {
        if (agentState.getWeapon().shoot(groupId, agentId,
                                         point.getX(), point.getY(),
                                         targetX, targetY))
        {
          stateMachine.setDoneState();
        }
      }
      else
      {
        // Point will never be found, so discard this command
        stateMachine.setDoneState();

        if (Dbg.DBGW)
        {
          if (Dbg.DBG1)
          {
            Dbg.warning("Cannot find start point for projectile - probably " +
                "targeting to location inside agent. Line: " +
                new Line2d(centerX, centerY, targetX, targetY) + " rectangle: " +
                new Rect2d(x - widthDx, y - heightDx, width + widthDx, height + heightDx) );
          }
          else
          {
            Dbg.warning("Cannot find start point for projectile - probably targeting to location inside agent.");
          }
        }
      }
    }
  }
}