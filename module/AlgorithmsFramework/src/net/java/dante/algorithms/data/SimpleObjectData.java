/*
 * Created on 2006-09-11
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.data;

import net.java.dante.sim.data.object.ObjectSize;

/**
 * Simple object's data, used mainly for still objects.
 *
 * @author M.Olszewski
 */
public interface SimpleObjectData
{
  /**
   * Gets object's 'x' coordinate.
   *
   * @return Returns object's 'x' coordinate.
   */
  double getX();

  /**
   * Gets object's 'y' coordinate.
   *
   * @return Returns object's 'y' coordinate.
   */
  double getY();

  /**
   * Gets object's size.
   *
   * @return Returns object's size.
   */
  ObjectSize getSize();
}