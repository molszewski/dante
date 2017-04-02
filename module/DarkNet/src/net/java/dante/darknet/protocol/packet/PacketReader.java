/*
 * Created on 2006-04-17
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.protocol.packet;

/**
 * Packet-reader interface, allowing easy access to data written via 
 * {@link PacketWriter} interface into packets. 
 * Methods for obtaining single values or arrays of all primitive types 
 * and {@link String} objects from packet are provided. 
 *
 * @author M.Olszewski
 */
public interface PacketReader
{

// Single value readers
  
  /**
   * Reads one boolean from the packet.
   * 
   * @return Returns read boolean.
   */
  public boolean readBoolean();
  
  /**
   * Reads one byte from the packet.
   * 
   * @return Returns read byte.
   */
  public byte readByte();
  
  /**
   * Reads one short integer number from the packet.
   * 
   * @return Returns read short integer number.
   */
  public short readShort();
  
  /**
   * Reads one character from the packet.
   * 
   * @return Returns read character.
   */
  public char readChar();
  
  /**
   * Reads one integer number from the packet.
   * 
   * @return Returns read integer number.
   */
  public int readInt();
  
  /**
   * Reads one long integer number from the packet.
   * 
   * @return Returns read long integer number.
   */
  public long readLong();
  
  /**
   * Reads one float number from the packet.
   * 
   * @return Returns read float number.
   */
  public float readFloat();
  
  /**
   * Reads one double number from the packet.
   * 
   * @return Returns read double number.
   */
  public double readDouble();
  
  /**
   * Reads object of {@link String} class from the packet, using
   * currently set character decoding (or default one, if not set).
   * 
   * @return Returns read {@link String} object.
   */
  public String readString();

  
// Arrays readers
  
  /**
   * Reads booleans array from the packet.
   * 
   * @return Returns read booleans array.
   */
  public boolean[] readBooleanArray();
  
  /**
   * Reads bytes array from the packet.
   * 
   * @return Returns read bytes array.
   */
  public byte[] readByteArray();
  
  /**
   * Reads short integer numbers array from the packet.
   * 
   * @return Returns read short integer numbers array.
   */
  public short[] readShortArray();
  
  /**
   * Reads characters array from the packet.
   * 
   * @return Returns read characters array.
   */
  public char[] readCharArray();
  
  /**
   * Reads integer numbers array from the packet.
   * 
   * @return Returns read integer numbers array.
   */
  public int[] readIntArray();
  
  /**
   * Reads long integer numbers array from the packet.
   * 
   * @return Returns read long integer numbers array.
   */
  public long[] readLongArray();
  
  /**
   * Reads float numbers array from the packet.
   * 
   * @return Returns read float numbers array.
   */
  public float[] readFloatArray();
  
  /**
   * Reads double numbers array from the packet.
   * 
   * @return Returns read double numbers array.
   */
  public double[] readDoubleArray();
  
  /**
   * Reads array of {@link String} objects from the packet, using
   * currently set character decoding (or default one, if not set).
   * 
   * @return Returns read array of {@link String} objects.
   */
  public String[] readStringArray();
  
  
// Position setting
  
  /**
   * Rewinds reader's position to the beginning of packet's data,
   * so data could be read once more.
   */
  public void rewind();
}
