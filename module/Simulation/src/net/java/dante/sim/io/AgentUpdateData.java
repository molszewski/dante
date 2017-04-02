/*
 * Created on 2006-07-14
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.io;

import net.java.dante.sim.data.common.PositionData;
import net.java.dante.sim.data.object.agent.ServerAgentState;

/**
 * Class containing agent's update data. Objects of this class are
 * immutable so they are thread-safe.
 *
 * @author M.Olszewski
 */
public class AgentUpdateData extends PositionData
{
  /** 
   * Index for {@link #isArgumentChanged(int)} method to check whether start
   * location 'x' coordinate has changed.  
   */
  public final int START_X_INDEX = 0;
  /** 
   * Index for {@link #isArgumentChanged(int)} method to check whether start
   * location 'y' coordinate has changed.  
   */
  public final int START_Y_INDEX = START_X_INDEX + 1;
  
  /** Total number of variable arguments. */
  private static final int VARIABLE_ARGUMENTS_NUMBER = 2;
  
  /** Agent's identifier. */
  private int identifier;
  /** Identifier of agent's group. */
  private int groupId;
  
  private boolean[] changed = null;
  
  
  
  /**
   * @param agentGroupId - identifier of agent's group.
   * @param agentIdentifier - agent's identifier.
   * @param state - agent's state.
   * @param changedArguments - specifies which parameters were changed. Must have
   *        length equal to total number of other arguments.
   */
  public AgentUpdateData(int agentGroupId, int agentIdentifier, 
      ServerAgentState state, boolean[] changedArguments)
  {
    super(state.getX(), state.getY());
    
    if (agentGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentGroupId - it must be positive integer or zero!");
    }
    if (agentIdentifier < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentIdentifier - it must be positive integer or zero!");
    }
    if (changedArguments == null)
    {
      throw new NullPointerException("Specified changed is null!");
    }
    if (changedArguments.length != VARIABLE_ARGUMENTS_NUMBER)
    {
      throw new IllegalArgumentException("Invalid argument changedArguments - its length must be equal to " + VARIABLE_ARGUMENTS_NUMBER + "!");
    }
    
    // Copy array
    changed = new boolean[changedArguments.length];
    System.arraycopy(changedArguments, 0, changed, 0, changed.length);
    
    identifier = agentIdentifier;
    groupId = agentGroupId;
  }
  
  
  /**
   * Gets agent's identifier.
   * 
   * @return Returns agent's identifier.
   */
  public int getIdentifier()
  {
    return identifier;
  }

  /**
   * Gets identifier of agent's group.
   * 
   * @return Returns identifier of agent's group.
   */
  public int getGroupId()
  {
    return groupId;
  }
  
  /**
   * Checks whether one of variable arguments was changed.
   * 
   * @param index - index of variable argument to check (e.g. {@link #START_X_INDEX}).
   * 
   * @return Returns <code>true</code> if variable arguments was changed,
   *         <code>false</code> otherwise.
   */
  public boolean isArgumentChanged(int index)
  {
    return changed[index];
  }
}