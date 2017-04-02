/*
 * Created on 2006-05-04
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.protocol.tcp;

import java.nio.ByteBuffer;
import java.util.Arrays;

import net.java.dante.darknet.common.PrimitivesSizes;
import net.java.dante.darknet.util.RandomGenerator;

import junit.framework.TestCase;

/**
 * Test class containing various tests for 
 * {@link TCPPacket} methods.
 *
 * @author M.Olszewski
 */
public class TCPPacketTest extends TestCase
{
  /**
   * Test for {@link TCPPacket#rewind()} method.
   */
  public void testRewind()
  {
    final int subclassId = 100;
    TCPPacket packet = new TCPPacket(subclassId);
    packet.rewind();
    ByteBuffer buffer = packet.getBuffer();
    assertTrue(buffer.position() == TCPPacket.PACKET_DATA_SIZE);
  }

  /**
   * Test for {@link TCPPacket#updatePacketData()} method.
   */
  public void testUpdatePacketData()
  {
    final int subclassId = 1123;
    final int intArrayLen = 100;
    final int totalSize = intArrayLen * PrimitivesSizes.INT_SIZE + TCPPacket.PACKET_DATA_SIZE + PrimitivesSizes.INT_SIZE;
    TCPPacket packet = new TCPPacket(subclassId, totalSize, System.currentTimeMillis());
    int[] ints = RandomGenerator.randomInts(intArrayLen);
    packet.writeIntArray(ints);
    packet.updatePacketData();
    
    ByteBuffer buffer = packet.getBuffer();
    buffer.flip();
    assertEquals(buffer.getInt(), totalSize - PrimitivesSizes.INT_SIZE);
    assertEquals(buffer.getInt(), subclassId);
    packet.rewind();
    assertTrue(Arrays.equals(ints, packet.readIntArray()));
  }
}