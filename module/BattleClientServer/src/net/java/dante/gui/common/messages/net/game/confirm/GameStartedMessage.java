/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.messages.net.game.confirm;

/**
 * Class representing message with confirmation that specified game
 * is started.
 * 
 * @author M.Olszewski
 */
public class GameStartedMessage extends GameConfirmationMessage
{
  /**
   * Creates instance of {@link GameStartedMessage} class.
   * Do not create instances of {@link GameStartedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public GameStartedMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link GameStartedMessage} class with the 
   * specified game's identifier.
   *
   * @param gameIdentifier - game's identifier denoting game for which message
   *        was generated.
   */
  public GameStartedMessage(int gameIdentifier)
  {
    super(gameIdentifier);
  }
}