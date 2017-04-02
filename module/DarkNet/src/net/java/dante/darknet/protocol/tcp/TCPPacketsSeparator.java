/*
 * Created on 2006-04-26
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.protocol.tcp;

import java.nio.ByteBuffer;

import net.java.dante.darknet.protocol.PacketsSeparator;
import net.java.dante.darknet.protocol.packet.Packet;


/**
 * Implementation of {@link PacketsSeparator} specific for Transport Control 
 * Protocol: for each {@link TCPPacket} object passed to {@link #separate(Packet)}
 * method {@link ByteBuffer} is obtained and returned in an array containing
 * only this object.
 * 
 * @author M.Olszewski
 */
public class TCPPacketsSeparator implements PacketsSeparator
{
  /** Array with zero {@link ByteBuffer} objects. */
  private static final ByteBuffer[] ZERO_BUFFERS_ARRAY = new ByteBuffer[0];
  
  /**
   * Constructs packets separator specific for TCP. 
   */
  public TCPPacketsSeparator()
  {
    // Intentionally left empty.
  }

  /** 
   * This method simply returns underlying {@link ByteBuffer} from specified
   * {@link Packet} if specified {@link Packet} is an instance of 
   * {@link TCPPacket} class.
   * Otherwise empty array is returned.
   * 
   * @see net.java.dante.darknet.protocol.PacketsSeparator#separate(net.java.dante.darknet.protocol.packet.Packet)
   */
  public ByteBuffer[] separate(Packet packet)
  {
    ByteBuffer[] buffers = ZERO_BUFFERS_ARRAY;
    if (packet instanceof TCPPacket)
    {
      buffers = new ByteBuffer[1];
      buffers[0] = packet.getBuffer();
    }
    
    return buffers;
  }
}
