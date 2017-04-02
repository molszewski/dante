/*
 * Created on 2006-08-21
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.games;

/**
 * Marker interface denoting input data for {@link net.java.dante.gui.common.games.Game} instances.
 *
 * @author M.Olszewski
 */
public interface GameInputData
{
  /**
   * Gets identifier of game for which this input data was generated.
   *
   * @return Returns identifier of game for which this input data was generated.
   */
  Integer getGameId();
}