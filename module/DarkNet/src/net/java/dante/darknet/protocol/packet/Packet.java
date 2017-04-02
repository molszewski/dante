/*
 * Created on 2006-04-14
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.protocol.packet;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import net.java.dante.darknet.common.MemoryLimitReachedException;
import net.java.dante.darknet.common.PrimitivesSizes;
import net.java.dante.darknet.messaging.NetworkMessage;



/**
 * Fundamental class for sending and receiving messages by
 * {@link SocketChannel}. It stores all received/prepared to sent bytes
 * in resizeable {@link ByteBuffer}. Contents of <code>Packet</code>
 * can be easily written/read by appropriate interface:
 * <ul>
 * <li>{@link PacketReader} - for reading,
 * <li>{@link PacketWriter} - for writing.
 * </ul>
 * <p>
 * Packet's data can be divided into two main parts:<br>
 * <ul>
 * <li>Packet type data - data general for specified type of packet.
 * <li>User data - user-specific data.
 * </ul>
 *
 * All implemented methods are not synchronized. Implementations or
 * class users should take care of synchronization.
 *
 * @author M.Olszewski
 */
abstract public class Packet implements PacketReader, PacketWriter
{
  /**
   * Name of char set used to write and read strings. If this char set is not
   * supported by JVM on which application is running, then following methods
   * will result in {@link UnsupportedCharsetException} thrown:
   * <ul>
   * <li>{@link #readString()}
   * <li>{@link #readStringArray()}
   * <li>{@link #writeString(String)}
   * <li>{@link #writeStringArray(String[])}
   * </ul>
   */
  public static final String DEFAULT_CHARSET_NAME = "UTF-16BE";

  /** Default size of the buffer. */
  protected static final int DEFAULT_BUFFER_SIZE = 1024;

  /** {@link Charset} for decoding/encoding sent/received characters. */
  private static Charset charset = null;

  /** Default strategy: doubles capacity of buffer. */
  private static final BufferResizeStrategy defaultResizeStrategy = new DoubleSizeStrategy();

  /** Current buffer resize strategy. */
  private static BufferResizeStrategy resizeStrategy = defaultResizeStrategy;

  /** Buffer storing all packet's data. */
  protected ByteBuffer buffer = null;

  /** Message subclass identifier. */
  protected int subclassId;

  /** Time stamp of this packet. */
  protected long timestamp;


  // Char set initialization - if not initialized, following methods could not be
  // called: writeString, writeStrings, readString and readStrings.
  static
  {
    if (Charset.isSupported(DEFAULT_CHARSET_NAME))
    {
      charset = Charset.forName(DEFAULT_CHARSET_NAME);
    }
  }


  /**
   * Creates packet with specified unique identifier of {@link NetworkMessage} subclass
   * and default buffer size.
   *
   * @param messageSubclassID - unique identifier of {@link NetworkMessage} subclass.
   *
   * @throws IllegalArgumentException if messageTypeID is negative.
   */
  protected Packet(int messageSubclassID)
  {
    this(messageSubclassID, DEFAULT_BUFFER_SIZE, 0);
  }

  /**
   * Creates packet with specified unique identifier of {@link NetworkMessage} subclass
   * and specified buffer size.
   *
   * @param messageSubclassID - unique identifier of {@link NetworkMessage} subclass.
   * @param bufferSize - size of the buffer.
   *
   * @throws IllegalArgumentException if message subclass identifier or
   *         buffer size are negative integers.
   */
  protected Packet(int messageSubclassID, int bufferSize)
  {
    this(messageSubclassID, bufferSize, 0);
  }

