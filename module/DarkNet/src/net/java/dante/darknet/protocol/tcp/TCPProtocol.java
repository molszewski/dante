/*
 * Created on 2006-05-02
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.protocol.tcp;

import net.java.dante.darknet.protocol.PacketsFetcher;
import net.java.dante.darknet.protocol.PacketsSeparator;
import net.java.dante.darknet.protocol.Protocol;

/**
 * Class delivers new instances of {@link TCPPacketsFetcher}
 * and {@link PacketsSeparator} classes on demand.
 * Each invocation of this class's methods provides new instance of 
 * specified class.
 *
 * @author M.Olszewski
 */
public class TCPProtocol implements Protocol
{
  /** 
   * @see net.java.dante.darknet.protocol.Protocol#createFetcher()
   */
  public PacketsFetcher createFetcher()
  {
    return new TCPPacketsFetcher();
  }

  /** 
   * @see net.java.dante.darknet.protocol.Protocol#createSeparator()
   */
  public PacketsSeparator createSeparator()
  {
    return new TCPPacketsSeparator(); 
  }
}