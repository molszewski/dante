/*
 * Created on 2006-07-09
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.map;

import net.java.dante.sim.data.common.DataSource;
import net.java.dante.sim.data.common.FileDataSource;
import net.java.dante.sim.data.common.InitializationDataSource;

/**
 * Factory class delivering instances of classes implementing {@link MapLoader}
 * interface.
 *
 * @author M.Olszewski
 */
public class MapLoadersFactory
{
  /** The only existing instance of {@link MapLoadersFactory}. */
  private static final MapLoadersFactory instance = new MapLoadersFactory();
  
  
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private MapLoadersFactory()
  {
    // Intentionally left empty.
  }
  
  
  /**
   * Gets the only instance of this singleton class.
   * 
   * @return Returns the only instance of this singleton class.
   */
  public static MapLoadersFactory getInstance()
  {
    return instance;
  }
  
  /**
   * Creates instance of class implementing {@link MapLoader} interface,
   * responsible for loading data from external sources - referred by 
   * specified {@link DataSource}. This method may also return <code>null</code>
   * if specified {@link DataSource} was not recognized.
   * 
   * @param dataSource - external source of {@link SimulationMap} for
   *        {@link MapLoader}.
   * 
   * @return Returns instance of class implementing {@link MapLoader} 
   *         responsible for loading map's data from specified {@link DataSource}.
   */
  public MapLoader createLoader(DataSource dataSource)
  {
    MapLoader loader = null;
    
    if (dataSource instanceof FileDataSource)
    {
      loader = new MapFileLoader(((FileDataSource)dataSource).getSource());
    }
    else if (dataSource instanceof InitializationDataSource)
    {
      loader = new MapInitializationDataLoader(((InitializationDataSource)dataSource).getData());
    }
    
    return loader;
  }
}