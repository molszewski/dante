/*
 * Created on 2006-08-13
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d;

import net.java.dante.sim.engine.graphics.java2d.Java2dSprite;
import net.java.dante.sim.engine.time.TimeCounter;

/**
 * Class managing animation for parent object, specified in constructor.
 * It switches current sprite of parent after specified period of time.
 *
 * @author M.Olszewski
 */
public class Engine2dAnimation
{
  /** Array with animation frames. */
  private Java2dSprite[] sprites;
  /** Counter firing action when time to change frame has come. */
  private TimeCounter counter;
  /** Index of current sprite - set to zero by default. */
  private int currentSprite;
  /** Parent object - its current sprite is switched. */
  private Engine2dObject parent;
  

  /**
   * Creates instance of {@link Engine2dAnimation} class.
   *
   * @param parentObject - the parent object.
   * @param animationSprites - array with animation frames (sprites).
   * @param spriteChangeDelay - time after which frame (sprite) will be changed.
   */
  public Engine2dAnimation(Engine2dObject parentObject, 
      Java2dSprite[] animationSprites, long spriteChangeDelay)
  {
    if (parentObject == null)
    {
      throw new NullPointerException("Specified parentObject is null!");
    }
    if (animationSprites == null)
    {
      throw new NullPointerException("Specified animationSprites is null!");
    }
    if (animationSprites.length <= 0)
    {
      throw new IllegalArgumentException("Invalid argument animationSprites - it must contain at least 1 element!");
    }
    if (spriteChangeDelay <= 0)
    {
      throw new IllegalArgumentException("Invalid argument spriteChangeDelay - it must be positive long integer!");
    }
    
    parent = parentObject;
    sprites = animationSprites;
    counter = new TimeCounter(spriteChangeDelay);
    counter.enable(sprites.length > 1);
  }


  /**
   * Updates current animation frame if necessary.
   * 
   * @param delta - time elapsed since last update.
   */
  public void update(long delta)
  {
    if (counter.isActionTime(delta))
    {
      currentSprite = (currentSprite + 1) % sprites.length;
      parent.setSprite(sprites[currentSprite]);
      counter.reset();
    }
  }
  
  /**
   * Gets current sprite index.
   * 
   * @return Returns current sprite index.
   */
  public int getCurrentSpriteIndex()
  {
    return currentSprite;
  }
  
  /**
   * Gets the count of sprites.
   * 
   * @return Returns the count of sprites.
   */
  public int getSpritesCount()
  {
    return sprites.length;
  }
}