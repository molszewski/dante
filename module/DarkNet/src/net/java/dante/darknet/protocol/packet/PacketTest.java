/*
 * Created on 2006-04-29
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.protocol.packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.java.dante.darknet.util.RandomGenerator;

import junit.framework.TestCase;

/**
 * Test class containing tests for {@link Packet} class.
 *
 * @author M.Olszewski
 */
public class PacketTest extends TestCase
{
  /**
   * Simple test class.
   *
   * @author M.Olszewski
   */
  private static class TestPacket extends Packet
  {
    /**
     * Default empty constructor, calling super constructor with zero argument.
     */
    TestPacket()
    {
      super(0);
    }

    /**
     * @see net.java.dante.darknet.protocol.packet.Packet#rewind()
     */
    @Override
    public void rewind()
    {
      buffer.rewind();
    }

    /**
     * @see net.java.dante.darknet.protocol.packet.Packet#updatePacketData()
     */
    @Override
    protected void updatePacketData()
    {
      // Not used.
    }
  }

  /** Random numbers generator used in this test. */
  static Random rand = new Random();

  /**
   * Writes one byte and read it afterwards.
   */
  public void testReadWriteByte()
  {
    Packet p = new TestPacket();

    byte written = (byte)rand.nextInt();
    p.writeByte(written);
    p.rewind();
    byte read = p.readByte();

    assertEquals(written, read);
  }

  /**
   * Writes array of bytes and read it afterwards.
   */
  public void testReadWriteBytes()
  {
    Packet p = new TestPacket();

    byte[] written = RandomGenerator.randomBytes(Packet.DEFAULT_BUFFER_SIZE*2);
    p.writeByteArray(written);
    p.rewind();
    byte[] read = p.readByteArray();

    assertTrue(Arrays.equals(written, read));
  }

  /**
   * Writes one character and read it afterwards.
   */
  public void testReadWriteChar()
  {
    Packet p = new TestPacket();

    char written = (char)rand.nextInt();
    p.writeChar(written);
    p.rewind();
    char read = p.readChar();

    assertEquals(written, read);
  }

  /**
   * Writes array of characters and read it afterwards.
   */
  public void testReadWriteChars()
  {
    Packet p = new TestPacket();

    char[] written = RandomGenerator.randomChars(Packet.DEFAULT_BUFFER_SIZE*2);
    p.writeCharArray(written);
    p.rewind();
    char[] read = p.readCharArray();

    assertTrue(Arrays.equals(written, read));
  }

  /**
   * Writes one short integer number and read it afterwards.
   */
  public void testReadWriteShort()
  {
    Packet p = new TestPacket();

    short written = (short)rand.nextInt();
    p.writeShort(written);
    p.rewind();
    short read = p.readShort();

    assertEquals(written, read);
  }

  /**
   * Writes array of short integer numbers and read it afterwards.
   */
  public void testReadWriteShorts()
  {
    Packet p = new TestPacket();

    short[] written = RandomGenerator.randomShorts(Packet.DEFAULT_BUFFER_SIZE*2);
    p.writeShortArray(written);
    p.rewind();
    short[] read = p.readShortArray();

    assertTrue(Arrays.equals(written, read));
  }

  /**
   * Writes one integer number and read it afterwards.
   */
  public void testReadWriteInt()
  {
    Packet p = new TestPacket();

    int written = rand.nextInt();
    p.writeInt(written);
    p.rewind();
    int read = p.readInt();

    assertEquals(written, read);
  }

  /**
   * Writes array of integer numbers and read it afterwards.
   */
  public void testReadWriteInts()
  {
    Packet p = new TestPacket();

    int[] written = RandomGenerator.randomInts(Packet.DEFAULT_BUFFER_SIZE*2);
    p.writeIntArray(written);
    p.rewind();
    int[] read = p.readIntArray();

    assertTrue(Arrays.equals(written, read));
  }

  /**
   * Writes one long integer number and read it afterwards.
   */
  public void testReadWriteLong()
  {
    Packet p = new TestPacket();

    long written = rand.nextLong();
    p.writeLong(written);
    p.rewind();
    long read = p.readLong();

    assertEquals(written, read);
  }

