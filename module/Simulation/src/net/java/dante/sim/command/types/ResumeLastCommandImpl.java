/*
 * Created on 2006-08-08
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.command.types;

/**
 * Implementation of {@link ResumeLastCommand} interface.
 *
 * @author M.Olszewski
 */
public class ResumeLastCommandImpl extends AbstractCommand implements ResumeLastCommand
{
  /**
   * Creates instance of {@link ResumeLastCommandImpl} class.
   *
   * @param commandId - command's identifier.
   */
  ResumeLastCommandImpl(int commandId)
  {
    super(commandId, CommandType.RESUME_LAST_COMMAND);
  }

  /**
   * @see net.java.dante.sim.command.types.AbstractCommand#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    return (super.equals(object) && (object instanceof ResumeLastCommand));
  }
}