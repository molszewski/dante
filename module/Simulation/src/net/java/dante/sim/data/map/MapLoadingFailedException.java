/*
 * Created on 2006-07-08
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.map;

import net.java.dante.sim.common.SimulationException;


/**
 * Exception thrown when any error occurred during loading of map definition
 * from external source.
 *
 * @author M.Olszewski
 */
public class MapLoadingFailedException extends SimulationException
{
  private static final long serialVersionUID = 1L;

  
  /**
   * Constructs a new {@link MapLoadingFailedException} with 
   * <code>null</code> as its detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   */
  public MapLoadingFailedException()
  {
    super();
  }

  /**
   * Constructs a new {@link MapLoadingFailedException} with the specified detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   * 
   * @param message - the detail message (which is saved for later retrieval 
   *        by the {@link Throwable#getMessage()} method).
   */
  public MapLoadingFailedException(String message)
  {
    super(message);
  }
  
  /**
   * Constructs a new {@link MapLoadingFailedException} with the specified detail message 
   * and cause.
   * Note that the detail message associated with cause is not automatically 
   * incorporated in this {@link SimulationException}'s detail message.
   *  
   * @param message - the detail message (which is saved for later retrieval 
   *        by the {@link Throwable#getMessage()} method).
   * @param cause - the cause (which is saved for later retrieval by the 
   *        {@link Throwable#getCause()} method). (A <code>null</code> value 
   *        is permitted and indicates that the cause is nonexistent or unknown.)
   */
  public MapLoadingFailedException(String message, Throwable cause)
  {
    super(message, cause);
  }
}