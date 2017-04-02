/*
 * Created on 2006-07-06
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data;

import net.java.dante.sim.data.common.DataSource;
import net.java.dante.sim.data.common.FileDataSource;
import net.java.dante.sim.data.common.InitializationDataSource;


/**
 * Factory class delivering instances of classes implementing {@link DataLoader}
 * interface.
 *
 * @author M.Olszewski
 */
public class LoadersFactory
{
  /** The only existing instance of {@link LoadersFactory}. */
  private static final LoadersFactory instance = new LoadersFactory();
  
  
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private LoadersFactory()
  {
    // Intentionally left empty.
  }
  
  
  /**
   * Gets the only instance of this singleton class.
   * 
   * @return Returns the only instance of this singleton class.
   */
  public static LoadersFactory getInstance()
  {
    return instance;
  }
  
  /**
   * Creates instance of class implementing {@link DataLoader} interface,
   * responsible for loading data from external sources - referred by 
   * specified {@link DataSource}. This method may also return <code>null</code>
   * if specified {@link DataSource} was not recognized.
   * 
   * @param dataSource - external source of {@link SimulationData} for
   *        {@link DataLoader}.
   * 
   * @return Returns instance of class implementing {@link DataLoader} 
   *         responsible for loading data from specified {@link DataSource}.
   */
  public DataLoader createLoader(DataSource dataSource)
  {
    DataLoader loader = null;
    
    if (dataSource instanceof FileDataSource)
    {
      loader = new MainFileLoader(((FileDataSource)dataSource).getSource());
    }
    else if (dataSource instanceof InitializationDataSource)
    {
      loader = new MainInitializationDataLoader(((InitializationDataSource)dataSource).getData());
    }
      
    
    return loader;
  }
}