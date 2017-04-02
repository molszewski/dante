/*
 * Created on 2006-09-12
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.examples;

import net.java.dante.algorithms.BaseAlgorithmImpl;

/**
 * Empty algorithm - does absolutely nothing.
 *
 * @author M.Olszewski
 */
public class EmptyAlgorithm extends BaseAlgorithmImpl
{
  /**
   * Creates instance of {@link EmptyAlgorithm} class.
   */
  public EmptyAlgorithm()
  {
    super(true);
  }

  /**
   * @see net.java.dante.algorithms.BaseAlgorithmImpl#runAlgorithm()
   */
  @Override
  protected void runAlgorithm()
  {
    // Intentionally left empty.
  }
}