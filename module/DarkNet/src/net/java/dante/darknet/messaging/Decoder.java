/*
 * Created on 2006-05-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.messaging;

import java.nio.ByteBuffer;


/**
 * Interface of {@link NetworkMessage} decoder, decoding specified {@link ByteBuffer}
 * objects into arrays containing {@link NetworkMessage} objects.
 *
 * @author M.Olszewski
 */
public interface Decoder
{
  /**
   * Decodes specified {@link ByteBuffer} object into array of {@link NetworkMessage} 
   * objects.
   * 
   * @param buffer - buffer to decode.
   * 
   * @return Returns array of {@link NetworkMessage} decoded from specified 
   *         (and probably previously passed) {@link ByteBuffer} object.
   */
  public NetworkMessage[] decode(ByteBuffer buffer);
}