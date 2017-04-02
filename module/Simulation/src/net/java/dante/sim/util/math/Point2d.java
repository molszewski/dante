/*
 * Created on 2006-08-03
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.util.math;

/**
 * Class representing point in two dimensional space.
 * 
 * @author M.Olszewski
 */
public final class Point2d
{
  /** Point's 'x' coordinate. */
  private double x;
  /** Point's 'y' coordinate. */
  private double y;

  /**
   * Creates instance of {@link Point2d} class using specified coordinates.
   *
   * @param pointX - point's 'x' coordinate.
   * @param pointY - point's 'y' coordinate.
   */
  public Point2d(double pointX, double pointY)
  {
    setPoint(pointX, pointY);
  }
  
  /**
   * Creates instance of {@link Point2d} class using coordinates
   * form specified {@link Point2d} object.
   *
   * @param point - the specified {@link Point2d} object.
   */
  public Point2d(Point2d point)
  {
    this(point.x, point.y);
  }
  

  /**
   * Sets this points coordinates.
   * 
   * @param pointX - point's 'x' coordinate.
   * @param pointY - point's 'y' coordinate.
   */
  public void setPoint(double pointX, double pointY)
  {
    x = pointX;
    y = pointY;
  }
  
  /**
   * Sets this points using specified point's coordinates.
   * 
   * @param point - the specified point.
   */
  public void setPoint(Point2d point)
  {
    setPoint(point.x, point.y);
  }
  
  /**
   * Gets point's 'x' coordinate.
   * 
   * @return Returns point's 'x' coordinate.
   */
  public double getX()
  {
    return x;
  }
  
  /**
   * Gets point's 'y' coordinate.
   * 
   * @return Returns point's 'y' coordinate.
   */
  public double getY()
  {
    return y;
  }
  
  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (object == this);
    if (!equal && (object instanceof Point2d))
    {
      Point2d point = (Point2d)object;
      equal = (point.x == x) && (point.y == y);
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
    
    long bits = Double.doubleToLongBits(x);
    code = 37 * code + (int)(bits ^ (bits >>> 32));
    bits = Double.doubleToLongBits(y);
    code = 37 * code + (int)(bits ^ (bits >>> 32));
    
    return code;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[x=" + x + "; y=" + y + "]");
  }
}