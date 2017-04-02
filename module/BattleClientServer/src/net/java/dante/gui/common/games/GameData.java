/*
 * Created on 2006-08-21
 *
 * @author M.Olszewski 
 */


package net.java.dante.gui.common.games;

/**
 * Interface representing data for games.
 * 
 * @author M.Olszewski
 */
public interface GameData
{
  /**
   * Gets this game identifier.
   * 
   * @return Returns this game identifier.
   */
  int getGameId();
  
  /**
   * Gets this game's state.
   * 
   * @return Returns this game's state.
   */
  GameState getState();
  
  /**
   * Gets number of clients connected to this game.
   * 
   * @return Returns number of clients connected to this game.
   */
  int getClientsCount();
  
  /**
   * Gets identifier of client at the specified index.
   * 
   * @param index - the specified index.
   * 
   * @return Returns the identifier of client at the specified index.
   */
  int getClientId(int index);
  
  /**
   * Gets maximum number of clients that can join this game.
   * 
   * @return Returns maximum number of clients that can join this game.
   */
  int getMaxClientsCount();
}