/*
 * Created on 2006-08-26
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.receiver;

import net.java.dante.receiver.ReceiverMessage;

/**
 * Message indicating that initialization of {@link net.java.dante.gui.common.games.Game} with
 * the specified identifier has failed.
 *
 * @author M.Olszewski
 */
public class GameInitializationFailed implements ReceiverMessage
{
  /** The game's identifier. */
  private Integer gameId;
  /** Failure's cause - only for debugging reason. */
  private Throwable cause;


  /**
   * Creates instance of {@link GameInitializationFailed} class with
   * the specified game's identifier.
   *
   * @param gameIdentifier - the specified game's identifier.
   * @param failureCause - {@link Throwable} object defining failure's cause.
   */
  public GameInitializationFailed(Integer gameIdentifier, Throwable failureCause)
  {
    if (gameIdentifier == null)
    {
      throw new NullPointerException("Specified gameIdentifier is null!");
    }
    if (failureCause == null)
    {
      throw new NullPointerException("Specified failureCause is null!");
    }

    gameId = gameIdentifier;
    cause  = failureCause;
  }


  /**
   * Gets the game's identifier.
   *
   * @return Returns the game's identifier.
   */
  public Integer getGameId()
  {
    return gameId;
  }

  /**
   * Gets the failure's cause.
   *
   * @return Returns the failure's cause.
   */
  public Throwable getFailureCause()
  {
    return cause;
  }
}