/*
 * Created on 2006-04-26
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.protocol.tcp;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.java.dante.darknet.common.PrimitivesSizes;
import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.protocol.PacketsFetcher;
import net.java.dante.darknet.protocol.packet.Packet;
import net.java.dante.darknet.util.reader.BytesReader;
import net.java.dante.darknet.util.reader.IntegerReader;
import net.java.dante.darknet.util.reader.LongIntegerReader;


/**
 * Implementation of {@link PacketsFetcher} specific for Transport Control
 * Protocol: before any packet's contents length of this contents is written.
 * TCP packets fetcher first reads length of the packet's contents,
 * then reads packet data (subclass identifier and time stamp) and finally reads
 * all other bytes as user data.
 *
 * @author M.Olszewski
 */
public class TCPPacketsFetcher implements PacketsFetcher
{
  /**
   * Enumeration specifying set of possible read subjects: length of the packet,
   * packet's data (subclass identifier, time stamp) and user's specific data.
   *
   * @author M.Olszewski
   */
  private enum ReadSubject
  {
    /** Indicates that currently length of the packet is read. */
    READ_LENGTH,
    /** Indicates that currently packet's data is read. */
    READ_PACKET_DATA,
    /** Indicates that currently user's specific data is read. */
    READ_USER_DATA;
  }

  /** Subject currently read by this {@link PacketsFetcher}. */
  private ReadSubject subject = ReadSubject.READ_LENGTH;
  /** List holding all fetched packets. */
  private List<Packet> fetchedPackets = new ArrayList<Packet>();
  /** Variable holding so far read packet's content. */
  private TCPPacketContent packetContent = new TCPPacketContent();
  /**
   * If no packet was fetched, reference to this array is returned.
   * This array does not contain any packet.
   */
  private static final Packet[] ZERO_PACKETS = new Packet[0];

  /**
   * Constructs packets fetcher specific for TCP.
   */
  public TCPPacketsFetcher()
  {
    // Intentionally left empty.
  }

  /**
   * @see net.java.dante.darknet.protocol.PacketsFetcher#fetch(java.nio.ByteBuffer)
   */
  public synchronized Packet[] fetch(ByteBuffer buffer)
  {
    Packet[] packets = ZERO_PACKETS;

    // prepare to read - if not prepared
    if ((buffer.position() != 0) && (buffer.position() == buffer.limit()))
    {
      buffer.flip();
    }

    while (buffer.hasRemaining())
    {
      switch(subject)
      {
        case READ_LENGTH:
        {
          if (packetContent.readLength(buffer))
          {
            subject = ReadSubject.READ_PACKET_DATA;
          }
          break;
        }
        case READ_PACKET_DATA:
        {
          if (packetContent.readSubclassId(buffer))
          {
            if (packetContent.readTimestamp(buffer))
            {
              if (packetContent.prepareUserDataReader())
              {
                subject = ReadSubject.READ_USER_DATA;
              }
              else
              {
                subject = ReadSubject.READ_LENGTH;
                Packet p = packetContent.generatePacket();
                fetchedPackets.add(p);
              }
            }
          }
          break;
        }
        case READ_USER_DATA:
        {
          if (packetContent.readUserData(buffer))
          {
            Packet p = packetContent.generatePacket();
            fetchedPackets.add(p);
            subject = ReadSubject.READ_LENGTH;
          }
          break;
        }
      }
    }

    if (!fetchedPackets.isEmpty())
    {
      packets = fetchedPackets.toArray(new Packet[0]);
      fetchedPackets.clear();
    }

    return packets;
  }

  /**
   * Class representing content of the TCP packet. Content is divined into four
   * fields:
   * <ul>
   * <li>length of whole TCP packet's contents,
   * <li>{@link NetworkMessage} subclass identifier,
   * <li>time stamp,
   * <li>user-specific data.
   * </ul>
   *
   * <p>
   * Class provides handy methods to read listed fields. After reading all
   * packet's content, {@link #generatePacket()} method should be invoked
   * to generate {@link TCPPacket} object.
   *
   * @author M.Olszewski
   */
  final class TCPPacketContent
  {
    /** Reads length of the packet's contents. */
    private IntegerReader lengthReader = new IntegerReader();
    /** Reads {@link NetworkMessage} subclass identifier. */
    private IntegerReader subClassIdReader = new IntegerReader();
    /** Reads packet's time stamp. */
    LongIntegerReader timestampReader = new LongIntegerReader();
    /**
     * Reads user data. It is set to <code>null</code> until other fields are
     * not read successfully.
     */
    BytesReader userDataReader = null;

    /**
     * Reads length of the packet's contents.
     *
     * @param buffer - buffer containing length.
     *
     * @return Returns <code>true</code> if length was read, <code>false</code>
     *         otherwise.
     */
    boolean readLength(ByteBuffer buffer)
    {
      return lengthReader.readBytes(buffer);
    }

    /**
     * Reads {@link NetworkMessage} subclass identifier.
     *
     * @param buffer - buffer containing subclass identifier.
     *
     * @return Returns <code>true</code> if subclass identifier was read,
     *         <code>false</code> otherwise.
     */
    boolean readSubclassId(ByteBuffer buffer)
    {
      return subClassIdReader.readBytes(buffer);
    }

    /**
     * Reads packet's time stamp.
     *
     * @param buffer - buffer containing time stamp.
     *
     * @return Returns <code>true</code> if time stamp was read, <code>false</code>
     *         otherwise.
     */
    boolean readTimestamp(ByteBuffer buffer)
    {
      return timestampReader.readBytes(buffer);
    }

    /**
     * Reads user-specific data.
     *
     * @param buffer - buffer containing user data.
     *
     * @return Returns <code>true</code> if user data was read, <code>false</code>
     *         otherwise.
     */
    boolean readUserData(ByteBuffer buffer)
    {
      return userDataReader.readBytes(buffer);
    }

    /**
     * Prepares user data reader, so it could read appropriate number of
     * bytes representing user data from {@link ByteBuffer}.
     *
     * @return Returns value indicating whether there is any user specific
     *         data to read.
     */
    boolean prepareUserDataReader()
    {
      int realLength = lengthReader.getInt();
      int toRead = realLength - PrimitivesSizes.INT_SIZE - PrimitivesSizes.LONG_SIZE;
      if (toRead > 0)
      {
        userDataReader = new BytesReader(toRead);
      }

      return (toRead > 0);
    }

    /**
     * Resets contents of all fields, especially setting {@link #userDataReader}
     * to <code>null</code>.
     */
    void reset()
    {
      lengthReader.reset();
      subClassIdReader.reset();
      timestampReader.reset();
      userDataReader = null;
    }

    /**
     * Method called after successful read of all fields. It generates
     * {@link TCPPacket} object containing all read data.
     *
     * @return Returns new {@link TCPPacket} object containing all read data.
     */
    Packet generatePacket()
    {
      int length = lengthReader.getInt() + PrimitivesSizes.INT_SIZE;
      int subclassId = subClassIdReader.getInt();
      long timestamp = timestampReader.getLong();

      Packet p = new TCPPacket(subclassId, length, timestamp);

      if (userDataReader != null)
      {
        byte[] userData = userDataReader.getBytes();
        p.write(userData);
      }

      reset();

      return p;
    }
  }
}