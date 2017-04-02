/*
 * Created on 2006-05-07
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.mina;


import net.java.dante.darknet.common.Dbg;
import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.session.SessionHandler;

import org.apache.mina.protocol.ProtocolHandlerAdapter;
import org.apache.mina.protocol.ProtocolSession;


/**
 * DarkNet protocol handler using MINA framework to retrieve all necessary
 * events and pass them to {@link SessionHandler}.
 *
 * @author M.Olszewski
 */
class DarkNetProtocolHandler extends ProtocolHandlerAdapter
{
  /** Client session handler, encapsulated by this object. */
  private SessionHandler handler = null;

  /**
   * Creates {@link DarkNetProtocolHandler} encapsulating specified
   * {@link SessionHandler}.
   *
   * @param sessionHandler - session handler which will be notified about
   *        every event.
   */
  DarkNetProtocolHandler(SessionHandler sessionHandler)
  {
    if (sessionHandler == null)
    {
      throw new NullPointerException("Specified sessionHandler cannot be null!");
    }

    handler = sessionHandler;
  }

  /**
   * @see org.apache.mina.protocol.ProtocolHandlerAdapter#sessionCreated(org.apache.mina.protocol.ProtocolSession)
   */
  @Override
  public void sessionCreated(ProtocolSession minaSession) throws Exception
  {
    super.sessionCreated(minaSession);

    handler.sessionCreated(SessionProvider.getInstance().getSession(minaSession));
  }

  /**
   * @see org.apache.mina.protocol.ProtocolHandlerAdapter#sessionOpened(org.apache.mina.protocol.ProtocolSession)
   */
  @Override
  public void sessionOpened(ProtocolSession minaSession) throws Exception
  {
    super.sessionOpened(minaSession);

    handler.sessionOpened(SessionProvider.getInstance().getSession(minaSession));
  }

  /**
   * @see org.apache.mina.protocol.ProtocolHandlerAdapter#sessionClosed(org.apache.mina.protocol.ProtocolSession)
   */
  @Override
  public void sessionClosed(ProtocolSession minaSession) throws Exception
  {
    super.sessionClosed(minaSession);

    handler.sessionClosed(SessionProvider.getInstance().getSession(minaSession));
    SessionProvider.getInstance().removeSession(minaSession);
  }

  /**
   * @see org.apache.mina.protocol.ProtocolHandlerAdapter#exceptionCaught(org.apache.mina.protocol.ProtocolSession, java.lang.Throwable)
   */
  @Override
  public void exceptionCaught(ProtocolSession minaSession, Throwable cause) throws Exception
  {
    super.exceptionCaught(minaSession, cause);

    handler.exceptionCaught(SessionProvider.getInstance().getSession(minaSession), cause);
  }

  /**
   * @see org.apache.mina.protocol.ProtocolHandlerAdapter#messageReceived(org.apache.mina.protocol.ProtocolSession, java.lang.Object)
   */
  @Override
  public void messageReceived(ProtocolSession minaSession, Object object) throws Exception
  {
    super.messageReceived(minaSession, object);

    if (object instanceof NetworkMessage)
    {
      SessionImpl session = SessionProvider.getInstance().getSessionImpl(minaSession);
      session.increaseReceivedMessagesCount(1);
      handler.messageReceived(session, (NetworkMessage)object);
    }
    else
    {
      if (Dbg.DBGE) Dbg.error("DarkNetProtocolHandler.messageReceived(): received object is not an instance of Message subclass!");
    }
  }

  /**
   * @see org.apache.mina.protocol.ProtocolHandlerAdapter#messageSent(org.apache.mina.protocol.ProtocolSession, java.lang.Object)
   */
  @Override
  public void messageSent(ProtocolSession minaSession, Object object) throws Exception
  {
    super.messageSent(minaSession, object);

    if (object instanceof NetworkMessage)
    {
      SessionImpl session = SessionProvider.getInstance().getSessionImpl(minaSession);
      session.increaseSentMessagesCount(1);
      handler.messageSent(session, (NetworkMessage)object);
    }
    else
    {
      if (Dbg.DBGE) Dbg.error("DarkNetProtocolHandler.messageSent(): sent object is not an instance of Message subclass!");
    }
  }
}