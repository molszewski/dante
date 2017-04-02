/*
 * Created on 2006-05-05
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.messaging;

import net.java.dante.darknet.protocol.Protocol;

/**
 * Implementation of {@link NetworkMessagingSystem} interface, containing one 
 * central register with registered {@link NetworkMessage} subclasses and
 * methods generating {@link Encoder} and {@link Decoder} objects
 * for encoding/decoding {@link NetworkMessage} objects into {@link java.nio.ByteBuffer} 
 * objects and vice versa.
 * <p>This implementation depends on {@link Protocol} specified at construction
 * time.
 *
 * @author M.Olszewski
 */
class NetworkMessagingSystemImpl implements NetworkMessagingSystem
{
  /** Protocol used by this {@link NetworkMessagingSystemImpl}. */
  private Protocol protocol = null;
  /** Messages register used by this {@link NetworkMessagingSystemImpl}. */
  private NetworkMessagesRegister register = new NetworkMessageRegisterImpl();
  
  
  /**
   * Constructs {@link NetworkMessagingSystemImpl} object depending on specified
   * {@link Protocol}.
   * 
   * @param usedProtocol - protocol which delivers objects capable of fetching
   *        and separating protocol-dependent packets.
   */
  NetworkMessagingSystemImpl(Protocol usedProtocol)
  {
    if (usedProtocol != null)
    {
      protocol = usedProtocol;
    }
    else
    {
      throw new NullPointerException("Specified usedProtocol is null!");
    }
  }
  
  
  /** 
   * @see net.java.dante.darknet.messaging.NetworkMessagingSystem#createDecoder()
   */
  public Decoder createDecoder()
  {
    return new DecoderImpl(protocol.createFetcher(), register);
  }

  /** 
   * @see net.java.dante.darknet.messaging.NetworkMessagingSystem#createEncoder()
   */
  public Encoder createEncoder()
  {
    return new EncoderImpl(protocol.createSeparator(), register);
  }
  
  /** 
   * @see net.java.dante.darknet.messaging.NetworkMessagingSystem#getRegister()
   */
  public NetworkMessagesRegister getRegister()
  {
    return register;
  }
}