  /**
   * Writes array of long integer numbers and read it afterwards.
   */
  public void testReadWriteLongs()
  {
    Packet p = new TestPacket();

    long[] written = RandomGenerator.randomLongs(Packet.DEFAULT_BUFFER_SIZE*2);
    p.writeLongArray(written);
    p.rewind();
    long[] read = p.readLongArray();

    assertTrue(Arrays.equals(written, read));
  }

  /**
   * Writes one float number and read it afterwards.
   */
  public void testReadWriteFloat()
  {
    Packet p = new TestPacket();

    float written = rand.nextFloat();
    p.writeFloat(written);
    p.rewind();
    float read = p.readFloat();

    assertEquals(Float.floatToIntBits(written), Float.floatToIntBits(read));
  }

  /**
   * Writes array of float numbers and read it afterwards.
   */
  public void testReadWriteFloats()
  {
    Packet p = new TestPacket();

    float[] written = RandomGenerator.randomFloats(Packet.DEFAULT_BUFFER_SIZE*2);
    p.writeFloatArray(written);
    p.rewind();
    float[] read = p.readFloatArray();

    assertTrue(Arrays.equals(written, read));
  }

  /**
   * Writes one double number and read it afterwards.
   */
  public void testReadWriteDouble()
  {
    Packet p = new TestPacket();

    double written = rand.nextDouble();
    p.writeDouble(written);
    p.rewind();
    double read = p.readDouble();

    assertEquals(Double.doubleToLongBits(written), Double.doubleToLongBits(read));
  }

  /**
   * Writes array of double numbers and read it afterwards.
   */
  public void testReadWriteDoubles()
  {
    Packet p = new TestPacket();

    double[] written = RandomGenerator.randomDoubles(Packet.DEFAULT_BUFFER_SIZE*2);
    p.writeDoubleArray(written);
    p.rewind();
    double[] read = p.readDoubleArray();

    assertTrue(Arrays.equals(written, read));
  }

  /**
   * Writes one {@link String} and read it afterwards.
   */
  public void testReadWriteString()
  {
    Packet p = new TestPacket();

    String written = RandomGenerator.randomString(Packet.DEFAULT_BUFFER_SIZE/2,
        Packet.DEFAULT_BUFFER_SIZE);
    p.writeString(written);
    p.rewind();
    String read = p.readString();

    assertEquals(written, read);
  }

  /**
   * Writes array of {@link String} objects and read it afterwards.
   */
  public void testReadWriteStrings()
  {
    final int MIN_STRINGS = 10;
    final int MAX_STRINGS = Packet.DEFAULT_BUFFER_SIZE;
    Packet p = new TestPacket();

    String[] written = RandomGenerator.randomStrings(
        RandomGenerator.randomInt(MIN_STRINGS, MAX_STRINGS),
        Packet.DEFAULT_BUFFER_SIZE/2, Packet.DEFAULT_BUFFER_SIZE);
    p.writeStringArray(written);
    p.rewind();
    String[] read = p.readStringArray();

    assertTrue(Arrays.equals(written, read));
  }

  /**
   * Writes single variables of different (but fixed) types to {@link Packet}
   * and reads them afterwards in the very same order.
   */
  public void testReadWriteMixedVariables()
  {
    Packet p = new TestPacket();

    byte byteVar = (byte)rand.nextInt();
    char charVar = (char)rand.nextInt();
    short shortVar = (short)rand.nextInt();
    int intVar = rand.nextInt();
    long longVar = rand.nextLong();
    float floatVar = rand.nextFloat();
    double doubleVar = rand.nextDouble();
    String strVar = RandomGenerator.randomString(Packet.DEFAULT_BUFFER_SIZE/2,
        Packet.DEFAULT_BUFFER_SIZE);

    p.writeByte(byteVar);
    p.writeChar(charVar);
    p.writeShort(shortVar);
    p.writeInt(intVar);
    p.writeLong(longVar);
    p.writeFloat(floatVar);
    p.writeDouble(doubleVar);
    p.writeString(strVar);

    p.rewind();

    assertEquals(p.readByte(), byteVar);
    assertEquals(p.readChar(), charVar);
    assertEquals(p.readShort(), shortVar);
    assertEquals(p.readInt(), intVar);
    assertEquals(p.readLong(), longVar);
    assertEquals(Float.floatToIntBits(p.readFloat()),
                 Float.floatToIntBits(floatVar));
    assertEquals(Double.doubleToLongBits(p.readDouble()),
                 Double.doubleToLongBits(doubleVar));
    assertEquals(p.readString(), strVar);
  }

