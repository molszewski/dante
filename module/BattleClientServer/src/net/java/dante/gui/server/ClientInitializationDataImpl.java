/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.server;

import java.util.Arrays;

import net.java.dante.gui.common.Const;
import net.java.dante.gui.common.clients.ClientData;
import net.java.dante.gui.common.clients.ClientInitializationData;
import net.java.dante.gui.common.games.GameData;




/**
 * Implementation of {@link ClientInitializationData} interface.
 *
 * @author M.Olszewski
 */
class ClientInitializationDataImpl implements ClientInitializationData
{
  /** Data for current client. */
  private ClientData clientData;
  /** Other clients data */
  private ClientData[] clientsData;
  /** Maximum number of simultaneous games. */
  private int maxGames;
  /** All games data. */
  private GameData[] gamesData;


  /**
   * Creates instance of {@link ClientInitializationDataImpl} class with
   * the specified parameters.
   *
   * @param connectedClientData - connected client's data.
   * @param clientsDataArray - other clients data.
   * @param maximumGames maximum number of simultaneous games.
   * @param gamesDataArray - all games data.
   */
  ClientInitializationDataImpl(ClientData connectedClientData,
                               ClientData[] clientsDataArray,
                               int maximumGames,
                               GameData[]   gamesDataArray)
  {
    if (connectedClientData == null)
    {
      throw new NullPointerException("Specified connectedClientData is null!");
    }
    if (clientsDataArray == null)
    {
      throw new NullPointerException("Specified clientsData is null!");
    }
    if (gamesDataArray == null)
    {
      throw new NullPointerException("Specified gamesDataArray is null!");
    }
    if (maximumGames < Const.MINIMUM_MAX_GAMES_COUNT)
    {
      throw new IllegalArgumentException("Invalid argument maxGamesCount - at least " + Const.MINIMUM_MAX_GAMES_COUNT + " games must be run simultaneously on server!");
    }

    clientData  = connectedClientData;
    clientsData = clientsDataArray;
    maxGames    = maximumGames;
    gamesData   = gamesDataArray;
  }


  /**
   * @see net.java.dante.gui.common.clients.ClientInitializationData#getCurrentClientData()
   */
  public ClientData getCurrentClientData()
  {
    return clientData;
  }

  /**
   * @see net.java.dante.gui.common.clients.ClientInitializationData#getClientsData()
   */
  public ClientData[] getClientsData()
  {
    return clientsData;
  }

  /**
   * @see net.java.dante.gui.common.clients.ClientInitializationData#getMaxGames()
   */
  public int getMaxGames()
  {
    return maxGames;
  }

  /**
   * @see net.java.dante.gui.common.clients.ClientInitializationData#getGamesData()
   */
  public GameData[] getGamesData()
  {
    return gamesData;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + clientData.hashCode();
    result = PRIME * result + maxGames;
    result = PRIME * result + Arrays.hashCode(clientsData);
    result = PRIME * result + Arrays.hashCode(gamesData);

    return result;
  }


  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof ClientInitializationDataImpl))
    {
      final ClientInitializationDataImpl other = (ClientInitializationDataImpl) object;
      equal = ((clientData.equals(other.clientData)) &&
               (Arrays.equals(clientsData, other.clientsData)) &&
               (maxGames == other.maxGames) &&
               (Arrays.equals(gamesData, other.gamesData)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[clientData=" + clientData +
        "; clientsData=" + Arrays.toString(clientsData) +
        "; maxGames=" + maxGames +
        "; gamesData=" + Arrays.toString(gamesData) + "]");
  }
}