/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;


/**
 * Implementation of {@link EnemyAgentDestroyedEvent} interface.
 *
 * @author M.Olszewski
 */
final class EnemyAgentDestroyedEventImpl extends AbstractEnemyAgentEvent implements EnemyAgentDestroyedEvent
{
  /** Shooter identifier */
  private int shooterId;


  /**
   * Creates instance of {@link EnemyAgentDestroyedEventImpl} class.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param enemyAgentId enemy agent's identifier.
   * @param shooterIdentifier shooter identifier.
   */
  EnemyAgentDestroyedEventImpl(int eventId, long eventTime,
                               int enemyAgentId, int shooterIdentifier)
  {
    super(eventId, eventTime, EventType.ENEMY_AGENT_DESTROYED, enemyAgentId);

    if (shooterIdentifier < 0)
    {
      throw new IllegalArgumentException("Invalid argument shooterIdentifier - it must be positive integer or zero!");
    }

    shooterId = shooterIdentifier;
  }


  /**
   * @see net.java.dante.sim.event.types.EnemyAgentDestroyedEvent#getShooterId()
   */
  public int getShooterId()
  {
    return shooterId;
  }

  /**
   * @see net.java.dante.sim.event.types.AbstractEnemyAgentEvent#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    return super.equals(object) &&
           (object instanceof EnemyAgentDestroyedEvent);
  }
}