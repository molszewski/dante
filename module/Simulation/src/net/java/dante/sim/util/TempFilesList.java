/*
 * Created on 2006-05-20
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class responsible for maintenance of list with temporary files.
 * Temporary file on the list are created by using one of the methods: 
 * {@link #fillList(String)} or {@link #fillList(String[])}.
 * They are filled with contents passed as parameter to these methods.
 *
 * @author M.Olszewski
 */
public class TempFilesList
{
  /** Prefix for all temporarily created files. */
  private static final String CONFIG_FILE_PREFIX = "TMP__file__";
  /** Suffix for all temporarily created files. */
  private static final String CONFIG_FILE_SUFFIX = ".template_file";
  /** {@link File} object pointing to current working directory. */
  private static final File WORKING_DIRECTORY = new File(".");
  
  /** Directory where all temporary files will be held. */
  private File directory = WORKING_DIRECTORY;
  /** List with all created files. */
  private List<File> createdFiles = new ArrayList<File>();
  
  /**
   * Default constructor - directory where all temporary files will be stored
   * is set to current working directory.
   */
  public TempFilesList()
  {
    // Intentionally left empty.
  }
  
  /**
   * Constructs {@link TempFilesList} object with specified directory
   * for temporary files.
   * 
   * @param filesDirectory - directory where all temporary files will be stored.
   * 
   * @throws IllegalStateException if <code>filesDirectory</code> is not a 
   *         directory.
   * @throws NullPointerException if <code>filesDirectory</code> is <code>null</code>.
   */
  public TempFilesList(String filesDirectory)
  {
    this(new File(filesDirectory));
  }
  
  /**
   * Constructs {@link TempFilesList} object with specified directory
   * for temporary files.
   * 
   * @param filesDirectory - directory where all temporary files will be stored.
   * 
   * @throws IllegalStateException if <code>filesDirectory</code> is not a 
   *         directory.
   * @throws NullPointerException if <code>filesDirectory</code> is <code>null</code>. 
   */
  public TempFilesList(File filesDirectory)
  {
    if (filesDirectory == null)
    {
      throw new NullPointerException("Specified filesDirectory is null!");
    }
    
    if (filesDirectory.isDirectory())
    {
      directory = filesDirectory;
    }
    else
    {
      throw new IllegalStateException("Specified filesDirectory is not a directory!");
    }
  }
  
  /**
   * Creates temporary files with specified contents. Each {@link String} 
   * in specified array is a content for new temporary file.
   * Array elements with <code>null</code> references are skipped.  
   * 
   * @param content - contents for temporary files.
   * 
   * @throws IOException if creation or writing to any of temporary files was
   *         failed.
   * @throws NullPointerException if <code>content</code> is <code>null</code>.
   */
  public void fillList(String[] content) throws IOException
  {
    if (content == null)
    {
      throw new NullPointerException("Specified content is null!");
    }
    
    for (int i = 0; i < content.length; i++)
    {
      if (content[i] != null)
      {
        File cfgFile = File.createTempFile(CONFIG_FILE_PREFIX, CONFIG_FILE_SUFFIX, directory);
        createdFiles.add(cfgFile);
        
        FileWriter writer = new FileWriter(cfgFile);
        writer.append(content[i]);
        writer.flush();
        writer.close();
      }
    }
  }
  
  /**
   * Creates one temporary file with specified contents. 
   * 
   * @param content - contents for temporary file.
   * 
   * @throws IOException if creation or writing to any of temporary files was
   *         failed.
   * @throws NullPointerException if <code>content</code> is <code>null</code>.
   */
  public void fillList(String content) throws IOException
  {
    if (content == null)
    {
      throw new NullPointerException("Specified content is null!");
    }
    
    fillList(new String[]{ content });
  }
  
  /**
   * Gets temporary file with specified index.
   * 
   * @param index - index of temporary file.
   * 
   * @return Returns temporary file with specified index.
   * @throws IndexOutOfBoundsException if the index is out of range 
   *         (<code>index < 0 || index >= size()</code>).
   */
  public File get(int index)
  {
    return createdFiles.get(index);
  }
  
  /**
   * Gets size of temporary files list.
   * 
   * @return Returns size of temporary files list.
   */
  public int size()
  {
    return createdFiles.size();
  }
  
  /**
   * Clears list with temporary files, also removing files from the list
   * if specified.
   * 
   * @param removeFiles - indicates whether files held in the list should be 
   *        removed from file system before clearing list.
   * 
   * @return Returns <code>true</code> if whole operation was successful,
   *         <code>false</code> otherwise.
   */
  public boolean clear(boolean removeFiles)
  {
    boolean cleared = true;
    if (removeFiles)
    {
      cleared = removeAll();
    }
    createdFiles.clear();
    
    return cleared;
  }
  
  /**
   * Removes all files stored in the list from file system.
   * 
   * @return Returns <code>true</code> if all files were removed, 
   *         <code>false</code> otherwise.
   */
  private boolean removeAll()
  {
    boolean removed = true;
    for (int i = 0, size = createdFiles.size(); i < size; i++)
    {
      File file = createdFiles.get(i);
      if (!file.delete())
      {
        removed = false;
      }
    }
    
    return removed;
  }
}