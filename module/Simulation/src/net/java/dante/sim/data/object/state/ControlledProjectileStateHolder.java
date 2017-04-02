/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski 
 */
package net.java.dante.sim.data.object.state;

/**
 * Class holding data defined in {@link ControlledProjectileState} interface.
 * 
 * @author M.Olszewski
 */
public class ControlledProjectileStateHolder implements ControlledProjectileState
{
  /** Projectile's maximum distance. */
  private double distance;
  /** Projectile's explosion range. */
  private double explosionRange;
  /** Projectile's damage. */
  private int damage;
  /** Projectile's maximum speed. */
  private double maxSpeed;

  
  /**
   * Creates instance of {@link ControlledProjectileStateHolder} class holding data 
   * specified in {@link ControlledProjectileState} interface.
   *
   * @param projectileDistance - maximum distance that will be traversed by this
   *        projectile.
   * @param projectileExplosionRange - projectile's explosion range.
   * @param projectileDamage - damage that will be inflicted by this projectile.
   * @param projectileMaxSpeed - maximum speed of projectile.
   */
  public ControlledProjectileStateHolder(double projectileDistance,
      double projectileExplosionRange, int projectileDamage, 
      double projectileMaxSpeed)
  {
    if (projectileDistance < 0.0)
    {
      throw new IllegalArgumentException("Invalid argument projectileRange - it must be positive real number!");
    }
    if (projectileExplosionRange < 0.0)
    {
      throw new IllegalArgumentException("Invalid argument projectileExplosionRange - it must be a real number greater than or equal to zero!");
    }
    if (projectileDamage <= 0)
    {
      throw new IllegalArgumentException("Invalid argument projectileDamage - it must be positive integer!");
    }
    if (projectileMaxSpeed <= 0.0)
    {
      throw new IllegalArgumentException("Invalid argument projectileMaxSpeed - it must be positive real number!");
    }
    
    distance = projectileDistance;
    explosionRange = projectileExplosionRange;
    damage = projectileDamage;
    maxSpeed = projectileMaxSpeed;
  }


  /**
   * @see net.java.dante.sim.data.object.state.ControlledProjectileState#getDistance()
   */
  public double getDistance()
  {
    return distance;
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ControlledProjectileState#getExplosionRange()
   */
  public double getExplosionRange()
  {
    return explosionRange;
  }

  /**
   * @see net.java.dante.sim.data.object.state.ControlledProjectileState#getDamage()
   */
  public int getDamage()
  {
    return damage;
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectMaxSpeedState#getMaxSpeed()
   */
  public double getMaxSpeed()
  {
    return maxSpeed;
  }
  
  /** 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[distance=" + distance + "; explosionRange=" + 
        explosionRange + "; damage=" + damage + "; maxSpeed=" + maxSpeed + "]"); 
  }
}