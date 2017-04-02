/*
 * Created on 2006-04-21
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.common;

import net.java.dante.darknet.messaging.SubclassAlreadyRegisteredException;
import net.java.dante.darknet.protocol.packet.Packet;


/**
 * Exception thrown when memory limit of some container (e.g. object of 
 * {@link Packet} subclass) was reached and no data can be written there.
 *
 * @author M.Olszewski
 */
public class MemoryLimitReachedException extends DarkNetException
{
  private static final long serialVersionUID = 3985778981444015167L;
  

  /**
   * Constructs a new {@link MemoryLimitReachedException} with 
   * <code>null</code> as its detail message. 
   * The cause is not initialized, and may subsequently be initialized 
   * by a call to {@link Throwable#initCause(java.lang.Throwable)}.
   */
  public MemoryLimitReachedException()
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
  public MemoryLimitReachedException(String message)
  {
    super(message);
  }
}
