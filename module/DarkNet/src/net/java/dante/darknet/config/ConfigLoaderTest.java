/*
 * Created on 2006-05-20
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.config;

import java.io.File;

import net.java.dante.darknet.util.RandomGenerator;
import net.java.dante.darknet.util.TempFilesList;

import junit.framework.TestCase;

/**
 * Test class containing tests for 
 * {@link ConfigLoader#load(CommonConfig, File, boolean)} method.
 *
 * @author M.Olszewski
 */
public class ConfigLoaderTest extends TestCase
{
  /** List with valid configuration files. */
  private TempFilesList validFiles = new TempFilesList();
  
  /** List with invalid configuration files. */
  private TempFilesList invalidFiles = new TempFilesList();
  
  /** Content for valid configuration file. */
  private static final String VALID_CONFIG_FILE_CONTENT = 
      "Protocol = \"TCP\"\nPorts = {\"4444\", \"3232\", \"43\", \"445\"}";
  
  
  /** Content for valid configuration file. */
  private static final String INVALID_CONFIG_FILE_CONTENT = 
      "Protocol = \"UCP\"\nPorts = 4444\", \"3232\", \"43\", \"445\"}";
  
  /** 
   * @see junit.framework.TestCase#setUp()
   */
  @Override
  protected void setUp() throws Exception
  {
    validFiles.fillList(VALID_CONFIG_FILE_CONTENT);
    invalidFiles.fillList(INVALID_CONFIG_FILE_CONTENT);
  }

  /** 
   * @see junit.framework.TestCase#tearDown()
   */
  @Override
  protected void tearDown() throws Exception
  {
    boolean cleared = validFiles.clear(true);
    cleared &= invalidFiles.clear(true);
    assertTrue(cleared);
  }

  /**
   * Test method for {@link net.java.dante.darknet.config.ConfigLoader#load(CommonConfig, File, boolean)}
   * with <code>null</code> arguments.
   */
  public final void testLoadNull()
  {
    ConfigLoader loader = ConfigLoader.getInstance();
    
    try
    {
      loader.load(null, new File("."), false);
      fail("NullPointerException was not thrown - failed.");
    }
    catch (NullPointerException e)
    {
      // OK
    }
    
    try
    {
      loader.load(new CommonConfig(), null, false);
      fail("NullPointerException was not thrown - failed.");
    }
    catch (NullPointerException e)
    {
      // OK
    }
    
    try
    {
      loader.load(null, null, false);
      fail("NullPointerException was not thrown - failed.");
    }
    catch (NullPointerException e)
    {
      // OK
    }
  }
  
  /**
   * Test method for {@link net.java.dante.darknet.config.ConfigLoader#load(CommonConfig, File, boolean)}
   * with <code>useDefault</code> set to <code>true</code>.
   */
  public final void testLoadDefault()
  {
    loadSingleNonExisting(true);
    loadSingleExistingInvalid(true);
    loadSingleExistingValid(true);
    
    final int MULTIPLE_COUNT = 100;
    loadMultipleNonExisting(true, MULTIPLE_COUNT);
    loadMultipleExistingInvalid(true, MULTIPLE_COUNT);
    loadMultipleExistingValid(true, MULTIPLE_COUNT);
  }
  
  /**
   * Test method for {@link net.java.dante.darknet.config.ConfigLoader#load(CommonConfig, File, boolean)}
   * with <code>useDefault</code> set to <code>false</code>.
   */
  public final void testLoadNoDefault()
  {
    loadSingleNonExisting(false);
    loadSingleExistingInvalid(false);
    loadSingleExistingValid(false);
    
    final int MULTIPLE_COUNT = 100;
    loadMultipleNonExisting(false, MULTIPLE_COUNT);
    loadMultipleExistingInvalid(false, MULTIPLE_COUNT);
    loadMultipleExistingValid(false, MULTIPLE_COUNT);
  }
  
  /**
   * Loads configuration from specified file many times.
   *  
   * @param file - file with configuration.
   * @param useDefault - passed as third parameter of 
   *        {@link ConfigLoader#load(CommonConfig, File, boolean)}.
   * @param count - number of calls to {@link ConfigLoader#load(CommonConfig, File, boolean)}.
   */
  private void loadMultiple(File file, boolean useDefault, int count)
  {
    ConfigLoader loader = ConfigLoader.getInstance();
    CommonConfig templateConfig = new CommonConfig();
    CommonConfig config = new CommonConfig();
    boolean catchException = false;
    
    try
    {
      loader.load(templateConfig, file, useDefault);
    }
    catch (ConfigFileErrorException e)
    {
      if (!useDefault)
      {
        catchException = true;
      }
      else
      {
        throw e;
      }
    }
    
    if (!catchException)
    {
      for (int i = 0; i < count; i++)
      {
        loader.load(config, file, useDefault);
        assertEquals(templateConfig, config);
      }
    }
    else
    {
      for (int i = 0; i < count; i++)
      {
        try
        {
          loader.load(config, file, useDefault);
          fail("ConfigFileErrorException not thrown - failed.");
        }
        catch (ConfigFileErrorException e)
        {
          // OK
        }
      }
    }
  }
  
