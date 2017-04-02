/*
 * Created on 2006-07-16
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.map;

import net.java.dante.sim.data.common.InitializationDataLoader;
import net.java.dante.sim.io.init.InitializationData;

/**
 * Class responsible for reading map data from {@link InitializationData} object
 * and converting it to simulation map format.
 * 
 * @author M.Olszewski
 */
class MapInitializationDataLoader extends InitializationDataLoader
    implements MapLoader
{

  /**
   * Creates instance of {@link MapInitializationDataLoader}.
   * 
   * @param initData - reference to {@link InitializationData} with 
   *        requested data.
   */
  MapInitializationDataLoader(InitializationData initData)
  {
    super(initData);
  }

  /** 
   * @see net.java.dante.sim.data.map.MapLoader#loadMap()
   */
  public SimulationMap loadMap() throws MapLoadingFailedException
  {
    return getInitData().getMap();
  }
}