/*
 * Created on 2006-04-25
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.protocol;

import java.nio.ByteBuffer;

import net.java.dante.darknet.protocol.packet.Packet;


/**
 * Interface providing a method for fetching packets from {@link ByteBuffer}.
 * Class implementing this interface should parse specified {@link ByteBuffer}
 * and return all packets fetched from it. 
 *
 * @author M.Olszewski
 */
public interface PacketsFetcher
{
  /**
   * Fetches objects of {@link Packet} class from specified {@link ByteBuffer}.
   * This method can return zero or greater number of {@link Packet} objects
   * after each call. 
   * 
   * @param buffer - buffer holding data, that will be used to fetch 
   *       {@link Packet} objects from them. 
   * 
   * @return Returns array holding zero or more {@link Packet} objects.
   */
  public Packet[] fetch(ByteBuffer buffer);
}
