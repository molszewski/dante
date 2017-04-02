/*
 * Created on 2006-08-28
 *
 * @author M.Olszewski
 */


package net.java.dante.gui.common.messages.net.game.sim;


import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;
import net.java.dante.sim.io.GroupEliminatedSimulationData;

/**
 * Class wrapping {@link GroupEliminatedSimulationData} object to transport it 
 * through the network.
 *
 * @author M.Olszewski
 */
public class GroupEliminatedNetworkMessage extends SimulationNetworkMessage
{
  /** Wrapped {@link GroupEliminatedSimulationData} object. */
  private GroupEliminatedSimulationData eliminatedData;
  

  /**
   * Creates instance of {@link GroupEliminatedNetworkMessage} class.
   * Do not create instances of {@link GroupEliminatedNetworkMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public GroupEliminatedNetworkMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link GroupEliminatedNetworkMessage} class with the
   * specified parameters.
   *
   * @param gameIdenifier the game's identifier.
   * @param groupEliminatedData wrapped {@link GroupEliminatedSimulationData}.
   */
  public GroupEliminatedNetworkMessage(int gameIdenifier,
                                       GroupEliminatedSimulationData groupEliminatedData)
  {
    super(gameIdenifier);
    
    if (groupEliminatedData == null)
    {
      throw new NullPointerException("Specified groupEliminatedData is null!");
    }

    eliminatedData = groupEliminatedData;
  }
 
  
  /**
   * Gets the wrapped {@link GroupEliminatedSimulationData} object.
   *
   * @return Returns the wrapped {@link GroupEliminatedSimulationData} object.
   */
  public GroupEliminatedSimulationData getGroupEliminatedData()
  {
    return eliminatedData;
  }
  
  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    super.fromPacket(reader);

    int groupId = reader.readInt();

    eliminatedData = new GroupEliminatedSimulationData(groupId);
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    super.toPacket(writer);

    if (eliminatedData == null)
    {
      throw new IllegalStateException("IllegalState in GroupEliminatedNetworkMessage: object is read only!");
    }

    writer.writeInt(eliminatedData.getGroupId());
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;

    int result = super.hashCode();
    result = PRIME * result + eliminatedData.hashCode();

    return result;
  }


  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof GroupEliminatedNetworkMessage))
    {
      final GroupEliminatedNetworkMessage other = (GroupEliminatedNetworkMessage) object;
      equal = ((eliminatedData == null)? (other.eliminatedData == null) : eliminatedData.equals(other.eliminatedData));
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
        "; eliminatedData=" + eliminatedData + "]");
  }
}