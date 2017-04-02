/*
 * Created on 2006-05-07
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.mina;


import net.java.dante.darknet.messaging.NetworkMessagingSystem;
import net.java.dante.darknet.session.SessionHandler;

import org.apache.mina.protocol.ProtocolCodecFactory;
import org.apache.mina.protocol.ProtocolHandler;
import org.apache.mina.protocol.ProtocolProvider;


/**
 * Class defining a link between {@link NetworkMessagingSystem} and 
 * {@link SessionHandler} from DarkNet library and implementation of
 * MINA framework.
 *
 * @author M.Olszewski
 */
public class DarkNetProtocolProvider implements ProtocolProvider
{
  /** Factory creating decoders and encoders for MINA framework. */
  private DarkNetCodecFactory factory = null;
  /** Protocol handler handling events from MINA framework. */
  private DarkNetProtocolHandler handler = null;
  
  
  /**
   * Constructs object of {@link DarkNetProtocolProvider} using specified
   * {@link NetworkMessagingSystem} and {@link SessionHandler}.
   * 
   * @param messagingSystem - messaging system providing encoders/decoders.
   * @param sessionHandler - session handler handling session events.
   */
  public DarkNetProtocolProvider(NetworkMessagingSystem messagingSystem, 
      SessionHandler sessionHandler)
  {
    if (messagingSystem == null)
    {
      throw new NullPointerException("Specified messagingSystem cannot be null!");
    }
    if (sessionHandler == null)
    {
      throw new NullPointerException("Specified sessionHandler cannot be null!");
    }
    
    factory = new DarkNetCodecFactory(messagingSystem);
    handler = new DarkNetProtocolHandler(sessionHandler);
  }
  
  /** 
   * @see org.apache.mina.protocol.ProtocolProvider#getCodecFactory()
   */
  public ProtocolCodecFactory getCodecFactory()
  {
    return factory;
  }
  
  /** 
   * @see org.apache.mina.protocol.ProtocolProvider#getHandler()
   */
  public ProtocolHandler getHandler()
  {
    return handler; 
  }
}