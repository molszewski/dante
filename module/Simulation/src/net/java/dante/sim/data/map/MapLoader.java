/*
 * Created on 2006-07-09
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.map;

/**
 * Interface for classes loading {@link SimulationMap} from external sources
 * (e.g. files, network).
 *
 * @author M.Olszewski
 */
public interface MapLoader
{
  /**
   * Loads map's data from external source and returns reference to it.
   * 
   * @return Returns instance of {@link SimulationMap} loaded from 
   *         external source.
   * @throws MapLoadingFailedException if data loading processes failed.
   */
  SimulationMap loadMap() throws MapLoadingFailedException;
}
