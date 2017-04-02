/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;


/**
 * Interface defining parameters for {@link EventType#ENEMY_AGENT_SEEN} 
 * event type.
 *
 * @author M.Olszewski
 */
public interface EnemyAgentSeenEvent extends EnemyAgentEvent, ObjectMoveEventParams
{
  // Intentionally left empty.
}