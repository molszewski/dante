/*
 * Created on 2006-06-10
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.common;


/**
 * Exception thrown when invalid transition was requested in any state machine.
 *
 * @author M.Olszewski
 */
public class InvalidTransitionException extends SimulationException
{
  private static final long serialVersionUID = 1L;

  
  /**
   * Constructs a new {@link InvalidTransitionException} with 
   * <code>null</code> as its detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   */
  public InvalidTransitionException()
  {
    super();
  }

  /**
   * Constructs a new {@link InvalidTransitionException} with the specified detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   * 
   * @param message - the detail message (which is saved for later retrieval 
   *        by the {@link Throwable#getMessage()} method).
   */
  public InvalidTransitionException(String message)
  {
    super(message);
  }
}