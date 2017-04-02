/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.client.update;


import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;
import net.java.dante.gui.common.games.GameData;
import net.java.dante.gui.common.games.GameState;

/**
 * Class representing message containing changed game's data.
 *
 * @author M.Olszewski
 */
public class GameDataChangedMessage extends ClientUpdateMessage
{
  /** Changed game's data. */
  private ClientGameData data;


  /**
   * Creates instance of {@link GameDataChangedMessage} class.
   * Do not create instances of {@link GameDataChangedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public GameDataChangedMessage()
  {
    super();
  }


  /**
   * Creates instance of {@link GameDataChangedMessage} class containing
   * data from specified {@link GameData} object.
   *
   * @param gameData - the specified {@link GameData} object.
   */
  public GameDataChangedMessage(GameData gameData)
  {
    if (gameData == null)
    {
      throw new NullPointerException("Specified gameData is null!");
    }

    data = new ClientGameData(gameData);
  }


  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    int gameId  = reader.readInt();
    GameState state = GameState.values()[reader.readInt()];
    int maxClientsCount = reader.readInt();
    int[] clientsIds = reader.readIntArray();
    data = new ClientGameData(gameId, state, maxClientsCount, clientsIds);
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    if (data == null)
    {
      throw new IllegalStateException("IllegalState in GameDataChangedMessage: object is read only!");
    }

    writer.writeInt(data.getGameId());
    writer.writeInt(data.getState().ordinal());
    writer.writeInt(data.getMaxClientsCount());
    writer.writeIntArray(data.getClientsIdsArray());
  }

  /**
   * Gets {@link GameData} object stored by this message.
   *
   * @return Returns {@link GameData} object stored by this message.
   */
  public GameData getGameData()
  {
    return data;
  }


  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + ((data == null) ? 0 : data.hashCode());

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof GameDataChangedMessage))
    {
      final GameDataChangedMessage other = (GameDataChangedMessage) object;
      equal = ((data == null)? (other.data == null) : (data.equals(other.data)));
    }
    return equal;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.client.update.ClientUpdateMessage#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[data=" + data + "]");
  }
}