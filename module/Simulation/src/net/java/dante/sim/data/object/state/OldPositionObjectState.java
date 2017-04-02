/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

/**
 * Interface defining set of objects attributes regarding their position
 * from last update.
 * 
 * @author M.Olszewski
 */
public interface OldPositionObjectState
{
  /**
   * Gets the object's old 'x' coordinate (from last update).
   *
   * @return Returns the object's old 'x' coordinate.
   */
  double getOldX();
  
 /**
  * Sets object's old 'x' coordinate.
  * 
  * @param newOldX - new value of object's old 'x' coordinate.
  */
 void setOldX(double newOldX);
  
  /**
   * Gets the object's old 'y' coordinate (from last update).
   *
   * @return Returns the object's old 'y' coordinate.
   */
  double getOldY();
  
  /**
   * Sets object's old 'y' coordinate.
   * 
   * @param newOldY - new value of object's old 'y' coordinate.
   */
  void setOldY(double newOldY);
}