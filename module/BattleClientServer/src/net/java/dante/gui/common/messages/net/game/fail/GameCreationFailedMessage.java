/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.messages.net.game.fail;

import net.java.dante.gui.common.games.FailureReason;


/**
 * Class representing message with reason of game creation failure.
 * 
 * @author M.Olszewski
 */
public class GameCreationFailedMessage extends GameRequestFailedMessage
{
  /**
   * Creates instance of {@link GameCreationFailedMessage} class.
   * Do not create instances of {@link GameCreationFailedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public GameCreationFailedMessage()
  {
    super();
  }
  
  /**
   * Creates instance of {@link GameCreationFailedMessage} class with the 
   * specified failure's reason.
   *
   * @param failureReason - the specified failure's reason.
   */
  public GameCreationFailedMessage(FailureReason failureReason)
  {
    super(failureReason);
  }
}