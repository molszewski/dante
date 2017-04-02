/*
 * Created on 2006-09-08
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.message;

import net.java.dante.receiver.ReceiverMessage;


/**
 * Receiver message sent when an exception was caught by client or server side
 * for some running algorithm.
 *
 * @author M.Olszewski
 */
public class AlgorithmExceptionMessage implements ReceiverMessage
{
  /** Cause of exception. */
  private Throwable cause;


  /**
   * Creates instance of {@link AlgorithmExceptionMessage} class.
   *
   * @param exceptionCause cause of caught exception.
   */
  public AlgorithmExceptionMessage(Throwable exceptionCause)
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