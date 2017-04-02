/*
 * Created on 2006-07-16
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data;

import net.java.dante.sim.data.common.InitializationDataLoader;
import net.java.dante.sim.data.common.InitializationDataSource;
import net.java.dante.sim.data.map.MapLoader;
import net.java.dante.sim.data.map.MapLoadersFactory;
import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.template.TemplatesLoader;
import net.java.dante.sim.data.template.TemplatesLoadersFactory;
import net.java.dante.sim.data.template.TemplatesTypesStorage;
import net.java.dante.sim.io.init.InitializationData;

/**
 * Implementation of {@link DataLoader} interface loading data to 
 * {@link SimulationData} from data held in {@link InitializationData}, 
 * received from input.
 *
 * @author M.Olszewski
 */
class MainInitializationDataLoader extends InitializationDataLoader implements DataLoader
{
  /**
   * Creates {@link MainInitializationDataLoader} object loading data to  
   * {@link SimulationData} from data held in specified 
   * {@link InitializationData} object. 
   * 
   * @param loaderInitData - {@link InitializationData} object storing data for 
   *        {@link SimulationData}.
   * 
   * @throws NullPointerException if specified <code>loaderInitData</code> is <code>null</code>.
   */
  MainInitializationDataLoader(InitializationData loaderInitData)
  {
    super(loaderInitData);
  }

  /** 
   * @see net.java.dante.sim.data.DataLoader#loadData()
   */
  public SimulationData loadData() throws DataLoadingFailedException
  {
    InitializationDataSource source = new InitializationDataSource(getInitData());
    
    // 1. Load map file
    MapLoader mapLoader = MapLoadersFactory.getInstance().createLoader(source);
    SimulationMap map = mapLoader.loadMap();
    
    // 2. Load templates types data
    TemplatesLoader templatesLoader = TemplatesLoadersFactory.getInstance().createLoader(source);
    TemplatesTypesStorage storage = templatesLoader.loadTemplates();
    
    // 3. Init objects
    ClientSimulationDataImpl simData = new ClientSimulationDataImpl(map, storage, source.getData().getGlobalData());
    simData.initObjects(source.getData().getAgentsInitData());
    
    return simData;
  }
}