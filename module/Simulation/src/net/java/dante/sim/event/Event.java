/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event;

/**
 * Interface for events.
 *
 * @author M.Olszewski
 */
public interface Event
{
  /**
   * Gets event's time.
   *
   * @return Returns event's time.
   */
  long getTime();

  /**
   * Gets type of the event.
   *
   * @return Returns type of the event.
   */
  EventType getEventType();

  /**
   * Gets event's identifier.
   *
   * @return Returns event's identifier.
   */
  int getId();
}