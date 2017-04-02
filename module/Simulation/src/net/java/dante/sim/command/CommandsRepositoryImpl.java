/*
 * Created on 2006-07-31
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.command;

import java.util.List;
import java.util.Map;

import net.java.dante.sim.command.types.Command;


/**
 * Implementation of {@link CommandsRepository} interface.
 *
 * @author M.Olszewski
 */
final class CommandsRepositoryImpl implements CommandsRepository
{
  /** Identifier of an agents group for which commands are stored. */
  private int groupId;
  /** Map with all commands and their identifiers. */
  private Map<Integer, Command> commands;
  /** List with {@link AgentCommands} objects. */
  private List<AgentCommands> groupCommands;


  /**
   * Creates instance of {@link CommandsRepositoryImpl} class.
   *
   * @param agentGroupId identifier of an agents group for which commands are stored.
   * @param commandsMap map with all commands and their identifiers.
   * @param agentsCommandsList list with {@link AgentCommands} objects.
   */
  CommandsRepositoryImpl(int agentGroupId, Map<Integer, Command> commandsMap,
      List<AgentCommands> agentsCommandsList)
  {
    if (commandsMap == null)
    {
      throw new NullPointerException("Specified commandsMap is null!");
    }
    if (agentsCommandsList == null)
    {
      throw new NullPointerException("Specified agentsCommandsList is null!");
    }
    if (agentGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument groupId - it must be zero or positive integer!");
    }
    if (commandsMap.size() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument commandsMap - it must contain at least 1 element!");
    }
    if (agentsCommandsList.size() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsCommandsList - it must contain at least 1 element!");
    }

    groupId       = agentGroupId;
    commands      = commandsMap;
    groupCommands = agentsCommandsList;
  }


  /**
   * @see net.java.dante.sim.command.CommandsRepository#getGroupId()
   */
  public int getGroupId()
  {
    return groupId;
  }

  /**
   * @see net.java.dante.sim.command.CommandsRepository#getAgentCommands(int)
   */
  public AgentCommands getAgentCommands(int index)
  {
    return groupCommands.get(index);
  }

  /**
   * @see net.java.dante.sim.command.CommandsRepository#getAgentCommandsCount()
   */
  public int getAgentCommandsCount()
  {
    return groupCommands.size();
  }

  /**
   * @see net.java.dante.sim.command.CommandsRepository#getCommandsCount()
   */
  public int getCommandsCount()
  {
    return commands.size();
  }

  /**
   * @see net.java.dante.sim.command.CommandsRepository#getCommand(int)
   */
  public Command getCommand(int commandId)
  {
    return commands.get(Integer.valueOf(commandId));
  }

  /**
   * @see net.java.dante.sim.command.CommandsRepository#clear()
   */
  public void clear()
  {
    commands.clear();
    groupCommands.clear();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + commands.hashCode();
    result = PRIME * result + groupCommands.hashCode();
    result = PRIME * result + groupId;

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof CommandsRepositoryImpl))
    {
      final CommandsRepositoryImpl other = (CommandsRepositoryImpl) object;
      equal = ((groupId == other.getGroupId()) &&
               (commands.equals(other.commands)) &&
               (groupCommands.equals(other.groupCommands)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[groupId=" + groupId + "; commands=" + commands + "; groupCommands=" + groupCommands + "]");
  }
}