/*
 * Created on 2006-07-27
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.agent.command;

/**
 * Class representing stopped state of command.
 *
 * @author M.Olszewski
 */
class StoppedState extends CommandState
{
  /**
   * Creates instance of {@link StoppedState} class.
   */
  StoppedState()
  {
    // Intentionally left empty.
  }

  /**
   * @see net.java.dante.sim.data.object.agent.command.CommandState#isStopped()
   */
  @Override
  boolean isStopped()
  {
    return true;
  }
}