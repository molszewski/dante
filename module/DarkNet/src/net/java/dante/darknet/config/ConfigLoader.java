/*
 * Created on 2006-05-13
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


/**
 * Helper class performing full process of loading configuration from
 * files: all properties are stored in {@link Properties} object and
 * then retrieved by {@link net.java.dante.darknet.config.CommonConfig#load(Properties, boolean)}
 * method.
 *
 * @author M.Olszewski
 */
public class ConfigLoader
{
  /** Default size of the buffer reading configuration file. */
  private static final int DEFAULT_BUFFER_SIZE = 1024;
  /** The only instance of this {@link ConfigLoader}. */
  private static final ConfigLoader instance = new ConfigLoader();
  
  /**
   * Default private constructor - no inheritance, no external objects 
   * creation.
   */
  private ConfigLoader()
  {
    // Intentionally left empty.
  }
  
  /**
   * Gets the only instance of {@link ConfigLoader}.
   * 
   * @return Returns the only instance of {@link ConfigLoader}.
   */
  public static ConfigLoader getInstance()
  {
    return instance;
  }
  
  /**
   * Performs full process of loading configuration from specified file to 
   * specified instance of {@link CommonConfig} or its subclass.
   * <p>
   * Second parameter determines whether default configuration should 
   * be used if loading configuration from file have failed. 
   * Also, if <code>useDefaults</code> is set to <code>true</code>, then 
   * any attribute's value read with error will be replaced by appropriate 
   * default value.
   * <p>
   * If third parameter is set to <code>false</code> then in case of any error
   * during configuration loading {@link ConfigFileErrorException} is thrown.
   *  
   * @param config - instance of {@link CommonConfig} or its subclass where
   *        all configuration will be stored.
   * @param configFilePath - path to configuration file.
   * @param useDefaults - indicates whether default configuration 
   *        should be returned if there was error during reading 
   *        configuration file.
   *        It also indicates whether attribute's values read with error should
   *        be replaced with appropriate default values.
   *         
   * @throws ConfigFileErrorException if any error occurred during reading 
   *         and second parameter was set to <code>false</code>.
   * @throws NullPointerException if specified <code>configFilePath</code> 
   *         is <code>null</code> or specified <code>config</code> is 
   *         <code>null</code>.
   */
  public void load(CommonConfig config, File configFilePath, boolean useDefaults) 
      throws ConfigFileErrorException
  {
    if (configFilePath == null)
    {
      throw new NullPointerException("Specified configFilePath is null!");
    }
    if (config == null)
    {
      throw new NullPointerException("Specified config is null!");
    }
    
    Properties properties = new Properties();
      
    try
    {
      BufferedInputStream bufferedIn = 
          new BufferedInputStream(new FileInputStream(configFilePath), DEFAULT_BUFFER_SIZE);
        
      properties.load(bufferedIn);
      bufferedIn.close();
      
      config.setConfigPath(configFilePath);
      config.load(properties, useDefaults);
    }
    catch (FileNotFoundException e)
    {
      if (!useDefaults)
      {
        throw new ConfigFileErrorException("Specified configuration file ("+ configFilePath +") does not exist!");
      }
    }
    catch (IOException e)
    {
      if (!useDefaults)
      {
        throw new ConfigFileErrorException("Error occurred while reading specified configuration file (" + configFilePath + ")!");
      }
    }
  }
}