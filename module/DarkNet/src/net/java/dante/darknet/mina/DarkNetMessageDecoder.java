/*
 * Created on 2006-05-07
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.mina;


import net.java.dante.darknet.messaging.Decoder;
import net.java.dante.darknet.messaging.NetworkMessage;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.protocol.ProtocolDecoder;
import org.apache.mina.protocol.ProtocolDecoderOutput;
import org.apache.mina.protocol.ProtocolSession;
import org.apache.mina.protocol.ProtocolViolationException;


/**
 * DarkNet decoder using external {@link Decoder} to decode 
 * {@link java.nio.ByteBuffer} to array of {@link NetworkMessage} objects.
 *
 * @author M.Olszewski
 */
class DarkNetMessageDecoder implements ProtocolDecoder
{
  /** Decoder used to decode {@link java.nio.ByteBuffer} to {@link NetworkMessage} object. */
  private Decoder decoder = null;
  
  
  /**
   * Construct new {@link DarkNetMessageDecoder} object using specified
   * decoder.
   * 
   * @param usedDecoder - decoder used to decode {@link java.nio.ByteBuffer} into
   *        array of {@link NetworkMessage} objects. 
   */
  DarkNetMessageDecoder(Decoder usedDecoder)
  {
    decoder = usedDecoder;
  }
  
  
  /** 
   * @see org.apache.mina.protocol.ProtocolDecoder#decode(org.apache.mina.protocol.ProtocolSession, org.apache.mina.common.ByteBuffer, org.apache.mina.protocol.ProtocolDecoderOutput)
   */
  public void decode(ProtocolSession session, ByteBuffer minaBuffer,
      ProtocolDecoderOutput output) throws ProtocolViolationException
  {
    NetworkMessage[] messages = decoder.decode(minaBuffer.buf());
    if (messages != null)
    {
      for (int i = 0; i < messages.length; i++)
      {
        NetworkMessage message = messages[i];
        output.write(message);
      }
    }
    else
    {
      throw new ProtocolViolationException("Obtained null messages!");
    }
  }

}