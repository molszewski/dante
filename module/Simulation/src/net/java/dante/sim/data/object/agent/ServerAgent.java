/*
 * Created on 2006-06-08
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.object.agent;

import net.java.dante.sim.command.types.ClearQueueCommand;
import net.java.dante.sim.command.types.Command;
import net.java.dante.sim.command.types.ResumeLastCommand;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.agent.command.AgentCommand;
import net.java.dante.sim.data.object.agent.command.CommandsQueue;
import net.java.dante.sim.data.object.agent.command.IdleCommand;
import net.java.dante.sim.data.object.state.ObjectState;

/**
 * Representation of agent used by server side for all agents.
 *
 * @author M.Olszewski
 */
public class ServerAgent extends SimulationObject
{
  /** Agent's commands queue. */
  private CommandsQueue queue;
  /** Command currently processed by this {@link ServerAgent}. */
  private AgentCommand currentCommand;
  /** Agent's state. */
  private ServerAgentState state;
  /** Agent's group identifier.   */
  private int groupId;


  /**
   * Creates instance of {@link ServerAgent} class with specified agent's state
   * and objects mediator.
   *
   * @param agentState - state of this agent.
   * @param queueSize - maximum size of agent's commands queue.
   */
  public ServerAgent(ServerAgentState agentState, int queueSize)
  {
    if (agentState == null)
    {
      throw new NullPointerException("Specified agentState is null!");
    }

    state = agentState;
    queue = new CommandsQueue(queueSize);

    // Take first command = IDLE
    currentCommand = queue.takeCommand();
  }


  /**
   * @see net.java.dante.sim.data.object.SimulationObject#getData()
   */
  public ObjectState getData()
  {
    return state;
  }

  /**
   * Updates state of the agent.
   *
   * @param delta - time that has passed since last update.
   */
  public void update(long delta)
  {
    // update weapon system.
    state.getWeapon().update(delta);

    if (isCurrentCommandDone())
    {
      currentCommand = queue.takeCommand();
    }
    if (isCommandExecutable(currentCommand))
    {
      currentCommand.execute(getId(), groupId, state, delta);
    }
  }

  /**
   * Checks whether current command execution is done or was aborted.
   *
   * @return Returns <code>true</code> if current command's execution is done
   *         or was aborted, <code>false</code> otherwise.
   */
  private boolean isCurrentCommandDone()
  {
    return ((currentCommand instanceof IdleCommand) || currentCommand.isDone());
  }

  /**
   * Checks whether current command - different than idle command -
   * execution was done.
   *
   * @return Returns <code>true</code> if current command's execution is done,
   *         <code>false</code> otherwise.
   */
  public boolean isCommandDone()
  {
    return (!(currentCommand instanceof IdleCommand) && currentCommand.isDone());
  }

  /**
   * Checks whether specified command is executable.
   *
   * @param command - command to check.
   *
   * @return Returns <code>true</code> if command is executable,
   *         <code>false</code> otherwise.
   */
  private boolean isCommandExecutable(AgentCommand command)
  {
    return (!(currentCommand instanceof IdleCommand) && !currentCommand.isStopped());
  }

  /**
   * Adds specified command to agent's queue. If command is 'clear queue'
   * command, command is not added but queue is cleared and execution of
   * current command is aborted (it cannot be executed anymore).
   * This method returns <code>true</code> if command was added or
   * <code>false</code> if command was not added because of
   * exceeding queue capacity.
   *
   * @param command - command to add.
   *
   * @return Returns <code>true</code> if command was added or
   *         <code>false</code> if command was not added because of
   *         exceeding queue capacity.
   */
  public boolean addCommand(Command command)
  {
    boolean added = true;

    if (command instanceof ClearQueueCommand)
    {
      queue.clear();
      currentCommand = queue.takeCommand();
    }
    else if (command instanceof ResumeLastCommand)
    {
      if (currentCommand.isStopped())
      {
        currentCommand.restart();
      }
    }
    else
    {
      added = queue.addCommand(command);
    }

    return added;
  }

  /**
   * Clears commands queue.
   */
  public void clearCommandsQueue()
  {
    queue.clear();
  }

  /**
   * Gets agent's group identifier.
   *
   * @return Returns agent's group identifier.
   */
  public int getGroupId()
  {
    return groupId;
  }

  /**
   * Sets the specified agent's group identifier.
   *
   * @param id - the specified agent's group identifier.
   */
  public void setGroupId(int id)
  {
    groupId = id;
  }

  /**
   * Callback method that should be invoked if under any circumstances execution
   * of current command was interrupted.
   */
  public void commandInterrupted()
  {
    currentCommand.reverse(state);
    currentCommand.stop();
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[state=" + state + "; currentCommand=" +
        currentCommand + "; queueSize=" + queue.size() + "]");
  }
}