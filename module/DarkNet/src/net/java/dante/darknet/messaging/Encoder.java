/*
 * Created on 2006-05-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.messaging;
import java.nio.ByteBuffer;


/**
 * Interface of {@link NetworkMessage} encoder, encoding specified {@link NetworkMessage}
 * objects into arrays of {@link ByteBuffer} objects.
 *
 * @author M.Olszewski
 */
public interface Encoder
{
  /**
   * Encodes specified {@link NetworkMessage} object into array of {@link ByteBuffer} 
   * objects.
   * 
   * @param message - message to encode.
   * 
   * @return Returns array of {@link ByteBuffer} representing encoded 
   *         {@link NetworkMessage} object.
   */
  public ByteBuffer[] encode(NetworkMessage message);
}