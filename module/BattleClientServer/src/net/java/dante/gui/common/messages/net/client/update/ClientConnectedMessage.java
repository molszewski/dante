/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.messages.net.client.update;

import net.java.dante.gui.common.clients.ClientData;

/**
 * Class representing network message notifying about some other client
 * connection event.
 * 
 * @author M.Olszewski
 */
public class ClientConnectedMessage extends ClientDataUpdateMessage
{
  /**
   * Creates instance of {@link ClientConnectedMessage} class.
   * Do not create instances of {@link ClientConnectedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public ClientConnectedMessage()
  {
    super();
  }
  
  /**
   * Creates instance of {@link ClientConnectedMessage} class containing 
   * data from specified {@link ClientData} object.
   *
   * @param clientData - the specified {@link ClientData} object.
   */
  public ClientConnectedMessage(ClientData clientData)
  {
    super(clientData);
  }
}