/*
 * Created on 2006-07-21
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d.server;

import java.awt.image.BufferedImage;

import net.java.dante.sim.engine.EngineObject;
import net.java.dante.sim.engine.collision.AbstractCollisionModule;
import net.java.dante.sim.engine.engine2d.Engine2dObject;
import net.java.dante.sim.engine.engine2d.Engine2dStillObject;
import net.java.dante.sim.util.math.Rect2d;


/**
 * Collision module for {@link net.java.dante.sim.engine.engine2d.server.ServerEngine2d} engine,
 * performing perfect pixel collision detection - works only with
 * {@link net.java.dante.sim.engine.engine2d.Engine2dObject} objects using 
 * {@link net.java.dante.sim.engine.graphics.java2d.Java2dSprite} objects for graphical
 * representation.
 * 
 * @author M.Olszewski
 */
class Server2dCollisionModule extends AbstractCollisionModule
{
  /** Mask for obtaining alpha value from ARGB color format. */
  private static final int ARGB_ALPHA_MASK = 0xFF000000;
  /** Full transparency value in ARGB color format. */
  private static final int ARGB_TRANSPARENCY_VALUE = 0x00000000;


  /**
   * Creates instance of {@link Server2dCollisionModule} class.
   */
  Server2dCollisionModule()
  {
    // Intentionally left empty.
  }
  

  /**
   * Pixel perfect collision implementation.
   * 
   * @see net.java.dante.sim.engine.collision.CollisionModule#checkCollision(net.java.dante.sim.engine.EngineObject, net.java.dante.sim.engine.EngineObject)
   */
  public boolean checkCollision(EngineObject o1, EngineObject o2)
  {
    boolean collided = false;
    if (o1.isActive() && o2.isActive())
    {
      // Don't check collisions between 2 still objects!
      if (!( (o1 instanceof Engine2dStillObject) && (o2 instanceof Engine2dStillObject) ))
      {
        Engine2dObject fo1 = (Engine2dObject)o1;
        Engine2dObject fo2 = (Engine2dObject)o2;
          
        if (fo1.getObjectBounds().intersects(fo2.getObjectBounds()))
        {
          Rect2d fo1Rect = fo1.getObjectBounds();
          Rect2d fo2Rect = fo2.getObjectBounds();
          Rect2d intersection = fo1Rect.intersection(fo2Rect);
          
          collided = checkPixels(
                fo1.getImage(), fo1Rect, 
                fo2.getImage(), fo2Rect, 
                intersection);
        }
      }
    }
    return collided;
  }
  
  /**
   * Performs pixel-by-pixel check of two images in intersected area.
   * 
   * @param img1 - first image to check.
   * @param r1 - first rectangle to check.
   * @param img2 - second image to check.
   * @param r2 - second rectangle to check.
   * @param intersection - rectangle determining intersection area.
   * 
   * @return Returns <code>true</code> if any corresponding pixels in both images
   *         are not fully transparent, <code>false</code> otherwise.
   */
  private static boolean checkPixels(BufferedImage img1, Rect2d r1, BufferedImage img2, 
      Rect2d r2, Rect2d intersection)
  {
    boolean collided = false;
    
    int startR1_x = (int)(intersection.getX() - r1.getX());
    int startR1_y = (int)(intersection.getY() - r1.getY());
    int startR2_x = (int)(intersection.getX() - r2.getX());
    int startR2_y = (int)(intersection.getY() - r2.getY());
    
    for (int i = 0; i < intersection.getWidth(); i++)
    {
      for (int j = 0; j < intersection.getHeight(); j++)
      {
        if (((img1.getRGB(startR1_x + i, startR1_y + j) & ARGB_ALPHA_MASK) != ARGB_TRANSPARENCY_VALUE) && 
            ((img2.getRGB(startR2_x + i, startR2_y + j) & ARGB_ALPHA_MASK) != ARGB_TRANSPARENCY_VALUE)) 
        {
          collided = true;
          break;
        }
      }
    }
    
    return collided;
  }
}