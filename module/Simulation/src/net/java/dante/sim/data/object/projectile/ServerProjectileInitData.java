/*
 * Created on 2006-07-31
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.object.projectile;

import net.java.dante.sim.data.common.InitData;

/**
 * Class representing initialization data for
 * {@link net.java.dante.sim.data.object.projectile.ServerProjectile} instances.
 *
 * @author M.Olszewski
 */
public class ServerProjectileInitData implements InitData
{
  /** Identifier of projectile owner's group. */
  private int ownerGroupId;
  /** Identifier of projectile's owner. */
  private int ownerId;
  /** Projectile's 'x' coordinate. */
  private double x;
  /** Projectile's 'y' coordinate. */
  private double y;
  /** Projectile's horizontal speed. */
  private double speedX;
  /** Projectile's vertical speed. */
  private double speedY;
  /** Projectile's requested distance. */
  private double requestedDistance;


  /**
   * Creates instance of {@link ServerProjectileInitData} class.
   *
   * @param projectileOwnerGroupId - identifier of projectile owner's group.
   * @param projectileOwnerId - identifier of projecitle's owner.
   * @param projectileX - projectile's 'x' coordinate.
   * @param projectileY - projectile's 'y' coordinate.
   * @param projectileSpeedX - projectile's horizontal speed.
   * @param projectileSpeedY - projectile's vertical speed.
   * @param projectileRequestedDistance - requested distance that should
   *        be traversed by this projectile.
   */
  public ServerProjectileInitData(int    projectileOwnerGroupId,
                                  int    projectileOwnerId,
                                  double projectileX,
                                  double projectileY,
                                  double projectileSpeedX,
                                  double projectileSpeedY,
                                  double projectileRequestedDistance)
  {
    if (projectileOwnerGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument projectileOwnerGroupId - it must be positive integer or zero!");
    }
    if (projectileOwnerId < 0)
    {
      throw new IllegalArgumentException("Invalid argument projectileOwnerId - it must be positive integer or zero!");
    }
    if (projectileRequestedDistance < 0.0)
    {
      throw new IllegalArgumentException("Invalid argument projectileRequestedDistance - it must be positive real number!");
    }

    ownerGroupId = projectileOwnerGroupId;
    ownerId      = projectileOwnerId;
    x = projectileX;
    y = projectileY;
    speedX = projectileSpeedX;
    speedY = projectileSpeedY;
    requestedDistance = projectileRequestedDistance;
  }


  /**
   * Gets identifier of group to which belongs owner of this projectile.
   * 'Owner' means here an object which created the projectile.
   *
   * @return Returns the identifier of projectile owner's group.
   */
  public int getOwnerGroupId()
  {
    return ownerGroupId;
  }

  /**
   * Gets the projectile's owner identifier.
   *
   * @return Returns the projectile's owner identifier.
   */
  public int getOwnerId()
  {
    return ownerId;
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

  /**
   * Gets requested distance that should be traversed by projectile.
   *
   * @return Returns requested distance that should be traversed by projectile.
   */
  public double getRequestedDistance()
  {
    return requestedDistance;
  }
}