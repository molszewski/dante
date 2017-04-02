/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.client;

import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;

/**
 * Class representing message that no free slots are available, so client cannot
 * be connected.
 *
 * @author M.Olszewski
 */
public class NoFreeClientsSlotsMessage extends NetworkMessage
{
  /**
   * Creates instance of {@link NoFreeClientsSlotsMessage} class.
   */
  public NoFreeClientsSlotsMessage()
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