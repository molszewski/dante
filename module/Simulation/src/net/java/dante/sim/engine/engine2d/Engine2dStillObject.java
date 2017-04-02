/*
 * Created on 2006-07-22
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d;

import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.engine.graphics.java2d.Java2dSprite;

/**
 * Class representing engine objects that cannot be moved.
 *
 * @author M.Olszewski
 */
public class Engine2dStillObject extends Engine2dObject
{
  /**
   * Creates instance of {@link Engine2dStillObject} class with specified
   * parameters.
   * 
   * @param objSprite - object's graphical representation.
   * @param startX - start 'x' coordinate of this {@link Engine2dObject}.
   * @param startY - start 'y' coordinate of this {@link Engine2dObject}.
   * @param size - size of this {@link Engine2dObject}.
   */
  public Engine2dStillObject(Java2dSprite objSprite, double startX,
      double startY, ObjectSize size)
  {
    super(objSprite, startX, startY, size);
  }
  
  
  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#update(long)
   */
  @Override
  public void update(long delta)
  {
    // Intentionally left empty.
  }

  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#getX()
   */
  @Override
  protected double getX()
  {
    return rectangle.getX();
  }

  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#getY()
   */
  @Override
  protected double getY()
  {
    return rectangle.getY();
  }
}