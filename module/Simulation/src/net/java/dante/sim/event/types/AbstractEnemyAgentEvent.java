/*
 * Created on 2006-08-03
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;

/**
 * Abstract base class implementing {@link EnemyAgentEvent} interface
 * for events regarding enemy agents.
 *
 * @author M.Olszewski
 */
abstract class AbstractEnemyAgentEvent extends AbstractEvent implements EnemyAgentEvent
{
  /** Enemy agent's identifier. */
  private int enemyId;


  /**
   * Creates instance of {@link AbstractEnemyAgentEvent} class.
   *
   * @param eventId - event's identifier.
   * @param eventTime - event's time.
   * @param eventType - event's type.
   * @param enemyAgentId - enemy agent's identifier.
   */
  AbstractEnemyAgentEvent(int eventId, long eventTime, EventType eventType, int enemyAgentId)
  {
    super(eventId, eventTime, eventType);

    if (enemyAgentId < 0)
    {
      throw new IllegalArgumentException("Invalid argument enemyAgentId - it must be positive integer or zero!");
    }

    enemyId = enemyAgentId;
  }


  /**
   * @see net.java.dante.sim.event.types.EnemyAgentEvent#getEnemyAgentId()
   */
  public int getEnemyAgentId()
  {
    return enemyId;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;

    int result = super.hashCode();
    result = PRIME * result + enemyId;

    return result;
  }

  /**
   * @see net.java.dante.sim.event.types.AbstractEvent#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof EnemyAgentEvent))
    {
      final EnemyAgentEvent other = (EnemyAgentEvent) object;
      equal = (enemyId == other.getEnemyAgentId());
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) + "; enemyAgentId=" + enemyId + "]");
  }
}