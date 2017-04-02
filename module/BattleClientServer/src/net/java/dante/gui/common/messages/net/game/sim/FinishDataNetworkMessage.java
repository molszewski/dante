/*
 * Created on 2006-08-27
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.sim;

import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;

/**
 * Class representing {@link net.java.dante.sim.io.FinishData} input data for client.
 *
 * @author M.Olszewski
 */
public class FinishDataNetworkMessage extends SimulationNetworkMessage
{
  /**
   * Creates instance of {@link FinishDataNetworkMessage} class.
   * Do not create instances of {@link FinishDataNetworkMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public FinishDataNetworkMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link SimulationNetworkMessage} class with the
   * specified parameters.
   *
   * @param gameIdenifier the game's identifier.
   */
  public FinishDataNetworkMessage(int gameIdenifier)
  {
    super(gameIdenifier);
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    super.fromPacket(reader);
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    super.toPacket(writer);
  }
}