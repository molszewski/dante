/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski
 */
package net.java.dante.sim.io;

/**
 * Class representing special data input data for each type of simulation 
 * controlling rendering process: it can enable or disable rendering,
 * depending on value specified in the 
 *
 * @author M.Olszewski
 */
public class EnableRenderingData implements ClientInputData, ServerInputData
{
  /** Indicates whether rendering should be enabled or disabled. */
  private boolean renderingEnabled;

  
  /**
   * Creates instance of {@link EnableRenderingData} class.
   *
   * @param enableRendering indicates whether rendering should be enabled
   *        or disabled.
   */
  public EnableRenderingData(boolean enableRendering)
  {
   renderingEnabled = enableRendering;
  }
  

  /**
   * Returns value indicating whether rendering should be enabled or disabled.
   * 
   * @return Returns <code>true</code> if rendering should be enabled or
   *         <code>false</code> otherwise.
   */
  public boolean isRenderingEnabled()
  {
    return renderingEnabled;
  }
}