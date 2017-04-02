/*
 * Created on 2006-07-06
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data;

/**
 * Interface for classes loading {@link SimulationData} from external sources
 * (e.g. files, network).
 *
 * @author M.Olszewski
 */
public interface DataLoader
{
  /**
   * Loads data from external source and returns reference to it.
   * 
   * @return Returns instance of {@link SimulationData} loaded from 
   *         external source.
   * @throws DataLoadingFailedException if data loading processes failed.
   */
  SimulationData loadData() throws DataLoadingFailedException;
}