/*
 * Created on 2006-05-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.config;

import java.io.File;

import net.java.dante.darknet.common.DarkNetException;


/**
 * Exception thrown by {@link net.java.dante.darknet.server.NetworkServerFactory#initServer(File, boolean)} 
 * method when error occurred during reading of configuration file.
 *
 * @author M.Olszewski
 */
public class ConfigFileErrorException extends DarkNetException
{
  private static final long serialVersionUID = 6773770128204128450L;
  

  /**
   * Constructs a new {@link ConfigFileErrorException} with 
   * <code>null</code> as its detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   */
  public ConfigFileErrorException()
  {
    super();
  }

  /**
   * Constructs a new {@link ConfigFileErrorException} with the specified detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   * 
   * @param message - the detail message (which is saved for later retrieval 
   *        by the {@link Throwable#getMessage()} method).
   */
  public ConfigFileErrorException(String message)
  {
    super(message);
  }

  /**
   * Constructs a new {@link ConfigFileErrorException} with the specified cause and a 
   * detail message of <code>(cause==null ? null : cause.toString())</code>  
   * (which typically contains the class and detail message of cause). 
   * This constructor is useful for runtime exceptions that are little more 
   * than wrappers for other throwables.
   * 
   * @param cause - the cause (which is saved for later retrieval by the 
   *        {@link Throwable#getCause()} method). (A <code>null</code> value 
   *        is permitted and indicates that the cause is nonexistent or unknown.)
   */
  public ConfigFileErrorException(Throwable cause)
  {
    super(cause);
  }
}