/*
 * Created on 2006-06-08
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.object.agent.command;

import java.util.LinkedList;

import net.java.dante.sim.command.types.AttackCommand;
import net.java.dante.sim.command.types.Command;
import net.java.dante.sim.command.types.MoveCommand;
import net.java.dante.sim.common.Dbg;



/**
 * Agent's commands queue.
 *
 * @author M.Olszewski
 */
public class CommandsQueue
{
  /** Capacity of this queue. */
  private final int queueCapacity;
  /** Linked list representing queue. */
  private LinkedList<AgentCommand> list = new LinkedList<AgentCommand>();
  /** Idle command. */
  private final IdleCommand idleCommand = new IdleCommand();


  /**
   * Creates instance of {@link CommandsQueue} class with maximum capacity
   * (equal to {@link Integer#MAX_VALUE}.
   */
  public CommandsQueue()
  {
    this(Integer.MAX_VALUE);
  }

  /**
   * Creates instance of {@link CommandsQueue} class with the specified capacity.
   *
   * @param queueSize the specified capacity.
   */
  public CommandsQueue(int queueSize)
  {
    queueCapacity = queueSize;
  }


  /**
   * Obtains first command from a queue and removes it from commands list.
   *
   * @return Returns first command from a queue or idle command if
   *         there are no commands in a queue.
   */
  public AgentCommand takeCommand()
  {
    AgentCommand command = list.poll();
    return ((command != null)? command : idleCommand);
  }

  /**
   * Adds specified command to queue. This method returns <code>true</code>
   * if command was added or <code>false</code> if command was not added
   * because of exceeding queue capacity.
   *
   * @param cmd command which will be added to this queue.
   *
   * @return Returns <code>true</code> if command was added or
   *         <code>false</code> if command was not added because of
   *         exceeding queue capacity.
   */
  public boolean addCommand(Command cmd)
  {
    boolean added = true;

    if (list.size() < queueCapacity)
    {
      if (cmd instanceof MoveCommand)
      {
        MoveCommand moveCmd = (MoveCommand)cmd;
        list.offer(
            new MoveAgentCommand(moveCmd.getDestinationX(), moveCmd.getDestinationY()));
      }
      else if (cmd instanceof AttackCommand)
      {
        AttackCommand attackCmd = (AttackCommand)cmd;
        list.offer(
            new AttackAgentCommand(attackCmd.getTargetX(), attackCmd.getTargetY()));
      }
      else
      {
        if (Dbg.DBGW) Dbg.warning("Not supported type of command detected: " + cmd);
      }
    }
    else
    {
      added = false;
    }

    return added;
  }

  /**
   * Gets size of this commands queue.
   *
   * @return Returns size of this commands queue.
   */
  public int size()
  {
    return list.size();
  }

  /**
   * Clears the commands queue.
   */
  public void clear()
  {
    list.clear();
  }
}