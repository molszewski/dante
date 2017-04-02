/*
 * Created on 2006-07-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.util;

import junit.framework.TestCase;

/**
 * Test class containing tests for {@link RandomIdGenerator} class.
 *
 * @author M.Olszewski
 */
public class RandomIdGeneratorTest extends TestCase
{
  /**
   * Test method for {@link net.java.dante.sim.util.RandomIdGenerator#generateId()}.
   */
  public final void testGenerateId()
  {
    {
      RandomIdGenerator rig1 = new RandomIdGenerator();
      
      for (int i = 0, size = (1 << 16); i < size; i++)
      {
        assertTrue(rig1.generateId() >= 0);
      }
      
      for (int i = 0, size = ((1 << 16) + 1); i < size; i++)
      {
        assertEquals(rig1.generateId(), RandomIdGenerator.INVALID_ID_VALUE);
      }
    }
    
    {
      final int CUSTOM_CAPACITY = 10;
      
      RandomIdGenerator rig2 = new RandomIdGenerator(CUSTOM_CAPACITY);
      
      for (int i = 0; i < CUSTOM_CAPACITY; i++)
      {
        assertTrue(rig2.generateId() >= 0);
      }
      
      for (int i = 0; i < CUSTOM_CAPACITY; i++)
      {
        assertEquals(rig2.generateId(), RandomIdGenerator.INVALID_ID_VALUE);
      }
    }
  }

  /**
   * Test method for {@link net.java.dante.sim.util.RandomIdGenerator#canGenerate()}.
   */
  public final void testCanGenerate()
  {
    {
      RandomIdGenerator rig1 = new RandomIdGenerator();
      
      for (int i = 0, size = (1 << 16); i < size; i++)
      {
        assertTrue(rig1.canGenerate());
        rig1.generateId();
      }
      
      for (int i = 0, size = (1 << 16); i < size; i++)
      {
        assertFalse(rig1.canGenerate());
        rig1.generateId();
      }
      assertFalse(rig1.canGenerate());
    }
    
    {
      final int CUSTOM_CAPACITY = 10;
      
      RandomIdGenerator rig2 = new RandomIdGenerator(CUSTOM_CAPACITY);
      
      for (int i = 0; i < CUSTOM_CAPACITY; i++)
      {
        assertTrue(rig2.canGenerate());
        rig2.generateId();
      }
      
      for (int i = 0; i < CUSTOM_CAPACITY; i++)
      {
        assertFalse(rig2.canGenerate());
        rig2.generateId();
      }
      assertFalse(rig2.canGenerate());
    }
  }
  
  /**
   * Test method for {@link net.java.dante.sim.util.RandomIdGenerator#reset()}.
   */
  public final void testReset()
  {
    {
      RandomIdGenerator rig1 = new RandomIdGenerator();
      
      for (int i = 0, size = (1 << 16); i < size; i++)
      {
        rig1.generateId();
      }
      assertFalse(rig1.canGenerate());
      
      rig1.reset();
      assertTrue(rig1.canGenerate());
      assertTrue(rig1.generateId() >= 0);
    }
    
    {
      final int CUSTOM_CAPACITY = 10;
      
      RandomIdGenerator rig2 = new RandomIdGenerator(CUSTOM_CAPACITY);
      
      for (int i = 0; i < CUSTOM_CAPACITY; i++)
      {
        rig2.generateId();
      }
      assertFalse(rig2.canGenerate());
      
      rig2.reset();
      assertTrue(rig2.canGenerate());
      assertTrue(rig2.generateId() >= 0);
    }
  }
}