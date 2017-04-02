/*
 * Created on 2006-07-26
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;


/**
 * Implementation of {@link FriendlyAgentDestroyedEvent} interface.
 *
 * @author M.Olszewski
 */
class FriendlyAgentDestroyedEventImpl extends AbstractFriendlyAgentEvent implements FriendlyAgentDestroyedEvent
{
  /**
   * Creates instance of {@link FriendlyAgentDestroyedEventImpl} class.
   *
   * @param eventId - event's identifier.
   * @param eventTime - event's time.
   * @param friendlyAgentId - friendly agent's identifier.
   */
  FriendlyAgentDestroyedEventImpl(int eventId, long eventTime, int friendlyAgentId)
  {
    super(eventId, eventTime, EventType.FRIENDLY_AGENT_DESTROYED, friendlyAgentId);
  }


  /**
   * @see net.java.dante.sim.event.types.AbstractFriendlyAgentEvent#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    return (super.equals(object) &&
            (object instanceof FriendlyAgentDestroyedEvent));
  }
}