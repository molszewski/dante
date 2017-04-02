/*
 * Created on 2006-09-01
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.message;


import net.java.dante.receiver.ReceiverMessage;
import net.java.dante.sim.command.CommandsRepository;

/**
 * Receiver's message sent when algorithm prepared command repository
 * that should be sent to server.
 *
 * @author M.Olszewski
 */
public class CommandsReadyMessage implements ReceiverMessage
{
  /** Commands repository. */
  private CommandsRepository commands;


  /**
   * Creates instance of {@link CommandsReadyMessage} class.
   *
   * @param commandsRepository - commands repository to sent.
   */
  public CommandsReadyMessage(CommandsRepository commandsRepository)
  {
    if (commandsRepository == null)
    {
      throw new NullPointerException("Specified commandsRepository is null!");
    }

    commands = commandsRepository;
  }


  /**
   * Gets the commands repository.
   *
   * @return Returns the commands repository.
   */
  public CommandsRepository getCommands()
  {
    return commands;
  }
}