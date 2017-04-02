/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.event.types;


/**
 * Interface defining parameters for {@link net.java.dante.sim.event.EventType#PROJECTILE_SEEN} 
 * event type.
 *
 * @author M.Olszewski
 */
public interface ProjectileSeenEvent extends ProjectileTypeEvent, ObjectMoveEventParams
{
  // Intentionally left empty.
}