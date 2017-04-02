/*
 * Created on 2006-09-08
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.receiver;

import net.java.dante.receiver.ReceiverMessage;

/**
 * Base class for all messages that notifies about exception in some module.
 *
 * @author M.Olszewski
 */
public abstract class ExceptionMessage implements ReceiverMessage
{
  /** Cause of exception. */
  private Throwable cause;


  /**
   * Creates instance of {@link ExceptionMessage} class.
   *
   * @param exceptionCause cause of caught exception.
   */
  public ExceptionMessage(Throwable exceptionCause)
  {
    if (exceptionCause == null)
    {
      throw new NullPointerException("Specified exceptionCause is null!");
    }

    cause = exceptionCause;
  }


  /**
   * Gets cause of caught exception.
   *
   * @return Returns cause of caught exception.
   */
  public Throwable getCause()
  {
    return cause;
  }
}