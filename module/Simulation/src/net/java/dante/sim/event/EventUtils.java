/*
 * Created on 2006-07-26
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.event;

import java.util.Arrays;

/**
 * Utility class delivering methods for creating instances of following classes, 
 * connected with {@link net.java.dante.sim.event} package:
 * <ol>
 * <li>{@link EventsRepositoryBuilder} class,
 * <li>{@link EventsRepository} class
 * </ol>
 * 
 * @author M.Olszewski
 */
public class EventUtils
{
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private EventUtils()
  {
    // Intentionally left empty.
  }
  
 
  /**
   * Creates default instance of class implementing {@link EventsRepositoryBuilder}
   * interface.
   * 
   * @param agentsGroupId - group of agents identifier.
   * 
   * @return Returns created instance of class implementing {@link EventsRepositoryBuilder}
   *         interface.
   */
  public static EventsRepositoryBuilder createDefaultBuilder(int agentsGroupId)
  {
    if (agentsGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsGroupId - it must be positive integer or zero!");
    }
    
    return new EventsRepositoryBuilderImpl(agentsGroupId);
  }
  
  /**
   * Creates instance of {@link EventsRepository} class with specified parameters.
   * 
   * @param agentsGroupId - agents group identifier.
   * @param events - array with events.
   * 
   * @return Returns instance of {@link EventsRepository} class.
   */
  public static EventsRepository createGroupEventsRepository(int agentsGroupId, 
      Event[] events)
  {
    if (events == null)
    {
      throw new NullPointerException("Specified events is null!");
    }
    if (agentsGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsGroupId - it must be positive integer or zero!");
    }
    
    return new EventsRepositoryImpl(agentsGroupId, Arrays.asList(events));
  }
}