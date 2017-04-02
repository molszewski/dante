/*
 * Created on 2006-08-13
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d;

import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.engine.graphics.java2d.Java2dSprite;

/**
 * Class representing explosion.
 *
 * @author M.Olszewski
 */
public class Engine2dExplosion extends Engine2dStillObject
{
  /** Object managing explosion's animation. */
  private Engine2dAnimation animation;

  
  /**
   * Creates instance of {@link Engine2dExplosion} class with 
   * specified parameters.
   * 
   * @param objSprites - sprites for this explosion.
   * @param spritesDelay - time after which sprite will be changed.
   * @param posX - 'x' coordinate of this {@link Engine2dObject}.
   * @param posY - 'y' coordinate of this {@link Engine2dObject}.
   * @param size - size of this {@link Engine2dObject}.
   */
  public Engine2dExplosion(Java2dSprite[] objSprites, long spritesDelay,
      double posX, double posY, ObjectSize size)
  {
    super(objSprites[0], posX, posY, size);
    
    animation = new Engine2dAnimation(this, objSprites, spritesDelay);
  }


  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dStillObject#update(long)
   */
  @Override
  public void update(long delta)
  {
    animation.update(delta);
    
    setActive(animation.getCurrentSpriteIndex() < (animation.getSpritesCount() - 1));
  }
}