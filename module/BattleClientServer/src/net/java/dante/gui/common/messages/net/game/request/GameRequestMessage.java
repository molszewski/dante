/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.request;

import net.java.dante.darknet.messaging.NetworkMessage;

/**
 * Class used to denote messages that contains requests controlling games
 * (e.g. create game, join game, start game, leave game).
 *
 * @author M.Olszewski
 */
public abstract class GameRequestMessage extends NetworkMessage
{
  /**
   * Constructor of {@link GameRequestMessage} class.
   */
  public GameRequestMessage()
  {
    super();
  }
}