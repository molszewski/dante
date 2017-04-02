/*
 * Created on 2006-08-31
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.data;


/**
 * Interface defining some object data.
 *
 * @author M.Olszewski
 */
public interface ObjectData extends SimpleObjectData
{
  /**
   * Gets name of object's type.
   *
   * @return Returns name of object's type.
   */
  String getType();

  /**
   * Gets object's identifier.
   *
   * @return Returns object's identifier.
   */
  int getId();

  /**
   * Determines whether specified object is alive or destroyed.
   *
   * @return Returns <code>true</code> if object is alive or <code>false</code>
   *         if it is destroyed.
   */
  boolean isAlive();

  /**
   * Gets object's maximum speed.
   *
   * @return Returns object's maximum speed.
   */
  double getMaxSpeed();

  /**
   * Gets object's horizontal speed.
   *
   * @return Returns object's horizontal speed.
   */
  double getSpeedX();

  /**
   * Gets object's vertical speed.
   *
   * @return Returns object's vertical speed.
   */
  double getSpeedY();
}