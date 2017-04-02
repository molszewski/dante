/*
 * Created on 2006-09-10
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;

/**
 * Implementation of {@link FriendlyAgentMoveFinishedEvent} interface.
 *
 * @author M.Olszewski
 */
public class FriendlyAgentMoveFinishedEventImpl extends AbstractFriendlyAgentEvent implements FriendlyAgentMoveFinishedEvent
{
  /**
   * Creates instance of {@link FriendlyAgentMoveFinishedEventImpl} class.
   *
   * @param eventId - event's identifier.
   * @param eventTime - event's time.
   * @param friendlyAgentId - friendly agent's identifier.
   */
  public FriendlyAgentMoveFinishedEventImpl(int eventId, long eventTime,
                                            int friendlyAgentId)
  {
    super(eventId, eventTime, EventType.FRIENDLY_AGENT_MOVE_FINISHED, friendlyAgentId);
  }


  /**
   * @see net.java.dante.sim.event.types.AbstractFriendlyAgentEvent#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    return (super.equals(object) &&
            (object instanceof FriendlyAgentMoveFinishedEvent));
  }
}