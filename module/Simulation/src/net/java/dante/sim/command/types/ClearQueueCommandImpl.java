/*
 * Created on 2006-08-08
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.command.types;

/**
 * Implementation of {@link ClearQueueCommand} interface.
 *
 * @author M.Olszewski
 */
final class ClearQueueCommandImpl extends AbstractCommand implements ClearQueueCommand
{
  /**
   * Creates instance of {@link ClearQueueCommandImpl} class.
   *
   * @param commandId - command's identifier.
   */
  ClearQueueCommandImpl(int commandId)
  {
    super(commandId, CommandType.CLEAR_QUEUE);
  }


  /**
   * @see net.java.dante.sim.command.types.AbstractCommand#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    return (super.equals(object) && (object instanceof ClearQueueCommand));
  }
}