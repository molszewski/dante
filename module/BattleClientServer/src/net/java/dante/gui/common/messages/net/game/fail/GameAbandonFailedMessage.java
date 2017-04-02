/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.messages.net.game.fail;

import net.java.dante.gui.common.games.FailureReason;


/**
 * Class representing message with reason of leaving game failure.
 * 
 * @author M.Olszewski
 */
public class GameAbandonFailedMessage extends GameRequestFailedMessage
{
  /**
   * Creates instance of {@link GameAbandonFailedMessage} class.
   * Do not create instances of {@link GameAbandonFailedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public GameAbandonFailedMessage()
  {
    super();
  }
  
  /**
   * Creates instance of {@link GameAbandonFailedMessage} class with the 
   * specified failure's reason.
   *
   * @param failureReason - the specified failure's reason.
   */
  public GameAbandonFailedMessage(FailureReason failureReason)
  {
    super(failureReason);
  }
}