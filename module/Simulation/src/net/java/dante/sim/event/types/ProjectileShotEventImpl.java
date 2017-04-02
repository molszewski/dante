/*
 * Created on 2006-08-14
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;

/**
 * Implementation of {@link ProjectileDestroyedEvent} interface.
 *
 * @author M.Olszewski
 */
final class ProjectileShotEventImpl extends AbstractProjectileTypeEvent
    implements ProjectileShotEvent
{
  /** Object holding data for performing movement. */
  private ObjectMovement movement;
  /** Shooter's identifier. */
  private int shooterId;


  /**
   * Creates instance of {@link ProjectileShotEventImpl} class.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param projectileType projectile's type.
   * @param projectileId destroyed projectile's identifier.
   * @param projectileX projectile's 'x' coordinate.
   * @param projectileY projectile's 'y' coordinate.
   * @param projectileSpeedX projectile's horizontal speed.
   * @param projectileSpeedY projectile's vertical speed.
   * @param shooterIdentifier shooter's identifier.
   */
  ProjectileShotEventImpl(int eventId, long eventTime,
      String projectileType, int projectileId,
      double projectileX, double projectileY,
      double projectileSpeedX, double projectileSpeedY,
      int shooterIdentifier)
  {
    super(eventId, eventTime, EventType.PROJECTILE_SHOT, projectileType, projectileId);

    movement = new ObjectMovement(projectileX, projectileY,
        projectileSpeedX, projectileSpeedY);

    if (shooterIdentifier < 0)
    {
      throw new IllegalArgumentException("Invalid argument shooterIdentifier - it must be positive integer or zero!");
    }

    shooterId = shooterIdentifier;
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
   * @see net.java.dante.sim.event.types.ProjectileShotEvent#getShooterId()
   */
  public int getShooterId()
  {
    return shooterId;
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
   * @see net.java.dante.sim.event.types.AbstractProjectileTypeEvent#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof ProjectileShotEvent))
    {
      final ProjectileShotEvent other = (ProjectileShotEvent) object;
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