  /**
   * Writes arrays holding different (but fixed) types to {@link Packet}
   * and reads them afterwards in the very same order.
   */
  public void testReadWriteMixedArrays()
  {
    final int MAX_MULTIPLIER = 4;
    final int MIN_MULTIPLIER = 1;

    Packet p = new TestPacket();

    byte[]   bytesArray   = RandomGenerator.randomBytes(Packet.DEFAULT_BUFFER_SIZE *
        RandomGenerator.randomInt(MIN_MULTIPLIER, MAX_MULTIPLIER));
    char[]   charsArray   = RandomGenerator.randomChars(Packet.DEFAULT_BUFFER_SIZE *
        RandomGenerator.randomInt(MIN_MULTIPLIER, MAX_MULTIPLIER));
    short[]  shortsArray  = RandomGenerator.randomShorts(Packet.DEFAULT_BUFFER_SIZE *
        RandomGenerator.randomInt(MIN_MULTIPLIER, MAX_MULTIPLIER));
    int[]    intsArray    = RandomGenerator.randomInts(Packet.DEFAULT_BUFFER_SIZE *
        RandomGenerator.randomInt(MIN_MULTIPLIER, MAX_MULTIPLIER));
    long[]   longsArray   = RandomGenerator.randomLongs(Packet.DEFAULT_BUFFER_SIZE *
        RandomGenerator.randomInt(MIN_MULTIPLIER, MAX_MULTIPLIER));
    float[]  floatsArray  = RandomGenerator.randomFloats(Packet.DEFAULT_BUFFER_SIZE *
        RandomGenerator.randomInt(MIN_MULTIPLIER, MAX_MULTIPLIER));
    double[] doublesArray = RandomGenerator.randomDoubles(Packet.DEFAULT_BUFFER_SIZE *
        RandomGenerator.randomInt(MIN_MULTIPLIER, MAX_MULTIPLIER));
    String[] stringsArray = RandomGenerator.randomStrings(
        RandomGenerator.randomInt(MIN_MULTIPLIER, MAX_MULTIPLIER),
        Packet.DEFAULT_BUFFER_SIZE/2, Packet.DEFAULT_BUFFER_SIZE);

    p.writeByteArray(bytesArray);
    p.writeCharArray(charsArray);
    p.writeShortArray(shortsArray);
    p.writeIntArray(intsArray);
    p.writeLongArray(longsArray);
    p.writeFloatArray(floatsArray);
    p.writeDoubleArray(doublesArray);
    p.writeStringArray(stringsArray);

    p.rewind();

    assertTrue(Arrays.equals(bytesArray, p.readByteArray()));
    assertTrue(Arrays.equals(charsArray, p.readCharArray()));
    assertTrue(Arrays.equals(shortsArray, p.readShortArray()));
    assertTrue(Arrays.equals(intsArray, p.readIntArray()));
    assertTrue(Arrays.equals(longsArray, p.readLongArray()));
    assertTrue(Arrays.equals(floatsArray, p.readFloatArray()));
    assertTrue(Arrays.equals(doublesArray, p.readDoubleArray()));
    assertTrue(Arrays.equals(stringsArray, p.readStringArray()));
  }


  /**
   * Special class holding information about performed write operation:
   * type of written variable (array/single value) and its value.
   *
   * @author M.Olszewski
   */
  private static class WriteOperationInfo
  {
    /**
     * Type of writen variable/array.
     *
     * @author M.Olszewski
     */
    enum WritenType
    {
      /** Byte was written. */
      BYTE,
      /** Character was written. */
      CHAR,
      /** Short integer was written. */
      SHORT,
      /** Integer was written. */
      INT,
      /** Long integer was written. */
      LONG,
      /** Float was written. */
      FLOAT,
      /** Double was written. */
      DOUBLE,
      /** String was written. */
      STRING
    }

    /** Array with all possible types that {@link WriteOperationInfo.WritenType}
     * can hold. */
    private static WritenType[] types = WritenType.values();

    /** Type of write operation. */
    WritenType type;
    /** Single value (false) or array (true) was written. */
    boolean isArray = false;

    /*
     * Variables below helds only value pointed by type and isArray variables.
     * Rest of the variables are set to zero or null respectively.
     */
    byte byteVar;
    byte[] bytesArray;

