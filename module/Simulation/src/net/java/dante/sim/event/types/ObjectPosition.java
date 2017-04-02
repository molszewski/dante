/*
 * Created on 2006-08-03
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;


/**
 * Class holding object's position.
 *
 * @author M.Olszewski
 */
class ObjectPosition
{
  /** Object's 'x' coordinate. */
  private double x;
  /** Object's 'y' coordinate. */
  private double y;


  /**
   * Creates instance of {@link ObjectPosition} class with specified
   * parameters.
   *
   * @param positionX - object's 'x' coordinate.
   * @param positionY - object's 'y' coordinate.
   */
  ObjectPosition(double positionX, double positionY)
  {
    x = positionX;
    y = positionY;
  }


  /**
   * Gets object's 'x' coordinate.
   *
   * @return Returns object's 'x' coordinate.
   */
  public double getX()
  {
    return x;
  }

  /**
   * Gets object's 'y' coordinate.
   *
   * @return Returns object's 'y' coordinate.
   */
  public double getY()
  {
    return y;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    long temp = Double.doubleToLongBits(x);
    result = PRIME * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = PRIME * result + (int) (temp ^ (temp >>> 32));

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof ObjectPosition))
    {
      final ObjectPosition other = (ObjectPosition) object;
      equal = ((Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x)) &&
               (Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y)));
    }
    return equal;
  }


  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[positionX=" + x + "; positionY=" + y + "]");
  }
}