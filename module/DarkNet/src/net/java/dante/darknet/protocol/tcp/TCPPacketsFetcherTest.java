/*
 * Created on 2006-04-27
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.protocol.tcp;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.java.dante.darknet.common.PrimitivesSizes;
import net.java.dante.darknet.protocol.PacketsFetcher;
import net.java.dante.darknet.protocol.packet.Packet;
import net.java.dante.darknet.util.RandomGenerator;

import junit.extensions.RepeatedTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test class containing various tests for 
 * {@link TCPPacketsFetcher#fetch(ByteBuffer)} method.
 *
 * @author M.Olszewski
 */
public class TCPPacketsFetcherTest extends TestCase
{
  /**
   * Constructs {@link TCPPacketsFetcherTest} object.
   * 
   * @param testName - name of the test.
   */
  public TCPPacketsFetcherTest(String testName)
  {
    super(testName);
  }

  /**
   * Simple test for {@link TCPPacketsFetcher#fetch(ByteBuffer)} method:
   * creates {@link TCPPacket} object and calls 
   * {@link TCPPacketsFetcher#fetch(ByteBuffer)} method with created  
   * {@link TCPPacket} object buffer.
   * Both {@link TCPPacket} objects (created and fetched) are compared
   * afterwards.
   */
  public void testFetchSimple()
  {
    final int userBytesLength = 16;
    TCPPacket packet = new TCPPacket(10, userBytesLength + 16, System.currentTimeMillis());
    packet.writeByteArray(RandomGenerator.randomBytes(userBytesLength - PrimitivesSizes.INT_SIZE));
    packet.updatePacketData();
    
    PacketsFetcher pf = new TCPPacketsFetcher();
    Packet[] fetched = pf.fetch(packet.getBuffer());
    
    assertNotNull(fetched);
    assertEquals(fetched.length, 1);
    
    Packet fetchedPacket = fetched[0];
    assertEquals(fetchedPacket.getSubclassId(), packet.getSubclassId());
    assertEquals(fetchedPacket.getTimestamp(), packet.getTimestamp());
    
    ((TCPPacket)fetchedPacket).updatePacketData();
    fetchedPacket.getBuffer().flip();
    packet.getBuffer().flip();
    assertEquals(fetchedPacket.getBuffer(), packet.getBuffer());
  }
  
  /**
   * A little bit advanced test for {@link TCPPacketsFetcher#fetch(ByteBuffer)} 
   * method: creates {@link TCPPacket} object, gets its buffer and splits it
   * into two {@link ByteBuffer} objects and then calls 
   * {@link TCPPacketsFetcher#fetch(ByteBuffer)} method with both  
   * {@link ByteBuffer} objects. Afterwards both {@link TCPPacket} objects 
   * (created and fetched) are compared.
   */
  public void testFetchTwoBuffers()
  {
    final int userBytesLength = 120;
    final int totalLength = userBytesLength + TCPPacket.PACKET_DATA_SIZE;
    TCPPacket packet = new TCPPacket(10, totalLength, System.currentTimeMillis());
    packet.writeByteArray(RandomGenerator.randomBytes(userBytesLength - PrimitivesSizes.INT_SIZE));
    packet.updatePacketData();
    
    ByteBuffer packetBuffer = packet.getBuffer();
    packetBuffer.flip();
    
    byte[] bytes1 = new byte[totalLength/3];
    byte[] bytes2 = new byte[packetBuffer.limit() - bytes1.length];
    packetBuffer.get(bytes1);
    packetBuffer.get(bytes2);
    
    ByteBuffer buffer1 = ByteBuffer.wrap(bytes1);
    ByteBuffer buffer2 = ByteBuffer.wrap(bytes2);
    
    PacketsFetcher fetcher = new TCPPacketsFetcher();
    Packet[] fetched = fetcher.fetch(buffer1);
    
    assertNotNull(fetched);
    assertEquals(fetched.length, 0);
    
    fetched = fetcher.fetch(buffer2);
    assertNotNull(fetched);
    assertEquals(fetched.length, 1);
    
    Packet fetchedPacket = fetched[0];
    assertEquals(fetchedPacket.getSubclassId(), packet.getSubclassId());
    assertEquals(fetchedPacket.getTimestamp(), packet.getTimestamp());

    ((TCPPacket)fetchedPacket).updatePacketData();
    fetchedPacket.getBuffer().flip();
    packet.getBuffer().flip();
    assertEquals(fetchedPacket.getBuffer(), packet.getBuffer());
  }
  
