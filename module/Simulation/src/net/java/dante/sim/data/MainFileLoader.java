/*
 * Created on 2006-07-06
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data;

import net.java.dante.sim.data.common.FileDataSource;
import net.java.dante.sim.data.common.FileLoader;
import net.java.dante.sim.data.map.MapLoader;
import net.java.dante.sim.data.map.MapLoadersFactory;
import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.settings.SettingsLoader;
import net.java.dante.sim.data.settings.SettingsLoaderFactory;
import net.java.dante.sim.data.settings.SimulationSettings;
import net.java.dante.sim.data.template.TemplatesLoader;
import net.java.dante.sim.data.template.TemplatesLoadersFactory;
import net.java.dante.sim.data.template.TemplatesTypesStorage;

/**
 * Implementation of {@link DataLoader} interface loading data to 
 * {@link SimulationData} from files.
 *
 * @author M.Olszewski
 */
class MainFileLoader extends FileLoader implements DataLoader
{
  /**
   * Creates {@link MainFileLoader} object loading data to  
   * {@link SimulationData} from specified file.
   * 
   * @param dataFileName - name of the file from which data will be loaded.
   * 
   * @throws NullPointerException if specified <code>dataFileName</code> is <code>null</code>.
   * @throws IllegalArgumentException if specified <code>dataFileName</code>
   *         does not refer to a file.
   */
  MainFileLoader(String dataFileName)
  {
    super(dataFileName);
  }
  
  /** 
   * @see net.java.dante.sim.data.DataLoader#loadData()
   */
  public SimulationData loadData() throws DataLoadingFailedException
  {
    // 1. Load main configuration file
    SettingsLoader settingsLoader = SettingsLoaderFactory.getInstance().createLoader(new FileDataSource(getFileName()));
    SimulationSettings settings = (SimulationSettings)settingsLoader.loadSettings();
    
    // 2. Load map file
    MapLoader mapLoader = MapLoadersFactory.getInstance().createLoader(
        new FileDataSource(settings.getMapFile()));
    SimulationMap map = mapLoader.loadMap();
    
    // 3. Load templates types data
    TemplatesLoader templatesLoader = TemplatesLoadersFactory.getInstance().createLoader(
        new FileDataSource(settings.getAgentFile()));
    TemplatesTypesStorage storage = templatesLoader.loadTemplates();
    
    return new ServerSimulationDataImpl(map, storage, settings);
  }
}