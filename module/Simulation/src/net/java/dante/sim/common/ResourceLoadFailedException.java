/*
 * Created on 2006-07-19
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.common;

/**
 * Exception thrown when loading of some kind of resource has failed.
 *
 * @author M.Olszewski
 */
public class ResourceLoadFailedException extends SimulationException
{
  private static final long serialVersionUID = 1L;

  
  /**
   * Constructs a new {@link ResourceLoadFailedException} with 
   * <code>null</code> as its detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   */
  public ResourceLoadFailedException()
  {
    super();
  }
  
  /**
   * Constructs a new {@link ResourceLoadFailedException} with the specified detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   * 
   * @param message - the detail message (which is saved for later retrieval 
   *        by the {@link Throwable#getMessage()} method).
   */
  public ResourceLoadFailedException(String message)
  {
    super(message);
  }

  /**
   * Constructs a new {@link ResourceLoadFailedException} with the specified detail message 
   * and cause.
   * Note that the detail message associated with cause is not automatically 
   * incorporated in this {@link ResourceLoadFailedException}'s detail message.
   *  
   * @param message - the detail message (which is saved for later retrieval 
   *        by the {@link Throwable#getMessage()} method).
   * @param cause - the cause (which is saved for later retrieval by the 
   *        {@link Throwable#getCause()} method). (A <code>null</code> value 
   *        is permitted and indicates that the cause is nonexistent or unknown.)
   */
  public ResourceLoadFailedException(String message, Throwable cause)
  {
    super(message, cause);
  }
}