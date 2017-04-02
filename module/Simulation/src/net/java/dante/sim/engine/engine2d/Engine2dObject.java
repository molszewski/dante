/*
 * Created on 2006-07-21
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d;

import java.awt.image.BufferedImage;

import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.engine.EngineObject;
import net.java.dante.sim.engine.graphics.java2d.Java2dSprite;
import net.java.dante.sim.util.math.Rect2d;


/**
 * Base class for all objects used by {@link net.java.dante.sim.engine.engine2d.server.ServerEngine2d} 
 * engine. It associates engine object with 
 * {@link net.java.dante.sim.engine.graphics.java2d.Java2dSprite} object.
 * 
 * @author M.Olszewski
 */
public abstract class Engine2dObject implements EngineObject
{
  /** Bounding rectangle. */
  protected Rect2d rectangle;
  /** Sprite representing image. */
  private Java2dSprite sprite;
  /** Determines whether this object is active. */
  private boolean active = true;
  
  
  /**
   * Protected constructor - intended only for inheritance.
   *
   * @param objSprite - graphical representation of engine object.
   * @param startX - start 'x' coordinate of this {@link Engine2dObject}.
   * @param startY - start 'y' coordinate of this {@link Engine2dObject}.
   * @param objSize - real size of this {@link Engine2dObject}.
   */
  public Engine2dObject(Java2dSprite objSprite, double startX, double startY, ObjectSize objSize)
  {
    if (objSprite == null)
    {
      throw new NullPointerException("Specified objSprite is null!");
    }
    if (objSize == null)
    {
      throw new NullPointerException("Specified objSize is null!");
    }
    if (objSprite.getWidth() != objSize.getWidth())
    {
      throw new IllegalArgumentException("Invalid arguments objSprite and objSize - they must have the same widths!");
    }
    if (objSprite.getHeight() != objSize.getHeight())
    {
      throw new IllegalArgumentException("Invalid arguments objSprite and objSize - they must have the same heights!");
    }
    
    sprite = objSprite;
    rectangle = new Rect2d(startX, startY, objSize.getWidth(), objSize.getHeight());
  }
  
  
  /**
   * Retrieves 'x' coordinate of this {@link Engine2dObject}.
   * 
   * @return Returns 'x' coordinate of this {@link Engine2dObject}.
   */
  protected abstract double getX();
  
  /**
   * Retrieves 'y' coordinate of this {@link Engine2dObject}.
   * 
   * @return Returns 'y' coordinate of this {@link Engine2dObject}.
   */
  protected abstract double getY();
  
  /**
   * @see net.java.dante.sim.engine.EngineObject#update(long)
   */
  public void update(long delta)
  {
    updateBounds();
  }

  /**
   * @see net.java.dante.sim.engine.EngineObject#render()
   */
  public void render()
  {
    sprite.draw(getX(), getY());
  }
  
  /**
   * Gets this object's bounds as instance of {@link java.awt.Rectangle}.
   * 
   * @return Returns this object's bounds as instance of {@link java.awt.Rectangle}.
   */
  public Rect2d getObjectBounds()
  {
    return rectangle;
  }
  
  /**
   * Updates rectangle bounds using current object's coordinates.
   */
  protected void updateBounds()
  {
    rectangle.setBounds(getX(), getY(), rectangle.getWidth(), rectangle.getHeight());
  }
  
  /**
   * Gets current image of this {@link Engine2dObject} object. 
   * Required to perform perfect pixel collision detection. 
   * 
   * @return Returns current image of this {@link Engine2dObject} object.
   */
  public BufferedImage getImage()
  {
    return sprite.getImage();
  }
  
  /**
   * Sets the specified sprite for this {@link Engine2dObject}.
   * 
   * @param java2dSprite - the specified sprite.
   */
  void setSprite(Java2dSprite java2dSprite)
  {
    if (java2dSprite == null)
    {
      throw new NullPointerException("Specified java2dSprite is null!");
    }
    
    sprite = java2dSprite;
  }
  
  /** 
   * @see net.java.dante.sim.engine.EngineObject#isActive()
   */
  public boolean isActive()
  {
    return active;
  }
  
  /**
   * Sets state of this object (active/not active).
   * 
   * @param objectActive - determines whether object is active or not.
   */
  public void setActive(boolean objectActive)
  {
    active = objectActive;
  }
  
  /** 
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return (getClass() + "[rectangle: " + rectangle + "; active=" + active + "; sprite=" + sprite + "]");
  }
}