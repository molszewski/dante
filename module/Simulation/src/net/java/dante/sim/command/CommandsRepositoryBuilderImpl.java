/*
 * Created on 2006-07-31
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.java.dante.sim.command.types.ClearQueueCommand;
import net.java.dante.sim.command.types.Command;
import net.java.dante.sim.command.types.CommandTypesUtils;


/**
 * Commands repository builder for client side of simulation.
 *
 * @author M.Olszewski
 */
final class CommandsRepositoryBuilderImpl implements CommandsRepositoryBuilder
{
  /** Identifier of an agents group for which commands repository will be created. */
  private int id;
  /** Map with all unique commands and their identifiers. */
  private Map<Integer, Command> commands = new HashMap<Integer, Command>();
  /** Inner repository holding all commands. */
  private Map<Integer, List<Integer>> repository = new HashMap<Integer, List<Integer>>();


  /**
   * Creates instance of {@link CommandsRepositoryBuilderImpl} class.
   *
   * @param groupId - identifier of an agents group for which commands
   *        repository will be created.
   */
  CommandsRepositoryBuilderImpl(int groupId)
  {
    if (groupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument groupId - it must be zero or positive integer!");
    }

    id = groupId;
  }


  /**
   * @see net.java.dante.sim.command.CommandsRepositoryBuilder#addCommand(int, net.java.dante.sim.command.types.Command)
   */
  public void addCommand(int agentId, Command command)
  {
    if (command == null)
    {
      throw new NullPointerException("Specified command is null!");
    }
    if (agentId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentId - it must be positive integer or zero!");
    }

    Integer commandIdObj = Integer.valueOf(command.getCommandId());

    // map agent id
    Integer aId = Integer.valueOf(agentId);
    List<Integer> agentCommandsIds = repository.get(aId);
    if (agentCommandsIds == null)
    {
      agentCommandsIds = new ArrayList<Integer>();
      repository.put(aId, agentCommandsIds);
    }

    if (!agentCommandsIds.contains(commandIdObj))
    {
      if (command instanceof ClearQueueCommand)
      {
        agentCommandsIds.clear();
        removeNotReferencedCommands();
      }

      agentCommandsIds.add(commandIdObj);

      if (!commands.containsKey(commandIdObj))
      {
        commands.put(commandIdObj, command);
      }
    }
  }


  /**
   * Removes all commands which are not referenced anymore after clearing
   * commands for one agent.
   */
  private void removeNotReferencedCommands()
  {
    Set<Integer> referencedCommands = new HashSet<Integer>(commands.size());

    for (Integer agentId : repository.keySet())
    {
      for (Integer commandId : repository.get(agentId))
      {
        if (commands.containsKey(commandId))
        {
          referencedCommands.add(commandId);
        }
      }
    }

    commands.keySet().retainAll(referencedCommands);
  }

  /**
   * @see net.java.dante.sim.command.CommandsRepositoryBuilder#build()
   */
  public CommandsRepository build()
  {
    CommandsRepository commandsRepository = null;

    if (commands.size() > 0)
    {
      List<AgentCommands> group = new ArrayList<AgentCommands>();

      for (Integer agentId : repository.keySet())
      {
        AgentCommands agentCommands =
            new AgentCommandsImpl(agentId.intValue(), repository.get(agentId));

        group.add(agentCommands);
      }

      commandsRepository = new CommandsRepositoryImpl(id,
          new HashMap<Integer, Command>(commands), group);

      repository.clear();
      commands.clear();
    }

    return commandsRepository;
  }

  /**
   * Test.
   *
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    CommandsRepositoryBuilderImpl builder = new CommandsRepositoryBuilderImpl(23);

    builder.addCommand(1, CommandTypesUtils.createAttackCommand(1, 1));
    builder.addCommand(1, CommandTypesUtils.createMoveCommand(12, 12));
    builder.addCommand(1, CommandTypesUtils.createAttackCommand(14, 14));
    builder.addCommand(1, CommandTypesUtils.createClearQueueCommand());
    builder.addCommand(1, CommandTypesUtils.createAttackCommand(14, 14));

    CommandsRepository rep = builder.build();
    rep.getCommandsCount();

    builder.addCommand(1, CommandTypesUtils.createAttackCommand(1, 1));
    Command c1 = CommandTypesUtils.createAttackCommand(14, 14);
    builder.addCommand(1, c1);
    builder.addCommand(1, c1);
    builder.addCommand(2, c1);
    builder.addCommand(3, c1);
    builder.addCommand(1, CommandTypesUtils.createClearQueueCommand());
    builder.addCommand(1, CommandTypesUtils.createAttackCommand(14, 14));

    builder.build();

    builder.addCommand(1, CommandTypesUtils.createAttackCommand(1, 1));
    builder.addCommand(2, CommandTypesUtils.createMoveCommand(12, 12));
    builder.addCommand(1, CommandTypesUtils.createAttackCommand(14, 14));
    builder.addCommand(1, CommandTypesUtils.createClearQueueCommand());
    builder.addCommand(2, CommandTypesUtils.createAttackCommand(14, 14));
    builder.addCommand(1, CommandTypesUtils.createClearQueueCommand());
    builder.addCommand(1, CommandTypesUtils.createAttackCommand(14, 14));
    builder.addCommand(2, CommandTypesUtils.createClearQueueCommand());
  }
}