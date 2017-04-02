/*
 * Created on 2006-04-27
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.protocol.tcp;

import java.nio.ByteBuffer;
import java.util.Random;

import net.java.dante.darknet.protocol.packet.Packet;

import junit.framework.TestCase;

/**
 * Test class containing test for
 * {@link TCPPacketsSeparator#separate(Packet)} method.
 *
 * @author M.Olszewski
 */
public class TCPPacketsSeparatorTest extends TestCase
{
  /**
   * Test method for {@link TCPPacketsSeparator#separate(Packet)}.
   */
  public final void testSeparate()
  {
    Packet p = new TCPPacket(1);
    Random rand = new Random();

    byte byteVar = (byte)rand.nextInt();
    char charVar = (char)rand.nextInt();
    short shortVar = (short)rand.nextInt();
    int intVar = rand.nextInt();
    long longVar = rand.nextLong();
    float floatVar = rand.nextFloat();
    double doubleVar = rand.nextDouble();

    p.writeByte(byteVar);
    p.writeChar(charVar);
    p.writeShort(shortVar);
    p.writeInt(intVar);
    p.writeLong(longVar);
    p.writeFloat(floatVar);
    p.writeDouble(doubleVar);

    TCPPacketsSeparator separator = new TCPPacketsSeparator();
    ByteBuffer[] buffers = separator.separate(p);

    assertNotNull(buffers);
    assertEquals(buffers.length, 1);

    ByteBuffer buffer = buffers[0];
    buffer.flip();
    buffer.position(16);

    assertEquals(p.getBuffer(), buffer);
    assertEquals(byteVar, buffer.get());
    assertEquals(charVar, buffer.asCharBuffer().get());
    buffer.position(buffer.position() + 2);
    assertEquals(shortVar, buffer.asShortBuffer().get());
    buffer.position(buffer.position() + 2);
    assertEquals(intVar, buffer.asIntBuffer().get());
    buffer.position(buffer.position() + 4);
    assertEquals(longVar, buffer.asLongBuffer().get());
    buffer.position(buffer.position() + 8);
    assertEquals(Float.floatToIntBits(floatVar),
                 Float.floatToIntBits(buffer.asFloatBuffer().get()));
    buffer.position(buffer.position() + 4);
    assertEquals(Double.doubleToLongBits(doubleVar),
                 Double.doubleToLongBits(buffer.asDoubleBuffer().get()));
    buffer.position(buffer.position() + 8);
    assertEquals(buffer.position(), buffer.limit());
  }
}