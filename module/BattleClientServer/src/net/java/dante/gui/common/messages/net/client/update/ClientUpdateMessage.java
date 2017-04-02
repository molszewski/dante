/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.client.update;

import net.java.dante.darknet.messaging.NetworkMessage;

/**
 * Class used to denote messages that contains updates for clients
 * (e.g. current data of client, current data of game).
 *
 * @author M.Olszewski
 */
public abstract class ClientUpdateMessage extends NetworkMessage
{
  /**
   * Constructor of {@link ClientUpdateMessage} class.
   */
  public ClientUpdateMessage()
  {
    super();
  }
}