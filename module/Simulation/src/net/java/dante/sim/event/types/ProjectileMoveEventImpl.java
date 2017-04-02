/*
 * Created on 2006-08-16
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;

/**
 * Implementation of {@link ProjectileMoveEvent} interface.
 *
 * @author M.Olszewski
 */
class ProjectileMoveEventImpl extends AbstractProjectileEvent implements ProjectileMoveEvent
{
  /** Object holding data for performing movement. */
  private ObjectMovement movement;


  /**
   * Creates instance of {@link ProjectileMoveEventImpl} class.
   *
   * @param eventId - event's identifier.
   * @param eventTime - event's time.
   * @param projectileId - projectile's identifier.
   * @param projectileDestinationX - projectile's destination 'x' coordinate.
   * @param projectileDestinationY - projectile's destination 'y' coordinate.
   * @param projectileSpeedX - projectile's horizontal speed.
   * @param projectileSpeedY - projectile's vertical speed.
   */
  public ProjectileMoveEventImpl(int eventId, long eventTime, int projectileId,
      double projectileDestinationX, double projectileDestinationY,
      double projectileSpeedX, double projectileSpeedY)
  {
    super(eventId, eventTime, EventType.PROJECTILE_MOVE, projectileId);

    movement = new ObjectMovement(projectileDestinationX, projectileDestinationY,
        projectileSpeedX, projectileSpeedY);
  }


  /**
   * @see net.java.dante.sim.event.types.ObjectMoveEventParams#getDestinationX()
   */
  public double getDestinationX()
  {
    return movement.getDestinationX();
  }

  /**
   * @see net.java.dante.sim.event.types.ObjectMoveEventParams#getDestinationY()
   */
  public double getDestinationY()
  {
    return movement.getDestinationY();
  }

  /**
   * @see net.java.dante.sim.event.types.ObjectMoveEventParams#getSpeedX()
   */
  public double getSpeedX()
  {
    return movement.getSpeedX();
  }

  /**
   * @see net.java.dante.sim.event.types.ObjectMoveEventParams#getSpeedY()
   */
  public double getSpeedY()
  {
    return movement.getSpeedY();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = super.hashCode();

    result = PRIME * result + movement.hashCode();

    return result;
  }

  /**
   * @see net.java.dante.sim.event.types.AbstractProjectileEvent#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof ProjectileMoveEvent))
    {
      final ProjectileMoveEvent other = (ProjectileMoveEvent) object;
      equal = ((Double.doubleToLongBits(getDestinationX()) == Double.doubleToLongBits(other.getDestinationX())) &&
               (Double.doubleToLongBits(getDestinationY()) == Double.doubleToLongBits(other.getDestinationY())) &&
               (Double.doubleToLongBits(getSpeedX()) == Double.doubleToLongBits(other.getSpeedX())) &&
               (Double.doubleToLongBits(getSpeedY()) == Double.doubleToLongBits(other.getSpeedY())));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) +
        "; movement=" + movement + "]");
  }
}