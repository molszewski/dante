/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common;

/**
 * Class containing set of constants for server and client logic.
 *
 * @author M.Olszewski
 */
public final class Const
{
  /** Minimum number of clients in one game. */
  public static final int MINIMUM_CLIENTS_IN_GAME_COUNT = 2;
  /** Minimum number of maximum games that can be run on server.*/
  public static final int MINIMUM_MAX_GAMES_COUNT = 1;


  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private Const()
  {
    // Intentionally left empty.
  }
}