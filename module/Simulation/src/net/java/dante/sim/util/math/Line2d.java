/*
 * Created on 2006-08-03
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.util.math;


/**
 * Class representing line in two dimensional space.
 *
 * @author M.Olszewski
 */
public final class Line2d
{
  /** 'x' coordinate of first endpoint. */
  private double x1;
  /** 'y' coordinate of first endpoint. */
  private double y1;
  /** 'x' coordinate of second endpoint. */
  private double x2;
  /** 'y' coordinate of second endpoint. */
  private double y2;


  /**
   * Creates instance of {@link Line2d} class with specified line's endpoints
   * coordinates.
   *
   * @param firstX - 'x' coordinate of first endpoint.
   * @param firstY - 'y' coordinate of first endpoint.
   * @param secondX - 'x' coordinate of second endpoint.
   * @param secondY - 'y' coordinate of second endpoint.
   */
  public Line2d(double firstX, double firstY, double secondX, double secondY)
  {
    setLine(firstX, firstY, secondX, secondY);
  }

  /**
   * Creates instance of {@link Line2d} class using endpoints coordinates
   * from specified {@link Line2d} object.
   *
   * @param line - the specified {@link Line2d} object.
   */
  public Line2d(Line2d line)
  {
    this(line.x1, line.y1, line.x2, line.y2);
  }


  /**
   * Sets the location of the endpoints of this line to the specified
   * double coordinates.
   *
   * @param firstX - 'x' coordinate of first endpoint.
   * @param firstY - 'y' coordinate of first endpoint.
   * @param secondX - 'x' coordinate of second endpoint.
   * @param secondY - 'y' coordinate of second endpoint.
   */
  public void setLine(double firstX, double firstY, double secondX, double secondY)
  {
    x1 = firstX;
    y1 = firstY;
    x2 = secondX;
    y2 = secondY;
  }

  /**
   * Sets the location of the endpoints of this line using endpoints coordinates
   * form specified {@link Line2d} object.
   *
   * @param line - the specified {@link Line2d} object.
   */
  public void setLine(Line2d line)
  {
    setLine(line.x1, line.y1, line.x2, line.y2);
  }

  /**
   * Gets 'x' coordinate of first endpoint.
   *
   * @return Returns 'x' coordinate of first endpoint.
   */
  public double getX1()
  {
    return x1;
  }

  /**
   * Gets 'y' coordinate of first endpoint.
   *
   * @return Returns 'y' coordinate of first endpoint.
   */
  public double getY1()
  {
    return y1;
  }

  /**
   * Gets 'x' coordinate of second endpoint.
   *
   * @return Returns 'x' coordinate of second endpoint.
   */
  public double getX2()
  {
    return x1;
  }

  /**
   * Gets 'x' coordinate of second endpoint.
   *
   * @return Returns 'x' coordinate of second endpoint.
   */
  public double getY2()
  {
    return y1;
  }

  /**
   * Checks whether there is intersection between this line and specified one.
   * Both lines are extended to infinity.
   *
   * @param line - line to check.
   *
   * @return Returns <code>true</code> if there is intersection point between
   *         two lines or <code>false</code> otherwise.
   */
  public boolean intersects(Line2d line)
  {
    boolean intersected = true;
    if (line != this)
    {
      intersected = intersects(this, line);
    }

    return intersected;
  }

  /**
   * Checks whether there is intersection between two lines.
   *
   * @param line1 - first line to check.
   * @param line2 - second line to check.
   *
   * @return Returns <code>true</code> if there is intersection point between
   *         two lines or <code>false</code> otherwise.
   */
  public static boolean intersects(Line2d line1, Line2d line2)
  {
    if (line1 == null)
    {
      throw new NullPointerException("Specified line1 is null!");
    }
    if (line2 == null)
    {
      throw new NullPointerException("Specified line2 is null!");
    }

    Vector2d vector1 = new Vector2d(line1.x2 - line1.x1, line1.y2 - line1.y1);
    Vector2d vector2 = new Vector2d(line2.x2 - line2.x1, line2.y2 - line2.y1);

    return (Vector2d.perpDotProduct(vector2, vector1) != 0.0);
  }

