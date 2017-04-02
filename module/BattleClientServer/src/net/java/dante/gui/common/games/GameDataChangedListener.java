/*
 * Created on 2006-08-21
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.games;

/**
 * Interface representing listener which methods are invoked when any 
 * changes were performed by {@link GamesManager} 
 * in the specified {@link GameData} object.
 *
 * @author M.Olszewski
 */
public interface GameDataChangedListener
{
  /**
   * Method invoked when any changes were performed by {@link GamesManager}
   * to the specified {@link GameData} object.
   * 
   * @param gameData - changed {@link GameData} object.
   */
  void gameDataChanged(GameData gameData);
}