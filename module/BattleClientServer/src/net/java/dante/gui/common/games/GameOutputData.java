/*
 * Created on 2006-08-21
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.games;

/**
 * Marker interface denoting output data for {@link net.java.dante.gui.common.games.Game} instances.
 *
 * @author M.Olszewski
 */
public interface GameOutputData
{
  /**
   * Gets identifier of game which generated this output data.
   *
   * @return Returns identifier of game which generated this output data.
   */
  Integer getGameId();
}
