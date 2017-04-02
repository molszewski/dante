/*
 * Created on 2006-05-07
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.mina;


import net.java.dante.darknet.messaging.NetworkMessagingSystem;

import org.apache.mina.protocol.ProtocolCodecFactory;
import org.apache.mina.protocol.ProtocolDecoder;
import org.apache.mina.protocol.ProtocolEncoder;


/**
 * DarkNet codec factory creating encoders and decoders based upon 
 * implementation of {@link net.java.dante.darknet.messaging.Decoder} and 
 * {@link net.java.dante.darknet.messaging.Encoder} interfaces. 
 *
 * @author M.Olszewski
 */
class DarkNetCodecFactory implements ProtocolCodecFactory
{
  /** 
   * Messaging system used to create {@link ProtocolDecoder} objects
   * and {@link ProtocolEncoder} objects. 
   */
  private NetworkMessagingSystem msgSystem = null;
  
  
  /**
   * Constructs object of {@link DarkNetCodecFactory} class. Sets specified 
   * {@link NetworkMessagingSystem} as provider of {@link net.java.dante.darknet.messaging.Decoder}
   * and {@link net.java.dante.darknet.messaging.Encoder} objects.
   *  
   * @param messagingSystem - provider for decoders and encoders.
   */
  DarkNetCodecFactory(NetworkMessagingSystem messagingSystem)
  {
    if (messagingSystem != null)
    {
      msgSystem = messagingSystem;
    }
    else
    {
      throw new NullPointerException("Specified messaging system is null!");
    }
  }
  
  
  /** 
   * @see org.apache.mina.protocol.ProtocolCodecFactory#newDecoder()
   */
  public ProtocolDecoder newDecoder()
  {
    return new DarkNetMessageDecoder(msgSystem.createDecoder());
  }

  /** 
   * @see org.apache.mina.protocol.ProtocolCodecFactory#newEncoder()
   */
  public ProtocolEncoder newEncoder()
  {
    return new DarkNetMessageEncoder(msgSystem.createEncoder());
  }
}