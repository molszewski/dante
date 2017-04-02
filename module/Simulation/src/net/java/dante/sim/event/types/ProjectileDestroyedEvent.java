/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;


/**
 * Interface defining parameters for {@link EventType#PROJECTILE_DESTROYED} 
 * event type.
 *
 * @author M.Olszewski
 */
public interface ProjectileDestroyedEvent extends ProjectileEvent
{
  /**
   * Gets last 'x' coordinate of projectile (also explosion's epicentre).
   * 
   * @return Returns last 'x' coordinate of projectile (also explosion's epicentre).
   */
  double getProjectileX();
  
  /**
   * Gets last 'y' coordinate of projectile (also explosion's epicentre).
   * 
   * @return Returns last 'y' coordinate of projectile (also explosion's epicentre).
   */
  double getProjectileY();
}