/*
 * Created on 2006-05-18
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.client;

import net.java.dante.darknet.common.DarkNetException;

/**
 * Exception thrown when client cannot be connected with desired server 
 * for some reason, e.g. it cannot find server's address.
 *
 * @author M.Olszewski
 */
public class ClientNotConnectedException extends DarkNetException
{
  private static final long serialVersionUID = 5341053200536971926L;
  

  /**
   * Constructs a new {@link ClientNotConnectedException} with 
   * <code>null</code> as its detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   */
  public ClientNotConnectedException()
  {
    super();
  }

  /**
   * Constructs a new {@link ClientNotConnectedException} with the specified 
   * detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   * 
   * @param message - the detail message (which is saved for later retrieval 
   *        by the {@link Throwable#getMessage()} method).
   */
  public ClientNotConnectedException(String message)
  {
    super(message);
  }
}