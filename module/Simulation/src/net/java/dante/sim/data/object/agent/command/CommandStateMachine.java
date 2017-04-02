/*
 * Created on 2006-07-27
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.agent.command;

import net.java.dante.sim.data.object.agent.ServerAgentState;

/**
 * Class representing state machine which operates on set of specified
 * states representation.
 *
 * @author M.Olszewski
 */
final class CommandStateMachine
{
  /** Current state. */
  private CommandState currentState = null;
  /** Command is ready state. */
  private ReadyState ready;
  /** Command is executing state. */
  private ExecutingState executing;
  /** Command is stopped state. */
  private StoppedState stopped;
  /** Command's execution is done state. */
  private DoneState done;

  
  /**
   * Creates instance of {@link CommandStateMachine} class with specified
   * objects representing states.
   * 
   * @param readyState - state specifying that command is ready. 
   * @param executingState - state specifying that command is executing.
   * @param stoppedState - state specifying that command is stopped.
   * @param doneState - state specifying that command is done.
   */
  CommandStateMachine(ReadyState readyState, 
      ExecutingState executingState, StoppedState stoppedState,
      DoneState doneState)
  {
    if (readyState == null)
    {
      throw new NullPointerException("Specified readyState is null!");
    }
    if (executingState == null)
    {
      throw new NullPointerException("Specified executingState is null!");
    }
    if (stoppedState == null)
    {
      throw new NullPointerException("Specified stoppedState is null!");
    }
    if (doneState == null)
    {
      throw new NullPointerException("Specified doneState is null!");
    }
    
    ready     = readyState;
    executing = executingState;
    stopped   = stoppedState;
    done      = doneState;
    
    currentState = readyState;
  }
  
  
  /**
   * Sets current state to ready state.
   */
  void setReadyState()
  {
    currentState = ready;
  }
  
  /**
   * Sets current state to executing state.
   */
  void setExecutingState()
  {
    currentState = executing;
  }
  
  /**
   * Sets current state to stopped state.
   */
  void setStoppedState()
  {
    currentState = stopped;
  }
  
  /**
   * Sets current state to done state.
   */
  void setDoneState()
  {
    currentState = done;
  }
  
  /**
   * Executes command.
   * 
   * @param agentId - agent's identifier.
   * @param groupId - agent's group identifier.
   * @param agentState - agent's state to manipulate.
   * @param elapsedTime - time elapsed since last update.
   */
  void execute(int agentId, int groupId, ServerAgentState agentState, 
      long elapsedTime)
  {
    currentState.execute(agentId, groupId, agentState, elapsedTime);
  }
  
  /**
   * Stops executing command.
   */
  void stop()
  {
    currentState.stop();
  }
  
  /**
   * Restarts execution of command.
   */
  void restart()
  {
    currentState.restart();
  }
  
  /**
   * Reverses execution of command.
   * 
   * @param agentState - agent's state to manipulate.
   */
  void reverse(ServerAgentState agentState)
  {
    currentState.reverse(agentState);
  }
  
  /**
   * Determines whether this command's execution is done.
   * 
   * @return Returns <code>true</code> if command's execution is done or
   *         <code>false</code> otherwise.
   */
  boolean isDone()
  {
    return currentState.isDone();
  }
  
  /**
   * Determines whether this command's execution is stopped.
   * 
   * @return Returns <code>true</code> if command's execution is stopped or
   *         <code>false</code> otherwise.
   */
  boolean isStopped()
  {
    return currentState.isStopped();
  }
}