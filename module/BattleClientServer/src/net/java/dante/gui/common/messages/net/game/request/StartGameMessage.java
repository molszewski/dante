/*
 * Created on 2006-08-28
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.request;

import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;

/**
 * Class representing message with request to start game in which
 * client is taking part.
 *
 * @author M.Olszewski
 */
public class StartGameMessage extends GameRequestMessage
{
  /**
   * Creates instance of {@link StartGameMessage} class.
   */
  public StartGameMessage()
  {
    super();
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    // Intentionally left empty.
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    // Intentionally left empty.
  }
}