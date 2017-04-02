/*
 * Created on 2006-07-19
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.graphics.java2d;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.java.dante.sim.engine.graphics.Sprite;


/**
 * Implementation of {@link Sprite} interface using Java2D rendering.
 *
 * @author M.Olszewski
 */
public class Java2dSprite implements Sprite
{
  /** The image with specified sprite. */
  protected BufferedImage image;
  /** The game context where this sprite is going to be drawn. */
  protected Java2dContext context;


  /**
   * Creates new instance of {@link Java2dSprite} class that belongs to
   * specified context and holds specified image.
   *
   * @param spriteCtx - game context where this sprite is going to be drawn.
   * @param spriteImage - image with specified sprite.
   */
  public Java2dSprite(Java2dContext spriteCtx, BufferedImage spriteImage)
  {
    if (spriteCtx == null)
    {
      throw new NullPointerException("Specified spriteCtx is null!");
    }
    if (spriteImage == null)
    {
      throw new NullPointerException("Specified spriteImage is null!");
    }

    image = spriteImage;
    context = spriteCtx;
  }


  /**
   * @see net.java.dante.sim.engine.graphics.Sprite#getWidth()
   */
  public int getWidth()
  {
    return image.getWidth(null);
  }

  /**
   * @see net.java.dante.sim.engine.graphics.Sprite#getHeight()
   */
  public int getHeight()
  {
    return image.getHeight(null);
  }

  /**
   * Specified coordinates are cast to integers.
   *
   * @see net.java.dante.sim.engine.graphics.Sprite#draw(double, double)
   */
  public void draw(double x, double y)
  {
    Graphics2D g = context.getAcceleratedGraphics();
    // It can return null!!!
    if (g != null)
    {
      g.drawImage(image, (int)x, (int)y, null);
    }
  }

  /**
   * Retrieves image that is currently used by this {@link Java2dSprite}.
   *
   * @return Returns image that is currently used by this {@link Java2dSprite}.
   */
  public BufferedImage getImage()
  {
    return image;
  }
}