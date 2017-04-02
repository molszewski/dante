/*
 * Created on 2006-08-17
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event;

import java.util.List;

/**
 * Implementation of {@link EventsRepository} interface.
 *
 * @author M.Olszewski
 */
class EventsRepositoryImpl implements EventsRepository
{
  /** Agents group identifier. */
  int groupId;
  /** List with {@link Event} objects. */
  private List<Event> events;


  /**
   * Creates instance of {@link EventsRepositoryImpl} class.
   *
   * @param agentsGroupId - agents group identifier.
   * @param eventsList - list with {@link Event} objects.
   */
  EventsRepositoryImpl(int agentsGroupId, List<Event> eventsList)
  {
    if (eventsList == null)
    {
      throw new NullPointerException("Specified eventsList is null!");
    }
    if (eventsList.size() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument eventsList - it must contain at least 1 element!");
    }
    if (agentsGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsGroupId - it must be positive integer or zero!");
    }

    groupId = agentsGroupId;
    events  = eventsList;
  }


  /**
   * @see net.java.dante.sim.event.EventsRepository#getGroupId()
   */
  public int getGroupId()
  {
    return groupId;
  }

  /**
   * @see net.java.dante.sim.event.EventsRepository#getEventsCount()
   */
  public int getEventsCount()
  {
    return events.size();
  }

  /**
   * @see net.java.dante.sim.event.EventsRepository#getEvent(int)
   */
  public Event getEvent(int index)
  {
    return events.get(index);
  }

  /**
   * @see net.java.dante.sim.event.EventsRepository#clear()
   */
  public void clear()
  {
    events.clear();
  }


  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + events.hashCode();
    result = PRIME * result + groupId;

    return result;
  }


  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof EventsRepository))
    {
      final EventsRepository other = (EventsRepository) object;
      equal = ((groupId == other.getGroupId()) &&
               (events.size() == other.getEventsCount()));
      if (equal)
      {
        for (int i = 0, size = events.size(); i < size; i++)
        {
          if (!events.get(i).equals(other.getEvent(i)))
          {
            equal = false;
            break;
          }
        }
      }

    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[groupId=" + groupId + "; events=" + events + "]");
  }
}