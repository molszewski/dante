/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.event.types;


/**
 * Generic interface defining set of parameters for events taking into 
 * consideration movement of objects. Move events defines start of movement time, 
 * destination coordinates and speed's components for an object.
 *
 * @author M.Olszewski
 */
public interface ObjectMoveEventParams
{
  /**
   * Gets object's destination 'x' coordinate.
   * 
   * @return Returns object's destination 'x' coordinate.
   */
  double getDestinationX();
  
  /**
   * Gets object's destination 'y' coordinate.
   * 
   * @return Returns object's destination 'y' coordinate.
   */
  double getDestinationY();
  
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