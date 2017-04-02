/*
 * Created on 2006-07-07
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.common;

import java.io.File;

import net.java.dante.sim.data.SimulationData;


/**
 * Class representing {@link DataSource} as file with data for 
 * {@link SimulationData}.
 *
 * @author M.Olszewski
 */
public class FileDataSource implements DataSource
{
  /** Existing file name - source of data for {@link SimulationData}. */
  private String fileName;
  
  /**
   * Creates {@link FileDataSource} object referring specified file name.
   * 
   * @param dataFileName - name of the file referred by this object.
   * 
   * @throws NullPointerException if specified <code>dataFileName</code> is <code>null</code>.
   * @throws IllegalArgumentException if specified <code>dataFileName</code>
   *         is an empty string.
   */
  public FileDataSource(String dataFileName)
  {
    if (dataFileName == null)
    {
      throw new NullPointerException("Specified dataFileName is null!");
    }
    if (dataFileName.length() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument dataFileName - it cannot be empty string!");
    }
    
    fileName = dataFileName;
  }
  
  /**
   * Gets source of {@link SimulationData} - in this case it is {@link File}
   * object.
   * 
   * @return Returns source of {@link SimulationData}.
   */
  public String getSource()
  {
    return fileName;
  }
}