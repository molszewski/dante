/*
 * Created on 2006-04-30
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.messaging;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.java.dante.darknet.protocol.PacketsFetcher;
import net.java.dante.darknet.protocol.packet.Packet;


/**
 * Implementation of {@link NetworkMessage} decoder interface, decoding specified 
 * {@link ByteBuffer} objects into arrays containing {@link NetworkMessage} objects.
 *
 * @author M.Olszewski
 */
public class DecoderImpl implements Decoder
{
  /** {@link PacketsFetcher} object used only in this {@link DecoderImpl}. */
  private PacketsFetcher fetcher = null;
  /** Instance of {@link NetworkMessagesRegister} used in this {@link NetworkMessagingSystem}. */
  NetworkMessagesRegister register = null;
  
  /**
   * Constructs object of {@link DecoderImpl} using specified 
   * {@link PacketsFetcher} to obtain {@link NetworkMessage} objects.
   * 
   * @param packetFetcher - packets fetcher used by this {@link DecoderImpl}.
   * @param msgRegister - register holding messages registered in this
   *        {@link NetworkMessagingSystem}.
   */
  DecoderImpl(PacketsFetcher packetFetcher, NetworkMessagesRegister msgRegister)
  {
    assert (packetFetcher == null) : "Specified packetFetcher cannot be null!";
    assert (msgRegister == null)   : "Specified msgRegister cannot be null!";
  
    fetcher = packetFetcher;
    register = msgRegister;
  }
  
  /** 
   * @see net.java.dante.darknet.messaging.Decoder#decode(java.nio.ByteBuffer)
   */
  public NetworkMessage[] decode(ByteBuffer buffer)
  {
    Packet[] packets = fetcher.fetch(buffer);
    List<NetworkMessage> messages = new ArrayList<NetworkMessage>(packets.length);
    
    for (int i = 0; i < packets.length; i++)
    {
      Packet p = packets[i];
      
      NetworkMessage msg = register.construct(p.getSubclassId());
      
      if (msg != null)
      {
        p.rewind();
        msg.fromPacket(p);
        messages.add(msg);
      }
    }
    
    return messages.toArray(new NetworkMessage[messages.size()]);
  }
}