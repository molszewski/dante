/*
 * Created on 2006-08-17
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.event;

/**
 * Interface for events repository builders. Events are stored internally
 * in builder until {@link #build()} method is invoked - then inner
 * representation is cleared and events are moved to proper 
 * {@link net.java.dante.sim.event.EventsRepository} object, which reference is returned
 * by {@link #build()} method.
 *
 * @author M.Olszewski
 */
public interface EventsRepositoryBuilder
{
  /**
   * Gets group identifier of this {@link net.java.dante.sim.event.EventsRepositoryBuilder}.
   * 
   * @return Returns group identifier of this 
   *         {@link net.java.dante.sim.event.EventsRepositoryBuilder}.
   */
  int getGroupId();
  
  /**
   * Adds event to this events repository builder. Added events must be
   * transformed into {@link net.java.dante.sim.event.EventsRepository} by call to {@link #build()} 
   * method.<p>
   * The same {@link Event} object cannot be added more than once 
   * for the same agent and group.
   * 
   * @param event - event to add.
   */
  void addEvent(Event event);
  
  /**
   * Builds all stored events into {@link net.java.dante.sim.event.EventsRepository} object.
   * Building means here that all events stored in inner representation 
   * are transformed into proper structure that can be obtained through
   * returned {@link net.java.dante.sim.event.EventsRepository} object. 
   * All data from inner representation must be cleared after successful 
   * building, so next {@link #addEvent(Event)} 
   * call will add first event.
   * If no event was added to this builder, <code>null</code> value must be
   * returned.
   * 
   * @return Returns {@link net.java.dante.sim.event.EventsRepository} object with proper
   *         events or <code>null</code> if no events were added to this
   *         builder before calling this method.
   */
  EventsRepository build();
}