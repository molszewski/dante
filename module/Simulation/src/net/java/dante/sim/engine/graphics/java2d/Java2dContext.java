/*
 * Created on 2006-07-19
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.graphics.java2d;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import net.java.dante.sim.engine.graphics.GraphicsContext;


/**
 * An implementation of {@link GraphicsContext} which uses Java2D rendering to
 * produce the scene. Be aware of a fact that this objects of this class must
 * be used with care: after each call to {@link #beginRendering()} rendering
 * must take place and if it is finished {@link #endRendering()} method
 * <b>MUST ALWAYS BE</b> invoked - otherwise output can be undefined.<p>
 * Example of using {@link Java2dContext}:
 * <pre>
 * // Create proper context<br>
 * Java2dContext context = new Java2dContext(parent, true);
 * context.beginRendering(); // Begin rendering
 * {
 *   Graphics2D g2d = context.getAcceleratedGraphics();
 *   g2d.drawRect(0, 0, 100, 100);
 * }
 * context.endRendering(); // It MUST BE ALWAYS invoked after beginRendering()
 * </pre>
 *
 * @author M.Olszewski
 */
public class Java2dContext extends Canvas implements GraphicsContext
{
  private static final long serialVersionUID = 1L;
  
  /** Determines whether this context is active (enabled). */
  private boolean active;
  /** The width of the context. */
  private int width;
  /** The height of the context. */
  private int height;
  /** The strategy that allows us to use accelerate page swapping. */
  private BufferStrategy strategy;
  /** The current accelerated graphics context */
  private Graphics2D g;
  /** Parent component, where everything will be drawn. */
  private Container parent;


  /**
   * Create a new context using Java2D to render scene. This context
   * is meant to be added into specified <code>parentComponent</code> - results
   * of rendering will be displayed there.
   *
   * @param parentComponent - this context's parent.
   * @param activated - specifies whether this context should be activated.
   */
  public Java2dContext(Container parentComponent, boolean activated)
  {
    parent = parentComponent;
    active = activated;
  }


  /**
   * @throws IllegalArgumentException if new width or height are not positive integers.
   *
   * @see net.java.dante.sim.engine.graphics.GraphicsContext#setResolution(int, int)
   */
  public void setResolution(int newWidth, int newHeight) throws IllegalArgumentException
  {
    if (newWidth <= 0)
    {
      throw new IllegalArgumentException("Invalid argument newWidth - it must be positive integer!");
    }
    if (newHeight <= 0)
    {
      throw new IllegalArgumentException("Invalid argument newHeight - it must be positive integer!");
    }

    width = newWidth;
    height = newHeight;
  }

  /**
   * Retrieves the current accelerated graphics context. This method can return
   * <code>null</code> if graphics context is not active.
   *
   * @return Returns current accelerated graphics {@link Graphics2D} for
   *         this context or <code>null</code> if context is not active.
   */
  public Graphics2D getAcceleratedGraphics()
  {
    return (active ? g : null);
  }

  /**
   * @see net.java.dante.sim.engine.graphics.GraphicsContext#initialize()
   */
  public void initialize()
  {
    setBounds(0,0, width, height);
    parent.add(this);
    setIgnoreRepaint(true);

    createBufferStrategy(2);
    strategy = getBufferStrategy();
  }

  /**
   * @see net.java.dante.sim.engine.graphics.GraphicsContext#dispose()
   */
  public void dispose()
  {
    parent.remove(this);
  }


  /**
   * @see net.java.dante.sim.engine.graphics.GraphicsContext#beginRendering()
   */
  public void beginRendering()
  {
    if (active)
    {
      g = (Graphics2D) strategy.getDrawGraphics();
      g.setColor(Color.black);
      g.fillRect(0, 0, width, height);
    }
  }

  /**
   * @see net.java.dante.sim.engine.graphics.GraphicsContext#endRendering()
   */
  public void endRendering()
  {
    if (active)
    {
      g.dispose();
      strategy.show();
    }
  }

  /**
   * @see net.java.dante.sim.engine.graphics.GraphicsContext#activate(boolean)
   */
  public void activate(boolean enabled)
  {
    // Change only if different
    if (enabled != active)
    {
      active = enabled;
    }
  }
}