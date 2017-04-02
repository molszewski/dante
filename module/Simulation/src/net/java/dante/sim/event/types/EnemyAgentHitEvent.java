/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;


/**
 * Interface defining parameters for {@link net.java.dante.sim.event.EventType#ENEMY_AGENT_HIT} event type.
 *
 * @author M.Olszewski
 */
public interface EnemyAgentHitEvent extends EnemyAgentEvent
{
  /**
   * Gets identifier of an agent who shot a projectile which hit enemy agent.
   *
   * @return Returns identifier of an agent who shot a projectile which
   *         hit enemy agent.
   */
  int getShooterId();
}