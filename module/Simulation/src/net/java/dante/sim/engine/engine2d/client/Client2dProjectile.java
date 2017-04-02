/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d.client;

import net.java.dante.sim.data.object.agent.ClientFriendlyAgent;
import net.java.dante.sim.data.object.projectile.ClientProjectile;
import net.java.dante.sim.data.object.projectile.ClientProjectileState;
import net.java.dante.sim.engine.engine2d.Engine2dAnimation;
import net.java.dante.sim.engine.engine2d.Engine2dObject;
import net.java.dante.sim.engine.graphics.java2d.Java2dSprite;
import net.java.dante.sim.event.types.ProjectileDestroyedEvent;
import net.java.dante.sim.event.types.ProjectileEvent;
import net.java.dante.sim.event.types.ProjectileGoneEvent;

/**
 * Class represents {@link ClientProjectile} objects as engine objects with 
 * graphical representations.
 *
 * @author M.Olszewski
 */
class Client2dProjectile extends Engine2dObject
{
  /** Wrapped simulation projectile. */
  private ClientProjectile projectile;
  /** Object managing projectile's animation. */
  private Engine2dAnimation animation;
  

  /**
   * Creates instance of {@link Client2dProjectile} class with specified
   * parameters.
   * 
   * @param simProjectile - wrapped simulation projectile.
   * @param objSprites - sprites for this projectile.
   * @param spritesDelay - time after which sprite will be changed.
   */
  public Client2dProjectile(ClientProjectile simProjectile, 
      Java2dSprite[] objSprites, long spritesDelay)
  {
    super(objSprites[0], 
         ((ClientProjectileState)simProjectile.getData()).getX(), 
         ((ClientProjectileState)simProjectile.getData()).getY(),
         ((ClientProjectileState)simProjectile.getData()).getSize());
    
    projectile = simProjectile;
    animation = new Engine2dAnimation(this, objSprites, spritesDelay);
  }

  
  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#getX()
   */
  @Override
  protected double getX()
  {
    return ((ClientProjectileState)projectile.getData()).getX();
  }

  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#getY()
   */
  @Override
  protected double getY()
  {
    return ((ClientProjectileState)projectile.getData()).getY();
  }

  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#update(long)
   */
  @Override
  public void update(long delta)
  {
    projectile.update(delta);
    
    ClientProjectileState state = (ClientProjectileState)projectile.getData();
    if (state.isPositionChanged())
    {
      animation.update(delta);
      super.update(delta);
    }
  }
  
  
  /**
   * Method invoked when this projectile is gone.
   * 
   * @param goneEvent - the {@link ProjectileGoneEvent} event.
   */
  public void projectileGone(ProjectileGoneEvent goneEvent)
  {
    projectile.projectileGone(goneEvent);
  }
  
  /**
   * Method invoked when this projectile is destroyed.
   * 
   * @param destroyedEvent - the {@link ProjectileDestroyedEvent} event.
   */
  void projectileDestroyed(ProjectileDestroyedEvent destroyedEvent)
  {
    projectile.projectileDestroyed(destroyedEvent);
    setActive(false);
  }
  
  /**
   * Adds the specified {@link ProjectileEvent} event to wrapped projectile.
   * 
   * @param event - the specified {@link ProjectileEvent} event.
   */
  void addEvent(ProjectileEvent event)
  {
    projectile.addEvent(event);
  }
  
  /**
   * Shifts time of wrapped {@link ClientFriendlyAgent} by the specified
   * amount of time.
   * 
   * @param time - the specified amount of time.
   */
  void timeShift(long time)
  {
    projectile.timeShift(time);
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