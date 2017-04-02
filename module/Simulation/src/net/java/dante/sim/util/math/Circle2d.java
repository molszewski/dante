/*
 * Created on 2006-08-09
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.util.math;

/**
 * Class representing circle in two dimensional space.
 * 
 * @author M.Olszewski
 */
public final class Circle2d
{
  /** Circle's coordinates. */
  private double x, y;
  /** Circle's radius. */
  private double r;
  
  
  /**
   * Creates instance of {@link Circle2d} class with specified circle's 
   * central point and radius.
   *
   * @param circleX - 'x' coordinate of circle's central point.
   * @param circleY - 'y' coordinate of circle's central point.
   * @param radius - circle's radius.
   */
  public Circle2d(double circleX, double circleY, double radius)
  {
    setCircle(circleX, circleY, radius);
  }
  
  /**
   * Creates instance of {@link Circle2d} class using coordinates
   * from specified {@link Circle2d} object.
   *
   * @param circle - the specified {@link Circle2d} object.
   */
  public Circle2d(Circle2d circle)
  {
    setCircle(circle.x, circle.y, circle.r);
  }
  
  
  /**
   * Sets the location of the circle's central point and radius to the specified
   * values.
   * 
   * @param circleX - 'x' coordinate of circle's central point.
   * @param circleY - 'y' coordinate of circle's central point.
   * @param radius - circle's radius.
   */
  public void setCircle(double circleX, double circleY, double radius)
  {
    x = circleX;
    y = circleY;
    r = radius;
  }
  
  /**
   * Gets the 'x' coordinate of this circle.
   * 
   * @return Returns the 'x' coordinate of this circle.
   */
  public double getX()
  {
    return x;
  }

  /**
   * Gets the 'y' coordinate of this circle.
   * 
   * @return Returns the 'y' coordinate of this circle.
   */
  public double getY()
  {
    return y;
  }
  
  /**
   * Gets the radius of this circle.
   * 
   * @return Returns the radius of this circle.
   */
  public double getRadius()
  {
    return r;
  }
  
  /**
   * Checks whether this circle contains specified point.
   * 
   * @param pointX - 'x' coordinate of the checked point.
   * @param pointY - 'y' coordinate of the checked point.
   * 
   * @return Returns <code>true</code> if this circle contains specified point,
   *         <code>false</code> otherwise.
   */
  public boolean contains(double pointX, double pointY)
  {
    return contains(this, pointX, pointY);
  }
  
  /**
   * Checks whether this circle contains specified point.
   * 
   * @param point - checked point.
   * 
   * @return Returns <code>true</code> if this circle contains specified point,
   *         <code>false</code> otherwise.
   */
  public boolean contains(Point2d point)
  {
    return contains(this, point.getX(), point.getY());
  }
  
  /**
   * Checks whether the specified circle contains the specified point.
   * 
   * @param circle - the specified circle.
   * @param pointX - 'x' coordinate of the checked point.
   * @param pointY - 'y' coordinate of the checked point.
   * 
   * @return Returns <code>true</code> if specified circle contains 
   *         specified point, <code>false</code> otherwise.
   */
  public static boolean contains(Circle2d circle, double pointX, double pointY)
  {
    double dx = pointX - circle.x;
    double dy = pointY - circle.y;
    return (Math.sqrt((dx * dx) + (dy * dy)) <= circle.r);
  }
  
  /**
   * Checks whether the specified circle contains the specified point.
   * 
   * @param circle - the specified circle.
   * @param point - the specified point.
   * 
   * @return Returns <code>true</code> if specified circle contains 
   *         specified point, <code>false</code> otherwise.
   */
  public static boolean contains(Circle2d circle, Point2d point)
  {
    return contains(circle, point.getX(), point.getY());
  }
}