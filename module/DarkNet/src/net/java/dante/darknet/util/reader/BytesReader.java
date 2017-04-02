/*
 * Created on 2006-04-27
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.util.reader;

import java.nio.ByteBuffer;


/**
 * Class reading specified number of bytes from sequence of 
 * {@link ByteBuffer} objects.
 *
 * @author M.Olszewski
 */
public class BytesReader implements ByteBufferReader
{
  /** Remaining number of bytes to read. */
  private int remainingBytes = 0;
  /** Array with read bytes. */
  private byte[] bytes = null;
  
  /**
   * Constructs object of {@link BytesReader} class with specified number
   * of bytes to read and a byte array holding all read bytes.
   *  
   * @param bytesNumber - number of bytes to read from {@link ByteBuffer}.
   */
  public BytesReader(int bytesNumber)
  {
    if (bytesNumber > 0)
    {
      remainingBytes = bytesNumber;
      bytes = new byte[remainingBytes];
    }
    else
    {
      throw new IllegalArgumentException("Specified bytesNumber is a negative integer or zero!");
    }
  }
  
  /** 
   * @see net.java.dante.darknet.util.reader.ByteBufferReader#readBytes(java.nio.ByteBuffer)
   * 
   * @throws NullPointerException if specified <code>buffer</code> is <code>null</code>.
   */
  public boolean readBytes(ByteBuffer buffer)
  {
    if (buffer != null)
    {
      if (remainingBytes > 0)
      {
        int curIndex = bytes.length - remainingBytes;
        if (buffer.remaining() >= remainingBytes)
        {
          //read number of required bytes
          buffer.get(bytes, curIndex, remainingBytes);
          remainingBytes = 0;
        }
        else
        {
          int remain = buffer.remaining();
          buffer.get(bytes, curIndex, remain);
          remainingBytes -= remain;
        }
      }
    }
    else
    {
      throw new NullPointerException("Specified buffer is null!");
    }
    
    return (remainingBytes == 0);
  }
  
  /** 
   * @see net.java.dante.darknet.util.reader.ByteBufferReader#isReadCompleted()
   */
  public boolean isReadCompleted()
  {
    return (remainingBytes == 0);
  }
  
  /** 
   * @see net.java.dante.darknet.util.reader.ByteBufferReader#reset()
   */
  public void reset()
  {
    remainingBytes = bytes.length;
  }
  
  /**
   * Gets all read bytes, but only if read was completed. Otherwise
   * {@link ReadNotCompletedException} is thrown.
   * 
   * @return Returns all read bytes.
   * @throws ReadNotCompletedException if reading was not completed.
   */
  public byte[] getBytes()
  {
    if (!isReadCompleted())
    {
      throw new ReadNotCompletedException();
    }
    return bytes;
  }
}