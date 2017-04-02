/*
 * Created on 2006-07-14
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.settings;

import net.java.dante.sim.data.DataLoadingFailedException;

/**
 * Interface for classes loading {@link Settings} from external sources
 * (e.g. files, network).
 *
 * @author M.Olszewski
 */
public interface SettingsLoader
{
  /**
   * Loads settings from external source and returns reference to it.
   * 
   * @return Returns instance of {@link Settings} loaded from 
   *         external source.
   * @throws DataLoadingFailedException if data loading processes failed.
   */
  Settings loadSettings() throws DataLoadingFailedException;
}
