/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.messages.net.game.fail;

import net.java.dante.gui.common.games.FailureReason;


/**
 * Class representing message with reason of starting game failure.
 * 
 * @author M.Olszewski
 */
public class GameStartFailedMessage extends GameRequestFailedMessage
{
  /**
   * Creates instance of {@link GameStartFailedMessage} class.
   * Do not create instances of {@link GameStartFailedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public GameStartFailedMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link GameStartFailedMessage} class with the 
   * specified failure's reason.
   *
   * @param failureReason - the specified failure's reason.
   */
  public GameStartFailedMessage(FailureReason failureReason)
  {
    super(failureReason);
  }
}