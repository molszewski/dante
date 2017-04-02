/*
 * Created on 2006-07-27
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.util.math;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class containing methods for calculating following values:
 * <ul>
 * <li>magnitude of vector,
 * <li>angle between two points, using specified origin point for calculations.
 * <li>velocity components, using specified maximum velocity and start and
 *     end locations,
 * <li>all intersection points between specified line and rectangle,
 * <li>first intersection point of line and rectangle.
 * </ul>
 * All methods calculates values for two dimensional space.
 *
 * @author M.Olszewski
 */
public final class MathUtils
{
  /** Vector with zero velocity components. */
  private final static Vector2d ZERO_VELOCITY = new Vector2d(0.0, 0.0);
  /** Maximum number of intersections between a line and a rectangle.  */
  private final static int MAX_LINE_TO_RECT_INTERSECTIONS = 2;


  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private MathUtils()
  {
    // Intentionally left empty.
  }


  /**
   * Calculates magnitude for vector with specified 'x' and 'y' components.
   *
   * @param vectorX - 'x' vector component.
   * @param vectorY - 'y' vector component.
   *
   * @return Returns calculated magnitude for vector with specified
   *         'x' and 'y' components.
   */
  public static double calculateVectorMagnitude(double vectorX, double vectorY)
  {
    return (Math.sqrt(vectorX * vectorX + vectorY * vectorY));
  }

  /**
   * Calculates distance between two specified points.
   *
   * @param firstX - 'x' coordinate of first point.
   * @param firstY - 'y' coordinate of first point.
   * @param secondX - 'x' coordinate of second point.
   * @param secondY - 'y' coordinate of second point.
   *
   * @return Returns calculated distance between two specified points.
   */
  public static double calculateDistance(double firstX, double firstY,
      double secondX, double secondY)
  {
    return calculateVectorMagnitude(secondX - firstX, secondY - firstY);
  }

  /**
   * Calculates angle between two specified points in two dimensional space,
   * using origin 'x' and 'y' coordinates as base for calculations. Calculated
   * angle is returned in radians.
   *
   * @param originX - origin point 'x' coordinate.
   * @param originY - origin point 'y' coordinate.
   * @param point1X - first point 'x' coordinate.
   * @param point1Y - first point 'y' coordinate.
   * @param point2X - second point 'x' coordinate.
   * @param point2Y - second point 'y' coordinate.
   *
   * @return Returns calculated angle (in radians) between specified points.
   */
  public static double calculateVectorsAngle(double originX, double originY,
      double point1X, double point1Y, double point2X, double point2Y)
  {
    return Vector2d.calculateAngle(
        new Vector2d(point1X - originX, point1Y - originY),
        new Vector2d(point2X - originX, point2Y - originY));
  }

  /**
   * Calculates vertical and horizontal velocity components based upon maximum
   * possible overall velocity, start position coordinates and end position
   * coordinates. This method returns {@link Vector2d} object containing
   * horizontal and vertical velocity components.
   *
   * @param maxVelocity - maximum possible overall velocity.
   * @param startX - start 'x' coordinate.
   * @param startY - start 'y' coordinate.
   * @param endX - end 'x' coordinate.
   * @param endY - end 'y' coordinate.
   *
   * @return Returns {@link Vector2d} objects containing both velocity components.
   */
  public static Vector2d calculateVelocity(double maxVelocity, double startX,
      double startY, double endX, double endY)
  {
    if (maxVelocity < 0.0)
    {
      throw new IllegalArgumentException("Invalid argument maxVelocity - it must be positive real number of zero!");
    }

    Vector2d velocity = null;
    if ((startX != endX) || (startY != endY))
    {
      Vector2d originToTarget = new Vector2d(endX - startX, endY - startY);
      // extra vector - only to calculate angle between it and 'originToTarget'
      Vector2d originToOther = new Vector2d(0, endY - startY);

      double speedX = 0.0;
      double speedY = 0.0;

      if (!originToOther.hasZeroLength())
      {
        // angle between current agent point and target point
        double angle = Vector2d.calculateAngle(originToTarget, originToOther);
        speedX = maxVelocity * Math.sin(angle);
        speedY = maxVelocity * Math.cos(angle);
      }
      else
      {
        if (originToTarget.getX() != 0.0)
        {
          speedX = maxVelocity;
        }
        else if (originToTarget.getY() != 0.0)
        {
          speedY = maxVelocity;
        }
      }

      speedX = (originToTarget.getX() < 0.0)? -speedX : speedX;
      speedY = (originToTarget.getY() < 0.0)? -speedY : speedY;

      velocity = new Vector2d(speedX, speedY);
    }
    else
    {
      velocity = ZERO_VELOCITY;
    }

    return velocity;
  }

