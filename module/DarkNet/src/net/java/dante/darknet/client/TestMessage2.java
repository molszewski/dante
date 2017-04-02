/*
 * Created on 2006-05-08
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.client;

import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;
import net.java.dante.darknet.util.RandomGenerator;

/**
 * Test message number two.
 *
 * @author M.Olszewski
 */
public final class TestMessage2 extends NetworkMessage
{
  private String myString = RandomGenerator.randomString(100, 200);



  /**
   * Creates instance of {@link TestMessage2} class.
   */
  public TestMessage2()
  {
    // Intentionally left empty.
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    myString = reader.readString();
  }

  /**
   * @return Returns stored string.
   */
  String getStr()
  {
    return myString;
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    writer.writeString(myString);
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return "TestMessage2: " + super.toString() + " content: " + myString;
  }
}