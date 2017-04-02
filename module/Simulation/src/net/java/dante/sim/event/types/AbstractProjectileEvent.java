/*
 * Created on 2006-07-31
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;


/**
 * Abstract base class implementing {@link ProjectileEvent} interface
 * for events regarding projectiles.
 *
 * @author M.Olszewski
 */
abstract class AbstractProjectileEvent extends AbstractEvent implements ProjectileEvent
{
  /** Projectile's identifier. */
  private int projId;


  /**
   * Constructor of {@link AbstractProjectileEvent} class with default access
   * level.
   *
   * @param eventId - event's identifier.
   * @param eventTime - event's time.
   * @param eventType - type of event.
   * @param projectileId - projectile's identifier.
   */
  AbstractProjectileEvent(int eventId, long eventTime, EventType eventType,
      int projectileId)
  {
    super(eventId, eventTime, eventType);

    if (projectileId < 0)
    {
      throw new IllegalArgumentException("Invalid argument projectileId - it must be positive integer or zero!");
    }

    projId = projectileId;
  }


  /**
   * @see net.java.dante.sim.event.types.ProjectileEvent#getProjectileId()
   */
  public int getProjectileId()
  {
    return projId;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;

    int result = super.hashCode();
    result = PRIME * result + projId;

    return result;
  }

  /**
   * @see net.java.dante.sim.event.types.AbstractEvent#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof ProjectileEvent))
    {
      final ProjectileEvent other = (ProjectileEvent) object;
      equal = (projId == other.getProjectileId());
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
        "; projectileId=" + projId + "]");
  }
}