  /**
   * More advanced test for {@link TCPPacketsFetcher#fetch(ByteBuffer)} 
   * method: creates two {@link TCPPacket} objects, gets its buffers and 
   * splits them in the way that length of the second buffer is split
   * between two buffers.
   * Then {@link TCPPacketsFetcher#fetch(ByteBuffer)} method is called with both  
   * {@link ByteBuffer} objects. Afterwards respective {@link TCPPacket} objects 
   * (created and fetched) are compared.
   */
  public void testFetchTwoPacketsSeparatedLength()
  {
    final int userBytesLength1 = 120;
    final int totalLength1 = userBytesLength1 + TCPPacket.PACKET_DATA_SIZE;
    TCPPacket packet1 = new TCPPacket(10, totalLength1, System.currentTimeMillis());
    packet1.writeByteArray(RandomGenerator.randomBytes(userBytesLength1 - PrimitivesSizes.INT_SIZE));
    packet1.updatePacketData();

    final int userBytesLength2 = 233;
    final int totalLength2 = userBytesLength2 + TCPPacket.PACKET_DATA_SIZE;
    TCPPacket packet2 = new TCPPacket(10, totalLength2, System.currentTimeMillis());
    packet2.writeByteArray(RandomGenerator.randomBytes(userBytesLength2 - PrimitivesSizes.INT_SIZE));
    packet2.updatePacketData();
    
    ByteBuffer buffer1 = packet1.getBuffer();
    ByteBuffer buffer2 = packet2.getBuffer();
    buffer1.flip();
    buffer2.flip();
    
    ByteBuffer bigBuffer = ByteBuffer.allocate(buffer1.limit() + buffer2.limit());
    bigBuffer.put(buffer1);
    bigBuffer.put(buffer2);
    bigBuffer.flip();
    
    byte[] bytes1 = new byte[buffer1.capacity() + PrimitivesSizes.SHORT_SIZE];
    byte[] bytes2 = new byte[buffer2.capacity() - PrimitivesSizes.SHORT_SIZE];
    bigBuffer.get(bytes1);
    bigBuffer.get(bytes2);
    buffer1 = ByteBuffer.wrap(bytes1);
    buffer2 = ByteBuffer.wrap(bytes2);
    
    PacketsFetcher fetcher = new TCPPacketsFetcher();
    Packet[] fetched = fetcher.fetch(buffer1);

    assertNotNull(fetched);
    assertEquals(fetched.length, 1);
    Packet fetchedPacket = fetched[0];
    assertEquals(fetchedPacket.getSubclassId(), packet1.getSubclassId());
    assertEquals(fetchedPacket.getTimestamp(), packet1.getTimestamp());
    ((TCPPacket)fetchedPacket).updatePacketData();
    fetchedPacket.getBuffer().flip();
    packet1.getBuffer().flip();
    assertEquals(fetchedPacket.getBuffer(), packet1.getBuffer());
    
    fetched = fetcher.fetch(buffer2);
    assertNotNull(fetched);
    assertEquals(fetched.length, 1);
    
    fetchedPacket = fetched[0];
    assertEquals(fetchedPacket.getSubclassId(), packet2.getSubclassId());
    assertEquals(fetchedPacket.getTimestamp(), packet2.getTimestamp());
    ((TCPPacket)fetchedPacket).updatePacketData();
    fetchedPacket.getBuffer().flip();
    packet2.getBuffer().flip();
    assertEquals(fetchedPacket.getBuffer(), packet2.getBuffer());
  }
  
