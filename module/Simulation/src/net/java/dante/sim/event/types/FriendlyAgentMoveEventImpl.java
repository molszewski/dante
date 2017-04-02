/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;


/**
 * Implementation of {@link FriendlyAgentMoveEvent} interface.
 *
 * @author M.Olszewski
 */
final class FriendlyAgentMoveEventImpl extends AbstractFriendlyAgentEvent implements FriendlyAgentMoveEvent
{
  /** Object holding data for performing movement. */
  private ObjectMovement movement;


  /**
   * Creates instance of {@link FriendlyAgentMoveEventImpl} class.
   *
   * @param eventId - event's identifier.
   * @param eventTime - event's time.
   * @param friendlyAgentId - friendly agent's identifier.
   * @param agentDestinationX - agent's destination 'x' coordinate.
   * @param agentDestinationY - agent's destination 'y' coordinate.
   * @param agentSpeedX - agent's horizontal speed.
   * @param agentSpeedY - agent's vertical speed.
   */
  FriendlyAgentMoveEventImpl(int eventId, long eventTime, int friendlyAgentId,
      double agentDestinationX, double agentDestinationY,
      double agentSpeedX, double agentSpeedY)
  {
    super(eventId, eventTime, EventType.FRIENDLY_AGENT_MOVE, friendlyAgentId);

    movement = new ObjectMovement(agentDestinationX, agentDestinationY, agentSpeedX, agentSpeedY);
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
   * @see net.java.dante.sim.event.types.AbstractFriendlyAgentEvent#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof FriendlyAgentMoveEvent))
    {
      final FriendlyAgentMoveEvent other = (FriendlyAgentMoveEvent) object;
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