    char charVar;
    char[] charsArray;

    short shortVar;
    short[] shortsArray;

    int intVar;
    int[] intsArray;

    long longVar;
    long[] longsArray;

    float floatVar;
    float[] floatsArray;

    double doubleVar;
    double[] doublesArray;

    String stringVar;
    String[] stringsArray;

    /**
     * Default constructor, chooses random type of writen variable,
     * whether it is array or not.
     */
    WriteOperationInfo()
    {
      type = getRandomWriteType();
      isArray = rand.nextBoolean();
    }

    /**
     * Gets randomly chosen {@link WriteOperationInfo.WritenType}.
     *
     * @return Returns randomly chosen {@link WriteOperationInfo.WritenType}.
     */
    private static WritenType getRandomWriteType()
    {
      int index = rand.nextInt(types.length);
      return types[index];
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      StringBuilder strB = new StringBuilder("Type: ");

      switch (type)
      {
        case BYTE:
        {
          strB.append("BYTE, ");
          break;
        }
        case CHAR:
        {
          strB.append("CHAR, ");
          break;
        }
        case SHORT:
        {
          strB.append("SHORT, ");
          break;
        }
        case INT:
        {
          strB.append("INT, ");
          break;
        }
        case LONG:
        {
          strB.append("LONG, ");
          break;
        }
        case FLOAT:
        {
          strB.append("FLOAT, ");
          break;
        }
        case DOUBLE:
        {
          strB.append("DOUBLE, ");
          break;
        }
        case STRING:
        {
          strB.append("STRING, ");
          break;
        }
      }
      if (isArray)
      {
        strB.append("array.");
      }
      else
      {
        strB.append("single.");
      }

      return strB.toString();
    }
  }

