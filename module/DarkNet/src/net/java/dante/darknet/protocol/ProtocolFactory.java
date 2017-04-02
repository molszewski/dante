/*
 * Created on 2006-05-04
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.protocol;

import java.util.EnumMap;
import java.util.Map;

import net.java.dante.darknet.protocol.tcp.TCPProtocol;


/**
 * Class responsible for getting instances of {@link Protocol} subclasses.
 * For each supported protocol (see {@link net.java.dante.darknet.protocol.SupportedProtocol})
 * one instance of {@link Protocol} class is created. Instances can be
 * obtained by invoking {@link #getProtocol(String)} or 
 * {@link #getProtocol(SupportedProtocol)} methods.
 *
 * @author M.Olszewski
 */
public class ProtocolFactory
{
  /** The only instance of {@link ProtocolFactory}. */
  private static ProtocolFactory instance = new ProtocolFactory();
  
  /** 
   * Enumeration map, holding associations between supported protocols 
   * and instances of {@link Protocol} subclasses.
   */
  private Map<SupportedProtocol, Protocol> protocols = 
      new EnumMap<SupportedProtocol, Protocol>(SupportedProtocol.class); 
  
  
  /**
   * Private constructor - no external objects creation and no inheritance
   * allowed.
   */
  private ProtocolFactory()
  {
    protocols.put(SupportedProtocol.TCP, new TCPProtocol());
  }
  
  /**
   * Standard singleton method - return the only instance of {@link ProtocolFactory}
   * class.
   * 
   * @return Returns the only instance of {@link ProtocolFactory} class.
   */
  public static ProtocolFactory getInstance()
  {
    return instance;
  }
  
  /**
   * Returns instance of specified protocol, only if protocol with
   * specified name is supported by DarkNet library. Otherwise <code>null</code>
   * is returned.
   * 
   * @param protocolName - name of the protocol.
   * 
   * @return Returns instance of specified protocol, only if it is supported
   *         by DarkNet library - otherwise <code>null</code> is returned.
   */
  public Protocol getProtocol(String protocolName)
  {
    Protocol protocol = null;
    String upperCaseName = protocolName.toUpperCase();
    
    if (SupportedProtocol.isSupported(upperCaseName))
    {
      protocol = getProtocol(SupportedProtocol.valueOf(protocolName));
    }
    
    return protocol;
  }
  
  /**
   * Returns instance of specified supported protocol.
   * 
   * @param supportedProtocol - supported protocol.
   * 
   * @return Returns instance of supported protocol.
   */
  public Protocol getProtocol(SupportedProtocol supportedProtocol)
  {
    Protocol protocol = protocols.get(supportedProtocol);
    
    if (protocol == null)
    {
      assert false : "Supported protocol was not created!";
    }
    
    return protocol;
  }
}