/*
 * Created on 2006-07-01
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.util;

import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

/**
 * First implementation: simply generates random number and checks whether
 * it has not been generated yet. If so, generates another one.
 *
 * @author M.Olszewski
 */
public class RandomIdGenerator
{
  /** Invalid value of identifier. */
  public static final int INVALID_ID_VALUE = -1;
  /**
   * Maximum possible numbers: <code>2^30</code> - half of positive integers
   * plus one (because {@link Integer#MAX_VALUE} is equal to <code>(2^31 - 1)</code>).
   */
  public static final int MAXIMUM_ALLOWED_SIZE = (Integer.MAX_VALUE >> 2) + 1;
  /** Default allowed size. */
  public static final int DEFAULT_ALLOWED_SIZE = (1 << 16);

  /** Minimum acceptable interval between two numbers. */
  private static final int MINIMUM_INTERVAL = 2;


  /** Maximum size of this {@link RandomIdGenerator}. */
  private int maxSize = DEFAULT_ALLOWED_SIZE;
  /** Random generator used by instances of this class. */
  private final Random rand = new Random();
  /** {@link java.util.TreeSet} containing all generated random identifiers. */
  private TreeSet<Integer> generated = new TreeSet<Integer>();


  /**
   * Default constructor.
   */
  public RandomIdGenerator()
  {
    // Intentionally left empty.
  }

  /**
   * Constructor creating object of {@link RandomIdGenerator} with specified
   * capacity of random numbers. Specified capacity cannot be greater
   * than {@link #MAXIMUM_ALLOWED_SIZE} (and of course less than zero).
   *
   * @param maxAllowedRandoms - maximum allowed number of generated random numbers.
   */
  public RandomIdGenerator(int maxAllowedRandoms)
  {
    if ((maxAllowedRandoms > 0) && (maxAllowedRandoms <= MAXIMUM_ALLOWED_SIZE))
    {
      maxSize = maxAllowedRandoms;
    }
    else
    {
      throw new IllegalArgumentException("Specified maxAllowedRandoms is less than zero or greater than MAXIMUM_ALLOWED_SIZE=" + MAXIMUM_ALLOWED_SIZE);
    }
  }

  /**
   * Generates random unique identifier. If all possible identifiers were
   * generated by this {@link RandomIdGenerator} object,
   * {@link #INVALID_ID_VALUE} will be returned.
   *
   * @return Returns generated unique identifier or {@link #INVALID_ID_VALUE}
   *         if all identifiers were generated.
   */
  public synchronized int generateId()
  {
    int id = INVALID_ID_VALUE;

    if (generated.size() < maxSize)
    {
      // id = 0 .. (Integer.MAX_VALUE - 1)
      id = rand.nextInt(Integer.MAX_VALUE);

      if (generated.contains(Integer.valueOf(id)))
      {
        id = findId();
      }

      generated.add(Integer.valueOf(id));
    }

    return id;
  }

  /**
   * Method searching for free identifier.
   * All numbers are stored in ascending order. Algorithm checks numbers
   * as pairs - if there is enough space between two numbers to create random
   * number, random number is being generated.
   *
   * @return Returns found identifier.
   */
  private int findId()
  {
    if (generated.size() <= 0)
    {
      throw new AssertionError("There are no generated numbers!");
    }

    Iterator<Integer> it = generated.iterator();
    int first = generated.iterator().next().intValue();
    int last = first;
    int current = first;

    int pairEnd = 0;
    int pairBegin = 0;

    while (it.hasNext())
    {
      current = it.next().intValue();
      if ((current - last) >= MINIMUM_INTERVAL)
      {
        pairBegin = last;
        pairEnd   = current;
        break;
      }

      last = current;
    }

    if (last == current)
    {
      if (first > (Integer.MAX_VALUE - last))
      {
        pairBegin = 0;
        pairEnd = first;
      }
      else
      {
        pairBegin = last;
        pairEnd = Integer.MAX_VALUE;
      }
    }

    return rand.nextInt(pairEnd - pairBegin) + pairBegin;
  }

  /**
   * Checks whether this generator is able to generate more random identifiers.
   *
   * @return Returns <code>true</code> if generator is able to generate.
   */
  public synchronized boolean canGenerate()
  {
    return (generated.size() < maxSize);
  }

  /**
   * Releases specified identifier from this generator -
   * it can be generated again from now on.
   *
   * @param id - the specified identifier.
   */
  public synchronized void releaseId(int id)
  {
    generated.remove(Integer.valueOf(id));
  }

  /**
   * Resets this {@link RandomIdGenerator} object.
   */
  public synchronized void reset()
  {
    generated.clear();
  }
}