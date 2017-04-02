/*
 * Created on 2006-08-10
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d;

import java.awt.Transparency;
import java.awt.image.BufferedImage;

import net.java.dante.sim.data.GlobalData;
import net.java.dante.sim.data.common.AnimationFramesData;
import net.java.dante.sim.engine.graphics.java2d.ImagesCache;
import net.java.dante.sim.engine.graphics.java2d.Java2dContext;
import net.java.dante.sim.engine.graphics.java2d.Java2dSprite;


/**
 * Class holding references to all sprites used by engines operating in
 * two dimensions.
 *
 * @author M.Olszewski
 */
public class SpritesRepository
{
  /** Sprite representing background image. */
  private Java2dSprite backgroundSprite;
  /** Sprites representing agent's movement. */
  private Java2dSprite[] agentMoveSprites;
  private long agentMoveDelay;
  /** Sprites representing agent's explosion. */
  private Java2dSprite[] agentExplosionSprites;
  private long agentExplosionDelay;
  /** Sprites representing projectile's movement. */
  private Java2dSprite[] projectileMoveSprites;
  private long projectileMoveDelay;
  /** Sprites representing projectile's explosion. */
  private Java2dSprite[] projectileExplosionSprites;
  private long projectileExplosionDelay;
  /** Sprite representing single map's tile image. */
  private Java2dSprite tileSprite;
  

  /**
   * Creates instance of {@link SpritesRepository} class which loads images 
   * for instances of {@link Java2dSprite} class and creates proper instances
   * of this class.
   * 
   * @param globalData - initialization data for this engine.
   * @param java2dContext - graphical context for sprites.
   */
  public SpritesRepository(GlobalData globalData, Java2dContext java2dContext)
  {
    if (globalData == null)
    {
      throw new NullPointerException("Specified globalData is null!");
    }
    if (java2dContext == null)
    {
      throw new NullPointerException("Specified java2dContext is null!");
    }
    
    backgroundSprite = new Java2dSprite(
        java2dContext,
        ImagesCache.getInstance().loadImage(globalData.getBackgroundImageFile(), Transparency.OPAQUE));
    tileSprite = new Java2dSprite(
        java2dContext,
        ImagesCache.getInstance().loadImage(globalData.getTileImageFile(), Transparency.BITMASK));
    
    agentMoveSprites = initSprites(java2dContext, globalData.getAgentMoveAnimation());
    agentMoveDelay = globalData.getAgentMoveAnimation().getFramesDelay();
    
    projectileMoveSprites = initSprites(java2dContext, globalData.getProjectileMoveAnimation());
    projectileMoveDelay = globalData.getProjectileMoveAnimation().getFramesDelay();
    
    agentExplosionSprites = initSprites(java2dContext, globalData.getAgentExplosionAnimation());
    agentExplosionDelay = globalData.getAgentExplosionAnimation().getFramesDelay();
    
    projectileExplosionSprites = initSprites(java2dContext, globalData.getProjectileExplosionAnimation());
    projectileExplosionDelay = globalData.getProjectileExplosionAnimation().getFramesDelay();
  }
  
  
  private Java2dSprite[] initSprites(Java2dContext java2dContext, 
      AnimationFramesData animationData)
  {
    BufferedImage[] images = ImagesCache.getInstance().loadImages(
        animationData.getFramesFile(), 
        animationData.getFramesCount(),
        Transparency.BITMASK);
    
    Java2dSprite[] sprites = new Java2dSprite[images.length];
    for (int i = 0; i < images.length; i++)
    {
      sprites[i] = new Java2dSprite(java2dContext, images[i]);
    }
    
    return sprites;
  }

  
  /**
   * Gets sprites representing agent's movement.
   * 
   * @return Returns sprites representing agent's movement.
   */
  public Java2dSprite[] getAgentMoveSprites()
  {
    return agentMoveSprites;
  }
  
  /**
   * Gets sprites representing projectile's movement.
   * 
   * @return Returns sprites representing projectile's movement.
   */
  public Java2dSprite[] getProjectileMoveSprites()
  {
    return projectileMoveSprites;
  }
  
  /**
   * Gets sprites representing agent's explosion.
   * 
   * @return Returns sprites representing agent's movement.
   */
  public Java2dSprite[] getAgentExplosionSprites()
  {
    return agentExplosionSprites;
  }
  
  /**
   * Gets sprites representing projectile's movement.
   * 
   * @return Returns sprites representing projectile's movement.
   */
  public Java2dSprite[] getProjectileExplosionSprites()
  {
    return projectileExplosionSprites;
  }
  
  /**
   * Gets sprite representing background image.
   * 
   * @return Returns sprite representing background image.
   */
  public Java2dSprite getBackgroundSprite()
  {
    return backgroundSprite;
  }

  /**
   * Gets sprite representing single map's tile image.
   * 
   * @return Returns sprite representing single map's tile image.
   */
  public Java2dSprite getTileSprite()
  {
    return tileSprite;
  }


  /**
   * Gets the agentExplosionDelay.
   *
   * @return Returns the agentExplosionDelay.
   */
  public long getAgentExplosionDelay()
  {
    return agentExplosionDelay;
  }


  /**
   * Gets the agentMoveDelay.
   *
   * @return Returns the agentMoveDelay.
   */
  public long getAgentMoveDelay()
  {
    return agentMoveDelay;
  }


  /**
   * Gets the projectileExplosionDelay.
   *
   * @return Returns the projectileExplosionDelay.
   */
  public long getProjectileExplosionDelay()
  {
    return projectileExplosionDelay;
  }


  /**
   * Gets the projectileMoveDelay.
   *
   * @return Returns the projectileMoveDelay.
   */
  public long getProjectileMoveDelay()
  {
    return projectileMoveDelay;
  }
}