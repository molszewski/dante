/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;


/**
 * Interface defining parameters for {@link net.java.dante.sim.event.EventType#PROJECTILE_SHOT}
 * event type.
 *
 * @author M.Olszewski
 */
public interface ProjectileShotEvent extends ProjectileTypeEvent, ObjectMoveEventParams
{
  /**
   * Gets shooter identifier.
   *
   * @return Returns shooter's identifier.
   */
  int getShooterId();
}