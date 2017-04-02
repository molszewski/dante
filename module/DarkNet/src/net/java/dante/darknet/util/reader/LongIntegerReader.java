/*
 * Created on 2006-05-02
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.util.reader;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import net.java.dante.darknet.common.PrimitivesSizes;


/**
 * Class reading long integer number from sequence of {@link ByteBuffer} 
 * objects.
 *
 * @author M.Olszewski
 */
public class LongIntegerReader implements ByteBufferReader
{
  /** Bytes reader reading eight bytes (size of long integer). */
  private BytesReader reader = new BytesReader(PrimitivesSizes.LONG_SIZE);
  
  /**
   * Constructs object of {@link IntegerReader} reading long integer number
   * from {@link ByteBuffer}.
   */
  public LongIntegerReader()
  {
    // Intentionally left empty.
  }
  
  /** 
   * @see net.java.dante.darknet.util.reader.ByteBufferReader#readBytes(java.nio.ByteBuffer)
   */
  public boolean readBytes(ByteBuffer buffer)
  {
    return reader.readBytes(buffer);
  }
  
  /** 
   * @see net.java.dante.darknet.util.reader.ByteBufferReader#isReadCompleted()
   */
  public boolean isReadCompleted()
  {
    return reader.isReadCompleted();
  }
  
  /** 
   * @see net.java.dante.darknet.util.reader.ByteBufferReader#reset()
   */
  public void reset()
  {
    reader.reset();
  }
  
  /**
   * Gets read long integer, but only if read was completed. Otherwise
   * {@link ReadNotCompletedException} is thrown.
   * 
   * @return Returns read long integer.
   * @throws ReadNotCompletedException if reading was not completed.
   */
  public long getLong()
  {
    long readLong = 0;
    if (reader.isReadCompleted())
    {
      readLong = (new BigInteger(reader.getBytes())).longValue();
    }
    else
    {
      throw new ReadNotCompletedException();
    }
    return readLong;
  }
}