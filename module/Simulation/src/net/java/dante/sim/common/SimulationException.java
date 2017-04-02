/*
 * Created on 2006-07-04
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.common;

/**
 * Base class for all classes representing new types of runtime 
 * exceptions in Simulation module. 
 *
 * @author M.Olszewski
 */
public class SimulationException extends RuntimeException
{
  private static final long serialVersionUID = 1L;
  

  /**
   * Constructs a new {@link SimulationException} with 
   * <code>null</code> as its detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   */
  public SimulationException()
  {
    super();
  }

  /**
   * Constructs a new {@link SimulationException} with the specified detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   * 
   * @param message - the detail message (which is saved for later retrieval 
   *        by the {@link Throwable#getMessage()} method).
   */
  public SimulationException(String message)
  {
    super(message);
  }

  /**
   * Constructs a new {@link SimulationException} with the specified detail message 
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
  public SimulationException(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Constructs a new {@link SimulationException} with the specified cause and a 
   * detail message of <code>(cause==null ? null : cause.toString())</code>  
   * (which typically contains the class and detail message of cause). 
   * This constructor is useful for runtime exceptions that are little more 
   * than wrappers for other throwables.
   * 
   * @param cause - the cause (which is saved for later retrieval by the 
   *        {@link Throwable#getCause()} method). (A <code>null</code> value 
   *        is permitted and indicates that the cause is nonexistent or unknown.)
   */
  public SimulationException(Throwable cause)
  {
    super(cause);
  }
}