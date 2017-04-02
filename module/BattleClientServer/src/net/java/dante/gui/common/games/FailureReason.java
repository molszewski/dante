/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.games;



/**
 * Enumeration representing possible reason of some operation's failure.
 *
 * @author M.Olszewski
 */
public enum FailureReason
{
  /** Client is taking part in other game. */
  CLIENT_IN_OTHER_GAME,
  /** Client is taking part in this game already. */
  CLIENT_IN_THIS_GAME,
  /** Client is not taking part in the specified game. */
  CLIENT_NOT_IN_GAME,
  /** There are no free games. */
  NO_FREE_GAMES,
  /** Specified game does not exist. */
  GAME_NOT_EXIST,
  /** There are no free slots for client in specified game. */
  NO_FREE_SLOTS,
  /** The specified game was not created. */
  GAME_NOT_CREATED,
  /** The specified game is not marked as ready to start. */
  GAME_NOT_READY,
  /** The specified game is not marked as ready to start. */
  GAME_NOT_RUNNIG,
  /** The specified game is paused at the moment. */
  GAME_PAUSED,
  /** The specified game is running at the moment. */
  GAME_RUNNING,
  /** The specified game is finished at the moment. */
  GAME_FINISHED,
  /** The specified game input was considered as invalid. */
  GAME_INPUT_INVALID;
}