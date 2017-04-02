/*
 * Created on 2006-07-27
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.agent.command;

import net.java.dante.sim.data.object.agent.ServerAgentState;

/**
 * Base class for all state classes for commands.
 *
 * @author M.Olszewski
 */
class CommandState
{
  /**
   * Creates instance of {@link CommandState} class.
   */
  CommandState()
  {
    // Intentionally left empty.
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
    // Intentionally left empty.
  }
  
  /**
   * Stops executing command.
   */
  void stop()
  {
    // Intentionally left empty.
  }
  
  /**
   * Restarts execution of command.
   */
  void restart()
  {
    // Intentionally left empty.
  }

  /**
   * Reverses execution of command.
   * 
   * @param agentState - agent's state to manipulate.
   */
  void reverse(ServerAgentState agentState)
  {
    // Intentionally left empty.
  }
  
  /**
   * Determines whether this command's execution is done.
   * 
   * @return Returns <code>true</code> if command's execution is done or
   *         <code>false</code> otherwise.
   */
  boolean isDone()
  {
    return false;
  }
  
  /**
   * Determines whether this command's execution is stopped.
   * 
   * @return Returns <code>true</code> if command's execution is stopped or
   *         <code>false</code> otherwise.
   */
  boolean isStopped()
  {
    return false;
  }
}