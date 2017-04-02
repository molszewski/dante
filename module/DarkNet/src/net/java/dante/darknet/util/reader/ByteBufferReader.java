/*
 * Created on 2006-05-02
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.util.reader;

import java.nio.ByteBuffer;

/**
 * Common interface for all classes reading their data from {@link ByteBuffer}
 * objects.
 * Class reading from the {@link ByteBuffer} objects should read required 
 * number of bytes from buffer passed to {@link #readBytes(ByteBuffer)} method
 * or - if specified buffer doesn't contain required number of bytes - 
 * whole content of buffer. The remaining bytes could be read next time,
 * with new {@link ByteBuffer}. 
 * <p>
 * If reader have finished reading, further calls to 
 * {@link #readBytes(ByteBuffer)} should return <code>true</code> and
 * have no other effect, until {@link #reset()} is called.
 * <p>
 * It is convenient to pass required number of bytes to constructor (and
 * nowhere else), so it could not be changed externally.
 *
 * @author M.Olszewski
 */
public interface ByteBufferReader
{
  /**
   * If reading is in progress, this method reads required number of bytes from
   * specified {@link ByteBuffer} - if buffer is big enough. If buffer is 
   * smaller, then whole content of buffer is read.
   * <p>
   * If reading is finished, this method should return <code>true</code>
   * and do not perform any reading.
   * 
   * @param buffer - buffer containing bytes to read.
   * 
   * @return Returns <code>true</code> if reading is done, <code>false</code>
   *         otherwise.
   */
  public boolean readBytes(ByteBuffer buffer);
  
  /**
   * Checks whether this reader finished reading.
   * 
   * @return Returns <code>true</code> if reader finished reading, 
   *         <code>false</code> otherwise.
   */
  public boolean isReadCompleted();
  
  /**
   * Resets reader, so it is ready to read required number of bytes again.
   */
  public void reset();
}