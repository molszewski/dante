/*
 * Created on 2006-08-27
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.sim;

import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;

/**
 * Base class for all network messages containing input for
 * {@link net.java.dante.sim.Simulation} instances.
 *
 * @author M.Olszewski
 */
public abstract class SimulationNetworkMessage extends NetworkMessage
{
  /** Game's identifier. */
  private int gameId;
  /** Indicates whether this object is read only or not. */
  private boolean readOnly = false;


  /**
   * Creates instance of {@link SimulationNetworkMessage} class.
   * Do not create instances of {@link SimulationNetworkMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public SimulationNetworkMessage()
  {
    readOnly = true;
  }


  /**
   * Creates instance of {@link SimulationNetworkMessage} class with the
   * specified parameters.
   *
   * @param gameIdenifier the game's identifier.
   */
  public SimulationNetworkMessage(int gameIdenifier)
  {
    gameId  = gameIdenifier;
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    gameId   = reader.readInt();
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
      throw new IllegalStateException("IllegalState in SimulationNetworkMessage: object is read only!");
    }

    writer.writeInt(gameId);
  }

  /**
   * Gets the game's identifier.
   *
   * @return Returns the game's identifier.
   */
  public int getGameId()
  {
    return gameId;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + gameId;

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof SimulationNetworkMessage))
    {
      final SimulationNetworkMessage other = (SimulationNetworkMessage) object;
      equal = (gameId == other.gameId);
    }
    return equal;
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[gameId=" + gameId + "]");
  }
}