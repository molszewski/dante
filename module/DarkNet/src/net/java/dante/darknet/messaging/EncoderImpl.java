/*
 * Created on 2006-05-08
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.messaging;

import java.nio.ByteBuffer;

import net.java.dante.darknet.protocol.PacketsSeparator;
import net.java.dante.darknet.protocol.tcp.TCPPacket;


/**
 * Implementation of {@link NetworkMessage} encoder, encoding specified {@link NetworkMessage}
 * objects into arrays of {@link ByteBuffer} objects.
 *
 * @author M.Olszewski
 */
public class EncoderImpl implements Encoder
{
  /** {@link PacketsSeparator} object used only in this {@link EncoderImpl}. */
  private PacketsSeparator separator = null;
  /** Instance of {@link NetworkMessagesRegister} used in this {@link NetworkMessagingSystem}. */
  private NetworkMessagesRegister register = null;
  
  /**
   * Constructs object of {@link EncoderImpl} using specified 
   * {@link PacketsSeparator} to obtain {@link ByteBuffer} objects.
   * 
   * @param packetsSeparator - packets separator used by this {@link EncoderImpl}.
   * @param msgRegister - register holding messages registered in this
   *        {@link NetworkMessagingSystem}.
   */
  EncoderImpl(PacketsSeparator packetsSeparator, NetworkMessagesRegister msgRegister)
  {
    assert (packetsSeparator == null) : "Specified packetsSeparator cannot be null!";
    assert (msgRegister == null)   : "Specified msgRegister cannot be null!";
    
    separator = packetsSeparator;
    register = msgRegister;
  }
  
  /** 
   * @see net.java.dante.darknet.messaging.Encoder#encode(net.java.dante.darknet.messaging.NetworkMessage)
   */
  public ByteBuffer[] encode(NetworkMessage message)
  {
    TCPPacket packet = new TCPPacket(register.getSubclassId(message));
    message.toPacket(packet);
    packet.getBuffer().flip();
    packet.updatePacketData();
    return separator.separate(packet);
  }
}