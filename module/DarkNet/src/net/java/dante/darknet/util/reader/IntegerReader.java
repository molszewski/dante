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
 * Class reading integer number from sequence of {@link ByteBuffer} objects.
 *
 * @author M.Olszewski
 */
public class IntegerReader implements ByteBufferReader
{
  /** Bytes reader reading four bytes (size of integer). */
  private BytesReader reader = new BytesReader(PrimitivesSizes.INT_SIZE);
  
  /**
   * Constructs object of {@link IntegerReader} reading integer number
   * from {@link ByteBuffer}.
   */
  public IntegerReader()
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
   * Gets read integer, but only if read was completed. Otherwise
   * {@link ReadNotCompletedException} is thrown.
   * 
   * @return Returns read integer.
   * @throws ReadNotCompletedException if reading was not completed.
   */
  public int getInt()
  {
    int readInt = 0;
    if (reader.isReadCompleted())
    {
      readInt = (new BigInteger(reader.getBytes())).intValue();
    }
    else
    {
      throw new ReadNotCompletedException();
    }
    return readInt;
  }
}