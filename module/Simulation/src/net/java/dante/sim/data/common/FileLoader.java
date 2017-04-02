/*
 * Created on 2006-07-12
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.java.dante.sim.common.ResourceLoadFailedException;



/**
 * Abstract class for subclasses that loads their data from files.
 *
 * @author M.Olszewski
 */
public abstract class FileLoader
{
  /** File name where data can be obtained. */
  private String fileName;

  
  /**
   * Creates instance of {@link FileLoader} holding reference to specified
   * file.
   * 
   * @param dataFileName - file name from which data should be obtained.
   * 
   * @throws NullPointerException if specified <code>dataFileName</code> is <code>null</code>.
   * @throws IllegalArgumentException if specified <code>dataFileName</code>
   *         is an empty string.
   */
  protected FileLoader(String dataFileName)
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
   * Gets file name from which data should be obtained.
   * 
   * @return Returns file name from which data should be obtained.
   */
  public final String getFileName()
  {
    return fileName;
  }
  
  /**
   * Gets {@link URL} object representing specified file.
   * 
   * @return Returns {@link URL} object representing specified file.
   */
  public final URL getURL()
  {
    return getClass().getClassLoader().getResource(fileName.trim());
  }
  
  /**
   * Opens file specified by the file name from {@link #FileLoader(String)} 
   * constructor. File is at first transformed into {@link URL} object and
   * result of {@link java.net.URL#openStream()} method call is returned.
   * 
   * @return Returns reference to {@link InputStream} object representing 
   *         opened file.
   * @throws IOException if file cannot be opened.
   * @throws ResourceLoadFailedException if {@link URL} object cannot be obtain 
   *         for the specified file.
   */
  public final InputStream openFile() throws IOException, ResourceLoadFailedException
  {
    URL url = getURL();
    if (url == null)
    {
      throw new ResourceLoadFailedException("Cannot find or access file: " + fileName + "!");
    }
    
    return url.openStream();
  }
}