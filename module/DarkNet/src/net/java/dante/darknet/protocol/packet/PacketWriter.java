/*
 * Created on 2006-04-17
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.protocol.packet;

/**
 * Packet-writer interface, allowing easy writing of all primitive types (single
 * values and arrays) and {@link String} objects. Written data can be easily
 * obtained via {@link PacketReader} interface.
 *
 * @author M.Olszewski
 */
public interface PacketWriter
{

// Single value writers
  
  /**
   * Writes one <code>boolean</code> to the packet.
   * 
   * @param booleanValue - <code>boolean</code> to write.
   */
  public void writeBoolean(boolean booleanValue);
  
  /**
   * Writes one byte to the packet.
   * 
   * @param byteValue - byte to write.
   */
  public void writeByte(byte byteValue);

  /**
   * Writes one short integer number to the packet.
   * 
   * @param shortValue - short integer number to write.
   */
  public void writeShort(short shortValue);
  
  /**
   * Writes one character to the packet.
   * 
   * @param character - character to write.
   */
  public void writeChar(char character);
  
  /**
   * Writes one integer number to the packet.
   * 
   * @param intValue - integer to write.
   */
  public void writeInt(int intValue);
  
  /**
   * Writes one long integer number to the packet.
   * 
   * @param longValue - long integer number to write.
   */
  public void writeLong(long longValue);
  
  /**
   * Writes one float number to the packet.
   * 
   * @param floatValue - float number to write.
   */
  public void writeFloat(float floatValue);
  
  /**
   * Writes one double number to the packet.
   * 
   * @param doubleValue - double number to write.
   */
  public void writeDouble(double doubleValue);
  
  /**
   * Writes one {@link String} object to the packet, using
   * currently set character decoding (or default one, if not set).
   * 
   * @param string - {@link String} object to write.
   */
  public void writeString(String string);

  
// Arrays writers
  
  /**
   * Writes array with <code>boolean</code> values to the packet.
   * 
   * @param booleanValues - <code>boolean</code> array to write.
   */
  public void writeBooleanArray(boolean[] booleanValues);
  
  /**
   * Writes bytes array to the packet.
   * 
   * @param byteValues - bytes array to write.
   */
  public void writeByteArray(byte[] byteValues);
  
  /**
   * Writes short integer numbers array to the packet.
   * 
   * @param shortValues - short integer numbers array to write.
   */
  public void writeShortArray(short[] shortValues);
  
  /**
   * Writes characters array to the packet.
   * 
   * @param characters - characters array to write.
   */
  public void writeCharArray(char[] characters);
  
  /**
   * Writes integer numbers array to the packet.
   * 
   * @param intValues - integer numbers array to write.
   */
  public void writeIntArray(int[] intValues);
  
  /**
   * Writes long integer numbers array to the packet.
   * 
   * @param longValues - long integer numbers array to write.
   */
  public void writeLongArray(long[] longValues);
  
  /**
   * Writes float numbers array to the packet.
   * 
   * @param floatValues - float numbers array to write.
   */
  public void writeFloatArray(float[] floatValues);
  
  /**
   * Writes double numbers array to the packet.
   * 
   * @param doubleValues - double numbers array to write.
   */
  public void writeDoubleArray(double[] doubleValues);
  
  /**
   * Writes array of {@link String} objects to the packet, using
   * currently set character decoding (or default one, if not set).
   * 
   * @param strings - array of {@link String} objects to write.
   */
  public void writeStringArray(String[] strings);
  
  
// Buffers contents
  
  /**
   * Clears the buffer contents so it can be written once more. 
   */
  public void clear();
  
  /**
   * Requests specified capacity for this {@link PacketWriter}. Because it
   * is only a request, it can be discarded by the implementation of
   * this method because of some reason, e.g. requested capacity
   * is lesser than size of stored data.
   * 
   * @param capacity - requested capacity.
   */
  public void requestCapacity(int capacity);
}
