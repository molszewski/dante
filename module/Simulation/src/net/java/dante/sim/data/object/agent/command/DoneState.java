/*
 * Created on 2006-07-27
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.agent.command;

/**
 * Class representing done state of command.
 *
 * @author M.Olszewski
 */
class DoneState extends CommandState
{
  /**
   * Creates instance of {@link DoneState} class.
   */
  DoneState()
  {
    // Intentionally left empty.
  }
  
  
  /** 
   * @see net.java.dante.sim.data.object.agent.command.CommandState#isDone()
   */
  boolean isDone()
  {
    return true;
  }
}