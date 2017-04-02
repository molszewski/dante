/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.gui.common.games;

/**
 * Interface representing game status of client that is 'in' some game.
 *
 * @author M.Olszewski
 */
public interface GameStatusData
{
  /**
   * Gets the identifier of this client.
   * 
   * @return Returns the identifier of this client.
   */
  int getClientId();
  
  /**
   * Gets game's status (ready to game start or not) of this client.
   * 
   * @return Returns game's status (ready to game start or not) of this client.
   */
  boolean getGameStatus();
}