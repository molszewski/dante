/*
 * Created on 2006-07-14
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.settings;

import net.java.dante.sim.data.common.DataSource;
import net.java.dante.sim.data.common.FileDataSource;

/**
 * Factory class delivering instances of classes implementing {@link SettingsLoader}
 * interface.
 *
 * @author M.Olszewski
 */
public class SettingsLoaderFactory
{
  /** The only existing instance of {@link SettingsLoaderFactory}. */
  private static final SettingsLoaderFactory instance = new SettingsLoaderFactory();
  
  
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private SettingsLoaderFactory()
  {
    // Intentionally left empty.
  }
  
  
  /**
   * Gets the only instance of this singleton class.
   * 
   * @return Returns the only instance of this singleton class.
   */
  public static SettingsLoaderFactory getInstance()
  {
    return instance;
  }
  
  /**
   * Creates instance of class implementing {@link SettingsLoader} interface,
   * responsible for loading data from external sources - referred by 
   * specified {@link DataSource}. This method may also return <code>null</code>
   * if specified {@link DataSource} was not recognized.
   * 
   * @param dataSource - external source of {@link SettingsLoader} for
   *        {@link SettingsLoader}.
   * 
   * @return Returns instance of class implementing {@link SettingsLoader} 
   *         responsible for loading map's data from specified {@link DataSource}.
   */
  public SettingsLoader createLoader(DataSource dataSource)
  {
    SettingsLoader loader = null;
    
    if (dataSource instanceof FileDataSource)
    {
      loader = new SettingsFileLoader(((FileDataSource)dataSource).getSource());
    }
    
    return loader;
  }
}