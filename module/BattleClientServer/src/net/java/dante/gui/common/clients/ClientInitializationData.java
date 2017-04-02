/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.clients;


import net.java.dante.darknet.client.NetworkClient;
import net.java.dante.gui.common.games.GameData;

/**
 * Interface specifying initialization data for {@link NetworkClient}
 * object, containing data for current client, other clients and
 * all games.
 *
 * @author M.Olszewski
 */
public interface ClientInitializationData
{
  /**
   * Gets client's data for client for which this initialization data
   * was generated.
   *
   * @return Returns client's data for client for which this initialization data
   *         was generated.
   */
  ClientData getCurrentClientData();

  /**
   * Gets array with clients data for clients other than one for which this
   * initialization data was generated.
   *
   * @return Returns array with clients data for other clients.
   */
  ClientData[] getClientsData();

  /**
   * Gets array with all games data.
   *
   * @return Returns array with all games data.
   */
  GameData[] getGamesData();

  /**
   * Gets number of games that can be played simultaneously on server.
   *
   * @return Returns number of games that can be played simultaneously
   *         on server.
   */
  int getMaxGames();
}