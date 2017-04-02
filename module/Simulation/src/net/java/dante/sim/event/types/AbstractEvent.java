/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.Event;
import net.java.dante.sim.event.EventType;


/**
 * Implementation of {@link Event} interface.
 *
 * @author M.Olszewski
 */
abstract class AbstractEvent implements Event
{
  /** Event's time. */
  private long time;
  /** Event's type. */
  private EventType type;
  /** Event's identifier.  */
  private int evId;


  /**
   * Creates instance of {@link AbstractEvent} with specified parameters.
   *
   * @param eventId - event's identifier.
   * @param eventTime - event's time.
   * @param eventType - type of event.
   */
  AbstractEvent(int eventId, long eventTime, EventType eventType)
  {
    if (eventType == null)
    {
      throw new NullPointerException("Specified type is null!");
    }
    if (eventId < 0)
    {
      throw new IllegalArgumentException("Invalid argument eventId - it must be zero or positive integer!");
    }
    if (eventTime < 0)
    {
      throw new IllegalArgumentException("Invalid argument eventTime - it must be zero or positive integer!");
    }

    evId = eventId;
    type = eventType;
    time = eventTime;
  }


  /**
   * @see net.java.dante.sim.event.Event#getTime()
   */
  public long getTime()
  {
    return time;
  }

  /**
   * @see net.java.dante.sim.event.Event#getEventType()
   */
  public EventType getEventType()
  {
    return type;
  }

  /**
   * @see net.java.dante.sim.event.Event#getId()
   */
  public int getId()
  {
    return evId;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + evId;
    result = PRIME * result + (int) (time ^ (time >>> 32));
    result = PRIME * result + type.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (object == this);
    if (!equal && (object instanceof Event))
    {
      Event event = (Event)object;
      equal = ((evId == event.getId()) &&
               (type.equals(event.getEventType())) &&
               (time == event.getTime()));
    }

    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[eventId=" + evId + "; type=" + type.name() + "; time=" + time + "]");
  }
}