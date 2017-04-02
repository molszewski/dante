/*
 * Created on 2006-08-08
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.command.types;

/**
 * Base class for commands subclasses.
 *
 * @author M.Olszewski
 */
abstract class AbstractCommand implements Command
{
  /** Command's identifier. */
  private int commandId;
  /** Command's type. */
  private CommandType type;


  /**
   * Constructor of {@link AbstractCommand} class.
   *
   * @param commandIdentifier - command's identifier.
   * @param commandType - command's type.
   */
  AbstractCommand(int commandIdentifier, CommandType commandType)
  {
    if (commandType == null)
    {
      throw new NullPointerException("Specified commandType is null!");
    }
    if (commandIdentifier < 0)
    {
      throw new IllegalArgumentException("Invalid argument commandId - it must be zero or positive integer!");
    }

    commandId = commandIdentifier;
    type      = commandType;
  }


  /**
   * @see net.java.dante.sim.command.types.Command#getCommandId()
   */
  public int getCommandId()
  {
    return commandId;
  }

  /**
   * @see net.java.dante.sim.command.types.Command#getCommandType()
   */
  public CommandType getCommandType()
  {
    return type;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + commandId;
    result = PRIME * result + type.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof Command))
    {
      final Command other = (Command) object;
      equal = ((commandId == other.getCommandId()) &&
               (type == other.getCommandType()));
    }
    return equal;
  }


  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[commandId=" + commandId +
        "; commandType=" + type + "]");
  }
}