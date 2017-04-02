/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.Event;


/**
 * Generic interface defining set of parameters for events regarding
 * projectiles.
 *
 * @author M.Olszewski
 */
public interface ProjectileEvent extends Event
{
  /**
   * Gets projectile's identifier.
   * 
   * @return Returns projectile's identifier.
   */
  int getProjectileId();
}