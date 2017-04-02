/*
 * Created on 2006-05-07
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.client;

import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;
import net.java.dante.darknet.util.RandomGenerator;

/**
 * Test message number one.
 *
 * @author M.Olszewski
 */
public final class TestMessage extends NetworkMessage
{
  int[] values = RandomGenerator.randomInts(100);
  String content;

  /**
   * Creates instance of {@link TestMessage} class.
   */
  public TestMessage()
  {
    StringBuilder strBld = new StringBuilder('[');
    for (int i = 0; i < values.length; i++)
    {
      strBld.append(values[i]).append(", ");
    }
    strBld.append(']');

    content = strBld.toString();
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    values = reader.readIntArray();
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    writer.writeIntArray(values);
  }

  /**
   * @return Returns all stored values.
   */
  int[] getValues()
  {
    return values;
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return "TestMessage: " + super.toString() + " content: " + content;
  }
}