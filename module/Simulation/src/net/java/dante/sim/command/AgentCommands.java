/*
 * Created on 2006-07-31
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.command;

/**
 * Interface for classes storing agent's commands. Agent's commands identifiers
 * are obtained in the same order, as they were put into proper
 * {@link net.java.dante.sim.command.CommandsRepository} object.
 *
 * @author M.Olszewski
 */
public interface AgentCommands
{
  /**
   * Gets this agent's identifier.
   * 
   * @return Returns this agent's identifier.
   */
  int getAgentId();
  
  /**
   * Gets number of commands for this agent.
   * 
   * @return Returns number of commands for this agent.
   */
  int getCommandsCount();
  
  /**
   * Gets command's identifier from specified index.
   * 
   * @param index - index of command's identifier to obtain.
   * 
   * @return Returns command's identifier from specified index.
   */
  int getCommandId(int index);
}