/*
 * Created on 2006-07-19
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.util.math;

import java.awt.Rectangle;

/**
 * Wrapper class for {@link Rectangle}.
 * 
 * @author M.Olszewski
 */
public final class Rect2d
{
  /** Rectangle's 'x' coordinate. */
  private double x;
  /** Rectangle's 'y' coordinate. */
  private double y;
  /** Rectangle's width. */
  private int width;
  /** Rectangle's height. */
  private int height;
  
  
  /**
   * Constructs instance of {@link Rect2d} class with specified position and size.
   *
   * @param posX - 'x' coordinate.
   * @param posY - 'y' coordinate.
   * @param rectWidth - rectangle's width.
   * @param rectHeight - rectangle's height.
   */
  public Rect2d(double posX, double posY, int rectWidth, int rectHeight)
  {
    setBounds(posX, posY, rectWidth, rectHeight);
  }

  
  /**
   * Checks whether this rectangle is empty one (its width or height is zero
   * or less than zero).
   * 
   * @return Returns <code>true</code> if this rectangle is empty,
   *         <code>false</code> otherwise.
   */
  public boolean isEmpty()
  {
    return (width <= 0) || (height <= 0);
  }
  
  /**
   * Checks whether this {@link Rect2d} intersects specified instance of 
   * {@link Rect2d} class.
   * 
   * @param rect2d -  instance of {@link Rect2d} class checked against 
   *        intersection with this {@link Rect2d}.
   * 
   * @return Returns <code>true</code> if this {@link Rect2d} intersects 
   *         specified instance of {@link Rect2d} class, <code>false</code>
   *         otherwise.
   */
  public boolean intersects(Rect2d rect2d)
  {
    boolean intersect = false;
    if (!isEmpty() && !rect2d.isEmpty())
    {
      intersect = ((rect2d.x + rect2d.width > x) &&
                   (rect2d.y + rect2d.height > y) &&
                   (rect2d.x < x + width) &&
                   (rect2d.y < y + height));
    }
    
    return intersect;
  }
  
  /**
   * Calculates {@link Rect2d} object representing intersection between this
   * and specified instance of {@link Rect2d} class.
   * 
   * @param rect2d - other instance of {@link Rect2d} class used to create
   *        intersection. 
   * 
   * @return Returns {@link Rect2d} object representing intersection between this
   *         and specified instance of {@link Rect2d} class.
   */
  public Rect2d intersection(Rect2d rect2d)
  {
    int tx1 = (int)x;
    int ty1 = (int)y;
    int rx1 = (int)rect2d.x;
    int ry1 = (int)rect2d.y;
    
    long tx2 = tx1 + width;
    long ty2 = ty1 + height;
    long rx2 = rx1 + rect2d.width;
    long ry2 = ry1 + rect2d.height;
    
    if (tx1 < rx1) tx1 = rx1;
    if (ty1 < ry1) ty1 = ry1;
    if (tx2 > rx2) tx2 = rx2;
    if (ty2 > ry2) ty2 = ry2;
    
    tx2 -= tx1;
    ty2 -= ty1;
    
    if (tx2 < Integer.MIN_VALUE) tx2 = Integer.MIN_VALUE;
    if (ty2 < Integer.MIN_VALUE) ty2 = Integer.MIN_VALUE;
    
    return new Rect2d(tx1, ty1, (int)tx2, (int)ty2);
  }
  
  /**
   * Gets 'x' coordinate of this {@link Rect2d}.
   * 
   * @return Returns 'x' coordinate of this {@link Rect2d}.
   */
  public double getX()
  {
    return x;
  }
  
  /**
   * Gets 'y' coordinate of this {@link Rect2d}.
   * 
   * @return Returns 'y' coordinate of this {@link Rect2d}.
   */
  public double getY()
  {
    return y;
  }
  
  /**
   * Gets width of this {@link Rect2d}.
   * 
   * @return Returns width of this {@link Rect2d}.
   */
  public int getWidth()
  {
    return width;
  }
  
  /**
   * Gets height of this {@link Rect2d}.
   * 
   * @return Returns height of this {@link Rect2d}.
   */
  public int getHeight()
  {
    return height;
  }
  
  /**
   * Sets bounds of this {@link Rect2d} using specified parameters.
   * 
   * @param newX - new 'x' coordinate.
   * @param newY - new 'y' coordinate.
   * @param newWidth - new width.
   * @param newHeight - new height.
   */
  public void setBounds(double newX, double newY, int newWidth, int newHeight)
  {
    x = newX;
    y = newY;
    width = newWidth;
    height = newHeight;
  }
  
  /**
   * Sets bounds of this {@link Rect2d} to bounds from specified {@link Rect2d}
   * instance.
   * 
   * @param rect2d - this object's bounds will be set.
   */
  public void setBounds(Rect2d rect2d)
  {
    setBounds(rect2d.x, rect2d.y, rect2d.width, rect2d.height);
  }
  
  /**
   * Changes 'x' and 'y' coordinates of this {@link Rect2d} without 
   * changing rectangle size.
   * 
   * @param newX - new 'x' coordinate.
   * @param newY - new 'y' coordinate.
   */
  public void moveTo(double newX, double newY)
  {
    x = newX;
    y = newY;
  }
  
  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (object == this);
    if (!equal && (object instanceof Rect2d))
    {
      Rect2d rectangle = (Rect2d)object;
      equal = (rectangle.x == x) && (rectangle.y == y) && 
              (rectangle.width == width) && 
              (rectangle.height == height);
    }
    
    return equal;
  }
  
  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    int code = 17;
    
    long bits = 37 * Double.doubleToLongBits(x);
    code = 37 * code + (int)(bits ^ (bits >>> 32));
    bits = 37 * Double.doubleToLongBits(y);
    code = 37 * code + (int)(bits ^ (bits >>> 32));
    
    code = 37 * code + width;
    code = 37 * code + height;
    
    return code;
  }

  /** 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[x=" + x + "; y=" + y + "; width=" + width + "; height=" + height + "]");
  }
}