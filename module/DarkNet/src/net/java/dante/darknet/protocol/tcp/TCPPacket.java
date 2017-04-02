/*
 * Created on 2006-04-18
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.protocol.tcp;

import net.java.dante.darknet.common.PrimitivesSizes;
import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.protocol.packet.Packet;


/**
 * Representation of packet send through Internet using TCP protocol.
 * Contains following packet's data:
 * <ol>
 * <li>Length of whole packet - because TCP sends data as a stream, there must
 *     exist a way of fetching whole sent packets. So, each packet starts with
 *     length field and afterwards contents of packet is sent. Length does not
 *     take into consideration itself - it is not counted as packet's data. 
 * <li>Type of the message - unique identifier representing a message type.
 * <li>Time stamp - 64-bit time stamp (local time).
 * </ol> 
 *
 * @author M.Olszewski
 */
public class TCPPacket extends Packet
{
  /** Offset of entry with packet's length. */
  private static final int LENGTH_OFFSET    = 0;
  /** Offset of entry with packet's subclass unique identifier. */
  private static final int TYPE_OFFSET      = 4;
  /** Offset of entry with packet's time stamp. */
  private static final int TIMESTAMP_OFFSET = 8;
  /** Offset of user data entries. */
  private static final int USER_DATA_OFFSET = 16;
  
  /** Size of packet data entries. */
  public static final int PACKET_DATA_SIZE = 16;
  
  /**
   * Creates packet specific for TCP protocol with specified unique 
   * identifier of {@link NetworkMessage} subclass and default buffer size.
   * 
   * @param messageSubclassID - unique identifier of {@link NetworkMessage} subclass.
   * 
   * @throws IllegalArgumentException if messageTypeID is negative.
   */
  public TCPPacket(int messageSubclassID)
  {
    this(messageSubclassID, Packet.DEFAULT_BUFFER_SIZE, 0);
  }
  
  /**
   * Creates packet specific for TCP protocol with specified unique 
   * identifier of {@link NetworkMessage} subclass and specified buffer size.
   * 
   * @param messageSubclassID - unique identifier of {@link NetworkMessage} subclass.
   * @param bufferSize - size of the buffer.
   * 
   * @throws IllegalArgumentException if message subclass identifier or 
   *         buffer size are negative integers.
   */
  public TCPPacket(int messageSubclassID, int bufferSize)
  {
    this(messageSubclassID, bufferSize, 0);
  }
  
  /**
   * Creates packet specific for TCP protocol with specified unique 
   * identifier of {@link NetworkMessage} subclass and specified buffer size.
   * 
   * @param messageSubclassID - unique identifier of {@link NetworkMessage} subclass.
   * @param bufferSize - size of the buffer.
   * @param packetTimestamp - time stamp of this packet.
   * 
   * @throws IllegalArgumentException if message subclass identifier, time stamp
   *         or buffer size are negative integers.
   */
  public TCPPacket(int messageSubclassID, int bufferSize, long packetTimestamp)
  {
    super(messageSubclassID, bufferSize, packetTimestamp);
    
    buffer.position(USER_DATA_OFFSET);
  }
  
  /** 
   * @see net.java.dante.darknet.protocol.packet.Packet#rewind()
   */
  @Override
  public void rewind()
  {
    buffer.position(USER_DATA_OFFSET);
  }

  /** 
   * @see net.java.dante.darknet.protocol.packet.Packet#updatePacketData()
   */
  @Override
  public void updatePacketData()
  {
    int length = buffer.limit() - PrimitivesSizes.INT_SIZE;
    buffer.putInt(LENGTH_OFFSET, length);
    buffer.putInt(TYPE_OFFSET, getSubclassId());
    buffer.putLong(TIMESTAMP_OFFSET, getTimestamp());
  }
  
  /** 
   * @see net.java.dante.darknet.protocol.packet.Packet#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[subclassId=" + subclassId + "; time stamp=" + timestamp + 
        "; buffer=" + buffer + "]");
  }
}