  /**
   * Tries to load configuration file from non-existing file once.
   *  
   * @param useDefault - passed as third parameter of 
   *        {@link ConfigLoader#load(CommonConfig, File, boolean)}.
   */
  private void loadSingleNonExisting(boolean useDefault)
  {
    loadMultipleNonExisting(useDefault, 1);
  }
  
  /**
   * Tries to load configuration file from non-existing file multiple times.
   *  
   * @param useDefault - passed as third parameter of 
   *        {@link ConfigLoader#load(CommonConfig, File, boolean)}.
   * @param count - number of calls to {@link ConfigLoader#load(CommonConfig, File, boolean)}. 
   */
  private void loadMultipleNonExisting(boolean useDefault, int count)
  {
    File nonExisting = getNonExistingFile(new File("."), 100);
    if (nonExisting != null)
    {
      loadMultiple(nonExisting, useDefault, count);
    }
    else
    {
      fail("Cannot get non-existing file!");
    }
  }
  
  /**
   * Tries to load configuration file from existing file with valid configuration 
   * once.
   *  
   * @param useDefault - passed as third parameter of 
   *        {@link ConfigLoader#load(CommonConfig, File, boolean)}.
   */
  private void loadSingleExistingValid(boolean useDefault)
  {
    loadMultipleExistingValid(useDefault, 1);
  }
  
  /**
   * Tries to load configuration file from existing file with valid configuration 
   * once.
   *  
   * @param useDefault - passed as third parameter of 
   *        {@link ConfigLoader#load(CommonConfig, File, boolean)}.
   * @param count - number of calls to {@link ConfigLoader#load(CommonConfig, File, boolean)}.
   */
  private void loadMultipleExistingValid(boolean useDefault, int count)
  {
    File validFile = validFiles.get(0);
    
    loadMultiple(validFile, useDefault, count);
  }
  
  /**
   * Tries to load configuration file from existing file with invalid configuration 
   * once.
   *  
   * @param useDefault - passed as third parameter of 
   *        {@link ConfigLoader#load(CommonConfig, File, boolean)}.
   */
  private void loadSingleExistingInvalid(boolean useDefault)
  {
    loadMultipleExistingInvalid(useDefault, 1);
  }
  
  /**
   * Tries to load configuration file from existing file with invalid configuration 
   * once.
   *  
   * @param useDefault - passed as third parameter of 
   *        {@link ConfigLoader#load(CommonConfig, File, boolean)}.
   * @param count - number of calls to {@link ConfigLoader#load(CommonConfig, File, boolean)}.
   */
  private void loadMultipleExistingInvalid(boolean useDefault, int count)
  {
    File validFile = invalidFiles.get(0);
    
    loadMultiple(validFile, useDefault, count);
  }
  
  /**
   * Creates {@link File} object referencing to non-existing file in
   * specified directory or <code>null</code> if creating such file was failed
   * for specified number of attempts.
   * <p>It is guaranteed that specified {@link File} object refers to file
   * that was not existing <i>before</i> calling this method.
   * 
   * @param directory - base directory for non-existing file.
   * @param attempts - number of attempts after which creation will be indicated
   *        as failed.
   * 
   * @return Returns {@link File} object referencing to non-existing file in
   *         specified directory or <code>null</code> if creating such file 
   *         was failed.
   */
  private File getNonExistingFile(File directory, int attempts)
  {
    final int MIN_FILE_NAME_LEN = 50;
    final int MAX_FILE_NAME_LEN = 150;
    final int MIN_EXT_LEN = 1;
    final int MAX_EXT_LEN = 6;
    
    File file = null;
    
    for (int i = 0; i <= Integer.MAX_VALUE; i++)
    {
      String fileName = RandomGenerator.randomString(MIN_FILE_NAME_LEN, MAX_FILE_NAME_LEN);
      String extName = RandomGenerator.randomString(MIN_EXT_LEN, MAX_EXT_LEN);
      file = new File(directory, fileName + File.separatorChar + extName);
      if (!file.exists())
      {
        break;
      }
    }
    
    return file;
  }
}