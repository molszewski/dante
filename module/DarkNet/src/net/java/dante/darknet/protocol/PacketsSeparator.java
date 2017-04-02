/*
 * Created on 2006-04-26
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.protocol;

import java.nio.ByteBuffer;

import net.java.dante.darknet.protocol.packet.Packet;


/**
 * Interface specifing method for separating {@link Packet} object 
 * into several {@link ByteBuffer} objects, preparing them to be sent 
 * over Internet.
 *
 * @author M.Olszewski
 */
public interface PacketsSeparator
{
  /**
   * Separates content of specified {@link Packet} object to several 
   * {@link ByteBuffer} objects, so it could be fetched by appropriate
   * {@link PacketsFetcher}. Each {@link ByteBuffer} will be sent 
   * independently over Internet.
   * 
   * @param packet - packet that will be separated into several 
   *        {@link ByteBuffer} objects.
   * 
   * @return Returns array containing {@link ByteBuffer} objects representing
   *         specified {@link Packet} object.
   */
  public ByteBuffer[] separate(Packet packet);
}
