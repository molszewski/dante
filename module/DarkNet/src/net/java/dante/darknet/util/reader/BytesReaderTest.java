/*
 * Created on 2006-05-16
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.util.reader;

import java.nio.ByteBuffer;
import java.util.Arrays;

import net.java.dante.darknet.util.RandomGenerator;

import junit.framework.TestCase;

/**
 * Test class containing various tests for {@link BytesReader} class.
 *
 * @author M.Olszewski
 */
public class BytesReaderTest extends TestCase
{
  /** Minimum possible number of bytes. */
  private static final int MIN_BYTES = 1024;
  /** Maximum possible number of bytes. */
  private static final int MAX_BYTES = 16384;
  
  /**
   * Test method for {@link net.java.dante.darknet.util.reader.BytesReader#BytesReader(int)}.
   */
  public final void testBytesReader()
  {
    try
    {
      final int NEGATIVE_SIZE = -1;
      
      new BytesReader(NEGATIVE_SIZE);
      fail("IllegalArgumentException not thrown - test failed!");
    }
    catch (IllegalArgumentException e)
    {
      // OK - exception thrown
    }
    
    final int BYTES_NUMBER = RandomGenerator.randomInt(MIN_BYTES, MAX_BYTES);
    
    byte[] bytes = RandomGenerator.randomBytes(BYTES_NUMBER);
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    
    BytesReader reader = new BytesReader(BYTES_NUMBER);
    assertTrue(reader.readBytes(buffer));
  }
  
  /**
   * Test method for {@link net.java.dante.darknet.util.reader.BytesReader#readBytes(ByteBuffer)}.
   */
  public final void testReadBytes()
  {
    final int BYTES_NUMBER = RandomGenerator.randomInt(MIN_BYTES, MAX_BYTES);
    
    byte[] bytes = RandomGenerator.randomBytes(BYTES_NUMBER);
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    
    BytesReader reader = new BytesReader(BYTES_NUMBER);

    assertTrue(reader.readBytes(buffer));
    assertTrue(Arrays.equals(reader.getBytes(), bytes));
    
    buffer = ByteBuffer.wrap(bytes, 0, (BYTES_NUMBER/2));
    reader.reset();
    
    assertFalse(reader.readBytes(buffer));
  }

  /**
   * Test method for {@link net.java.dante.darknet.util.reader.BytesReader#isReadCompleted()}.
   */
  public final void testIsReadCompleted()
  {
    final int BYTES_NUMBER = RandomGenerator.randomInt(MIN_BYTES, MAX_BYTES);
    
    byte[] bytes = RandomGenerator.randomBytes(BYTES_NUMBER);
    BytesReader reader = new BytesReader(BYTES_NUMBER);
    for (int i = 0; i < bytes.length; i++)
    {
      assertFalse(reader.isReadCompleted());
      ByteBuffer buffer = ByteBuffer.wrap(bytes, i, 1);
      reader.readBytes(buffer);
    }
    
    assertTrue(reader.isReadCompleted());
    assertTrue(Arrays.equals(reader.getBytes(), bytes));
  }

  /**
   * Test method for {@link net.java.dante.darknet.util.reader.BytesReader#reset()}.
   */
  public final void testReset()
  {
    final int BYTES_NUMBER = RandomGenerator.randomInt(MIN_BYTES, MAX_BYTES);
    
    byte[] bytes = RandomGenerator.randomBytes(BYTES_NUMBER);
    ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, (BYTES_NUMBER/2));
    
    BytesReader reader = new BytesReader(BYTES_NUMBER);
    assertFalse(reader.readBytes(buffer));
    reader.reset();
    
    buffer = ByteBuffer.wrap(bytes, 0, BYTES_NUMBER);
    assertTrue(reader.readBytes(buffer));
  }

  /**
   * Test method for {@link net.java.dante.darknet.util.reader.BytesReader#getBytes()}.
   */
  public final void testGetBytes()
  {
    final int BYTES_NUMBER = RandomGenerator.randomInt(MIN_BYTES, MAX_BYTES);
    
    byte[] bytes = RandomGenerator.randomBytes(BYTES_NUMBER);
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    
    BytesReader reader = new BytesReader(BYTES_NUMBER);

    assertTrue(reader.readBytes(buffer));
    assertTrue(Arrays.equals(reader.getBytes(), bytes));
  }
  
  /**
   * Test method for {@link net.java.dante.darknet.util.reader.BytesReader#getBytes()}
   * throwing {@link ReadNotCompletedException}.
   */
  public final void testGetBytesException() 
  {
    final int BYTES_NUMBER = RandomGenerator.randomInt(MIN_BYTES, MAX_BYTES);
    
    byte[] bytes = RandomGenerator.randomBytes(BYTES_NUMBER);
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    
    BytesReader reader = new BytesReader(BYTES_NUMBER * 2);

    assertFalse(reader.readBytes(buffer));
    //Should throw exception
    try
    {
      reader.getBytes();
      fail("ReadNotCompletedException not thrown - test failed!");
    }
    catch (ReadNotCompletedException e)
    {
      // OK - exception was thrown
    }
  }
}