  /**
   * Sequence of random writes is performed upon packet - then data written to
   * packet is read with the very same order.
   */
  public void testRandomWriteRead()
  {
    final int MIN_OPERATIONS = 10;
    final int MAX_OPERATIONS = 1000;
    final int MIN_ARRAY_SIZE = Packet.DEFAULT_BUFFER_SIZE;
    final int MAX_ARRAY_SIZE = Packet.DEFAULT_BUFFER_SIZE * 9;

    Packet p = new TestPacket();
    List<WriteOperationInfo> ops = new ArrayList<WriteOperationInfo>();

    int maxOps = RandomGenerator.randomInt(MIN_OPERATIONS, MAX_OPERATIONS);

    for (int i = 0; i < maxOps; i++)
    {
      WriteOperationInfo opInfo = new WriteOperationInfo();
      int arraySize = RandomGenerator.randomInt(MIN_ARRAY_SIZE, MAX_ARRAY_SIZE);

      switch (opInfo.type)
      {
        case BYTE:
        {
          if (opInfo.isArray)
          {
            opInfo.bytesArray = RandomGenerator.randomBytes(arraySize);
            p.writeByteArray(opInfo.bytesArray);
          }
          else
          {
            opInfo.byteVar = (byte)rand.nextInt();
            p.writeByte(opInfo.byteVar);
          }
          break;
        }
        case CHAR:
        {
          if (opInfo.isArray)
          {
            opInfo.charsArray = RandomGenerator.randomChars(arraySize);
            p.writeCharArray(opInfo.charsArray);
          }
          else
          {
            opInfo.charVar = (char)rand.nextInt();
            p.writeChar(opInfo.charVar);
          }
          break;
        }
        case SHORT:
        {
          if (opInfo.isArray)
          {
            opInfo.shortsArray = RandomGenerator.randomShorts(arraySize);
            p.writeShortArray(opInfo.shortsArray);
          }
          else
          {
            opInfo.shortVar = (short)rand.nextInt();
            p.writeShort(opInfo.shortVar);
          }
          break;
        }
        case INT:
        {
          if (opInfo.isArray)
          {
            opInfo.intsArray = RandomGenerator.randomInts(arraySize);
            p.writeIntArray(opInfo.intsArray);
          }
          else
          {
            opInfo.intVar = rand.nextInt();
            p.writeInt(opInfo.intVar);
          }
          break;
        }
        case LONG:
        {
          if (opInfo.isArray)
          {
            opInfo.longsArray = RandomGenerator.randomLongs(arraySize);
            p.writeLongArray(opInfo.longsArray);
          }
          else
          {
            opInfo.longVar = rand.nextLong();
            p.writeLong(opInfo.longVar);
          }
          break;
        }
        case FLOAT:
        {
          if (opInfo.isArray)
          {
            opInfo.floatsArray = RandomGenerator.randomFloats(arraySize);
            p.writeFloatArray(opInfo.floatsArray);
          }
          else
          {
            opInfo.floatVar = rand.nextFloat();
            p.writeFloat(opInfo.floatVar);
          }
          break;
        }
        case DOUBLE:
        {
          if (opInfo.isArray)
          {
            opInfo.doublesArray = RandomGenerator.randomDoubles(arraySize);
            p.writeDoubleArray(opInfo.doublesArray);
          }
          else
          {
            opInfo.doubleVar = rand.nextDouble();
            p.writeDouble(opInfo.doubleVar);
          }
          break;
        }
        case STRING:
        {
          final int maxString = 1024;
          final int minString = 64;
          if (opInfo.isArray)
          {
            final int minStrings = 10;
            final int maxStrings = 100;

            int stringsSize = rand.nextInt(maxStrings - minStrings) + minStrings;
            opInfo.stringsArray = RandomGenerator.randomStrings(stringsSize, minString, maxString);
            p.writeStringArray(opInfo.stringsArray);
          }
          else
          {
            opInfo.stringVar = RandomGenerator.randomString(minString, maxString);
            p.writeString(opInfo.stringVar);
          }
        }
      }

      ops.add(opInfo);
    }

    // read all written stuff
    p.rewind();

    for (int i = 0, size = ops.size(); i < size; i++)
    {
      WriteOperationInfo opInfo = ops.get(i);

      switch (opInfo.type)
      {
        case BYTE:
        {
          if (opInfo.isArray)
          {
            byte[] read = p.readByteArray();
            assertTrue(Arrays.equals(opInfo.bytesArray, read));
          }
          else
          {
            byte read = p.readByte();
            assertEquals(opInfo.byteVar, read);
          }
          break;
        }
        case CHAR:
        {
          if (opInfo.isArray)
          {
            char[] read = p.readCharArray();
            assertTrue(Arrays.equals(opInfo.charsArray, read));
          }
          else
          {
            char read = p.readChar();
            assertEquals(opInfo.charVar, read);
          }
          break;
        }
        case SHORT:
        {
          if (opInfo.isArray)
          {
            short[] read = p.readShortArray();
            assertTrue(Arrays.equals(opInfo.shortsArray, read));
          }
          else
          {
            short read = p.readShort();
            assertEquals(opInfo.shortVar, read);
          }
          break;
        }
        case INT:
        {
          if (opInfo.isArray)
          {
            int[] read = p.readIntArray();
            assertTrue(Arrays.equals(opInfo.intsArray, read));
          }
          else
          {
            int read = p.readInt();
            assertEquals(opInfo.intVar, read);
          }
          break;
        }
        case LONG:
        {
          if (opInfo.isArray)
          {
            long[] read = p.readLongArray();
            assertTrue(Arrays.equals(opInfo.longsArray, read));
          }
          else
          {
            long read = p.readLong();
            assertEquals(opInfo.longVar, read);
          }
          break;
        }
        case FLOAT:
        {
          if (opInfo.isArray)
          {
            float[] read = p.readFloatArray();
            assertTrue(Arrays.equals(opInfo.floatsArray, read));
          }
          else
          {
            float read = p.readFloat();
            assertEquals(Float.floatToIntBits(opInfo.floatVar),
                         Float.floatToIntBits(read));
          }
          break;
        }
        case DOUBLE:
        {
          if (opInfo.isArray)
          {
            double[] read = p.readDoubleArray();
            assertTrue(Arrays.equals(opInfo.doublesArray, read));
          }
          else
          {
            double read = p.readDouble();
            assertEquals(Double.doubleToLongBits(opInfo.doubleVar),
                         Double.doubleToLongBits(read));
          }
          break;
        }
        case STRING:
        {
          if (opInfo.isArray)
          {
            String[] read = p.readStringArray();
            assertTrue(Arrays.equals(opInfo.stringsArray, read));
          }
          else
          {
            String read = p.readString();
            assertEquals(opInfo.stringVar, read);
          }
        }
      }
    }
  }
}