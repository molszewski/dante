/*
 * Created on 2006-08-17
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.event;

/**
 * Events repository: contains all events regarding one specified group of 
 * agents.
 * 
 * @author M.Olszewski
 */
public interface EventsRepository
{
  /**
   * Gets agents group's identifier.
   * 
   * @return Returns agents group's identifier.
   */
  int getGroupId();
  
  /**
   * Gets number of events stored in this repository.
   * 
   * @return Returns number of events stored in this repository.
   */
  int getEventsCount();
  
  /**
   * Gets event from the specified index.
   * 
   * @param index - the specified index.
   * 
   * @return Returns event from the specified index.
   */
  Event getEvent(int index);
  
  /**
   * Clears all information stored in this {@link EventsRepository}.
   */
  void clear();
}