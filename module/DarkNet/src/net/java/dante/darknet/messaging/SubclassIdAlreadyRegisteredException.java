/*
 * Created on 2006-04-19
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.messaging;

import net.java.dante.darknet.common.DarkNetException;

/**
 * Exception thrown by {@link NetworkMessageRegisterImpl#register(Class, int)} method 
 * when specified subclass identifier representing {@link NetworkMessage} subclass
 * is already registered in the {@link NetworkMessageRegisterImpl}.
 *
 * @author M.Olszewski
 */
public class SubclassIdAlreadyRegisteredException extends DarkNetException
{
  private static final long serialVersionUID = 3632158959209710798L;
  

  /**
   * Constructs a new {@link SubclassIdAlreadyRegisteredException} with 
   * <code>null</code> as its detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   */
  public SubclassIdAlreadyRegisteredException()
  {
    super();
  }

  /**
   * Constructs a new {@link SubclassIdAlreadyRegisteredException} with 
   * the specified detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   * 
   * @param message - the detail message (which is saved for later retrieval 
   *        by the {@link Throwable#getMessage()} method).
   */
  public SubclassIdAlreadyRegisteredException(String message)
  {
    super(message);
  }
}