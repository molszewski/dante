/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

/**
 * Interface defining set of client's projectile attributes: explosion range 
 * and maximum speed.
 *
 * @author M.Olszewski
 */
public interface ObservedProjectileState extends ObjectMaxSpeedState, ObservedObjectState
{
  /**
   * Gets projectile's explosion range.
   * 
   * @return Returns projectile's explosion range.
   */
  double getExplosionRange();
}