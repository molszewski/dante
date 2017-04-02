/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

import net.java.dante.sim.data.object.ObjectSize;


/**
 * Interface defining set of fundamental simulation objects attributes,
 * allowing to modify object's coordinates and speed's components.
 * 
 * @author M.Olszewski
 */
public interface ObjectBaseState extends ObjectState
{
  /**
   * Gets name of object's type.
   * 
   * @return Returns name of object's type.
   */
  String getType();
  
  /**
   * Gets object's 'x' coordinate.
   * 
   * @return Returns object's 'x' coordinate.
   */
  double getX();
  
  /**
   * Sets object's 'x' coordinate.
   * 
   * @param newX - new object's 'x' coordinate.
   */
  void setX(double newX);
  
  /**
   * Gets object's 'y' coordinate.
   * 
   * @return Returns object's 'y' coordinate.
   */
  double getY();
  
  /**
   * Sets object's 'y' coordinate.
   * 
   * @param newY - new object's 'y' coordinate.
   */
  void setY(double newY);
  
  /**
   * Gets object's size.
   * 
   * @return Returns object's size.
   */
  ObjectSize getSize();
  
  /**
   * Gets object's horizontal speed.
   * 
   * @return Returns object's horizontal speed.
   */
  double getSpeedX();
  
  /**
   * Sets new object's horizontal speed.
   *
   * @param newSpeedX - new object's horizontal speed.
   */
  void setSpeedX(double newSpeedX);
  
  /**
   * Gets object's vertical speed.
   * 
   * @return Returns object's vertical speed.
   */
  double getSpeedY();
  
  /**
   * Sets new object's vertical speed.
   *
   * @param newSpeedY - new object's vertical speed.
   */
  void setSpeedY(double newSpeedY);
}