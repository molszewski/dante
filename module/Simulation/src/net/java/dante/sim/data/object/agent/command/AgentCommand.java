/*
 * Created on 2006-06-08
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.agent.command;

import net.java.dante.sim.data.object.agent.ServerAgentState;

/**
 * Class defining set of methods for commands regarding agents.
 * Each command must start as ready to execute. Than it can be
 * executed, restarted or stopped. 
 *
 * @author M.Olszewski 
 */
public interface AgentCommand
{
  /**
   * Determines whether this command's execution is done.
   * 
   * @return Returns <code>true</code> if command's execution is done or
   *         <code>false</code> otherwise.
   */
  public boolean isDone();
  
  /**
   * Determines whether this command's execution is stopped.
   * 
   * @return Returns <code>true</code> if command's execution is stopped or
   *         <code>false</code> otherwise.
   */
  public boolean isStopped();
  
  /**
   * Executes the command by manipulating the agent's state. If execution
   * is finished, command's execution is done and further calls should 
   * return immediately.
   * 
   * @param agentId - agent's identifier.
   * @param groupId - agent's group identifier.
   * @param agentState - agent's state to manipulate.
   * @param elapsedTime - time elapsed since last execution.
   */
  void execute(int agentId, int groupId, ServerAgentState agentState, long elapsedTime);
  
  /**
   * Stops executing command now. Only commands being executed can be stopped. 
   * Otherwise it should return immediately without changing anything.
   */
  void stop();
  
  /**
   * Restarts command now. If command was stopped, it should be restarted
   * with last data. 
   */
  void restart();

  /**
   * Reverses this command now. State of agent must return to state exactly
   * as before last execution of command.
   * 
   * @param agentState - agent's state to reverse.
   */
  void reverse(ServerAgentState agentState);
}