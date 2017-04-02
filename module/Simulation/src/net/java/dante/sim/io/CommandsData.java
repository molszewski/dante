/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io;

import net.java.dante.sim.command.CommandsRepository;

/**
 * Commands data generated as input for simulation servers.
 *
 * @author M.Olszewski
 */
public class CommandsData implements ServerInputData
{
  /** Commands repository. */
  private CommandsRepository repository;


  /**
   * Creates instance of {@link CommandsData} class.
   *
   * @param commandsRepository - commands repository.
   */
  public CommandsData(CommandsRepository commandsRepository)
  {
    if (commandsRepository == null)
    {
      throw new NullPointerException("Specified commandsRepository is null!");
    }
    if (commandsRepository.getAgentCommandsCount() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument commandsRepository - it must contain at least 1 agents group receiving events!");
    }

    repository = commandsRepository;
  }


  /**
   * Gets commands repository.
   *
   * @return Returns commands repository.
   */
  public CommandsRepository getRepository()
  {
    return repository;
  }


  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + repository.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof CommandsData))
    {
      final CommandsData other = (CommandsData) object;
      equal = (repository.equals(other.repository));
    }
    return equal;
  }
}