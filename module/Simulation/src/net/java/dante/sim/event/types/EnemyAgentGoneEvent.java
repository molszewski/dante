/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.event.types;


/**
 * Interface defining parameters for {@link net.java.dante.sim.event.EventType#ENEMY_AGENT_GONE} 
 * event type.
 *
 * @author M.Olszewski
 */
public interface EnemyAgentGoneEvent extends EnemyAgentEvent, ObjectMoveEventParams
{
  // Intentionally left empty.
}