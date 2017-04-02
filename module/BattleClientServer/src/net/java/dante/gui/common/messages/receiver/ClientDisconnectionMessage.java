package net.java.dante.gui.common.messages.receiver;
import net.java.dante.darknet.session.Session;

/*
 * Created on 2006-08-28
 *
 * @author M.Olszewski
 */

/**
 * Message indicating that client represented by the specified {@link Session}
 * object disconnected from server.
 *
 * @author M.Olszewski
 */
public class ClientDisconnectionMessage extends ClientSessionMessage
{
  /**
   * Creates instance of {@link ClientSessionMessage} class.
   *
   * @param clientSession - client's session.
   */
  public ClientDisconnectionMessage(Session clientSession)
  {
    super(clientSession);
  }
}