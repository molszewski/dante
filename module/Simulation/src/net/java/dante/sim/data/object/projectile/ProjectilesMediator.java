/*
 * Created on 2006-07-30
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.object.projectile;

import net.java.dante.sim.data.object.WeaponSystem;

/**
 * Interface for subclasses representing mediator design
 * pattern between weapon systems and projectiles.
 *
 * @author M.Olszewski
 */
public interface ProjectilesMediator
{
  /**
   * Create a projectile using specified weapon system.
   *
   * @param ownerGroupId - identifier of projectile owner's group.
   * @param ownerId - identifier of projecitle's owner.
   * @param startX - projectile's start 'x' coordinate.
   * @param startY - projectile's start 'y' coordinate.
   * @param targetX - projectile's target 'x' coordinate.
   * @param targetY - projectile's target 'y' coordinate.
   * @param weaponSystem - weapon system used to create
   *        projectile.
   */
  void createProjectile(int ownerGroupId, int ownerId,
                        double startX, double startY,
                        double targetX, double targetY,
                        WeaponSystem weaponSystem);
}