/*
 * Created on 2006-07-31
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.command;

import java.util.List;

/**
 * Implementation of {@link AgentCommands} interface.
 *
 * @author M.Olszewski
 */
final class AgentCommandsImpl implements AgentCommands
{
  /** Agent's identifier. */
  private int agentId;
  /** List with commands identifiers. */
  private List<Integer> commands;


  /**
   * Creates instance of {@link AgentCommandsImpl} class.
   *
   * @param agentIdentifier - agent's identifier.
   * @param agentCommandsIds - list with commands identifiers regarding this agent.
   */
  public AgentCommandsImpl(int agentIdentifier, List<Integer> agentCommandsIds)
  {
    if (agentIdentifier < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentId - it must be positive integer or zero!");
    }
    if (agentCommandsIds == null)
    {
      throw new NullPointerException("Specified agentCommandsIds is null!");
    }
    if (agentCommandsIds.size() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument agentCommandsIds - it must contain at least 1 element!");
    }

    agentId = agentIdentifier;
    commands = agentCommandsIds;
  }


  /**
   * @see net.java.dante.sim.command.AgentCommands#getAgentId()
   */
  public int getAgentId()
  {
    return agentId;
  }

  /**
   * @see net.java.dante.sim.command.AgentCommands#getCommandsCount()
   */
  public int getCommandsCount()
  {
    return commands.size();
  }

  /**
   * @see net.java.dante.sim.command.AgentCommands#getCommandId(int)
   */
  public int getCommandId(int index)
  {
    return commands.get(index).intValue();
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
    result = PRIME * result + agentId;

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof AgentCommandsImpl))
    {
      final AgentCommandsImpl other = (AgentCommandsImpl) object;
      equal = ((agentId == other.agentId) &&
               (commands.equals(other.commands)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[agentId=" + agentId + "; commands=" + commands + "]");
  }
}