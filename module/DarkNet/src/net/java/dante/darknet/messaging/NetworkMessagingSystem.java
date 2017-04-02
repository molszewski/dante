/*
 * Created on 2006-05-02
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.messaging;

import java.nio.ByteBuffer;


/**
 * {@link NetworkMessagingSystem} interface with methods for accessing
 * central register with registered {@link NetworkMessage} subclasses and
 * methods generating {@link Encoder} and {@link Decoder} objects
 * for encoding/decoding {@link NetworkMessage} objects into {@link ByteBuffer} 
 * objects and vice versa.
 *
 * @author M.Olszewski
 */
public interface NetworkMessagingSystem
{
  /**
   * Creates new {@link Decoder} object.
   * 
   * @return Returns new {@link Decoder} object.
   */
  Decoder createDecoder();
  
  /**
   * Creates new {@link Encoder} object.
   * 
   * @return Returns new {@link Encoder} object.
   */
  Encoder createEncoder();
  
  /**
   * Gets the only existing instance of {@link NetworkMessagesRegister} 
   * used only in this {@link NetworkMessagingSystem}. 
   * 
   * @return Returns {@link NetworkMessagesRegister} used in this 
   *         {@link NetworkMessagingSystem}.
   */
  NetworkMessagesRegister getRegister();
}