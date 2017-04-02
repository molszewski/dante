/*
 * Created on 2006-08-28
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.sim;

import java.util.HashSet;
import java.util.Set;

import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;
import net.java.dante.sim.command.AgentCommands;
import net.java.dante.sim.command.CommandUtils;
import net.java.dante.sim.command.CommandsRepository;
import net.java.dante.sim.command.types.AttackCommand;
import net.java.dante.sim.command.types.Command;
import net.java.dante.sim.command.types.CommandType;
import net.java.dante.sim.command.types.CommandTypesUtils;
import net.java.dante.sim.command.types.MoveCommand;
import net.java.dante.sim.io.CommandsData;

/**
 * Class wrapping {@link CommandsData} object to transport it through
 * the network.
 *
 * @author M.Olszewski
 */
public class CommandsNetworkMessage extends SimulationNetworkMessage
{
  /** Commands data for server. */
  CommandsData commands;

  /**
   * Creates instance of {@link CommandsNetworkMessage} class.
   * Do not create instances of {@link CommandsNetworkMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public CommandsNetworkMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link CommandsNetworkMessage} class.
   *
   * @param gameIdenifier
   * @param commandsData commands data for client.
   */
  public CommandsNetworkMessage(int gameIdenifier,
                                CommandsData commandsData)
  {
    super(gameIdenifier);

    if (commandsData == null)
    {
      throw new NullPointerException("Specified commandsData is null!");
    }

    commands = commandsData;
  }


  /**
   * Gets {@link CommandsData} object wrapped by this object.
   *
   * @return {@link CommandsData} object wrapped by this object.
   */
  public CommandsData getCommandsData()
  {
    return commands;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    super.fromPacket(reader);

    commands = new CommandsData(loadCommandsData(reader));
  }

  private CommandsRepository loadCommandsData(PacketReader reader)
  {
    // Read group identifier
    int groupId = reader.readInt();

    // Read sets with commands for agents
    AgentCommands[] agentsCommands = loadAgentCommands(reader);

    // Read commands
    Command[] commandsArray = loadCommands(reader);

    return CommandUtils.createCommandsRepository(groupId, agentsCommands, commandsArray);
  }

  private AgentCommands[] loadAgentCommands(PacketReader reader)
  {
    // Read number of sets with commands for agents from the group
    int agentsCommandsCount = reader.readInt();

    // Read sets with commands for agents
    AgentCommands[] agentsCommands = new AgentCommands[agentsCommandsCount];
    for (int i = 0; i < agentsCommandsCount; i++)
    {
      // Read agent identifier
      int agentId = reader.readInt();

      // Read number of commands for agent
      int commandsCount = reader.readInt();

      // Read commands identifiers for agent
      int[] commandsIds = new int[commandsCount];
      for (int j = 0; j < commandsCount; j++)
      {
        commandsIds[j] = reader.readInt();
      }

      agentsCommands[i] = CommandUtils.createAgentCommands(agentId, commandsIds);
    }

    return agentsCommands;
  }

  private Command[] loadCommands(PacketReader reader)
  {
    // Read commands count
    int commandsCount = reader.readInt();

    // Read commands
    Command[] commandsArray = new Command[commandsCount];
    for (int i = 0; i < commandsCount; i++)
    {
      commandsArray[i] = loadCommand(reader);
    }

    return commandsArray;
  }

  private Command loadCommand(PacketReader reader)
  {
    Command command = null;

    int commandId = reader.readInt();
    CommandType commandType = CommandType.values()[reader.readInt()];

    switch (commandType)
    {
      case CLEAR_QUEUE:
      {
        command = CommandTypesUtils.createClearQueueCommand(commandId);
        break;
      }
      case RESUME_LAST_COMMAND:
      {
        command = CommandTypesUtils.createResumeLastCommand(commandId);
        break;
      }
      case ATTACK:
      {
        command = CommandTypesUtils.createAttackCommand(commandId,
                                                        Double.longBitsToDouble(reader.readLong()),
                                                        Double.longBitsToDouble(reader.readLong()));
        break;
      }
      case MOVE:
      {
        command = CommandTypesUtils.createMoveCommand(commandId,
                                                      Double.longBitsToDouble(reader.readLong()),
                                                      Double.longBitsToDouble(reader.readLong()));
        break;
      }
    }

    return command;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    super.toPacket(writer);

    if (commands == null)
    {
      throw new IllegalStateException("IllegalState in CommandsNetworkMessage: object is read only!");
    }

    storeCommandsData(writer);
  }

  private void storeCommandsData(PacketWriter writer)
  {
    CommandsRepository repository = commands.getRepository();

    // Write group identifier
    writer.writeInt(repository.getGroupId());

    // Write sets with commands for agents
    Set<Integer> commandsIds = storeAgentCommands(writer, repository);

    // Write commands count
    storeCommands(writer, repository, commandsIds);
  }

  private Set<Integer> storeAgentCommands(PacketWriter writer,
                                  CommandsRepository repository)
  {
    Set<Integer> commandsIds = new HashSet<Integer>(repository.getCommandsCount());

    // Write number of sets with commands for agents from the group
    int agentsCommandsCount = repository.getAgentCommandsCount();
    writer.writeInt(agentsCommandsCount);

    // Write sets with commands for agents
    for (int i = 0; i < agentsCommandsCount; i++)
    {
      AgentCommands agentCommands = repository.getAgentCommands(i);

      // Write agent identifier
      writer.writeInt(agentCommands.getAgentId());

      // Write number of commands for agent
      int commandsCount = agentCommands.getCommandsCount();
      writer.writeInt(commandsCount);

      // Write commands identifiers for agent
      for (int j = 0; j < commandsCount; j++)
      {
        int commandId = agentCommands.getCommandId(j);
        writer.writeInt(commandId);
        commandsIds.add(Integer.valueOf(commandId));
      }
    }

    assert commandsIds.size() == repository.getCommandsCount() : "Count of commands identifier must be the same as number of commands in repository!";

    return commandsIds;
  }

  private void storeCommands(PacketWriter writer,
                             CommandsRepository repository,
                             Set<Integer> commandsIds)
  {
    // Write commands count
    int commandsCount = repository.getCommandsCount();
    writer.writeInt(commandsCount);

    // Write commands
    for (Integer commandId : commandsIds)
    {
      Command command = repository.getCommand(commandId.intValue());

      // Write data generic for each command
      writer.writeInt(command.getCommandId());
      writer.writeInt(command.getCommandType().ordinal());

      // Write data specific for attack command
      if (command instanceof AttackCommand)
      {
        AttackCommand attackCmd = (AttackCommand) command;
        writer.writeLong(Double.doubleToLongBits(attackCmd.getTargetX()));
        writer.writeLong(Double.doubleToLongBits(attackCmd.getTargetY()));
      }
      // Write data specific for move command
      else if (command instanceof MoveCommand)
      {
        MoveCommand attackCmd = (MoveCommand) command;
        writer.writeLong(Double.doubleToLongBits(attackCmd.getDestinationX()));
        writer.writeLong(Double.doubleToLongBits(attackCmd.getDestinationY()));
      }
    }
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;

    int result = super.hashCode();
    result = PRIME * result + ((commands == null) ? 0 : commands.hashCode());

    return result;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof CommandsNetworkMessage))
    {
      final CommandsNetworkMessage other = (CommandsNetworkMessage) object;
      equal = ((commands == null) ? (other.commands == null) : commands.equals(other.commands));
    }
    return equal;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#toString()
   */
  @Override
  public String toString()
  {
    final String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) +
        "; commandsData=" + commands + "]");
  }
}