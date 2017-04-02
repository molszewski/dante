/*
 * Created on 2006-07-16
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.common;

import net.java.dante.sim.io.init.InitializationData;

/**
 * Class representing {@link DataSource} as data obtained from 
 * one of {@link net.java.dante.sim.io.SimulationInput} implementations.
 *
 * @author M.Olszewski
 */
public class InitializationDataSource implements DataSource
{
  /** Data received from one of {@link net.java.dante.sim.io.SimulationInput} implementations. */
  private InitializationData input;
  
  
  /**
   * Creates instance of {@link InitializationData} with specified reference to 
   * {@link InitializationData} implementation.
   * 
   * @param inputData - data received from one of {@link net.java.dante.sim.io.SimulationInput} 
   *        implementations.
   */
  public InitializationDataSource(InitializationData inputData)
  {
    if (inputData == null)
    {
      throw new NullPointerException("Specified inputData is null!");
    }
    
    input = inputData;
  }
  
  
  /**
   * Gets reference to {@link InitializationData} held by this 
   * {@link InitializationDataSource}.
   * 
   * @return Returns reference to {@link InitializationData} held by this 
   *         {@link InitializationDataSource}.
   */
  public InitializationData getData()
  {
    return input;
  }
}