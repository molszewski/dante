/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.messages.net.game.confirm;

/**
 * Class representing message with confirmation that specified game
 * was abandoned by the client.
 * 
 * @author M.Olszewski
 */
public class GameAbandonedMessage extends GameConfirmationMessage
{
  /**
   * Creates instance of {@link GameAbandonedMessage} class.
   * Do not create instances of {@link GameAbandonedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public GameAbandonedMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link GameAbandonedMessage} class with the 
   * specified game's identifier.
   *
   * @param gameIdentifier - game's identifier denoting game for which message
   *        was generated.
   */
  public GameAbandonedMessage(int gameIdentifier)
  {
    super(gameIdentifier);
  }
}