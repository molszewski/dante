/*
 * Created on 2006-08-03
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;


/**
 * Abstract base class implementing {@link FriendlyAgentEvent} interface
 * for events regarding agents controlled by client (also called friendly agents).
 *
 * @author M.Olszewski
 */
abstract class AbstractFriendlyAgentEvent extends AbstractEvent implements FriendlyAgentEvent
{
  /** Friendly agent's identifier. */
  private int friendlyId;


  /**
   * Constructor of {@link FriendlyAgentHitEventImpl} class with default access
   * level.
   *
   * @param eventId - event's identifier.
   * @param eventTime - event's time.
   * @param eventType - event's type.
   * @param friendlyAgentId - friendly agent's identifier.
   */
  AbstractFriendlyAgentEvent(int eventId, long eventTime, EventType eventType, int friendlyAgentId)
  {
    super(eventId, eventTime, eventType);

    if (friendlyAgentId < 0)
    {
      throw new IllegalArgumentException("Invalid argument friendlyAgentId - it must be positive integer or zero!");
    }

    friendlyId = friendlyAgentId;
  }


  /**
   * @see net.java.dante.sim.event.types.FriendlyAgentEvent#getFriendlyAgentId()
   */
  public int getFriendlyAgentId()
  {
    return friendlyId;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;

    int result = super.hashCode();
    result = PRIME * result + friendlyId;

    return result;
  }

  /**
   * @see net.java.dante.sim.event.types.AbstractEvent#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof FriendlyAgentEvent))
    {
      final FriendlyAgentEvent other = (FriendlyAgentEvent) object;
      equal = (friendlyId == other.getFriendlyAgentId());
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
    return (superString.substring(0, superString.length() - 1) + "; friendlyId=" + friendlyId + "]");
  }
}