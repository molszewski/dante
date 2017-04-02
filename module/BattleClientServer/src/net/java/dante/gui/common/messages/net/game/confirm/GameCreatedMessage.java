/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.messages.net.game.confirm;

/**
 * Class representing message with confirmation of new game creation.
 * 
 * @author M.Olszewski
 */
public class GameCreatedMessage extends GameConfirmationMessage
{
  /**
   * Creates instance of {@link GameCreatedMessage} class.
   * Do not create instances of {@link GameCreatedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public GameCreatedMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link GameCreatedMessage} class with the 
   * specified game's identifier.
   *
   * @param gameIdentifier - game's identifier denoting game for which message
   *        was generated.
   */
  public GameCreatedMessage(int gameIdentifier)
  {
    super(gameIdentifier);
  }
}