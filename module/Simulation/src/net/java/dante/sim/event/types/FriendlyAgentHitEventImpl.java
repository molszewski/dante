/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;


/**
 * Implementation of {@link FriendlyAgentHitEvent} interface.
 *
 * @author M.Olszewski
 */
final class FriendlyAgentHitEventImpl extends AbstractFriendlyAgentEvent implements FriendlyAgentHitEvent
{
  /** Damage received by agent. */
  private int damage;


  /**
   * Creates instance of {@link FriendlyAgentHitEventImpl} class.
   *
   * @param eventId - event's identifier.
   * @param eventTime - event's time.
   * @param friendlyAgentId - friendly agent's identifier.
   * @param damageAmount - amount of received damage.
   */
  FriendlyAgentHitEventImpl(int eventId, long eventTime, int friendlyAgentId, int damageAmount)
  {
    super(eventId, eventTime, EventType.FRIENDLY_AGENT_HIT, friendlyAgentId);

    if (damageAmount <= 0)
    {
      throw new IllegalArgumentException("Invalid argument damageAmount - it must be positive integer!");
    }
    damage = damageAmount;
  }


  /**
   * @see net.java.dante.sim.event.types.FriendlyAgentHitEvent#getDamage()
   */
  public int getDamage()
  {
    return damage;
  }

  /**
   * @see net.java.dante.sim.event.types.AbstractFriendlyAgentEvent#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof FriendlyAgentHitEvent))
    {
      final FriendlyAgentHitEvent other = (FriendlyAgentHitEvent) object;
      equal = (damage == other.getDamage());
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
    return (superString.substring(0, superString.length() - 1) +
        "; damage=" + damage + "]");
  }
}