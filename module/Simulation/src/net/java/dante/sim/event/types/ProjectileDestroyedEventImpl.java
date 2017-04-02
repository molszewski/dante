/*
 * Created on 2006-07-25
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
final class ProjectileDestroyedEventImpl extends AbstractProjectileEvent
    implements ProjectileDestroyedEvent
{
  /** Destroyed projectile's most recent position. */
  private ObjectPosition position;


  /**
   * Creates instance of {@link ProjectileDestroyedEventImpl} class.
   *
   * @param eventId - event's identifier.
   * @param eventTime - event's time.
   * @param projectileId - destroyed projectile's identifier.
   * @param projectileX - last projectile's 'x' coordinate.
   * @param projectileY - last projectile's 'y' coordinate.
   */
  ProjectileDestroyedEventImpl(int eventId, long eventTime, int projectileId,
      double projectileX, double projectileY)
  {
    super(eventId, eventTime, EventType.PROJECTILE_DESTROYED, projectileId);

    position = new ObjectPosition(projectileX, projectileY);
  }


  /**
   * @see net.java.dante.sim.event.types.ProjectileDestroyedEvent#getProjectileX()
   */
  public double getProjectileX()
  {
    return position.getX();
  }

  /**
   * @see net.java.dante.sim.event.types.ProjectileDestroyedEvent#getProjectileY()
   */
  public double getProjectileY()
  {
    return position.getY();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = super.hashCode();

    result = PRIME * result + position.hashCode();

    return result;
  }

  /**
   * @see net.java.dante.sim.event.types.AbstractProjectileEvent#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof ProjectileDestroyedEvent))
    {
      final ProjectileDestroyedEvent other = (ProjectileDestroyedEvent) object;
      equal = ((Double.doubleToLongBits(getProjectileX()) == Double.doubleToLongBits(other.getProjectileX())) &&
               (Double.doubleToLongBits(getProjectileY()) == Double.doubleToLongBits(other.getProjectileY())));
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
        "; position=" + position + "]");
  }
}