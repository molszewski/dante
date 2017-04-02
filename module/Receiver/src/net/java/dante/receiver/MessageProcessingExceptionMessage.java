/*
 * Created on 2006-09-09
 *
 * @author M.Olszewski
 */

package net.java.dante.receiver;


/**
 * Special class that contains cause of exception that was caught while
 * processing some message by {@link MessagesProcessor}. It is always
 * sent to {@link MessagesProcessor} that caused exception and should be
 * processed by it.
 *
 * @author M.Olszewski
 */
public final class MessageProcessingExceptionMessage implements ReceiverMessage
{
  /** Cause of exception. */
  private Throwable cause;


  /**
   * Creates instance of {@link MessageProcessingExceptionMessage} class.
   *
   * @param exceptionCause cause of caught exception.
   */
  public MessageProcessingExceptionMessage(Throwable exceptionCause)
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