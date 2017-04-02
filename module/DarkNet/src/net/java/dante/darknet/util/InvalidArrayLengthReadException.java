/*
 * Created on 2006-04-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.util;

import net.java.dante.darknet.common.DarkNetException;

/**
 * Exception thrown by methods reading array of values if array's lengthh
 * is lesser or equal to zero.
 *
 * @author M.Olszewski
 */
public class InvalidArrayLengthReadException extends DarkNetException
{
  private static final long serialVersionUID = 5335723621742097951L;
  

  /**
   * Constructs a new {@link InvalidArrayLengthReadException} with 
   * <code>null</code> as its detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   */
  public InvalidArrayLengthReadException()
  {
    super();
  }

  /**
   * Constructs a new {@link InvalidArrayLengthReadException} with the specified detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   * 
   * @param message - the detail message (which is saved for later retrieval 
   *        by the {@link Throwable#getMessage()} method).
   */
  public InvalidArrayLengthReadException(String message)
  {
    super(message);
  }
}