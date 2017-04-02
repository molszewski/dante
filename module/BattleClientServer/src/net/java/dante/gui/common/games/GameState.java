/*
 * Created on 2006-08-21
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.games;

/**
 * Enumeration representing possible states of game.
 *
 * @author M.Olszewski
 */
public enum GameState
{
  /** Game is free - it is not created, nor running. */
  FREE,
  /** Game was created - it is waiting to be marked as ready to run. */
  CREATED,
  /** Game was created and marked as ready to start. */
  READY_TO_RUN,
  /** Game is initializing. */
  INITIALIZING,
  /** Game is running. */
  RUNNING,
  /** Game is paused. */
  PAUSED,
  /** Game is finished. */
  FINISHED;
}