/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.client.update;

import java.util.ArrayList;
import java.util.List;

import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;
import net.java.dante.gui.common.Const;
import net.java.dante.gui.common.clients.ClientData;
import net.java.dante.gui.common.games.GameData;
import net.java.dante.gui.common.games.GameState;

/**
 * Class representing message containing initialization data for each
 * newly connected client with games and clients data.
 *
 * @author M.Olszewski
 */
public class InitDataMessage extends ClientUpdateMessage
{
  /** Data for current client. */
  private ConnectedClientData clientData;
  /** Maximum number of simultaneous games. */
  private int maxGames;
  /** List with {@link ClientGameData} objects. */
  private List<ClientGameData> gamesData;
  /** List with {@link ConnectedClientData} objects for clients different than current one. */
  private List<ConnectedClientData> clientsData;


  /**
   * Creates instance of {@link InitDataMessage} class.
   * Do not create instances of {@link InitDataMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public InitDataMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link InitDataMessage} class containing data obtained
   * from the specified arrays with {@link GameData} and {@link ClientData}
   * objects.
   *
   * @param connectedClientData data for newly connected client.
   * @param clientsDataArray the specified array with
   *        {@link ClientData} objects.
   * @param maximumGames maximum number of simultaneous games.
   * @param gamesDataArray the specified array with {@link GameData} objects.
   */
  public InitDataMessage(ClientData connectedClientData,
                         ClientData[] clientsDataArray,
                         int maximumGames,
                         GameData[] gamesDataArray)
  {
    if (connectedClientData == null)
    {
      throw new NullPointerException("Specified connectedClientData is null!");
    }
    if (clientsDataArray == null)
    {
      throw new NullPointerException("Specified clientsDataArray is null!");
    }
    if (gamesDataArray == null)
    {
      throw new NullPointerException("Specified gamesDataArray is null!");
    }
    if (maximumGames <= Const.MINIMUM_MAX_GAMES_COUNT)
    {
      throw new IllegalArgumentException("Invalid argument maxGamesCount - at least " + Const.MINIMUM_MAX_GAMES_COUNT + " games must be run simultaneously on server!");
    }

    clientData = new ConnectedClientData(connectedClientData);
    // Copy data - do not store references
    clientsData = new ArrayList<ConnectedClientData>(clientsDataArray.length);
    for (int i = 0; i < clientsDataArray.length; i++)
    {
      clientsData.add(new ConnectedClientData(clientsDataArray[i]));
    }

    maxGames  = maximumGames;
    gamesData = new ArrayList<ClientGameData>(gamesDataArray.length);
    for (int i = 0; i < gamesDataArray.length; i++)
    {
      gamesData.add(new ClientGameData(gamesDataArray[i]));
    }
  }


  /**
   * Gets {@link ClientData} object stored by this message for current client.
   *
   * @return Returns {@link ClientData} object for current client.
   */
  public ClientData getCurrentClientData()
  {
    return clientData;
  }

  /**
   * Gets count of {@link ClientData} object stored by this message.
   *
   * @return Returns count of {@link ClientData} object stored by this message.
   */
  public int getClientsDataCount()
  {
    return clientsData.size();
  }

  /**
   * Gets {@link ClientData} object stored by this message from the specified
   * index.
   *
   * @param index the specified index.
   *
   * @return Returns {@link ClientData} object from the specified index.
   */
  public ClientData getClientData(int index)
  {
    return clientsData.get(index);
  }

  /**
   * Gets maximum count of games that can be run on server.
   *
   * @return Returns maximum count of games that can be run on server.
   */
  public int getMaxGamesCount()
  {
    return maxGames;
  }

  /**
   * Gets count of {@link GameData} object stored by this message.
   *
   * @return Returns count of {@link GameData} object stored by this message.
   */
  public int getGamesDataCount()
  {
    return gamesData.size();
  }

  /**
   * Gets {@link GameData} object stored by this message from the specified
   * index.
   *
   * @param index the specified index.
   *
   * @return Returns {@link GameData} object from the specified index.
   */
  public GameData getGameData(int index)
  {
    return gamesData.get(index);
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    // Current client
    int currentClientId = reader.readInt();
    clientData = new ConnectedClientData(currentClientId);

    // Other clients
    int clientsSize = reader.readInt();
    clientsData = new ArrayList<ConnectedClientData>(clientsSize);
    for (int i = 0; i < clientsSize; i++)
    {
      int clientId  = reader.readInt();
      clientsData.add(new ConnectedClientData(clientId));
    }

    // Games
    maxGames = reader.readInt();
    int gamesSize = reader.readInt();
    gamesData = new ArrayList<ClientGameData>(gamesSize);
    for (int i = 0; i < gamesSize; i++)
    {
      int gameId  = reader.readInt();
      GameState state = GameState.values()[reader.readInt()];
      int maxClientsCount = reader.readInt();
      int[] clientsIds = reader.readIntArray();
      gamesData.add(new ClientGameData(gameId, state, maxClientsCount, clientsIds));
    }
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    // Do not try to write packet after calling default constructor
    if (clientData == null)
    {
      throw new IllegalStateException("IllegalState in InitDataMessage: object is read only!");
    }
    if (clientsData == null)
    {
      throw new IllegalStateException("IllegalState in InitDataMessage: object is read only!");
    }
    if (gamesData == null)
    {
      throw new IllegalStateException("IllegalState in InitDataMessage: object is read only!");
    }

    // Current client
    writer.writeInt(clientData.getClientId());

    // Other clients
    int clientsSize = clientsData.size();
    writer.writeInt(clientsSize);
    for (int i = 0; i < clientsSize; i++)
    {
      ConnectedClientData data = clientsData.get(i);
      writer.writeInt(data.getClientId());
    }

    // Games
    writer.writeInt(maxGames);
    int gamesSize = gamesData.size();
    writer.writeInt(gamesSize);
    for (int i = 0; i < gamesSize; i++)
    {
      ClientGameData data = gamesData.get(i);
      writer.writeInt(data.getGameId());
      writer.writeInt(data.getState().ordinal());
      writer.writeInt(data.getMaxClientsCount());
      writer.writeIntArray(data.getClientsIdsArray());
    }
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + maxGames;
    result = PRIME * result + ((clientData == null) ? 0 : clientData.hashCode());
    result = PRIME * result + ((clientsData == null) ? 0 : clientsData.hashCode());
    result = PRIME * result + ((gamesData == null) ? 0 : gamesData.hashCode());

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof InitDataMessage))
    {
      final InitDataMessage other = (InitDataMessage) object;
      equal = (((clientData == null)? (other.clientData == null) : (clientData.equals(other.clientData))) &&
               ((clientsData == null)? (other.clientsData == null) : (clientsData.equals(other.clientsData))) &&
               ((gamesData == null)? (other.gamesData == null) : (gamesData.equals(other.gamesData))) &&
               (maxGames == other.maxGames));
    }

    return equal;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.client.update.ClientUpdateMessage#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[clientData=" + clientData +
        "; clientsData=" + clientsData + "; maxGames=" + maxGames +
        "; gamesData=" + gamesData + "]");
  }
}