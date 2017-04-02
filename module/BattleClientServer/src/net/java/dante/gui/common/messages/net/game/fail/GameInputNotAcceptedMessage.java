/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.messages.net.game.fail;

import net.java.dante.gui.common.games.FailureReason;

/**
 * Class representing message with reason of not accepting game input.
 * 
 * @author M.Olszewski
 */
public class GameInputNotAcceptedMessage extends GameRequestFailedMessage
{
  /**
   * Creates instance of {@link GameInputNotAcceptedMessage} class.
   * Do not create instances of {@link GameInputNotAcceptedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public GameInputNotAcceptedMessage()
  {
    super();
  }
  
  /**
   * Creates instance of {@link GameInputNotAcceptedMessage} class with the 
   * specified failure's reason.
   *
   * @param failureReason - the specified failure's reason.
   */
  public GameInputNotAcceptedMessage(FailureReason failureReason)
  {
    super(failureReason);
  }
}