  /**
   * Creates packet with specified unique identifier of {@link NetworkMessage}
   * subclass, specified buffer size and specified time stamp.
   *
   * @param messageSubclassID - unique identifier of {@link NetworkMessage} subclass.
   * @param bufferSize - size of the buffer.
   * @param packetTimestamp - time stamp of this packet.
   *
   * @throws IllegalArgumentException if message subclass identifier, time stamp
   *         or buffer size are negative integers.
   */
  protected Packet(int messageSubclassID, int bufferSize, long packetTimestamp)
  {
    if (messageSubclassID >= 0)
    {
      if (packetTimestamp >= 0)
      {
        timestamp = packetTimestamp;
        subclassId = messageSubclassID;
        buffer = ByteBuffer.allocate(bufferSize);
      }
      else
      {
        throw new IllegalArgumentException("Packet(): specified packetTimestamp is a negative integer!");
      }
    }
    else
    {
      throw new IllegalArgumentException("Packet(): specified messageSubclassID is a negative integer!");
    }
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj)
  {
    boolean equal = (obj == this);

    if (!equal && (obj instanceof Packet))
    {
      Packet packet = (Packet)obj;
      equal = (subclassId == packet.subclassId) &&
              (timestamp == packet.timestamp) &&
              (buffer.equals(packet.buffer));
    }

    return equal;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    int result = 19;
    result = 37 * result + subclassId;
    result = 37 * result + buffer.hashCode();
    result = 37 * result + (int)(timestamp ^ (timestamp >>>32));

    return result;
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#rewind()
   */
  public abstract void rewind();

  /**
   * Method called every time just before sending a packet: it provides a last
   * chance to update system data of the packet (e.g. writing lengthh of the
   * packet) with assurance that user data will not be changed afterwards.
   */
  protected abstract void updatePacketData();

  /**
   * Gets the timestamp of this packet
   *
   * @return Returns the timestamp of this packet.
   */
  public long getTimestamp()
  {
    return timestamp;
  }

  /**
   * Sets the timestamp for this packet
   *
   * @param netTimestamp - new timestamp to set.
   */
  public void setTimestamp(long netTimestamp)
  {
    timestamp = netTimestamp;
  }

  /**
   * Gets unique identifier of {@link NetworkMessage} subclass represented by
   * this packet.
   *
   * @return Returns the unique identifier of {@link NetworkMessage} subclass.
   */
  public int getSubclassId()
  {
    return subclassId;
  }

  /**
   * Gets reference to {@link ByteBuffer} held by this {@link Packet} object.
   *
   * @return Returns reference to {@link ByteBuffer} from this
   *         {@link Packet} object.
   */
  public ByteBuffer getBuffer()
  {
    return buffer;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (Packet.class + "[subclassId=" + subclassId + "; timestamp=" + timestamp +
           "; buffer=" + buffer + "]");
  }

  /**
   * Sets {@link BufferResizeStrategy} that will be used for calculating
   * capacity of this packet's buffer if current capacity will not be
   * sufficent.
   * <p>If method is called with <code>null</code> argument, default strategy
   * will be set.
   *
   * @param newResizeStrategy - resize strategy that will be used by this packet
   *        to resize buffer's capacity.
   */
  public static void setBufferResizeStrategy(BufferResizeStrategy newResizeStrategy)
  {
    resizeStrategy = (newResizeStrategy != null)? newResizeStrategy : defaultResizeStrategy;
  }


  /**
   * Writes specified bytes to this packet's buffer without treating them
   * as an array of bytes.
   *
   * @param bytes - array of bytes which will be written to this packet's
   *        buffer.
   */
  public void write(byte[] bytes)
  {
    ensureCapacity(bytes.length);
    buffer.put(bytes);
  }

  /**
   * TODO inefficient implementation - it writes boolean value as byte.
   *
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeBoolean(boolean)
   */
  public void writeBoolean(boolean booleanValue)
  {
    byte byteValue = (byte)(booleanValue ? 1 : 0);
    writeByte(byteValue);
  }

  /**
   * TODO inefficient implementation - it writes boolean values as bytes.
   *
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeBooleanArray(boolean[])
   */
  public void writeBooleanArray(boolean[] booleanValues)
  {
    byte[] byteValues = new byte[booleanValues.length];
    for (int i = 0; i < byteValues.length; i++)
    {
      byteValues[i] = (byte)(booleanValues[i] ? 1 : 0);
    }
    writeByteArray(byteValues);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeByte(byte)
   */
  public void writeByte(byte byteValue)
  {
    ensureCapacity(PrimitivesSizes.BYTE_SIZE);
    buffer.put(byteValue);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeByteArray(byte[])
   */
  public void writeByteArray(byte[] byteValues)
  {
    ensureCapacity(PrimitivesSizes.INT_SIZE + byteValues.length);
    buffer.putInt(byteValues.length);
    buffer.put(byteValues);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeChar(char)
   */
  public void writeChar(char character)
  {
    ensureCapacity(PrimitivesSizes.CHAR_SIZE);
    buffer.putChar(character);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeCharArray(char[])
   */
  public void writeCharArray(char[] characters)
  {
    int bytesSize = characters.length * PrimitivesSizes.CHAR_SIZE;
    ensureCapacity(PrimitivesSizes.INT_SIZE + bytesSize);
    buffer.putInt(characters.length);
    buffer.asCharBuffer().put(characters);
    buffer.position(buffer.position() + bytesSize);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeShort(short)
   */
  public void writeShort(short shortValue)
  {
    ensureCapacity(PrimitivesSizes.SHORT_SIZE);
    buffer.putShort(shortValue);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeShortArray(short[])
   */
  public void writeShortArray(short[] shortValues)
  {
    int bytesSize = shortValues.length * PrimitivesSizes.SHORT_SIZE;
    ensureCapacity(PrimitivesSizes.INT_SIZE + bytesSize);
    buffer.putInt(shortValues.length);
    buffer.asShortBuffer().put(shortValues);
    buffer.position(buffer.position() + bytesSize);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeInt(int)
   */
  public void writeInt(int intValue)
  {
    ensureCapacity(PrimitivesSizes.INT_SIZE);
    buffer.putInt(intValue);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeIntArray(int[])
   */
  public void writeIntArray(int[] intValues)
  {
    int bytesSize = intValues.length * PrimitivesSizes.INT_SIZE;
    ensureCapacity(PrimitivesSizes.INT_SIZE + bytesSize);
    buffer.putInt(intValues.length);
    buffer.asIntBuffer().put(intValues);
    buffer.position(buffer.position() + bytesSize);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeLong(long)
   */
  public void writeLong(long longValue)
  {
    ensureCapacity(PrimitivesSizes.LONG_SIZE);
    buffer.putLong(longValue);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeLongArray(long[])
   */
  public void writeLongArray(long[] longValues)
  {
    int bytesSize = longValues.length * PrimitivesSizes.LONG_SIZE;
    ensureCapacity(PrimitivesSizes.INT_SIZE + bytesSize);
    buffer.putInt(longValues.length);
    buffer.asLongBuffer().put(longValues);
    buffer.position(buffer.position() + bytesSize);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeFloat(float)
   */
  public void writeFloat(float floatValue)
  {
    ensureCapacity(PrimitivesSizes.FLOAT_SIZE);
    buffer.putFloat(floatValue);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeFloatArray(float[])
   */
  public void writeFloatArray(float[] floatValues)
  {
    int bytesSize = floatValues.length * PrimitivesSizes.FLOAT_SIZE;
    ensureCapacity(PrimitivesSizes.INT_SIZE + bytesSize);
    buffer.putInt(floatValues.length);
    buffer.asFloatBuffer().put(floatValues);
    buffer.position(buffer.position() + bytesSize);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeDouble(double)
   */
  public void writeDouble(double doubleValue)
  {
    ensureCapacity(PrimitivesSizes.DOUBLE_SIZE);
    buffer.putDouble(doubleValue);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeDoubleArray(double[])
   */
  public void writeDoubleArray(double[] doubleValues)
  {
    int bytesSize = doubleValues.length * PrimitivesSizes.DOUBLE_SIZE;
    ensureCapacity(PrimitivesSizes.INT_SIZE + bytesSize);
    buffer.putInt(doubleValues.length);
    buffer.asDoubleBuffer().put(doubleValues);
    buffer.position(buffer.position() + bytesSize);
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeString(java.lang.String)
   */
  public void writeString(String string)
  {
    if (charset != null)
    {
      if (string != null)
      {
        int bytesSize = string.length() * PrimitivesSizes.CHAR_SIZE;
        ensureCapacity(PrimitivesSizes.INT_SIZE + bytesSize);
        buffer.putInt(bytesSize);
        buffer.put(charset.encode(string));
      }
      else
      {
        throw new NullPointerException("Packet.writeString(): specified string is null!");
      }
    }
    else
    {
      throw new UnsupportedCharsetException("Charset: " + charset + " is not supported - strings cannot be read or written!");
    }
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#writeStringArray(java.lang.String[])
   */
  public void writeStringArray(String[] strings)
  {
    if (charset != null)
    {
      if (strings != null)
      {
        // put number of all strings
        ensureCapacity(PrimitivesSizes.INT_SIZE);
        buffer.putInt(strings.length);

        // put each string
        for (int i = 0; i < strings.length; i++)
        {
          writeString(strings[i]);
        }
      }
      else
      {
        throw new NullPointerException("Packet.writeStrings(): specified strings is null!");
      }
    }
    else
    {
      throw new UnsupportedCharsetException("Charset: " + charset + " is not supported - strings cannot be read or written!");
    }
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#clear()
   */
  public void clear()
  {
    buffer.clear();
  }

  /**
   * Requests specified capacity for this {@link Packet}. Only requests
   * with capacity greater than capacity of this {@link Packet} are taken
   * into consideration.
   *
   * @see net.java.dante.darknet.protocol.packet.PacketWriter#requestCapacity(int)
   * @throws IllegalArgumentException if specified capacity is a negative integer.
   */
  public void requestCapacity(int capacity)
  {
    if (capacity > 0)
    {
      if (capacity > buffer.capacity())
      {
        ByteBuffer newBuffer = ByteBuffer.allocate(capacity);
        buffer.flip();
        newBuffer.put(buffer);
        buffer = newBuffer;
      }
    }
    else
    {
      throw new IllegalArgumentException("Packet.requestCapacity(): specified capacity is a negative integer!");
    }
  }

  /**
   * TODO inefficent implementation - it reads byte and converts it into boolean.
   *
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readBoolean()
   */
  public boolean readBoolean()
  {
    return ((readByte() != 0) ? true : false);
  }

  /**
   * TODO inefficient implementation - it reads array of bytes and converts them
   *      into boolean array.
   *
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readBooleanArray()
   */
  public boolean[] readBooleanArray()
  {
    byte[] byteValues = readByteArray();
    boolean[] booleanValues = new boolean[byteValues.length];
    for (int i = 0; i < byteValues.length; i++)
    {
      booleanValues[i] = ((byteValues[i] != 0) ? true : false);
    }

    return booleanValues;
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readByte()
   */
  public byte readByte()
  {
    return buffer.get();
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readByteArray()
   */
  public byte[] readByteArray()
  {
    int length = buffer.getInt();
    byte[] bytes = null;

    if (length >= 0)
    {
      bytes = new byte[length];
      buffer.get(bytes);
    }
    else
    {
      throw new NegativeArraySizeException("Packet.readBytes(): Invalid length of the bytes array was read (negative integer)!");
    }

    return bytes;
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readChar()
   */
  public char readChar()
  {
    return buffer.getChar();
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readCharArray()
   */
  public char[] readCharArray()
  {
    int length = buffer.getInt();
    char[] chars = null;

    if (length >= 0)
    {
      chars = new char[length];
      buffer.asCharBuffer().get(chars);
      buffer.position(buffer.position() + (length * PrimitivesSizes.CHAR_SIZE));
    }
    else
    {
      throw new NegativeArraySizeException("Packet.readChars(): Invalid length of the characters array was read (negative integer)!");
    }

    return chars;
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readShort()
   */
  public short readShort()
  {
    return buffer.getShort();
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readShortArray()
   */
  public short[] readShortArray()
  {
    int length = buffer.getInt();
    short[] shorts = null;
    if (length >= 0)
    {
      shorts = new short[length];
      buffer.asShortBuffer().get(shorts);
      buffer.position(buffer.position() + (length * PrimitivesSizes.SHORT_SIZE));
    }
    else
    {
      throw new NegativeArraySizeException("Packet.readShorts(): Invalid length of the short integer numbers array was read (negative integer)!");
    }

    return shorts;
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readInt()
   */
  public int readInt()
  {
    return buffer.getInt();
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readIntArray()
   */
  public int[] readIntArray()
  {
    int length = buffer.getInt();
    int[] ints = null;

    if (length >= 0)
    {
      ints = new int[length];
      buffer.asIntBuffer().get(ints);
      buffer.position(buffer.position() + (length * PrimitivesSizes.INT_SIZE));
    }
    else
    {
      throw new NegativeArraySizeException("Packet.readInts(): Invalid length of the integer numbers array was read (negative integer)!");
    }

    return ints;
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readLong()
   */
  public long readLong()
  {
    return buffer.getLong();
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readLongArray()
   */
  public long[] readLongArray()
  {
    int length = buffer.getInt();
    long[] longs = null;

    if (length >= 0)
    {
      longs = new long[length];
      buffer.asLongBuffer().get(longs);
      buffer.position(buffer.position() + (length * PrimitivesSizes.LONG_SIZE));
    }
    else
    {
      throw new NegativeArraySizeException("Packet.readLongs(): Invalid length of the long integer numbers array was read (negative integer)!");
    }

    return longs;
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readFloat()
   */
  public float readFloat()
  {
    return buffer.getFloat();
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readFloatArray()
   */
  public float[] readFloatArray()
  {
    int length = buffer.getInt();
    float[] floats = null;

    if (length >= 0)
    {
      floats = new float[length];
      buffer.asFloatBuffer().get(floats);
      buffer.position(buffer.position() + (length * PrimitivesSizes.FLOAT_SIZE));
    }
    else
    {
      throw new NegativeArraySizeException("Packet.readFloats(): Invalid length of the float numbers array was read (negative integer)!");
    }

    return floats;
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readDouble()
   */
  public double readDouble()
  {
    return buffer.getDouble();
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readDoubleArray()
   */
  public double[] readDoubleArray()
  {
    int length = buffer.getInt();
    double[] doubles = null;

    if (length >= 0)
    {
      doubles = new double[length];
      buffer.asDoubleBuffer().get(doubles);
      buffer.position(buffer.position() + (length * PrimitivesSizes.DOUBLE_SIZE));
    }
    else
    {
      throw new NegativeArraySizeException("Packet.readDoubles(): Invalid length of the double numbers array was read (negative integer)!");
    }

    return doubles;
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readString()
   */
  public String readString()
  {
    if (charset == null)
    {
      throw new UnsupportedCharsetException("Charset: " + charset + " is not supported - strings cannot be read or written!");
    }

    int length = buffer.getInt();
    String string = null;

    if (length >= 0)
    {
      byte[] stringBytes = new byte[length];
      buffer.get(stringBytes);
      string = charset.decode(ByteBuffer.wrap(stringBytes)).toString();
    }
    else
    {
      throw new NegativeArraySizeException("Packet.readString(): Invalid length of the string was read (negative integer)!");
    }

    return string;
  }

  /**
   * @see net.java.dante.darknet.protocol.packet.PacketReader#readStringArray()
   */
  public String[] readStringArray()
  {
    if (charset == null)
    {
      throw new UnsupportedCharsetException("Charset: " + charset + " is not supported - strings cannot be read or written!");
    }

    int length = buffer.getInt();
    String[] strings = null;

    if (length >= 0)
    {
      strings = new String[length];
      for (int i = 0; i < length; i++)
      {
        strings[i] = readString();
      }
    }
    else
    {
      throw new NegativeArraySizeException("Packet.readStrings(): Invalid length of the strings array was read (negative integer)!");
    }

    return strings;
  }

  /**
   * Ensures that capacity of the {@link ByteBuffer} will be enough to hold
   * specified number of bytes.
   *
   * @param numberOfBytes - number of bytes that will stored in
   *        {@link ByteBuffer}.
   * @throws MemoryLimitReachedException if no more data can be stored in
   *         this {@link Packet} object.
   */
  private void ensureCapacity(int numberOfBytes)
  {
    int targetCapacity = buffer.position() + numberOfBytes;
    if (targetCapacity > 0)
    {
      if (targetCapacity > buffer.capacity())
      {
        // Upper limit reached?
        int requiredBytesnNumber = targetCapacity - buffer.capacity();

        if (targetCapacity > 0)
        {
          // First try 'resize strategy'
          int newCapacity = resizeStrategy.newCapacity(requiredBytesnNumber, buffer.capacity());

          // Last resort - if newCapacity is lesser than targetCapacity, use it
          if (newCapacity < targetCapacity)
          {
            newCapacity = targetCapacity;
          }

          // Finally - resize buffer with new capacity (always > 0)
          resizeBuffer(newCapacity);
        }
        else
        {
          throw new MemoryLimitReachedException("Specified data not written - upper memory limit for single packet reached!");
        }
      }
    }
    else
    {
      throw new MemoryLimitReachedException("Specified data not written - upper memory limit for single packet reached!");
    }
  }

  /**
   * Resizes buffer so it is set to new capacity.
   *
   * @param newCapacity - buffer capacity after resize operation.
   */
  private void resizeBuffer(int newCapacity)
  {
    ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
    buffer.flip();
    newBuffer.put(buffer);
    buffer = newBuffer;
  }

  /**
   * Default strategy for {@link Packet} object's buffer, always returning
   * double size
   *
   * @author M.Olszewski
   */
  private static class DoubleSizeStrategy implements BufferResizeStrategy
  {
    /**
     * Default, empty constructor.
     */
    DoubleSizeStrategy()
    {
      // Empty.
    }

    /**
     * @see net.java.dante.darknet.protocol.packet.BufferResizeStrategy#newCapacity(int, int)
     */
    public int newCapacity(int requiredBytesNumber, int oldCapacity)
    {
      return (oldCapacity << 1);
    }
  }
}