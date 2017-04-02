/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.messages.net.game.confirm;

/**
 * Class representing message with confirmation that specified game
 * is finished.
 * 
 * @author M.Olszewski
 */
public class GameFinishedMessage extends GameConfirmationMessage
{
  /**
   * Creates instance of {@link GameFinishedMessage} class.
   * Do not create instances of {@link GameFinishedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public GameFinishedMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link GameFinishedMessage} class with the 
   * specified game's identifier.
   *
   * @param gameIdentifier - game's identifier denoting game for which message
   *        was generated.
   */
  public GameFinishedMessage(int gameIdentifier)
  {
    super(gameIdentifier);
  }
}