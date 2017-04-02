/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski
 */


package net.java.dante.gui.common.messages.net.game.confirm;

import java.util.ArrayList;
import java.util.List;

import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;
import net.java.dante.gui.common.games.GameStatusData;

/**
 * Class representing message with confirmation of joining specified game.
 *
 * @author M.Olszewski
 */
public class GameJoinedMessage extends GameConfirmationMessage
{
  /** List with {@link GameStatusData} objects. */
  private List<GameStatusData> statuses = new ArrayList<GameStatusData>();


  /**
   * Creates instance of {@link GameJoinedMessage} class.
   * Do not create instances of {@link GameJoinedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public GameJoinedMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link GameJoinedMessage} class with the
   * specified game's identifier and information about clients status data
   * fetched from specified array with {@link GameStatusData} objects.
   *
   * @param gameIdentifier - game's identifier denoting game for which message
   *        was generated.
   * @param statusesArray - the specified array with {@link GameStatusData}
   *        objects.
   */
  public GameJoinedMessage(int gameIdentifier, GameStatusData[] statusesArray)
  {
    super(gameIdentifier);

    if (statusesArray == null)
    {
      throw new NullPointerException("Specified statusesArray is null!");
    }

    statuses = new ArrayList<GameStatusData>(statusesArray.length);
    for (int i = 0; i < statusesArray.length; i++)
    {
      statuses.add(new InternalClientData(statusesArray[i].getClientId(),
                                          statusesArray[i].getGameStatus()));
    }
  }


  /**
   * @see net.java.dante.gui.common.messages.net.game.confirm.GameConfirmationMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    super.fromPacket(reader);

    int[]     clientIds    = reader.readIntArray();
    boolean[] gameStatuses = reader.readBooleanArray();

    for (int i = 0; i < clientIds.length; i++)
    {
      statuses.add(new InternalClientData(clientIds[i], gameStatuses[i]));
    }
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.confirm.GameConfirmationMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    super.toPacket(writer);

    if (statuses == null)
    {
      throw new IllegalStateException("IllegalState in GameStatusesMessage: object is read only!");
    }

    int size = statuses.size();
    boolean[] gameStatuses = new boolean[size];
    int[]     clientIds    = new int[size];
    for (int i = 0; i < size; i++)
    {
      gameStatuses[i] = statuses.get(i).getGameStatus();
      clientIds[i]    = statuses.get(i).getClientId();
    }

    writer.writeIntArray(clientIds);
    writer.writeBooleanArray(gameStatuses);
  }

  /**
   * Gets number of stored {@link GameStatusData} objects.
   *
   * @return Returns number of stored {@link GameStatusData} objects.
   */
  int getGameStatusDataCount()
  {
    return statuses.size();
  }

  /**
   * Gets {@link GameStatusData} object from the specified index.
   *
   * @param index - the specified index.
   *
   * @return Returns client's data for this game from the specified index.
   */
  GameStatusData getGameStatusData(int index)
  {
    return statuses.get(index);
  }


  /**
   * Class representing internal data for clients (identifiers and statuses).
   *
   * @author M.Olszewski
   */
  private class InternalClientData implements GameStatusData
  {
    /** Client's identifier. */
    private int clientId;
    /** Client's status. */
    private boolean status = false;


    /**
     * Creates instance of {@link InternalClientData} class with the
     * specified parameters.
     *
     * @param clientIdentifier - client's identifier.
     * @param clientStatus - client's status.
     */
    InternalClientData(int clientIdentifier, boolean clientStatus)
    {
      clientId = clientIdentifier;
      status   = clientStatus;
    }


    /**
     * @see net.java.dante.gui.common.games.GameStatusData#getClientId()
     */
    public int getClientId()
    {
      return clientId;
    }

    /**
     * @see net.java.dante.gui.common.games.GameStatusData#getGameStatus()
     */
    public boolean getGameStatus()
    {
      return status;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
      final int PRIME = 37;
      int result = 17;

      result = PRIME * result + clientId;
      result = PRIME * result + (status ? 1231 : 1237);

      return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object)
    {
      boolean equal = (this == object);
      if (!equal && (object instanceof InternalClientData))
      {
        final InternalClientData other = (InternalClientData) object;
        equal = ((clientId == other.clientId) &&
                 (status   == other.status));
      }
      return equal;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      return (getClass() + "[clientId=" + clientId + "; status=" + status + "]");
    }
  }


  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = super.hashCode();

    result = PRIME * result + ((statuses == null) ? 0 : statuses.hashCode());

    return result;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.confirm.GameConfirmationMessage#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof GameJoinedMessage))
    {
      final GameJoinedMessage other = (GameJoinedMessage) object;
      equal = ((statuses == null)? (other.statuses == null) : (statuses.equals(other.statuses)));
    }
    return equal;
  }
}