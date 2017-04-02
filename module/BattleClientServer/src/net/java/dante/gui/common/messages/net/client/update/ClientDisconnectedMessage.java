/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.messages.net.client.update;

import net.java.dante.gui.common.clients.ClientData;

/**
 * Class representing network message notifying about some other client
 * disconnection event.
 * 
 * @author M.Olszewski
 */
public class ClientDisconnectedMessage extends ClientDataUpdateMessage
{
  /**
   * Creates instance of {@link ClientDisconnectedMessage} class.
   * Do not create instances of {@link ClientDisconnectedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public ClientDisconnectedMessage()
  {
    super();
  }
  
  /**
   * Creates instance of {@link ClientDisconnectedMessage} class containing 
   * data from specified {@link ClientData} object.
   *
   * @param clientData - the specified {@link ClientData} object.
   */
  public ClientDisconnectedMessage(ClientData clientData)
  {
    super(clientData);
  }
}