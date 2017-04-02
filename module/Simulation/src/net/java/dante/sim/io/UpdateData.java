/*
 * Created on 2006-07-15
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io;

import net.java.dante.sim.event.EventsRepository;

/**
 * Update data generated as output by servers and maybe clients, treated as
 * input data by clients.
 *
 * @author M.Olszewski
 */
public class UpdateData implements ClientInputData, ServerOutputData
{
  /** Simulation's time. */
  private long time;
  /** Events repository. */
  private EventsRepository repository;


  /**
   * Creates instance of {@link UpdateData} class.
   *
   * @param simTime - server's time.
   * @param eventsRepository - events repository.
   */
  public UpdateData(long simTime, EventsRepository eventsRepository)
  {
    if (eventsRepository == null)
    {
      throw new NullPointerException("Specified eventsRepository is null!");
    }
    if (eventsRepository.getEventsCount() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument eventsRepository - it must contain at least 1 agents group receiving events!");
    }

    time       = simTime;
    repository = eventsRepository;
  }


  /**
   * Gets simulation's time.
   *
   * @return Returns simulation's time.
   */
  public long getTime()
  {
    return time;
  }

  /**
   * Gets events repository.
   *
   * @return Returns events repository.
   */
  public EventsRepository getRepository()
  {
    return repository;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + repository.hashCode();
    result = PRIME * result + (int) (time ^ (time >>> 32));

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof UpdateData))
    {
      final UpdateData other = (UpdateData) object;
      equal = ((time == other.time) && (repository.equals(other.repository)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[time=" + time + "; repository=" + repository + "]");
  }
}