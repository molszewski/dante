/*
 * Created on 2006-07-12
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.template;

import net.java.dante.sim.data.common.DataSource;
import net.java.dante.sim.data.common.FileDataSource;
import net.java.dante.sim.data.common.InitializationDataSource;

/**
 * Factory class delivering instances of classes implementing {@link TemplatesLoader}
 * interface.
 *
 * @author M.Olszewski
 */
public class TemplatesLoadersFactory
{
  /** The only existing instance of {@link TemplatesLoadersFactory}. */
  private static final TemplatesLoadersFactory instance = new TemplatesLoadersFactory();
  
  
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private TemplatesLoadersFactory()
  {
    // Intentionally left empty.
  }
  
  
  /**
   * Gets the only instance of this singleton class.
   * 
   * @return Returns the only instance of this singleton class.
   */
  public static TemplatesLoadersFactory getInstance()
  {
    return instance;
  }
  
  /**
   * Creates instance of class implementing {@link TemplatesLoader} interface,
   * responsible for loading templates data from external sources - referred by 
   * specified {@link DataSource}. This method may also return <code>null</code>
   * if specified {@link DataSource} was not recognized.
   * 
   * @param dataSource - external source of {@link TemplatesTypesStorage} for
   *        {@link TemplatesLoader}.
   * 
   * @return Returns instance of class implementing {@link TemplatesLoader} 
   *         responsible for loading template's data from specified {@link DataSource}.
   */
  public TemplatesLoader createLoader(DataSource dataSource)
  {
    TemplatesLoader loader = null;
    
    if (dataSource instanceof FileDataSource)
    {
      loader = new TemplatesFileLoader(((FileDataSource)dataSource).getSource());    
    }
    else if (dataSource instanceof InitializationDataSource)
    {
      loader = new TemplatesInitializationDataLoader(((InitializationDataSource)dataSource).getData());
    }
    
    return loader;
  }
}