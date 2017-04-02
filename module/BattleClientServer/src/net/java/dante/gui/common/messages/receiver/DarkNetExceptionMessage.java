/*
 * Created on 2006-08-30
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.receiver;

import net.java.dante.darknet.session.Session;

/**
 * Receiver message sent when an exception was caught by client or server side
 * for some DarkNet session.
 *
 * @author M.Olszewski
 */
public class DarkNetExceptionMessage extends ExceptionMessage
{
  /** Session in which exception was caught. */
  private Session source;


  /**
   * Creates instance of {@link DarkNetExceptionMessage} class.
   *
   * @param exceptionSource - session in which exception was caught.
   * @param exceptionCause - cause of caught exception.
   */
  public DarkNetExceptionMessage(Session exceptionSource, Throwable exceptionCause)
  {
    super(exceptionCause);

    if (exceptionSource == null)
    {
      throw new NullPointerException("Specified exceptionSource is null!");
    }

    source = exceptionSource;
  }


  /**
   * Gets source of caught exception - {@link Session} object in which
   * exception was caught.
   *
   * @return Returns source of caught exception - {@link Session} object
   *         in which exception was caught.
   */
  public Session getSource()
  {
    return source;
  }
}