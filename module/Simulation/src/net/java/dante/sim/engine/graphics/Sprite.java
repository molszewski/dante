/*
 * Created on 2006-07-18
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.graphics;


/**
 * Interface for graphical representation of objects that can used by 
 * {@link net.java.dante.sim.engine.Engine} implementations. 
 * 
 * @author M.Olszewski
 */
public interface Sprite 
{
  /**
   * Get the width of the drawn sprite.
   * 
   * @return Returns the width of the drawn sprite.
   */
  int getWidth();

  /**
   * Get the height of the drawn sprite.
   * 
   * @return Returns the height of the drawn sprite.
   */
  int getHeight();
  
  /**
   * Draws the sprite at specified place.
   * 
   * @param x - 'x' coordinates of place where sprite should be drawn.
   * @param y - 'y' coordinates of place where sprite should be drawn.
   */
  void draw(double x, double y);
}