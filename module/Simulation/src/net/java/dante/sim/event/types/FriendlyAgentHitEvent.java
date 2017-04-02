/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.event.types;

/**
 * Interface defining parameters for {@link net.java.dante.sim.event.EventType#FRIENDLY_AGENT_HIT} 
 * event type.
 *
 * @author M.Olszewski
 */
public interface FriendlyAgentHitEvent extends FriendlyAgentEvent
{
  /**
   * Gets amount of damage that was dealt to agent.
   * 
   * @return Returns amount of damage that was dealt to agent.
   */
  int getDamage();
}