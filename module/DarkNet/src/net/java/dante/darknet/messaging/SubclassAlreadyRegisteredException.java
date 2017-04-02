/*
 * Created on 2006-04-20
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.messaging;

import net.java.dante.darknet.common.DarkNetException;

/**
 * Exception thrown by {@link NetworkMessageRegisterImpl#register(Class, int)} method 
 * when specified subclass of {@link NetworkMessage} class
 * is already registered in the {@link NetworkMessageRegisterImpl}.
 *
 * @author M.Olszewski
 */
public class SubclassAlreadyRegisteredException extends DarkNetException
{
  private static final long serialVersionUID = 7069343014334122565L;
  

  /**
   * Constructs a new {@link SubclassAlreadyRegisteredException} with 
   * <code>null</code> as its detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   */
  public SubclassAlreadyRegisteredException()
  {
    super();
  }

  /**
   * Constructs a new {@link SubclassAlreadyRegisteredException} with 
   * the specified detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   * 
   * @param message - the detail message (which is saved for later retrieval 
   *        by the {@link Throwable#getMessage()} method).
   */
  public SubclassAlreadyRegisteredException(String message)
  {
    super(message);
  }
}