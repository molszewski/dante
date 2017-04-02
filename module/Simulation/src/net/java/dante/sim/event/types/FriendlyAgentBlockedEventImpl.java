/*
 * Created on 2006-08-03
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;


/**
 * Implementation of {@link FriendlyAgentBlockedEvent} interface.
 *
 * @author M.Olszewski
 */
class FriendlyAgentBlockedEventImpl extends AbstractFriendlyAgentEvent implements FriendlyAgentBlockedEvent
{
  /** Coordinates of blocked agent.  */
  private ObjectPosition blocked;


  /**
   * Creates instance of {@link FriendlyAgentHitEventImpl} class.
   *
   * @param eventId - event's identifier.
   * @param eventTime - event's time.
   * @param friendlyAgentId - friendly agent's identifier.
   * @param blockedX - blocked agent's 'x' coordinate.
   * @param blockedY - blocked agent's 'y' coordinate.
   */
  FriendlyAgentBlockedEventImpl(int eventId, long eventTime, int friendlyAgentId,
      double blockedX, double blockedY)
  {
    super(eventId, eventTime, EventType.FRIENDLY_AGENT_BLOCKED, friendlyAgentId);

    blocked = new ObjectPosition(blockedX, blockedY);
  }


  /**
   * @see net.java.dante.sim.event.types.FriendlyAgentBlockedEvent#getBlockedX()
   */
  public double getBlockedX()
  {
    return blocked.getX();
  }

  /**
   * @see net.java.dante.sim.event.types.FriendlyAgentBlockedEvent#getBlockedY()
   */
  public double getBlockedY()
  {
    return blocked.getY();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = super.hashCode();

    result = PRIME * result + blocked.hashCode();

    return result;
  }

  /**
   * @see net.java.dante.sim.event.types.AbstractFriendlyAgentEvent#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof FriendlyAgentBlockedEvent))
    {
      final FriendlyAgentBlockedEvent other = (FriendlyAgentBlockedEvent) object;
      equal = ((Double.doubleToLongBits(getBlockedX()) == Double.doubleToLongBits(other.getBlockedX())) &&
               (Double.doubleToLongBits(getBlockedY()) == Double.doubleToLongBits(other.getBlockedY())));
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
        "; position=" + blocked + "]");
  }
}