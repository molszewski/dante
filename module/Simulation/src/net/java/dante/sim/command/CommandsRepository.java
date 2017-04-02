/*
 * Created on 2006-07-31
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.command;

import net.java.dante.sim.command.types.Command;

/**
 * Commands repository: contains all commands for one specified group of agents
 * and information to which agents specified command should be applied.
 * 
 * @author M.Olszewski
 */
public interface CommandsRepository
{
  /**
   * Gets agents group's identifier.
   * 
   * @return Returns agents group's identifier.
   */
  int getGroupId();
  
  /**
   * Gets number of commands stored in this repository.
   * 
   * @return Returns number of commands stored in this repository.
   */
  int getCommandsCount();
  
  /**
   * Gets command with specified identifier or <code>null</code> if command with
   * specified identifier is not stored in this repository.
   * 
   * @param commandId - identifier of command to obtain.
   * 
   * @return Returns command with specified identifier or <code>null</code> 
   *         if command with specified identifier is not stored in this repository.
   */
  Command getCommand(int commandId);
  
  /**
   * Gets number of agents that has some issued commands.
   * 
   * @return Returns number of agents that has some issued commands.
   */
  int getAgentCommandsCount();
  
  /**
   * Gets object representing all commands for one agent from specified index.
   * 
   * @param index - index of object representing all commands for one agent to obtain.
   * 
   * @return Returns object representing all commands for one agent from specified index.
   */
  AgentCommands getAgentCommands(int index);
  
  /**
   * Clears all information stored in this {@link CommandsRepository}.
   */
  void clear();
}