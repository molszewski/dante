/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

/**
 * Class holding data defined in {@link ObservedProjectileState} interface.
 *
 * @author M.Olszewski
 */
public class ObservedProjectileStateHolder implements ObservedProjectileState
{
  /** Projectile's explosion range. */
  private double explosionRange;
  /** Projectile's maximum speed. */
  private double maxSpeed;
  /** Observed agent state holder. */
  private ObservedObjectStateHolder observedObjectHolder;
  

  /**
   * Creates instance of {@link ObservedProjectileStateHolder} class holding data 
   * specified in {@link ObservedProjectileState} interface.
   *
   * @param projectileExplosionRange - projectile's explosion range.
   * @param projectileMaxSpeed - maximum speed of projectile.
   */
  public ObservedProjectileStateHolder(double projectileExplosionRange, 
      double projectileMaxSpeed)
  {
    if (projectileExplosionRange < 0.0)
    {
      throw new IllegalArgumentException("Invalid argument projectileExplosionRange - it must be a real number greater than or equal to zero!");
    }
    if (projectileMaxSpeed <= 0.0)
    {
      throw new IllegalArgumentException("Invalid argument projectileMaxSpeed - it must be positive real number!");
    }
    
    explosionRange = projectileExplosionRange;
    maxSpeed = projectileMaxSpeed;
    observedObjectHolder = new ObservedObjectStateHolder(true);
  }

  
  /** 
   * @see net.java.dante.sim.data.object.state.ObservedProjectileState#getExplosionRange()
   */
  public double getExplosionRange()
  {
    return explosionRange;
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObjectMaxSpeedState#getMaxSpeed()
   */
  public double getMaxSpeed()
  {
    return maxSpeed;
  }
  
  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#inSightRange()
   */
  public void inSightRange()
  {
    observedObjectHolder.inSightRange();
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#outOfSightRange()
   */
  public void outOfSightRange()
  {
    observedObjectHolder.outOfSightRange();
  }
  
  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#isVisible()
   */
  public boolean isVisible()
  {
    return observedObjectHolder.isVisible();
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#isAlive()
   */
  public boolean isAlive()
  {
    return observedObjectHolder.isAlive();
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#destroyed()
   */
  public void destroyed()
  {
    observedObjectHolder.destroyed();
  }
  
  /** 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[explosionRange=" + explosionRange + 
        "; observedState=" + observedObjectHolder + "]"); 
  }
}