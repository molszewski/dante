/*
 * Created on 2006-05-05
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.messaging;

import net.java.dante.darknet.protocol.Protocol;
import net.java.dante.darknet.protocol.ProtocolFactory;
import net.java.dante.darknet.protocol.SupportedProtocol;

/**
 * Factory providing {@link NetworkMessagingSystem} objects depending on selected
 * {@link net.java.dante.darknet.protocol.SupportedProtocol}.
 *
 * @author M.Olszewski
 */
public class NetworkMessagingSystemFactory
{
  /** The only existing instance of {@link NetworkMessagingSystemFactory} class. */
  private static final NetworkMessagingSystemFactory instance = new NetworkMessagingSystemFactory();
  
  
  /**
   * Private constructor - no inheritance and objects creation outside this
   * class.
   */
  private NetworkMessagingSystemFactory()
  {
    //
  }
  
  
  /**
   * Gets the only existing instance of this class.
   * 
   * @return Returns the only existing instance of this class.
   */
  public static NetworkMessagingSystemFactory getInstance()
  {
    return instance;
  }
  
  /**
   * Creates object implementing {@link NetworkMessagingSystem} interface, depending
   * on given {@link SupportedProtocol}.
   * 
   * @param supportedProtocol - protocol on which implementation of 
   *        {@link NetworkMessagingSystem} will be depending.
   *        
   * @return Returns object implementing {@link NetworkMessagingSystem} interface.
   */
  public NetworkMessagingSystem createMessagingSystem(SupportedProtocol supportedProtocol)
  {
    Protocol protocol = ProtocolFactory.getInstance().getProtocol(supportedProtocol);
    
    assert protocol != null : "Obtained protocol cannot be null!";
    
    NetworkMessagingSystem ms = new NetworkMessagingSystemImpl(protocol);
    return ms;
  }
  
  /**
   * Creates object implementing {@link NetworkMessagingSystem} interface, depending
   * on given name of {@link SupportedProtocol}.
   * This method can return <code>null</code> if specified name is not 
   * correct name of any of existing {@link SupportedProtocol} objects.
   * 
   * @param supportedProtocolName - name of protocol on which implementation of 
   *        {@link NetworkMessagingSystem} will be depending.
   *        
   * @return Returns object implementing {@link NetworkMessagingSystem} interface
   *         or <code>null</code> if specified supported protocol name was
   *         wrong.
   */
  public NetworkMessagingSystem createMessagingSystem(String supportedProtocolName)
  {
    Protocol protocol = ProtocolFactory.getInstance().getProtocol(supportedProtocolName);
    NetworkMessagingSystem ms = null;
    if (protocol != null)
    {
      ms = new NetworkMessagingSystemImpl(protocol);
    }
    
    return ms;
  }
}