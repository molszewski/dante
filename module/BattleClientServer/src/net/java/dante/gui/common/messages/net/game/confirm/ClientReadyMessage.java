/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.messages.net.game.confirm;

/**
 * Class representing message with confirmation that specified client is 
 * prepared for game start.
 * 
 * @author M.Olszewski
 */
public class ClientReadyMessage extends GameConfirmationMessage
{
  /**
   * Creates instance of {@link ClientReadyMessage} class.
   * Do not create instances of {@link ClientReadyMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public ClientReadyMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link ClientReadyMessage} class with the 
   * specified game's identifier.
   *
   * @param gameIdentifier - game's identifier denoting game for which message
   *        was generated.
   */
  public ClientReadyMessage(int gameIdentifier)
  {
    super(gameIdentifier);
  }
}