/*
 * Created on 2006-05-04
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.protocol.tcp;

import net.java.dante.darknet.protocol.PacketsFetcher;
import net.java.dante.darknet.protocol.PacketsSeparator;

import junit.framework.TestCase;

/**
 * Test class containing various tests for {@link TCPProtocol} methods. 
 *
 * @author M.Olszewski
 */
public class TCPProtocolTest extends TestCase
{
  /**
   * Test for {@link TCPProtocol#createFetcher()} method. 
   */
  public final void testCreateFetcher()
  {
    TCPProtocol tcp = new TCPProtocol();
    PacketsFetcher fetcher1 = tcp.createFetcher();
    PacketsFetcher fetcher2 = tcp.createFetcher();
    assertNotSame(fetcher1, fetcher2);
  }

  /**
   * Test for {@link TCPProtocol#createSeparator()} method.
   */
  public final void testCreateSeparator()
  {
    TCPProtocol tcp = new TCPProtocol();
    PacketsSeparator separator1 = tcp.createSeparator();
    PacketsSeparator separator2 = tcp.createSeparator();
    assertNotSame(separator1, separator2);
  }
}