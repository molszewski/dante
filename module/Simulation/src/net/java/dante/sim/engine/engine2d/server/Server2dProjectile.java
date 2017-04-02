/*
 * Created on 2006-07-30
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d.server;

import net.java.dante.sim.data.object.projectile.ServerProjectile;
import net.java.dante.sim.data.object.projectile.ServerProjectileState;
import net.java.dante.sim.engine.engine2d.Engine2dAnimation;
import net.java.dante.sim.engine.engine2d.Engine2dObject;
import net.java.dante.sim.engine.graphics.java2d.Java2dSprite;

/**
 * Class represents {@link ServerProjectile} objects as engine objects with 
 * graphical representations.
 *
 * @author M.Olszewski
 */
class Server2dProjectile extends Engine2dObject
{
  /** Wrapped simulation projectile. */
  private ServerProjectile projectile;
  /** Object managing projectile's animation. */
  private Engine2dAnimation animation;
  
  
  /**
   * Creates instance of {@link Server2dProjectile} class with specified
   * parameters.
   * 
   * @param simProjectile - wrapped simulation projectile.
   * @param objSprites - sprites for this projectile.
   * @param spritesDelay - time after which sprite will be changed.
   */
  Server2dProjectile(ServerProjectile simProjectile, 
      Java2dSprite[] objSprites, long spritesDelay)
  {
    super(objSprites[0], ((ServerProjectileState)simProjectile.getData()).getX(), 
        ((ServerProjectileState)simProjectile.getData()).getY(),
        ((ServerProjectileState)simProjectile.getData()).getSize());
    
    projectile = simProjectile;
    animation = new Engine2dAnimation(this, objSprites, spritesDelay);
  }

  
  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#getX()
   */
  @Override
  protected double getX()
  {
    return ((ServerProjectileState)projectile.getData()).getX();
  }

  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#getY()
   */
  @Override
  protected double getY()
  {
    return ((ServerProjectileState)projectile.getData()).getY();
  }
  
  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#update(long)
   */
  @Override
  public void update(long delta)
  {
    projectile.update(delta);
    
    ServerProjectileState state = (ServerProjectileState)projectile.getData();
    
    if (state.isPositionChanged())
    {
      animation.update(delta);
      super.update(delta);
    }
  }

  /**
   * Gets the wrapped simulation's projectile.
   *
   * @return Returns the wrapped simulation'sprojectile.
   */
  ServerProjectile getProjectile()
  {
    return projectile;
  }
  
  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#toString()
   */
  @Override
  public String toString()
  {
    String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) + 
        "; projectile=" + projectile + "]");
  }
}