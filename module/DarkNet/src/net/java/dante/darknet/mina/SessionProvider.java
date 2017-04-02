package net.java.dante.darknet.mina;

import java.util.IdentityHashMap;
import java.util.Map;

import net.java.dante.darknet.session.Session;

import org.apache.mina.protocol.ProtocolSession;


/**
 * Class providing implementation of {@link Session} interface specific
 * for MINA framework.
 * 
 * @author M.Olszewski
 */
public class SessionProvider
{
  /** The only instance of {@link SessionProvider}. */
  private static SessionProvider instance = new SessionProvider();
  
  /** 
   * Map holding association between {@link ProtocolSession} objects and all 
   * created {@link SessionImpl} objects. 
   */
  private Map<ProtocolSession, SessionImpl> sessions = 
      new IdentityHashMap<ProtocolSession, SessionImpl>();
  
  
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private SessionProvider()
  {
    // Intentionally left empty.
  }
  
  
  /**
   * Gets the only existing instance of {@link SessionProvider}.
   * 
   * @return Returns the only existing instance of {@link SessionProvider}.
   */
  public static SessionProvider getInstance()
  {
    return instance;
  }
  
  /**
   * Provides new session for the specified {@link ProtocolSession} from MINA 
   * framework.
   * 
   * @param minaSession - the specified {@link ProtocolSession} from MINA 
   *        framework.
   * 
   * @return Returns new session created for 
   *         the specified {@link ProtocolSession} from MINA framework.
   */
  public Session getSession(ProtocolSession minaSession)
  {
    if (minaSession == null)
    {
      throw new NullPointerException("Specified minaSession is null!");
    }
    
    Session session = sessions.get(minaSession);
    if (session == null)
    {
      sessions.put(minaSession, new SessionImpl(minaSession));
    }
    return session;
  }
  
  /**
   * Removes the session created for the specified {@link ProtocolSession} 
   * from MINA framework.
   * 
   * @param minaSession - the specified {@link ProtocolSession} from MINA 
   *        framework.
   */
  public void removeSession(ProtocolSession minaSession)
  {
    if (minaSession != null)
    {
      sessions.remove(minaSession);
    }
  }
  
  /**
   * Returns existing implementation of {@link Session} interface 
   * created for the specified {@link ProtocolSession} from MINA 
   * framework.
   *  
   * @param minaSession - the specified {@link ProtocolSession} from MINA 
   *        framework.
   *        
   * @return Returns existing implementation of {@link Session} interface 
   *         created for the specified {@link ProtocolSession} from MINA 
   *         framework.
   */
  SessionImpl getSessionImpl(ProtocolSession minaSession)
  {
    return (SessionImpl)getSession(minaSession);
  }
}