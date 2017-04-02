/*
 * Created on 2006-04-26
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.util.reader;

import java.nio.ByteBuffer;

import net.java.dante.darknet.common.DarkNetException;


/**
 * Exception thrown by classes implementing 
 * {@link net.java.dante.darknet.util.reader.ByteBufferReader} interface after an attempt
 * to read data from them before reading was completed (not all required bytes
 * from {@link ByteBuffer} were read).
 *
 * @author M.Olszewski
 */
public class ReadNotCompletedException extends DarkNetException
{
  private static final long serialVersionUID = -15346221007680114L;
  

  /**
   * Constructs a new {@link ReadNotCompletedException} with 
   * <code>null</code> as its detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   */
  public ReadNotCompletedException()
  {
    super();
  }

  /**
   * Constructs a new {@link ReadNotCompletedException} with the specified detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   * 
   * @param message - the detail message (which is saved for later retrieval 
   *        by the {@link Throwable#getMessage()} method).
   */
  public ReadNotCompletedException(String message)
  {
    super(message);
  }
}