/*
 * Created on 2006-07-12
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.template;

import net.java.dante.sim.data.DataLoadingFailedException;

/**
 * Interface for classes loading {@link TemplatesTypesStorage} from external sources
 * (e.g. files, network).
 *
 * @author M.Olszewski
 */
public interface TemplatesLoader
{
  /**
   * Loads and initializes objects templates from external source and returns 
   * reference to it.
   * 
   * @return Returns instance of {@link TemplatesTypesStorage} loaded from 
   *         external source.
   * @throws DataLoadingFailedException if data loading processes failed.
   */
  TemplatesTypesStorage loadTemplates() throws DataLoadingFailedException;
}