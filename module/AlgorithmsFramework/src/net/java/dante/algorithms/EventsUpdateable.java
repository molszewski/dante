/*
 * Created on 2006-09-01
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms;

import net.java.dante.sim.event.EventsRepository;

/**
 * Interface for classes that are capable of updating themselves by using
 * the specified event's repository.
 *
 * @author M.Olszewski
 */
interface EventsUpdateable
{
  /**
   * Updates this object by using the specified event's repository.
   *
   * @param repository the specified event's repository.
   */
  void update(EventsRepository repository);
}