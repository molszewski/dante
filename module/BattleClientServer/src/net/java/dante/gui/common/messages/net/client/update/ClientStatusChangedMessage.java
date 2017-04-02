/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.client.update;

import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;

/**
 * Class representing status update of specified client (different than
 * current one) in some game.
 *
 * @author M.Olszewski
 */
public class ClientStatusChangedMessage extends ClientUpdateMessage
{
  /** Client's identifier. */
  private int clientId;
  /** Game's identifier. */
  private int gameId;
  /** Client's status. */
  private boolean status;
  /** Indicates whether this object is read only or not. */
  private boolean readOnly = false;


  /**
   * Creates instance of {@link ClientStatusChangedMessage} class.
   * Do not create instances of {@link ClientStatusChangedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public ClientStatusChangedMessage()
  {
    super();

    // It is read only
    readOnly = true;
  }

  /**
   * Creates instance of {@link ClientStatusChangedMessage} class with the
   * specified client's identifier, game's identifier and status.
   *
   * @param clientIdentifier - client's identifier denoting client who changed
   *        its status.
   * @param gameIdentifier - game's identifier denoting game for which message
   *        was generated.
   * @param clientStatus - client's current status.
   */
  public ClientStatusChangedMessage(int clientIdentifier, int gameIdentifier,
      boolean clientStatus)
  {
    clientId = clientIdentifier;
    gameId   = gameIdentifier;
    status   = clientStatus;
  }


  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    clientId = reader.readInt();
    gameId   = reader.readInt();
    status   = reader.readBoolean();

    readOnly = false;
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    if (readOnly)
    {
      throw new IllegalStateException("IllegalState in ClientStatusChangedMessage: object is read only!");
    }

    writer.writeInt(clientId);
    writer.writeInt(gameId);
    writer.writeBoolean(status);
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
    result = PRIME * result + gameId;
    result = PRIME * result + (readOnly ? 1231 : 1237);
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
    if (!equal && (object instanceof ClientStatusChangedMessage))
    {
      final ClientStatusChangedMessage other = (ClientStatusChangedMessage) object;
      equal = ((clientId == other.clientId) &&
               (gameId   == other.gameId) &&
               (status   == other.status));
    }
    return equal;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.client.update.ClientUpdateMessage#toString()
   */
  @Override
  public String toString()
  {
    final String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) +
        "; clientId=" + clientId + "; gameId=" + gameId +
        "; status=" + status + "; readOnly=" + readOnly + "]");
  }
}