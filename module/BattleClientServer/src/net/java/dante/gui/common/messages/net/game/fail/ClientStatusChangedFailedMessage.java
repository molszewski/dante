/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.messages.net.game.fail;

import net.java.dante.gui.common.games.FailureReason;


/**
 * Class representing message with reason of client's status changed (e.g. 
 * changing status from ready to start to not ready) failure.
 * 
 * @author M.Olszewski
 */
public class ClientStatusChangedFailedMessage extends GameRequestFailedMessage
{
  /**
   * Creates instance of {@link ClientStatusChangedFailedMessage} class.
   * Do not create instances of {@link ClientStatusChangedFailedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public ClientStatusChangedFailedMessage()
  {
    super();
  }
  
  /**
   * Creates instance of {@link ClientStatusChangedFailedMessage} class with the 
   * specified failure's reason.
   *
   * @param failureReason - the specified failure's reason.
   */
  public ClientStatusChangedFailedMessage(FailureReason failureReason)
  {
    super(failureReason);
  }
}