  /**
   * Calculates all intersection points between specified line and specified
   * rectangle. This method can return empty array if no intersection points
   * are found, array containing one element or array containing two elements
   * (maximum number of intersection points between straight line and rectangle).
   *
   * @param line - the specified line.
   * @param rect - the specified rectangle.
   *
   * @return Returns array containing all found intersection points.
   */
  public static Point2d[] calculateIntersections(Line2d line, Rect2d rect)
  {
    Set<Point2d> points = new HashSet<Point2d>(MAX_LINE_TO_RECT_INTERSECTIONS);

    Line2d rectLine = new Line2d(rect.getX(), rect.getY(), rect.getX() + rect.getWidth(), rect.getY());
    Point2d p1 = rectLine.intersection(line);
    if (p1 != null)
    {
      points.add(p1);
    }

    rectLine.setLine(rect.getX() + rect.getWidth(), rect.getY(), rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
    Point2d p2 = rectLine.intersection(line);
    if (p2 != null)
    {
      points.add(p2);
    }

    if (points.size() < MAX_LINE_TO_RECT_INTERSECTIONS)
    {
      rectLine.setLine(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight(), rect.getX(), rect.getY() + rect.getHeight());
      Point2d p3 = rectLine.intersection(line);
      if (p3 != null)
      {
        points.add(p3);
      }

      if (points.size() < MAX_LINE_TO_RECT_INTERSECTIONS)
      {
        rectLine.setLine(rect.getX(), rect.getY() + rect.getHeight(), rect.getX(), rect.getY());
        Point2d p4 = rectLine.intersection(line);
        if (p4 != null)
        {
          points.add(p4);
        }
      }
    }

    return points.toArray(new Point2d[points.size()]);
  }

  /**
   * Calculates first intersection point between specified line and specified
   * rectangle. This method can return <code>null</code> value if
   * intersection point was not found or point containing coordinates of
   * found intersection point.
   *
   * @param line - the specified line.
   * @param rect - the specified rectangle.
   *
   * @return Returns point containing found intersection point or <code>null</code>
   *         if intersection point was not found.
   */
  public static Point2d calculateFirstIntersection(Line2d line, Rect2d rect)
  {
    Point2d point = null;

    // First side of rectangle
    Line2d rectLine = new Line2d(rect.getX(), rect.getY(), rect.getX() + rect.getWidth(), rect.getY());
    point = rectLine.intersection(line);
    if (point == null)
    {
      // Second side of rectangle
      rectLine.setLine(rect.getX() + rect.getWidth(), rect.getY(), rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
      point = rectLine.intersection(line);
      if (point == null)
      {
        // Third side of rectangle
        rectLine.setLine(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight(), rect.getX(), rect.getY() + rect.getHeight());
        point = rectLine.intersection(line);
        if (point == null)
        {
          // Fourth side of rectangle
          rectLine.setLine(rect.getX(), rect.getY() + rect.getHeight(), rect.getX(), rect.getY());
          point = rectLine.intersection(line);
        }
      }
    }

    return point;
  }

  /**
   * Checks whether the specified circle intersects with the specified
   * rectangle.
   *
   * @param circle - the specified circle.
   * @param rect - the specified rectangle.
   *
   * @return Returns <code>true</code> if circle intersects with rectangle,
   *         <code>false</code> otherwise.
   */
  public static boolean checkIntersection(Circle2d circle, Rect2d rect)
  {
    return (circle.contains(rect.getX(), rect.getY()) ||
            circle.contains(rect.getX() + rect.getWidth(), rect.getY()) ||
            circle.contains(rect.getX(), rect.getY() + rect.getHeight()) ||
            circle.contains(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight()));
  }

  /**
   * Tests.
   *
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    Rect2d rect = new Rect2d(2.0, 6.0, 4, 4);
    {
      Line2d line = new Line2d(0.0, 4.0, 8.0, 4.0);

      Point2d[] points = calculateIntersections(line, rect);
      System.out.println("points size=" + points.length);
      for (int i = 0; i < points.length; i++)
      {
        System.out.println("point " + i + ". " + points[i]);
      }
    }
    {
      Line2d line = new Line2d(8.0, 0.0, 8.0, 8.0);
      Point2d[] points = calculateIntersections(line, rect);
      System.out.println("points size=" + points.length);
      for (int i = 0; i < points.length; i++)
      {
        System.out.println("point " + i + ". " + points[i]);
      }
    }
    {
      Line2d line = new Line2d(1.0, 0.0, 5.0, 8.0);
      Point2d[] points = calculateIntersections(line, rect);
      System.out.println("points size=" + points.length);
      for (int i = 0; i < points.length; i++)
      {
        System.out.println("point " + i + ". " + points[i]);
      }
    }
    {
      Line2d line = new Line2d(2.0, 6.0, 6.0, 11.0);
      Point2d[] points = calculateIntersections(line, rect);
      System.out.println("points size=" + points.length);
      for (int i = 0; i < points.length; i++)
      {
        System.out.println("point " + i + ". " + points[i]);
      }
    }
    {
      Line2d line = new Line2d(1.0, 6.0, 2.0, 6.0);
      Point2d[] points = calculateIntersections(line, rect);
      System.out.println("points size=" + points.length);
      for (int i = 0; i < points.length; i++)
      {
        System.out.println("point " + i + ". " + points[i]);
      }
    }
  }
}