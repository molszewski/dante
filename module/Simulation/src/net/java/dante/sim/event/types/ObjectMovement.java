/*
 * Created on 2006-08-03
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;


/**
 * Class holding all parameters of object's movement.
 *
 * @author M.Olszewski
 */
class ObjectMovement
{
  /** Object's destination coordinates. */
  private ObjectPosition destination;
  /** Object's horizontal speed. */
  private double speedX;
  /** Object's vertical speed. */
  private double speedY;


  /**
   * Creates instance of {@link ObjectPosition} class with specified
   * parameters.
   *
   * @param objectDestinationX - object's destination'x' coordinate.
   * @param objectDestinationY - object's destination 'y' coordinate.
   * @param objectSpeedX - object's horizontal speed.
   * @param objectSpeedY - object's vertical speed.
   */
  ObjectMovement(double objectDestinationX, double objectDestinationY, double objectSpeedX,
      double objectSpeedY)
  {
    destination = new ObjectPosition(objectDestinationX, objectDestinationY);
    speedX = objectSpeedX;
    speedY = objectSpeedY;
  }


  /**
   * Gets object's destination 'x' coordinate.
   *
   * @return Returns object's destination 'x' coordinate.
   */
  public double getDestinationX()
  {
    return destination.getX();
  }

  /**
   * Gets object's destination 'y' coordinate.
   *
   * @return Returns object's destination 'y' coordinate.
   */
  public double getDestinationY()
  {
    return destination.getY();
  }

  /**
   * Gets object's horizontal speed.
   *
   * @return Returns object's horizontal speed.
   */
  public double getSpeedX()
  {
    return speedX;
  }

  /**
   * Gets object's vertical speed.
   *
   * @return Returns object's vertical speed.
   */
  public double getSpeedY()
  {
    return speedY;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + destination.hashCode();
    long temp = Double.doubleToLongBits(speedX);
    result = PRIME * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(speedY);
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
    if (!equal && (object instanceof ObjectMovement))
    {
      final ObjectMovement other = (ObjectMovement) object;
      equal = (destination.equals(other.destination) &&
               (Double.doubleToLongBits(speedX) == Double.doubleToLongBits(other.speedX)) &&
               (Double.doubleToLongBits(speedY) == Double.doubleToLongBits(other.speedY)));
    }
    return equal;
  }


  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[destinationX=" + destination.getX() +
        "; destinationY=" + destination.getY() + "; speedX=" + speedX +
        "; speedY=" + speedY + "]");
  }
}