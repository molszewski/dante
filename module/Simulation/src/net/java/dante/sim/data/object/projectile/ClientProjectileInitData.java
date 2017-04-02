/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.projectile;

import net.java.dante.sim.data.common.InitData;


/**
 * Class representing initialization data for 
 * {@link net.java.dante.sim.data.object.projectile.ClientProjectile} instances.
 *
 * @author M.Olszewski
 */
public class ClientProjectileInitData implements InitData
{
  /** Identifier of projectile. */
  private int id;
  /** Current simulation's time. */
  private long currentTime;
  /** Projectile's 'x' coordinate. */
  private double x; 
  /** Projectile's 'y' coordinate. */
  private double y; 
  /** Projectile's horizontal speed. */
  private double speedX;
  /** Projectile's vertical speed. */
  private double speedY;
  

  /**
   * Creates instance of {@link ClientProjectileInitData} class.
   * 
   * @param projectileId - projectile's identifier.
   * @param currentSimTime - the current simulation's time.
   * @param projectileX - projectile's 'x' coordinate.
   * @param projectileY - projectile's 'y' coordinate.
   * @param projectileSpeedX - projectile's horizontal speed.
   * @param projectileSpeedY - projectile's vertical speed.
   */
  public ClientProjectileInitData(int    projectileId,
                                  long   currentSimTime,
                                  double projectileX, 
                                  double projectileY, 
                                  double projectileSpeedX, 
                                  double projectileSpeedY)
  {
    if (projectileId < 0)
    {
      throw new IllegalArgumentException("Invalid argument projectileId - it must be positive integer or zero!");
    }
    if (currentSimTime < 0)
    {
      throw new IllegalArgumentException("Invalid argument currentSimTime - it must be positive long integer or zero!");
    }
    
    id          = projectileId;
    currentTime = currentSimTime;
    x           = projectileX;
    y           = projectileY;
    speedX      = projectileSpeedX;
    speedY      = projectileSpeedY;
  }
  

  /**
   * Gets the projectile's identifier.
   * 
   * @return Returns the projectile's identifier.
   */
  public int getId()
  {
    return id;
  }
  
  /**
   * Gets the current simulation's time.
   * 
   * @return Returns the current simulation's time.
   */
  public long getCurrentTime()
  {
    return currentTime;
  }
  
  /**
   * Gets the projectile's 'x' coordinate.
   *
   * @return Returns the projectile's 'x' coordinate.
   */
  public double getX()
  {
    return x;
  }

  /**
   * Gets the projectile's 'y' coordinate.
   *
   * @return Returns the projectile's 'y' coordinate.
   */
  public double getY()
  {
    return y;
  }
  
  /**
   * Gets the projectile's horizontal speed.
   *
   * @return Returns the projectile's horizontal speed.
   */
  public double getSpeedX()
  {
    return speedX;
  }

  /**
   * Gets the projectile's vertical speed.
   *
   * @return Returns the projectile's vertical speed.
   */
  public double getSpeedY()
  {
    return speedY;
  }
}