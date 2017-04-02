/*
 * Created on 2006-07-27
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.util.math;

import java.awt.geom.Point2D;


/**
 * Vector class in two dimensional space.
 *
 * @author M.Olszewski
 */
public final class Vector2d
{
  /** Vector's components. */
  private double x, y;


  /**
   * Creates new instance of {@link Vector2d} with specified x, y components.
   *
   * @param vectorX - vector's x component.
   * @param vectorY - vector's y component.
   */
  public Vector2d(double vectorX, double vectorY)
  {
    x = vectorX;
    y = vectorY;
  }

  /**
   * Creates new instance of {@link Vector2d} with specified endpoints
   * defining vector's components.
   *
   * @param vectorX1 - vector's 'x' coordinate of first endpoint.
   * @param vectorY1 - vector's 'y' coordinate of first endpoint.
   * @param vectorX2 - vector's 'x' coordinate of second endpoint.
   * @param vectorY2 - vector's 'y' coordinate of second endpoint.
   */
  public Vector2d(double vectorX1, double vectorY1, double vectorX2, double vectorY2)
  {
    this(vectorX2 - vectorX1, vectorY2 - vectorY1);
  }

  /**
   * Creates new instance of {@link Vector2d} with x, y components
   * equal to ones in specified {@link Vector2d} object.
   *
   * @param vector - base {@link Vector2d} object for this vector.
   */
  public Vector2d(Vector2d vector)
  {
    this(vector.x, vector.y);
  }


  /**
   * Gets vector's magnitude.
   *
   * @return Returns vector's magnitude.
   */
  public double magnitude()
  {
    return Math.sqrt((x * x) + (y * y));
  }

  /**
   * Gets vector's x component.
   *
   * @return Returns vector's x component.
   */
  public double getX()
  {
    return x;
  }

  /**
   * Sets 'x' component to the specified value.
   *
   * @param newX new value of 'x' component.
   */
  public void setX(double newX)
  {
    x = newX;
  }

  /**
   * Gets vector's y component.
   *
   * @return Returns vector's y component.
   */
  public double getY()
  {
    return y;
  }

  /**
   * Sets 'y' component to the specified value.
   *
   * @param newY new value of 'y' component.
   */
  public void setY(double newY)
  {
    y = newY;
  }

  /**
   * Sets vector components to the specified values.
   *
   * @param newX new value of 'x' component.
   * @param newY new value of 'y' component.
   */
  public void setVector(double newX, double newY)
  {
    x = newX;
    y = newY;
  }

  /**
   * Checks whether this vector has zero length.
   *
   * @return Returns <code>true</code> if this vector has zero length.
   */
  public boolean hasZeroLength()
  {
    return ((x == 0) && (y==0));
  }

  /**
   * Produces 'perp dot' product of this and specfied vector.
   *
   * @param vector - the specified vector.
   *
   * @return Returns result of 'perp dot' of this and specfied vector.
   */
  public double perpDotProduct(Vector2d vector)
  {
    return perpDotProduct(this, vector);
  }

  /**
   * Produces 'perp dot' product between two vectors.
   *
   * @param vector1 - first vector.
   * @param vector2 - second vector.
   *
   * @return Returns result of 'perp dot' product of two vectors.
   */
  public static double perpDotProduct(Vector2d vector1, Vector2d vector2)
  {
    return (vector1.x * vector2.y - vector1.y * vector2.x);
  }

  /**
   * Generates dot product of this and specfied vector.
   *
   * @param vector - the specified vector.
   *
   * @return Returns dot product result of this and specfied vector.
   */
  public double dotProduct(Vector2d vector)
  {
    return dotProduct(this, vector);
  }

  /**
   * Generates dot product of two vectors, multiplying their 'x' and 'y'
   * components.
   *
   * @param vector1 - first vector.
   * @param vector2 - second vector.
   *
   * @return Returns generated dot product.
   */
  public static double dotProduct(Vector2d vector1, Vector2d vector2)
  {
    return ((vector1.x * vector2.x) + (vector1.y * vector2.y));
  }

  /**
   * Calculates angle between this vector and specified one. Calculated
   * angle is returned in radians.
   *
   * @param vector - the specified vector.
   *
   * @return Returns angle (in radians) between this vector and specified one.
   */
  public double calculateAngle(Vector2d vector)
  {
    return calculateAngle(this, vector);
  }

  /**
   * Calculates angle between two specified vectors. Calculated
   * angle is returned in radians.
   *
   * @param vector1 - first vector.
   * @param vector2 - second vector.
   *
   * @return Returns angle (in radians) between two specified vectors.
   */
  public static double calculateAngle(Vector2d vector1, Vector2d vector2)
  {
    return (Math.acos(dotProduct(vector1, vector2) / (vector1.magnitude() * vector2.magnitude())));
  }

