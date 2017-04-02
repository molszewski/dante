/*
 * Created on 2006-04-26
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.util;

import java.util.Random;

/**
 * Class useful especially for tests: generates random strings, arrays
 * of all primitive types and arrays with {@link String} objects.
 *
 * @author M.Olszewski
 */
public final class RandomGenerator
{
  /**
   * Private constructor: no instances or inheritance possible.
   */
  private RandomGenerator()
  {
    // Empty
  }

  /** One generator for all class methods. */
  private static Random rand = new Random();

  /**
   * Generates random integer within specified boundaries.
   *
   * @param minInt - minimum integer boundary (equal or greater).
   * @param maxInt - maximum integer boundary (lesser).
   *
   * @return Returns random integer within specified boundaries.
   *
   * @throws IllegalArgumentException if specified minimum boundary is not
   *         lesser than maximum boundary.
   */
  public static int randomInt(int minInt, int maxInt)
  {
    if (minInt >= maxInt)
    {
      throw new IllegalArgumentException("RandomGenerator.randomInt(): minInt must be lesser than maxInt!");
    }
    return (rand.nextInt(maxInt - minInt) + minInt);
  }

  /**
   * Generates string filled with random lower case characters with length within
   * specified boundaries.
   *
   * @param minStringLength - minimum string length.
   * @param maxStringLength - maximum string length.
   *
   * @return Returns new object of {@link String} class filled with
   *         random characters.
   */
  public static String randomString(int minStringLength, int maxStringLength)
  {
    int length = randomInt(minStringLength, maxStringLength);
    char[] characters = new char[length];
    for (int i = 0; i < length; i++)
    {
      characters[i] = (char)randomInt('a', 'z');
    }

    return new String(characters);
  }

  /**
   * Generates array of bytes with random length, but within specified
   * boundaries and filled with random values of bytes.
   *
   * @param minLength - minimum array length.
   * @param maxLength - maximum array length.
   *
   * @return Returns new array with random length and filled with random values
   *         of bytes.
   */
  public static byte[] randomBytes(int minLength, int maxLength)
  {
    int length = randomInt(minLength, maxLength);
    byte[] bytes = new byte[length];
    for (int i = 0; i < bytes.length; i++)
    {
      bytes[i] = (byte)rand.nextInt();
    }

    return bytes;
  }

  /**
   * Generates array of bytes with specified length and filled with
   * random bytes.
   *
   * @param length - array's length.
   *
   * @return Returns new array with specified length and filled with random
   *         bytes.
   */
  public static byte[] randomBytes(int length)
  {
    return randomBytes(length, length + 1);
  }

  /**
   * Generates array of characters with specified length and filled with
   * random characters.
   *
   * @param length - array's length.
   *
   * @return Returns new array with specified length and filled with random
   *         characters.
   */
  public static char[] randomChars(int length)
  {
    char[] writtens = new char[length];
    for (int i = 0; i < writtens.length; i++)
    {
      writtens[i] = (char)rand.nextInt();
    }

    return writtens;
  }

  /**
   * Generates array of short integers with specified length and filled with
   * random short integers.
   *
   * @param length - array's length.
   *
   * @return Returns new array with specified length and filled with random
   *         short integers.
   */
  public static short[] randomShorts(int length)
  {
    short[] writtens = new short[length];
    for (int i = 0; i < writtens.length; i++)
    {
      writtens[i] = (short)rand.nextInt();
    }

    return writtens;
  }

  /**
   * Generates array of integers with specified length and filled with
   * random short integers.
   *
   * @param length - array's length.
   *
   * @return Returns new array with specified length and filled with random
   *         integers.
   */
  public static int[] randomInts(int length)
  {
    int[] writtens = new int[length];
    for (int i = 0; i < writtens.length; i++)
    {
      writtens[i] = rand.nextInt();
    }

    return writtens;
  }

  /**
   * Generates array of long integers with specified length and filled with
   * random short integers.
   *
   * @param length - array's length.
   *
   * @return Returns new array with specified length and filled with random
   *         long integers.
   */
  public static long[] randomLongs(int length)
  {
    long[] writtens = new long[length];
    for (int i = 0; i < writtens.length; i++)
    {
      writtens[i] = rand.nextLong();
    }

    return writtens;
  }

  /**
   * Generates array of float numbers with specified length and filled with
   * random short integers.
   *
   * @param length - array's length.
   *
   * @return Returns new array with specified length and filled with random
   *         float numbers.
   */
  public static float[] randomFloats(int length)
  {
    float[] writtens = new float[length];
    for (int i = 0; i < writtens.length; i++)
    {
      writtens[i] = rand.nextFloat();
    }

    return writtens;
  }

  /**
   * Generates array of double numbers with specified length and filled with
   * random short integers.
   *
   * @param length - array's length.
   *
   * @return Returns new array with specified length and filled with random
   *         double numbers.
   */
  public static double[] randomDoubles(int length)
  {
    double[] writtens = new double[length];
    for (int i = 0; i < writtens.length; i++)
    {
      writtens[i] = rand.nextDouble();
    }

    return writtens;
  }

  /**
   * Generates array of {@link String} objects with specified length and
   * filled with random strings.
   *
   * @param length - array's length.
   * @param minStringLength - minimum string's length.
   * @param maxStringLength - maximum string's length.
   *
   * @return Returns new array with specified length and filled with random
   *         strings.
   */
  public static String[] randomStrings(int length, int minStringLength, int maxStringLength)
  {
    String[] strings = new String[length];
    for (int i = 0; i < length; i++)
    {
      strings[i] = randomString(minStringLength, maxStringLength);
    }

    return strings;
  }
}