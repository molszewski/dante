/*
 * Created on 2006-08-17
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.java.dante.sim.command.types.Command;


/**
 * Utility class delivering methods for creating instances of following classes,
 * connected with {@link net.java.dante.sim.event} package:
 * <ol>
 * <li>{@link CommandsRepositoryBuilder} class,
 * <li>{@link AgentCommands} class,
 * <li>{@link CommandsRepository} class
 * </ol>
 *
 * @author M.Olszewski
 */
public class CommandUtils
{
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private CommandUtils()
  {
    // Intentionally left empty.
  }

  /**
   * Creates default instance of class implementing {@link CommandsRepositoryBuilder}
   * interface creating commands for group of agents with specified identifier.
   *
   * @param groupId - group of agents identifier.
   *
   * @return Returns created instance of class implementing {@link CommandsRepositoryBuilder}
   *         interface.
   */
  public static CommandsRepositoryBuilder createDefaultBuilder(int groupId)
  {
    return new CommandsRepositoryBuilderImpl(groupId);
  }

  /**
   * Creates instance of {@link AgentCommands} class with specified parameters.
   *
   * @param agentId - agent's identifier.
   * @param commandsIdsArray - array with commands identifiers.
   *
   * @return Returns instance of {@link AgentCommands} class.
   */
  public static AgentCommands createAgentCommands(int agentId,
      int[] commandsIdsArray)
  {
    if (commandsIdsArray == null)
    {
      throw new NullPointerException("Specified commandsIdsArray is null!");
    }
    if (agentId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentId - it must be positive integer or zero!");
    }

    List<Integer> commandsIdsList = new ArrayList<Integer>(commandsIdsArray.length);
    for (int i = 0; i < commandsIdsArray.length; i++)
    {
      commandsIdsList.add(Integer.valueOf(commandsIdsArray[i]));
    }

    return new AgentCommandsImpl(agentId, commandsIdsList);
  }

  /**
   * Creates instance of {@link CommandsRepository} class with specified parameters.
   *
   * @param agentsGroupId - agents group's identifier.
   * @param agentCommands - array with agent commands.
   * @param commands - array with commands.
   *
   * @return Returns instance of {@link CommandsRepository} class.
   */
  public static CommandsRepository createCommandsRepository(int agentsGroupId,
      AgentCommands[] agentCommands, Command[] commands)
  {
    if (agentCommands == null)
    {
      throw new NullPointerException("Specified agentCommands is null!");
    }
    if (commands == null)
    {
      throw new NullPointerException("Specified commands is null!");
    }
    if (agentsGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsGroupId - it must be positive integer or zero!");
    }

    Map<Integer, Command> commandsMap = new HashMap<Integer, Command>();
    for (int i = 0; i < commands.length; i++)
    {
      Command command = commands[i];
      commandsMap.put(Integer.valueOf(command.getCommandId()), command);
    }

    return new CommandsRepositoryImpl(agentsGroupId, commandsMap, Arrays.asList(agentCommands));
  }
}