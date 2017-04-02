/*
 * Created on 2006-08-28
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.receiver;

import net.java.dante.darknet.session.Session;

/**
 * Message indicating that client represented by the specified {@link Session}
 * object connected to server.
 *
 * @author M.Olszewski
 */
public class ClientConnectionMessage extends ClientSessionMessage
{
  /**
   * Creates instance of {@link ClientConnectionMessage} class.
   *
   * @param clientSession - client's session.
   */
  public ClientConnectionMessage(Session clientSession)
  {
    super(clientSession);
  }
}