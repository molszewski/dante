/*
 * Created on 2006-04-26
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.common;

/**
 * Class describing number of bytes required to store any primitive types.
 *
 * @author M.Olszewski
 */
public final class PrimitivesSizes
{
  /** Number of bytes required to store a byte. */
  public static int BYTE_SIZE = Byte.SIZE >> 3;
  /** Number of bytes required to store a character. */
  public static int CHAR_SIZE = Character.SIZE >> 3;
  /** Number of bytes required to store a short integer number. */
  public static int SHORT_SIZE = Short.SIZE >> 3;
  /** Number of bytes required to store an integer number. */
  public static int INT_SIZE = Integer.SIZE >> 3;
  /** Number of bytes required to store a long integer number. */
  public static int LONG_SIZE = Long.SIZE >> 3;
  /** Number of bytes required to store a float number. */
  public static int FLOAT_SIZE = Float.SIZE >> 3;
  /** Number of bytes required to store a double number. */
  public static int DOUBLE_SIZE = Double.SIZE >> 3;
  
  
  /**
   * Simple test method.
   * 
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    Dbg.write("byte size equal 1? " + (BYTE_SIZE == 1));
    Dbg.write("char size equal 2? " + (CHAR_SIZE == 2));
    Dbg.write("short size equal 2? " + (SHORT_SIZE == 2));
    Dbg.write("int size equal 4? " + (INT_SIZE == 4));
    Dbg.write("long size equal 8? " + (LONG_SIZE == 8));
    Dbg.write("float size equal 4? " + (FLOAT_SIZE == 4));
    Dbg.write("double size equal 8? " + (DOUBLE_SIZE == 8));
  }
}