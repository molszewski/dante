/*
 * Created on 2006-07-11
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.creator;

import net.java.dante.sim.common.SimulationException;

/**
 * Exception thrown when object cannot be created by class implementing
 * {@link ObjectsCreator} interface.
 *
 * @author M.Olszewski
 */
public class ObjectCreationFailedException extends SimulationException
{
  private static final long serialVersionUID = 1L;

  
  /**
   * Constructs a new {@link ObjectCreationFailedException} with 
   * <code>null</code> as its detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   */
  public ObjectCreationFailedException()
  {
    super();
  }

  /**
   * Constructs a new {@link ObjectCreationFailedException} with the specified detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   * 
   * @param message - the detail message (which is saved for later retrieval 
   *        by the {@link Throwable#getMessage()} method).
   */
  public ObjectCreationFailedException(String message)
  {
    super(message);
  }
}