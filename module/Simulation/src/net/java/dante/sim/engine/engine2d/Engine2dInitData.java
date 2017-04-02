/*
 * Created on 2006-07-20
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d;

import java.awt.Container;

import net.java.dante.sim.data.GlobalData;
import net.java.dante.sim.data.common.InitData;


/**
 * Class containing external settings for {@link net.java.dante.sim.engine.engine2d.server.ServerEngine2d} object.
 * 
 * @author M.Olszewski
 */
public class Engine2dInitData implements InitData
{
  /** Parent's container for engine's graphics context. */
  private Container parent; 
  /** Global data. */
  private GlobalData global;
  
  
  /**
   * Creates instance of {@link Engine2dInitData} with specified parameters.
   *
   * @param parentContainer - parent's container for engine's graphics context.
   * @param globalData - global data.
   */
  public Engine2dInitData(Container parentContainer, 
                          GlobalData globalData)
  {
    if (parentContainer == null)
    {
      throw new NullPointerException("Specified parentContainer is null!");
    }
    if (globalData == null)
    {
      throw new NullPointerException("Specified globalData is null!");
    }
    
    parent = parentContainer;
    global = globalData;
  }


  /**
   * Gets parent's container for engine's graphics context.
   * 
   * @return parent's container for engine's graphics context.
   */
  public Container getParent()
  {
    return parent;
  }

  
  /**
   * Gets reference to global data.
   * 
   * @return Returns reference to global data.
   */
  public GlobalData getGlobalData()
  {
    return global;
  }
}