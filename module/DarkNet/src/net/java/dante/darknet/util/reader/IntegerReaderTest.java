/*
 * Created on 2006-05-18
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.util.reader;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import net.java.dante.darknet.common.PrimitivesSizes;
import net.java.dante.darknet.util.RandomGenerator;

import junit.framework.TestCase;

/**
 * Test class containing various tests for {@link IntegerReader} class.
 *
 * @author M.Olszewski
 */
public class IntegerReaderTest extends TestCase
{
  /**
   * Test method for {@link net.java.dante.darknet.util.reader.IntegerReader#IntegerReader()}.
   */
  public final void testIntegerReader()
  {
    new IntegerReader();
  }

  /**
   * Test method for {@link net.java.dante.darknet.util.reader.IntegerReader#readBytes(ByteBuffer)}.
   */
  public final void testReadBytes()
  {
    byte[] bytes = RandomGenerator.randomBytes(PrimitivesSizes.INT_SIZE);
    BigInteger value = new BigInteger(bytes);
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    
    IntegerReader reader = new IntegerReader();
    assertTrue(reader.readBytes(buffer));
    assertEquals(value.intValue(), reader.getInt());
    
    buffer = ByteBuffer.wrap(bytes, 0, (PrimitivesSizes.INT_SIZE/2));
    reader.reset();
    
    assertFalse(reader.readBytes(buffer));
  }

  /**
   * Test method for (@link darknet.util.reader.IntegerReader#isReadCompleted()}.
   */
  public final void testIsReadCompleted()
  {
    byte[] bytes = RandomGenerator.randomBytes(PrimitivesSizes.INT_SIZE);
    BigInteger value = new BigInteger(bytes);
    IntegerReader reader = new IntegerReader();
    
    for (int i = 0; i < bytes.length; i++)
    {
      assertFalse(reader.isReadCompleted());
      ByteBuffer buffer = ByteBuffer.wrap(bytes, i, 1);
      reader.readBytes(buffer);
    }
    
    assertTrue(reader.isReadCompleted());
    assertEquals(reader.getInt(), value.intValue());
  }

  /**
   * Test method for {@link net.java.dante.darknet.util.reader.IntegerReader#reset()}.
   */
  public final void testReset()
  {
    byte[] bytes = RandomGenerator.randomBytes(PrimitivesSizes.INT_SIZE);
    ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, (PrimitivesSizes.INT_SIZE/2));
    
    IntegerReader reader = new IntegerReader();
    assertFalse(reader.readBytes(buffer));
    reader.reset();
    
    buffer = ByteBuffer.wrap(bytes);
    assertTrue(reader.readBytes(buffer));
  }

  /**
   * Test method for {@link net.java.dante.darknet.util.reader.IntegerReader#getInt()}.
   */
  public final void testGetInt()
  {
    byte[] bytes = RandomGenerator.randomBytes(PrimitivesSizes.INT_SIZE);
    BigInteger value = new BigInteger(bytes);
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    
    IntegerReader reader = new IntegerReader();

    assertTrue(reader.readBytes(buffer));
    assertEquals(reader.getInt(), value.intValue());
  }
  
  /**
   * Test method for {@link net.java.dante.darknet.util.reader.IntegerReader#getInt()}
   * throwing {@link ReadNotCompletedException}.
   */
  public final void testGetIntException()
  {
    byte[] bytes = RandomGenerator.randomBytes(PrimitivesSizes.INT_SIZE);
    ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, (PrimitivesSizes.INT_SIZE/2));
    
    IntegerReader reader = new IntegerReader();

    assertFalse(reader.readBytes(buffer));
    try
    {
      reader.getInt();
      fail("ReadNotCompletedException not thrown - test failed!");
    }
    catch (ReadNotCompletedException e)
    {
      // OK - exception thrown
    }
  }
}