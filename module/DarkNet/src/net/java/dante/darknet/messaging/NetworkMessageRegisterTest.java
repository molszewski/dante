/*
 * Created on 2006-05-04
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.messaging;

import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;

import junit.framework.TestCase;

/**
 * Test class containing various tests for
 * {@link NetworkMessageRegisterImpl} methods.
 *
 * @author M.Olszewski
 */
public final class NetworkMessageRegisterTest extends TestCase
{
  /**
   * Test class number one.
   *
   * @author M.Olszewski
   */
  private static class MessageTest1 extends NetworkMessage
  {
    /**
     * Constructs object of this class with specified type identifier.
     */
    MessageTest1()
    {
      // Intentionally left empty.
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return "MessageTest1";
    }

    /**
     * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
     */
    public void fromPacket(PacketReader r)
    {
      //Intentionally left empty.
    }
    /**
     * @see net.java.dante.darknet.messaging.NetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
     */
    public void toPacket(PacketWriter w)
    {
      //Intentionally left empty.
    }
  }

  /**
   * Test class number two.
   *
   * @author M.Olszewski
   */
  private static class MessageTest2 extends NetworkMessage
  {
    /**
     * Constructs object of this class with specified type identifier.
     *
     * @param type - type identifier.
     */
    MessageTest2(int type)
    {
      // Intentionally left empty.
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return "MessageTest2";
    }

    /**
     * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
     */
    public void fromPacket(PacketReader r)
    {
      //Intentionally left empty.
    }
    /**
     * @see net.java.dante.darknet.messaging.NetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
     */
    public void toPacket(PacketWriter w)
    {
      // Intentionally left empty.
    }
  }

  /**
   * Test for {@link NetworkMessageRegisterImpl#register(Class, int)} method.
   */
  public final void testRegister()
  {
    final int FIRST_TYPE  = 1010;
    final int SECOND_TYPE = FIRST_TYPE + 1;
    NetworkMessagesRegister register = new NetworkMessageRegisterImpl();
    // 1. registration of a message
    try
    {
      register.register(MessageTest1.class, FIRST_TYPE);
    }
    catch (SubclassIdAlreadyRegisteredException e)
    {
      fail("SubclassIdAlreadyRegisteredException thrown - failed.");
    }
    register.clear();

    // 2. register different messages with the same types.
    try
    {
      register.register(MessageTest1.class, FIRST_TYPE);
      register.register(MessageTest2.class, FIRST_TYPE);
      fail("SubclassIdAlreadyRegisteredException not thrown - failed.");
    }
    catch (SubclassIdAlreadyRegisteredException e)
    {
      // Intentionally left empty.
    }
    register.clear();

    // 3. register the same messages with the same types.
    try
    {
      register.register(MessageTest1.class, FIRST_TYPE);
      register.register(MessageTest1.class, FIRST_TYPE);
    }
    catch (SubclassIdAlreadyRegisteredException e)
    {
      fail("SubclassIdAlreadyRegisteredException thrown - failed.");
    }
    register.clear();

    // 4. register the same messages with different types.
    try
    {
      register.register(MessageTest1.class, FIRST_TYPE);
      register.register(MessageTest1.class, SECOND_TYPE);
      fail("SubclassAlreadyRegisteredException not thrown - failed.");
    }
    catch (SubclassAlreadyRegisteredException e)
    {
      // Intentionally left empty.
    }
    register.clear();

    // 5. register different messages with different types.
    try
    {
      register.register(MessageTest1.class, FIRST_TYPE);
      register.register(MessageTest2.class, SECOND_TYPE);
    }
    catch (SubclassIdAlreadyRegisteredException e)
    {
      fail("SubclassIdAlreadyRegisteredException thrown - failed.");
    }
    register.clear();
  }

  /**
   * Test for {@link NetworkMessageRegisterImpl#isRegistered(Class)} and
   * {@link NetworkMessageRegisterImpl#isRegistered(NetworkMessage)} methods.
   */
  public final void testIsRegistered()
  {
    NetworkMessagesRegister register = new NetworkMessageRegisterImpl();
    register.clear();
    register.register(MessageTest1.class, 100);

    assertTrue(register.isRegistered(MessageTest1.class));
    assertFalse(register.isRegistered(MessageTest2.class));

    register.clear();
    NetworkMessage msg = new MessageTest1();
    register.register(msg, 1001);

    assertTrue(register.isRegistered(MessageTest1.class));
    assertFalse(register.isRegistered(MessageTest2.class));
  }

  /**
   * Test for {@link NetworkMessageRegisterImpl#isUsed(int)} method
   */
  public final void testIsUsed()
  {
    NetworkMessagesRegister register = new NetworkMessageRegisterImpl();
    final int subclassId1 = 100;

    register.clear();
    register.register(MessageTest1.class, subclassId1);

    assertTrue(register.isUsed(subclassId1));
    for (int i = Integer.MAX_VALUE; i < Integer.MAX_VALUE; i++)
    {
      if (i != subclassId1)
      {
        assertFalse(register.isUsed(i));
      }
    }
    assertFalse(register.isUsed(Integer.MAX_VALUE));
  }

  /**
   * Test for {@link NetworkMessageRegisterImpl#construct(int)} method.
   */
  public final void testConstruct()
  {
    NetworkMessagesRegister register = new NetworkMessageRegisterImpl();
    final int subclassId = 100;

    register.clear();
    register.register(MessageTest1.class, subclassId);

    NetworkMessage msg = register.construct(subclassId);
    assertNotNull(msg);
    NetworkMessage invMsg = register.construct(subclassId + 1);
    assertNull(invMsg);
  }
}