  /**
   * Calculates intersection point between this and specified lines.
   * If there is no intersection point, <code>null</code> is returned.
   *
   * @param line - the specified {@link Line2d} object.
   *
   * @return Returns intersection point between two lines or <code>null</code>
   *         if there is no intersection point.
   */
  public Point2d intersection(Line2d line)
  {
    return intersection(this, line);
  }

  /**
   * Calculates intersection point of two lines. If there is no intersection
   * point, <code>null</code> is returned.
   *
   * @param line1 - first line.
   * @param line2 - second line.
   *
   * @return Returns intersection point between two lines or <code>null</code>
   *         if there is no intersection point.
   */
  public static Point2d intersection(Line2d line1, Line2d line2)
  {
    if (line1 == null)
    {
      throw new NullPointerException("Specified line1 is null!");
    }
    if (line2 == null)
    {
      throw new NullPointerException("Specified line2 is null!");
    }

    Point2d result = null;

    Vector2d vector1 = new Vector2d(line1.x2 - line1.x1, line1.y2 - line1.y1);
    Vector2d vector2 = new Vector2d(line2.x2 - line2.x1, line2.y2 - line2.y1);

    double mx1 = Vector2d.perpDotProduct(vector1, vector2);
    if (mx1 != 0.0)
    {
      Vector2d vector3 = new Vector2d(line2.x1 - line1.x1, line2.y1 - line1.y1);
      double insideVector1 = Vector2d.perpDotProduct(vector3, vector2) / mx1;
      if ((insideVector1 >= 0.0) && (insideVector1 <= 1.0))
      {
        double mx2 = Vector2d.perpDotProduct(vector2, vector1); // mx2 cannot be null

        vector3.setVector(line1.x1 - line2.x1, line1.y1 - line2.y1);
        double insideVector2 = Vector2d.perpDotProduct(vector3, vector1) / mx2;
        if ((insideVector2 >= 0.0) && (insideVector2 <= 1.0))
        {
          double resultX = line1.getX1() + (vector1.getX() * insideVector1);
          double resultY = line1.getY1() + (vector1.getY() * insideVector1);

          result = new Point2d(resultX, resultY);
        }
      }
    }
    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof Line2d))
    {
      Line2d other = (Line2d)object;
      equal = ((Double.doubleToLongBits(x1) == Double.doubleToLongBits(other.x1)) &&
               (Double.doubleToLongBits(x2) == Double.doubleToLongBits(other.x2)) &&
               (Double.doubleToLongBits(y1) == Double.doubleToLongBits(other.y1)) &&
               (Double.doubleToLongBits(y2) == Double.doubleToLongBits(other.y2)));
    }

    return equal;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    long bits = Double.doubleToLongBits(x1);
    result = PRIME * result + (int)(bits ^ (bits >>> 32));
    bits = Double.doubleToLongBits(y1);
    result = PRIME * result + (int)(bits ^ (bits >>> 32));
    bits = Double.doubleToLongBits(x2);
    result = PRIME * result + (int)(bits ^ (bits >>> 32));
    bits = Double.doubleToLongBits(y2);
    result = PRIME * result + (int)(bits ^ (bits >>> 32));

    return result;
  }


  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[x1=" + x1 + "; y1=" + y1 + "; x2=" + x2 + "; y2=" + y2 + "]");
  }

  /**
   * Tests.
   *
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    {
      Line2d line1 = new Line2d(0.0, 2.0, 4.0, 2.0);
      Line2d line2 = new Line2d(2.0, 0.0, 2.0, 4.0);
      Point2d p1 = intersection(line1, line2);
      System.out.println("p1=" + p1);
      Point2d p2 = line1.intersection(line2);
      System.out.println("p2=" + p2);
    }
    {
      Line2d line = new Line2d(0.0, 2.0, 4.0, 2.0);
      Point2d p1 = intersection(line, line);
      System.out.println("p1=" + p1);
      Point2d p2 = line.intersection(line);
      System.out.println("p2=" + p2);
    }
  }
}