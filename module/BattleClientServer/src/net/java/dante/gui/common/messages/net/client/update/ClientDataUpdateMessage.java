/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.client.update;


import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;
import net.java.dante.gui.common.clients.ClientData;

/**
 * Class representing network message notifying about change in specified
 * client data.
 *
 * @author M.Olszewski
 */
public abstract class ClientDataUpdateMessage extends ClientUpdateMessage
{
  /** Connected client's data. */
  private ConnectedClientData data;


  /**
   * Creates instance of {@link ClientDataUpdateMessage} class.
   * Do not create instances of {@link ClientDataUpdateMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public ClientDataUpdateMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link ClientDataUpdateMessage} class containing
   * data from specified {@link ClientData} object.
   *
   * @param clientData - the specified {@link ClientData} object.
   */
  public ClientDataUpdateMessage(ClientData clientData)
  {
    if (clientData == null)
    {
      throw new NullPointerException("Specified clientData is null!");
    }

    data = new ConnectedClientData(clientData);
  }


  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    int clientId = reader.readInt();
    data = new ConnectedClientData(clientId);
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    if (data == null)
    {
      throw new IllegalStateException("IllegalState in ClientDataUpdateMessage: object is read only!");
    }

    writer.writeInt(data.getClientId());
  }

  /**
   * Gets {@link ClientData} object stored by this message.
   *
   * @return Returns {@link ClientData} object stored by this message.
   */
  public ClientData getClientData()
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
    if (!equal && (object instanceof ClientDataUpdateMessage))
    {
      final ClientDataUpdateMessage other = (ClientDataUpdateMessage) object;
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