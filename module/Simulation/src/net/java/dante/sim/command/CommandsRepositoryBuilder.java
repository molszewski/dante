/*
 * Created on 2006-07-31
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.command;

import net.java.dante.sim.command.types.Command;

/**
 * Interface for commands repository builders. Commands are stored internally
 * in builder until {@link #build()} method is invoked - then inner
 * representation is cleared and commands are moved to proper 
 * {@link net.java.dante.sim.command.CommandsRepository} object, which reference is returned
 * by {@link #build()} method.<p>
 * Builder must assure that order of all commands identifiers that can be 
 * obtained via {@link net.java.dante.sim.command.AgentCommands} object
 * will be <b>exactly the same<b> as the order of adding respective commands to
 * repository.
 *
 * @author M.Olszewski
 */
public interface CommandsRepositoryBuilder
{
  /**
   * Adds command to this commands repository builder. Added commands must be
   * transformed into {@link CommandsRepository} by call to {@link #build()} 
   * method.<p>
   * The same {@link Command} object cannot be added more than once 
   * for the same agent and group.
   * 
   * @param agentId - agent's identifier.
   * @param command - command to add.
   */
  void addCommand(int agentId, Command command);
  
  /**
   * Builds all stored commands into {@link net.java.dante.sim.command.CommandsRepository} object.
   * Building means here that all commands stored internally by builder
   * are transformed into proper structure that can be obtained through
   * returned {@link net.java.dante.sim.command.CommandsRepository} object. 
   * All data from inner representation must be cleared after successful 
   * building, so next {@link #addCommand(int, Command)} 
   * call will add first command.
   * If no command was added to this builder, <code>null</code> value must be
   * returned.
   * 
   * @return Returns {@link net.java.dante.sim.command.CommandsRepository} object with proper
   *         commands or <code>null</code> if no commands were added to this
   *         builder before calling this method.
   */
  CommandsRepository build();
}