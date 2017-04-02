/*
 * Created on 2006-05-07
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.mina;

import java.nio.ByteBuffer;

import net.java.dante.darknet.messaging.Encoder;
import net.java.dante.darknet.messaging.NetworkMessage;

import org.apache.mina.protocol.ProtocolEncoder;
import org.apache.mina.protocol.ProtocolEncoderOutput;
import org.apache.mina.protocol.ProtocolSession;
import org.apache.mina.protocol.ProtocolViolationException;


/**
 * DarkNet encoder using external {@link Encoder} to encode
 * {@link NetworkMessage} object to array of {@link java.nio.ByteBuffer} objects.
 *
 * @author M.Olszewski
 */
class DarkNetMessageEncoder implements ProtocolEncoder
{
  /** 
   * Encoder used to encode {@link NetworkMessage} to array of 
   * {@link java.nio.ByteBuffer} objects. 
   */
  private Encoder encoder = null;
  
  
  /**
   * Construct new {@link DarkNetMessageEncoder} object using specified
   * encoder.
   * 
   * @param usedEncoder - encoder used to encode {@link NetworkMessage} object into 
   *        array of {@link ByteBuffer} objects.
   */
  DarkNetMessageEncoder(Encoder usedEncoder)
  {
    encoder = usedEncoder;
  }
  
  
  /** 
   * @see org.apache.mina.protocol.ProtocolEncoder#encode(org.apache.mina.protocol.ProtocolSession, java.lang.Object, org.apache.mina.protocol.ProtocolEncoderOutput)
   */
  public void encode(ProtocolSession session, Object object,
      ProtocolEncoderOutput output) throws ProtocolViolationException
  {
    if (object instanceof NetworkMessage)
    {
      NetworkMessage message = (NetworkMessage)object;
      ByteBuffer[] buffers = encoder.encode(message);
      for (int i = 0; i < buffers.length; i++)
      {
        ByteBuffer buffer = buffers[i];
        org.apache.mina.common.ByteBuffer minaBuffer = org.apache.mina.common.ByteBuffer.wrap(buffer);
        output.write(minaBuffer);
      }
    }
    else
    {
      throw new ProtocolViolationException("Specified object is not an instance of Message subclass!");
    }
  }
}