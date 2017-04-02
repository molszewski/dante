/*
 * Created on 2006-08-28
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.receiver;


import net.java.dante.darknet.session.Session;
import net.java.dante.receiver.ReceiverMessage;

/**
 * Message connected with clients represented as {@link Session} objects.
 *
 * @author M.Olszewski
 */
public abstract class ClientSessionMessage implements ReceiverMessage
{
  /** Client's session. */
  private Session session;


  /**
   * Creates instance of {@link ClientSessionMessage} class.
   *
   * @param clientSession - client's session.
   */
  public ClientSessionMessage(Session clientSession)
  {
    if (clientSession == null)
    {
      throw new NullPointerException("Specified clientSession is null!");
    }

    session = clientSession;
  }


  /**
   * Gets client's session.
   *
   * @return Returns client's session.
   */
  public Session getSession()
  {
    return session;
  }
}