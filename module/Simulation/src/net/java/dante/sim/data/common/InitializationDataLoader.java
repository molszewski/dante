/*
 * Created on 2006-07-16
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.common;

import net.java.dante.sim.io.init.InitializationData;


/**
 * Abstract class for subclasses that loads their data from {@link net.java.dante.sim.io.SimulationInput}
 * implementations.
 *
 * @author M.Olszewski
 */
public class InitializationDataLoader
{
  /** Reference to {@link InitializationData} with requested data. */
  private InitializationData inputData;
  
  /**
   * Creates instance of {@link InitializationDataLoader} holding reference to specified
   * {@link InitializationData}.
   * 
   * @param loaderInputData - reference to {@link InitializationData} with requested data.
   * 
   * @throws NullPointerException if specified <code>loaderInputData</code> is <code>null</code>.
   */
  protected InitializationDataLoader(InitializationData loaderInputData)
  {
    if (loaderInputData == null)
    {
      throw new NullPointerException("Specified loaderInputData is null!");
    }
    
    inputData = loaderInputData;
  }

  
  /**
   * Gets reference to {@link InitializationData} with data to load.
   * 
   * @return Returns the inputData.
   */
  public InitializationData getInitData()
  {
    return inputData;
  }
  
}