  /**
   * Normalizes this vector.
   */
  public void normalize()
  {
    double m = magnitude();

    if (m != 0)
    {
      x = x / m;
      y = y / m;
    }
    else
    {
      x = y = 0;
    }
  }

  /**
   * Adjusts this vector's 'x' and 'y' componets to the specified magnitude,
   * without changing vector's direction.
   *
   * @param magnitude new vector's magnitude.
   */
  public void adjustVectorToMagnitude(double magnitude)
  {
    normalize();

    x *= magnitude;
    y *= magnitude;
  }

  /**
   * Adds the components of the specified vector to this one's components.
   *
   * @param addedVector the specified vector.
   */
  public void addVector(Vector2d addedVector)
  {
    x += addedVector.x;
    y += addedVector.y;
  }

  /**
   * Adds the specified components values to this vector.
   *
   * @param vectorX the specified 'x' component to add.
   * @param vectorY the specified 'y' component to add.
   */
  public void addVector(double vectorX, double vectorY)
  {
    x += vectorX;
    y += vectorY;
  }

  /**
   * Subtracts the components of the specified vector from this one's components.
   *
   * @param subtractedVector the specified vector.
   */
  public void subtractVector(Vector2d subtractedVector)
  {
    x -= subtractedVector.x;
    y -= subtractedVector.y;
  }

  /**
   * Subtracts the components values from this vector.
   *
   * @param vectorX the specified 'x' component to subtract.
   * @param vectorY the specified 'y' component to subtract.
   */
  public void subtractVector(double vectorX, double vectorY)
  {
    x -= vectorX;
    y -= vectorY;
  }

  /**
   * Divides this vector's components by the specified vector.
   *
   * @param factor division factor.
   */
  public void divideBy(double factor)
  {
    if (factor == 0)
    {
      throw new IllegalArgumentException("Invalid argument factor - it cannot be zero!");
    }

    x /= factor;
    y /= factor;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (object == this);
    if (!equal && (object instanceof Vector2d))
    {
      Vector2d vector = (Vector2d)object;
      equal = (vector.x == x) && (vector.y == y);
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

  /**
   * Tests.
   *
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    {
      Vector2d v1 = new Vector2d(1.0, 2.0);
      Vector2d v2 = new Vector2d(2.0, 0.0);
      double angle = Vector2d.calculateAngle(v1, v2);

      double angle3 = MathUtils.calculateVectorsAngle(0,0,1.0, 2.0, 2.0, 0.0);

      Point2D.Double p = new Point2D.Double(1.0, 2.0);
      Point2D.Double q = new Point2D.Double(2.0, 0.0);
      double angle2 = Math.acos((p.getX() * q.getX() + p.getY() * q.getY()) / (p.distance(0, 0) * q.distance(0, 0)) );

      double angle4 = (Math.atan2(p.y,p.x) - Math.atan2(q.y,q.x));

      System.out.println("angle="+ angle/Math.PI * 180);
      System.out.println("angle2="+angle2/Math.PI * 180);
      System.out.println("angle3="+angle3/Math.PI * 180);
      System.out.println("angle4="+angle4/Math.PI * 180);

  //    System.out.println("before angle=" + );

      final double MAX_V = 25.0;
      double vx = MAX_V * Math.cos(angle);
      double vy = MAX_V * Math.sin(angle);
      System.out.println("vx=" + vx + " vy=" + vy);

      double y = 6.9282032302755091741097853660235;
      System.out.println(Math.toDegrees(MathUtils.calculateVectorsAngle(0,0,4,0,4,y)));
    }

    // Perp dot product
    {
      Vector2d v1 = new Vector2d(0, 0);
      Vector2d v2 = new Vector2d(0, 0);

      final int start = -100;
      final int end   = 100;

      for (int x1 = start, i = 0; x1 <= end; x1++, i++)
      {
        for (int y1 = start; y1 <= end; y1++)
        {
          for (int x2 = start; x2 <= end; x2++)
          {
            for (int y2 = start; y2 <= end; y2++)
            {
              v1.setVector(x1, y1);
              v2.setVector(x2, y2);
              if (perpDotProduct(v1, v2) != -perpDotProduct(v2, v1))
              {
                System.err.println("perpDot(v1, v2) != -perpDot(v2, v1) for: v1=" + v1 + "; v2=" + v2);
              }
            }
          }
        }

        System.out.println("v1=" + v1);
        System.out.println("v2=" + v2);
        System.out.println((i+1) + "/" + (end - start + 1) + " done.");
      }
    }
  }
}