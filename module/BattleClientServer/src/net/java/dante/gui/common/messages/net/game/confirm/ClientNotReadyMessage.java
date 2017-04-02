/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.messages.net.game.confirm;

/**
 * Class representing message with confirmation that specified client is not
 * prepared for game start.
 * 
 * @author M.Olszewski
 */
public class ClientNotReadyMessage extends GameConfirmationMessage
{
  /**
   * Creates instance of {@link ClientNotReadyMessage} class.
   * Do not create instances of {@link ClientNotReadyMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public ClientNotReadyMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link ClientNotReadyMessage} class with the 
   * specified game's identifier.
   *
   * @param gameIdentifier - game's identifier denoting game for which message
   *        was generated.
   */
  public ClientNotReadyMessage(int gameIdentifier)
  {
    super(gameIdentifier);
  }
}