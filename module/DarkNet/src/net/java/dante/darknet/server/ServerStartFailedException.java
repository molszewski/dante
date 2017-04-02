/*
 * Created on 2006-05-12
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.server;

import net.java.dante.darknet.common.DarkNetException;

/**
 * Exception thrown when server cannot be started for some reason, 
 * e.g. it cannot be bound to any of ports specified in configuration. 
 *
 * @author M.Olszewski
 */
public class ServerStartFailedException extends DarkNetException
{
  private static final long serialVersionUID = -8739947858301581984L;
  

  /**
   * Constructs a new {@link ServerStartFailedException} with 
   * <code>null</code> as its detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   */
  public ServerStartFailedException()
  {
    super();
  }

  /**
   * Constructs a new {@link ServerStartFailedException} with the specified 
   * detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   * 
   * @param message - the detail message (which is saved for later retrieval 
   *        by the {@link Throwable#getMessage()} method).
   */
  public ServerStartFailedException(String message)
  {
    super(message);
  }
}