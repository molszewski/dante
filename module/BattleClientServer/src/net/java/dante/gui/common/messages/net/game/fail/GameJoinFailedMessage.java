/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.messages.net.game.fail;

import net.java.dante.gui.common.games.FailureReason;


/**
 * Class representing message with reason of joining game failure.
 * 
 * @author M.Olszewski
 */
public class GameJoinFailedMessage extends GameRequestFailedMessage
{
  /**
   * Creates instance of {@link GameJoinFailedMessage} class.
   * Do not create instances of {@link GameJoinFailedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public GameJoinFailedMessage()
  {
    super();
  }
  
  /**
   * Creates instance of {@link GameJoinFailedMessage} class with the 
   * specified failure's reason.
   *
   * @param failureReason - the specified failure's reason.
   */
  public GameJoinFailedMessage(FailureReason failureReason)
  {
    super(failureReason);
  }
}