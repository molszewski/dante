/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import net.java.dante.sim.event.Event;



/**
 * Class representing simulation object that is driven by external 
 * {@link net.java.dante.sim.event.Event} objects. Proper events can be added to this objects
 * (invalid events are filtered in {@link #filterEvent(Event)} method)
 * and then they can be obtained one-by-one in {@link #update(long)} method.
 * 
 * @author M.Olszewski
 */
public abstract class EventDrivenSimulationObject extends SimulationObject
{
  /** List with objects events. */
  private Set<Event> events = new TreeSet<Event>(new TimeComparator());
  /** Total time that has passed. */
  protected long countedTime = 0;
  
  
  /**
   * Creates instance of {@link EventDrivenSimulationObject} 
   * class with specified parameters.
   *
   * @param objectId - simulation object's identifier.
   * @param currentTime - current simulation's time.
   */
  public EventDrivenSimulationObject(int objectId, long currentTime)
  {
    super(objectId);
    
    if (currentTime < 0)
    {
      throw new IllegalArgumentException("Invalid argument currentTime - it must be positive long integer or zero!");
    }
    
    countedTime = currentTime;
  }

  
  /**
   * Checks whether specified event is suitable for this 
   * {@link EventDrivenSimulationObject}. This method should remove 
   * <code>true</code> if event is suitable and can be processed
   * or <code>false</code> otherwise.
   * 
   * @param event - event to check.
   * 
   * @return Returns <code>true</code> if event is suitable and can be processed
   *         or <code>false</code> otherwise.
   */
  protected abstract boolean filterEvent(Event event);
  
  /**
   * Processes specified event. Processed events are removed and never
   * processed again.
   * 
   * @param event - event to process.
   */
  protected abstract void processEvent(Event event);

  /**
   * @see net.java.dante.sim.data.object.SimulationObject#update(long)
   */
  @Override
  public void update(long delta)
  {
    countedTime += delta;
    
    for (Iterator<Event> it = events.iterator(); it.hasNext(); )
    {
      Event event = it.next();
      if (event.getTime() <= countedTime)
      {
        processEvent(event);
        it.remove();
      }
      else
      {
        // Don't execute events with time > totalTime
        break;
      }
    }
  }
  
  /**
   * Adds event to this object.
   * 
   * @param event - event to add.
   */
  public final void addEvent(Event event)
  {
    if (filterEvent(event))
    {
      events.add(event);
    }
  }
  
  /**
   * Shifts time of this {@link EventDrivenSimulationObject} by the specified
   * amount of time.
   * 
   * @param time - the specified amount of time.
   */
  public final void timeShift(long time)
  {
    countedTime += time;
  }
  
  /**
   * Class comparing times of two events.
   * 
   * @author M.Olszewski
   */
  static class TimeComparator implements Comparator<Event>
  {
    /**
     * Compares times of two events.
     * 
     * @param event1 - first event.
     * @param event2 - second event.
     * 
     * @return Returns a negative integer, zero, or a positive integer 
     *         as the time of the first event is less than, equal to, or greater 
     *         than time of the second event.
     */
    public int compare(Event event1, Event event2)
    {
      return ((event1.getTime() < event2.getTime())? -1 : 
             ((event1.equals(event2)) ? 0 : 1));
    }
  }
}