  /**
   * The most advanced test for {@link TCPPacketsFetcher#fetch(ByteBuffer)}
   * method: creates random {@link TCPPacket} objects, writes all of them
   * to one big {@link ByteBuffer} object then splits it contents into
   * {@link ByteBuffer} objects with fixed size and calls 
   * {@link TCPPacketsFetcher#fetch(ByteBuffer)} method with each of 
   * this {@link ByteBuffer} objects. All obtained packets are compared
   * with randomly generated ones.
   */
  public void testFetch()
  {
    final int minPackets = 10;
    final int maxPackets = 100;
    final int minBufferSize = 256;
    final int maxBufferSize = 16384;
    
    // Cannot be < 0
    int totalSize = 0;
    Packet[] packets = new Packet[RandomGenerator.randomInt(minPackets, maxPackets)];
    
    for (int i = 0; i < packets.length; i++)
    {
      int bufferSize = RandomGenerator.randomInt(minBufferSize, maxBufferSize);
      TCPPacket tcpPacket = new TCPPacket(i, bufferSize, System.currentTimeMillis() + i);
      byte[] userData = RandomGenerator.randomBytes(bufferSize - 16);
      tcpPacket.write(userData);
      tcpPacket.updatePacketData();
      
      tcpPacket.getBuffer().flip();
      packets[i] = tcpPacket;
      totalSize += bufferSize;
      if (totalSize < 0)
      {
        fail("Test failed - packets exceeded memory limits!");
      }
    }
    
    // Collect all into big buffer!
    ByteBuffer bigBuffer = ByteBuffer.allocate(totalSize);
    
    for (int i = 0; i < packets.length; i++)
    {
      bigBuffer.put(packets[i].getBuffer());
    }
    
    // Split big buffer into chunks with fixed lengthh
    final int fixedBuffersLength = 700;
    bigBuffer.flip();
    
    int chunksSize = bigBuffer.limit()/fixedBuffersLength;
    if ((bigBuffer.limit() % fixedBuffersLength) != 0)
    {
      chunksSize++;
    }
    
    List<ByteBuffer> chunksList = new ArrayList<ByteBuffer>(chunksSize);
    {
      for (int i = 0; i < (chunksSize - 1); i++)
      {
        byte[] chunk = new byte[fixedBuffersLength];
        bigBuffer.get(chunk);
        chunksList.add(ByteBuffer.wrap(chunk));
      }
      byte[] chunk = new byte[bigBuffer.limit() - bigBuffer.position()];
      bigBuffer.get(chunk);
      chunksList.add(ByteBuffer.wrap(chunk));
    }
    
    // OK - buffers chunks are ready!
    PacketsFetcher fetcher = new TCPPacketsFetcher();
    List<Packet> obtainedList = new ArrayList<Packet>(packets.length);
    for (int i = 0, size = chunksList.size(); i < size; i++)
    {
      ByteBuffer chunk = chunksList.get(i);
      Packet[] obtained = fetcher.fetch(chunk);
      for (int j = 0; j < obtained.length; j++)
      {
        obtainedList.add(obtained[j]);
      }
    }
    // OK - obtained packets are ready to compare!
    if (obtainedList.size() == packets.length)
    {
      for (int i = 0, size = obtainedList.size(); i < size; i++)
      {
        Packet packet = packets[i];
        Packet obtainedPacket = obtainedList.get(i);
        
        assertEquals(packet.getSubclassId(), obtainedPacket.getSubclassId());
        assertEquals(packet.getTimestamp(), obtainedPacket.getTimestamp());
        assertEquals(packet.getBuffer(), obtainedPacket.getBuffer());
      }
    }
    else
    {
      fail("Obtained packets size != original packets size.");
    }
  }
  
  /**
   * Creates and returns custom test suite containing e.g. repeated tests.
   *  
   * @return Returns custom test suite. 
   */
  public static Test suite()
  {
    TestSuite mySuite = new TestSuite();
    mySuite.addTest(new TCPPacketsFetcherTest("testFetchSimple"));
    mySuite.addTest(new TCPPacketsFetcherTest("testFetchTwoBuffers"));
    mySuite .addTest(new TCPPacketsFetcherTest("testFetchTwoPacketsSeparatedLength"));
    mySuite.addTest(new RepeatedTest(new TCPPacketsFetcherTest("testFetch"), 100));
    return mySuite;
  }

}