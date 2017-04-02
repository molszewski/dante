/*
 * Created on 2006-08-16
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;


/**
 * Abstract base class implementing {@link ProjectileTypeEvent} interface
 * for events regarding projectiles.
 *
 * @author M.Olszewski
 */
abstract class AbstractProjectileTypeEvent extends AbstractProjectileEvent implements ProjectileTypeEvent
{
  /** Projectile's type. */
  private String type;


  /**
   * Constructor of {@link AbstractProjectileEvent} class with default access
   * level.
   *
   * @param eventId - event's identifier.
   * @param eventTime - event's time.
   * @param eventType - type of event.
   * @param projectileType - projectile's type.
   * @param projectileId - projectile's identifier.
   */
  AbstractProjectileTypeEvent(int eventId, long eventTime, EventType eventType,
      String projectileType, int projectileId)
  {
    super(eventId, eventTime, eventType, projectileId);

    if (projectileType == null)
    {
      throw new NullPointerException("Specified projectileType is null!");
    }
    if (projectileType.length() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument projectileType - it cannot be empty string!");
    }

    type = projectileType;
  }


  /**
   * @see net.java.dante.sim.event.types.ProjectileTypeEvent#getProjectileType()
   */
  public String getProjectileType()
  {
    return type;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;

    int result = super.hashCode();
    result = PRIME * result + type.hashCode();

    return result;
  }

  /**
   * @see net.java.dante.sim.event.types.AbstractEvent#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof ProjectileTypeEvent))
    {
      final ProjectileTypeEvent other = (ProjectileTypeEvent) object;
      equal = (type.equals(other.getProjectileType()));
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
        "; projectileType=" + type + "]");
  }
}