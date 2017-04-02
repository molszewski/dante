/*
 * Created on 2006-07-19
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.graphics;

/**
 * Graphics context for drawing.
 * 
 * @author M.Olszewski
 */
public interface GraphicsContext
{
  /**
   * Sets size of the graphics context resolution.
   * 
   * @param newWidth - new width of this {@link GraphicsContext}.
   * @param newHeight - new height of this {@link GraphicsContext}.
   */
  void setResolution(int newWidth, int newHeight);
  
  /**
   * Enables or disables this {@link GraphicsContext}. 
   * Disabled {@link GraphicsContext} should not render anything.
   * 
   * @param enabled - determines whether this {@link GraphicsContext} should
   *        be enabled or disabled.
   */
  void activate(boolean enabled);
  
  /**
   * Initializes this {@link GraphicsContext}. This method should perform
   * proper initialization also for not active context.
   */
  void initialize();
  
  /**
   * Disposes this {@link GraphicsContext}. This method should perform
   * proper disposal of resources also for not active context.
   */
  void dispose();
  
  /**
   * Begins process of rendering new frame.
   */
  void beginRendering();
  
  /**
   * Ends process of rendering new frame and displays rendered scene.
   */
  void endRendering();
}