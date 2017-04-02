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
 * Test class containing various tests for {@link LongIntegerReader} class.
 *
 * @author M.Olszewski
 */
public class LongIntegerReaderTest extends TestCase
{
  /**
   * Test method for {@link net.java.dante.darknet.util.reader.LongIntegerReader#LongIntegerReader()}.
   */
  public final void testLongIntegerReader()
  {
    new LongIntegerReader();
  }

  /**
   * Test method for {@link net.java.dante.darknet.util.reader.LongIntegerReader#readBytes(ByteBuffer)}.
   */
  public final void testReadBytes()
  {
    byte[] bytes = RandomGenerator.randomBytes(PrimitivesSizes.LONG_SIZE);
    BigInteger value = new BigInteger(bytes);
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    
    LongIntegerReader reader = new LongIntegerReader();
    assertTrue(reader.readBytes(buffer));
    assertEquals(value.longValue(), reader.getLong());
    
    buffer = ByteBuffer.wrap(bytes, 0, (PrimitivesSizes.LONG_SIZE/2));
    reader.reset();
    
    assertFalse(reader.readBytes(buffer));
  }

  /**
   * Test method for (@link darknet.util.reader.LongIntegerReader#isReadCompleted()}.
   */
  public final void testIsReadCompleted()
  {
    byte[] bytes = RandomGenerator.randomBytes(PrimitivesSizes.LONG_SIZE);
    BigInteger value = new BigInteger(bytes);
    LongIntegerReader reader = new LongIntegerReader();
    
    for (int i = 0; i < bytes.length; i++)
    {
      assertFalse(reader.isReadCompleted());
      ByteBuffer buffer = ByteBuffer.wrap(bytes, i, 1);
      reader.readBytes(buffer);
    }
    
    assertTrue(reader.isReadCompleted());
    assertEquals(reader.getLong(), value.longValue());
  }

  /**
   * Test method for {@link net.java.dante.darknet.util.reader.LongIntegerReader#reset()}.
   */
  public final void testReset()
  {
    byte[] bytes = RandomGenerator.randomBytes(PrimitivesSizes.LONG_SIZE);
    ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, (PrimitivesSizes.LONG_SIZE/2));
    
    LongIntegerReader reader = new LongIntegerReader();
    assertFalse(reader.readBytes(buffer));
    reader.reset();
    
    buffer = ByteBuffer.wrap(bytes);
    assertTrue(reader.readBytes(buffer));
  }

  /**
   * Test method for {@link net.java.dante.darknet.util.reader.LongIntegerReader#getLong()}.
   */
  public final void testGetLong()
  {
    byte[] bytes = RandomGenerator.randomBytes(PrimitivesSizes.LONG_SIZE);
    BigInteger value = new BigInteger(bytes);
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    
    LongIntegerReader reader = new LongIntegerReader();

    assertTrue(reader.readBytes(buffer));
    assertEquals(reader.getLong(), value.longValue());
  }
  
  /**
   * Test method for {@link net.java.dante.darknet.util.reader.LongIntegerReader#getLong()}
   * throwing {@link ReadNotCompletedException}.
   */
  public final void testGetLongException()
  {
    byte[] bytes = RandomGenerator.randomBytes(PrimitivesSizes.LONG_SIZE);
    ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, (PrimitivesSizes.LONG_SIZE/2));
    
    LongIntegerReader reader = new LongIntegerReader();

    assertFalse(reader.readBytes(buffer));
    try
    {
      reader.getLong();
      fail("ReadNotCompletedException not thrown - test failed!");
    }
    catch (ReadNotCompletedException e)
    {
      // OK - exception thrown
    }
  }
}