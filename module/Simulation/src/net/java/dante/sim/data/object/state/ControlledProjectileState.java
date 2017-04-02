/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

/**
 * Interface defining set of controlled projectile attributes: range,
 * explosion range, damage and maximum speed.
 * 
 * @author M.Olszewski
 */
public interface ControlledProjectileState extends ObjectMaxSpeedState
{
  /**
   * Gets maximum distance that will be traversed by projectile.
   * 
   * @return Returns maximum distance that will be traversed by projectile.
   */
  double getDistance();
  
  /**
   * Gets projectile's explosion range.
   * 
   * @return Returns projectile's explosion range.
   */
  double getExplosionRange();
  
  /**
   * Gets projectile's damage.
   * 
   * @return Returns projectile's damage.
   */
  int getDamage();
}