/*
 * Created on 2006-05-22
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.session;

import net.java.dante.darknet.messaging.NetworkMessage;

/**
 * Simple implementation of {@link net.java.dante.darknet.session.SessionHandler}
 * interface, containing empty stubs of all methods.
 *
 * @author M.Olszewski
 */
public class SessionAdapter implements SessionHandler
{
  /** 
   * @see net.java.dante.darknet.session.SessionHandler#sessionCreated(net.java.dante.darknet.session.Session)
   */
  public void sessionCreated(Session session)
  {
    // Intentionally left empty.
  }

  /** 
   * @see net.java.dante.darknet.session.SessionHandler#sessionOpened(net.java.dante.darknet.session.Session)
   */
  public void sessionOpened(Session session)
  {
    // Intentionally left empty.
  }

  /** 
   * @see net.java.dante.darknet.session.SessionHandler#sessionClosed(net.java.dante.darknet.session.Session)
   */
  public void sessionClosed(Session session)
  {
    // Intentionally left empty.
  }

  /** 
   * @see net.java.dante.darknet.session.SessionHandler#exceptionCaught(net.java.dante.darknet.session.Session, java.lang.Throwable)
   */
  public void exceptionCaught(Session session, Throwable cause)
  {
    // Intentionally left empty.
  }

  /** 
   * @see net.java.dante.darknet.session.SessionHandler#messageReceived(net.java.dante.darknet.session.Session, net.java.dante.darknet.messaging.NetworkMessage)
   */
  public void messageReceived(Session session, NetworkMessage message)
  {
    // Intentionally left empty.
  }

  /** 
   * @see net.java.dante.darknet.session.SessionHandler#messageSent(net.java.dante.darknet.session.Session, net.java.dante.darknet.messaging.NetworkMessage)
   */
  public void messageSent(Session session, NetworkMessage message)
  {
    // Intentionally left empty.
  }
}