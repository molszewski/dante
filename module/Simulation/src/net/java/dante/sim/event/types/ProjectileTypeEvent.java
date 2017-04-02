/*
 * Created on 2006-08-16
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.event.types;

/**
 * Generic interface defining set of parameters for events regarding
 * projectiles adding information about projectile's type.
 *
 * @author M.Olszewski
 */
public interface ProjectileTypeEvent extends ProjectileEvent
{
  /**
   * Gets projectile's type.
   * 
   * @return Returns projectile's type.
   */
  String getProjectileType();
}