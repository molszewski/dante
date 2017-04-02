/*
 * Created on 2006-08-27
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.sim;


import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;
import net.java.dante.sim.io.TimeSyncData;

/**
 * Class wrapping {@link TimeSyncData} object to transport it through
 * the network.
 *
 * @author M.Olszewski
 */
public class TimeSyncNetworkMessage extends SimulationNetworkMessage
{
  /** Wrapped {@link TimeSyncData} object. */
  private TimeSyncData sync;


  /**
   * Creates instance of {@link TimeSyncNetworkMessage} class.
   * Do not create instances of {@link TimeSyncNetworkMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public TimeSyncNetworkMessage()
  {
    super();
  }


  /**
   * Creates instance of {@link TimeSyncNetworkMessage} class with the
   * specified parameters.
   *
   * @param gameIdenifier the game's identifier.
   * @param syncData wrapped {@link TimeSyncData}.
   */
  public TimeSyncNetworkMessage(int gameIdenifier, TimeSyncData syncData)
  {
    super(gameIdenifier);

    if (syncData == null)
    {
      throw new NullPointerException("Specified syncData is null!");
    }

    sync    = syncData;
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    super.fromPacket(reader);

    int groupId = reader.readInt();
    long time   = reader.readLong();

    sync = new TimeSyncData(groupId, time);
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    super.toPacket(writer);

    if (sync == null)
    {
      throw new IllegalStateException("IllegalState in TimeSyncNetworkMessage: object is read only!");
    }

    writer.writeInt(sync.getGroupId());
    writer.writeLong(sync.getTime());
  }

  /**
   * Gets the wrapped {@link TimeSyncData} object.
   *
   * @return Returns the wrapped {@link TimeSyncData} object.
   */
  public TimeSyncData getSync()
  {
    return sync;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 31;

    int result = super.hashCode();
    result = PRIME * result + sync.hashCode();

    return result;
  }


  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof TimeSyncNetworkMessage))
    {
      final TimeSyncNetworkMessage other = (TimeSyncNetworkMessage) object;
      equal = ((sync == null)? (other.sync == null) : sync.equals(other.sync));
    }
    return equal;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#toString()
   */
  @Override
  public String toString()
  {
    final String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) +
        "; timeSync=" + sync + "]");
  }
}