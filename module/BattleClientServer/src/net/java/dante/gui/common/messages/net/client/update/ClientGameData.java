/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.client.update;

import java.util.Arrays;

import net.java.dante.gui.common.Const;
import net.java.dante.gui.common.games.GameData;
import net.java.dante.gui.common.games.GameState;




/**
 * Representation of {@link GameData} sent to clients.
 *
 * @author M.Olszewski
 */
class ClientGameData implements GameData
{
  /** Max clients count. */
  private int maxClients;
  /** Game's identifier. */
  private int gameId;
  /** Game's state. */
  private GameState state = GameState.FREE;
  /** Clients connected with this game. */
  private int[] clientsIds = new int[0];


  /**
   * Creates instance of {@link ClientGameData} class from the specified
   * {@link GameData} object.
   *
   * @param gameData - the specified {@link GameData} object.
   */
  ClientGameData(GameData gameData)
  {
    if (gameData == null)
    {
      throw new NullPointerException("Specified gameData is null!");
    }

    gameId = gameData.getGameId();
    state = gameData.getState();
    maxClients = gameData.getMaxClientsCount();

    clientsIds = new int[gameData.getClientsCount()];
    for (int i = 0; i < clientsIds.length; i++)
    {
      clientsIds[i] = gameData.getClientId(i);
    }
  }

  /**
   * Creates instance of {@link ClientGameData} class from the specified
   * parameters.
   *
   * @param gameIdentifier - game's identifier.
   * @param gameState - game's state.
   * @param maxClientsCount - maximum clients count in game.
   * @param clientsIdentifiers - clients identifiers that are in game.
   */
  ClientGameData(int gameIdentifier, GameState gameState, int maxClientsCount,
      int[] clientsIdentifiers)
  {
    if (gameState == null)
    {
      throw new NullPointerException("Specified gameState is null!");
    }
    if (clientsIdentifiers == null)
    {
      throw new NullPointerException("Specified clientsIdentifiers is null!");
    }
    if (maxClientsCount < Const.MINIMUM_CLIENTS_IN_GAME_COUNT)
    {
      throw new IllegalArgumentException("Invalid argument maxClientsCount - it must be greater than or equal to MINIMUM_CLIENTS_COUNT=" + Const.MINIMUM_CLIENTS_IN_GAME_COUNT + "!");
    }

    gameId     = gameIdentifier;
    state      = gameState;
    maxClients = maxClientsCount;
    clientsIds = clientsIdentifiers;
  }

  /**
   * @see net.java.dante.gui.common.games.GameData#getClientId(int)
   */
  public int getClientId(int index)
  {
    return clientsIds[index];
  }

  /**
   * @see net.java.dante.gui.common.games.GameData#getClientsCount()
   */
  public int getClientsCount()
  {
    return clientsIds.length;
  }

  /**
   * @see net.java.dante.gui.common.games.GameData#getGameId()
   */
  public int getGameId()
  {
    return gameId;
  }

  /**
   * @see net.java.dante.gui.common.games.GameData#getMaxClientsCount()
   */
  public int getMaxClientsCount()
  {
    return maxClients;
  }

  /**
   * @see net.java.dante.gui.common.games.GameData#getState()
   */
  public GameState getState()
  {
    return state;
  }

  /**
   * Gets reference to array with clients identifiers.
   *
   * @return Returns reference to array with clients identifiers.
   */
  int[] getClientsIdsArray()
  {
    return clientsIds;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + Arrays.hashCode(clientsIds);
    result = PRIME * result + gameId;
    result = PRIME * result + maxClients;
    result = PRIME * result + state.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof ClientGameData))
    {
      final ClientGameData other = (ClientGameData) object;
      equal = ((gameId == other.gameId) &&
               (maxClients == other.maxClients) &&
               (state == other.state) &&
               (Arrays.equals(clientsIds, other.clientsIds)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[gameId=" + gameId + "; state=" + state +
        "; maxClients=" + maxClients +
        "; clientsIds=" + Arrays.toString(clientsIds) + "]");
  }
}