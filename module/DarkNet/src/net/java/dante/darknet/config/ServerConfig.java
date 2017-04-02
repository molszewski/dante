/*
 * Created on 2006-05-13
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.config;

import java.io.File;
import java.util.Properties;

import net.java.dante.darknet.protocol.SupportedProtocol;
import net.java.dante.darknet.util.ValuesParser;



/**
 * Class responsible for providing objects containing configuration for
 * servers loaded from specified configuration file
 * (by calling {@link #loadConfig(File, boolean)} method) or objects
 * containing default configuration.
 * <p>
 * Default server's configuration contains:
 * <ul>
 * <li>Protocol: TCP,
 * <li>Port: 5335,
 * <li>Max connections: 8.
 * </ul>
 *
 * @author M.Olszewski
 */
public class ServerConfig extends CommonConfig
{
  /** Default maximum number of connections. */
  private static final int DEFAULT_MAX_CONNECTIONS = 8;
  /** Maximum number of connections server can handle at one time. */
  private int maxConnections = DEFAULT_MAX_CONNECTIONS;


  /**
   * Constructs object representing customized default server's configuration.
   * Protected constructor - for inheritance only.
   *
   * @param defaultProtocol - custom default protocol.
   * @param defaultPorts - custom default ports.
   * @param defaultMaxConnections - custom default maximum connections.
   *
   * @throws NullPointerException if <code>defaultProtocol</code> or
   *         <code>defaultPorts</code> are <code>null</code>.
   * @throws IllegalArgumentException if any port value in
   *         <code>defaultPorts</code> is not between 1 and 65535 or
   *         maximum connections are zero or negative integer.
   */
  protected ServerConfig(SupportedProtocol defaultProtocol, int[] defaultPorts, int defaultMaxConnections)
  {
    super(defaultProtocol, defaultPorts);

    if (defaultMaxConnections > 0)
    {
      maxConnections = defaultMaxConnections;
    }
    else
    {
      throw new IllegalArgumentException("Specified defaultMaxConnections is zero or a negative integer!");
    }
  }

  /**
   * Constructs object representing default server's configuration.
   * Protected constructor - for inheritance only.
   */
  protected ServerConfig()
  {
    super();
  }


  /**
   * Creates new {@link ServerConfig} object containing default
   * configuration:
   * <ul>
   * <li>Protocol: TCP,
   * <li>Port: 5335,
   * <li>Max connections: 8.
   * </ul>
   *
   * @return Returns new {@link ServerConfig} object containing default
   *         configuration.
   */
  public static ServerConfig defaultConfig()
  {
    return new ServerConfig();
  }

  /**
   * Loads server's configuration from specified configuration file to new
   * {@link ServerConfig} object.
   *
   * @param configFilePath - path to server's configuration file.
   * @param useDefaults - indicates whether default server's configuration
   *        should be returned if there was error during reading
   *        configuration file.
   *
   * @return Returns new {@link ServerConfig} object with server's configuration loaded
   *         from file or default server's configuration in case of an error
   *         (if second parameter was set to <code>true</code>).
   * @throws ConfigFileErrorException if any error occurred during reading
   *         and second parameter was set to <code>false</code>.
   * @throws NullPointerException if specified <code>configFilePath</code>
   *         is <code>null</code>.
   */
  public static ServerConfig loadConfig(File configFilePath, boolean useDefaults)
      throws ConfigFileErrorException
  {
    if (configFilePath == null)
    {
      throw new NullPointerException("Specified configFilePath is null!");
    }

    ServerConfig config = new ServerConfig();
    ConfigLoader.getInstance().load(config, configFilePath, useDefaults);
    return config;
  }

  /**
   * Loads server's configuration from specified configuration file to new
   * {@link ServerConfig} object.
   *
   * @param configFilePath - path to server's configuration file.
   * @param useDefaults - indicates whether default server's configuration
   *        should be returned if there was error during reading
   *        configuration file.
   *
   * @return Returns new {@link ServerConfig} object with server's configuration loaded
   *         from file or default server's configuration in case of an error
   *         (if second parameter was set to <code>true</code>).
   * @throws ConfigFileErrorException if any error occurred during reading
   *         and second parameter was set to <code>false</code>.
   * @throws NullPointerException if specified <code>configFilePath</code>
   *         is <code>null</code>.
   */
  public static ServerConfig loadConfig(String configFilePath, boolean useDefaults)
      throws ConfigFileErrorException
  {
    if (configFilePath == null)
    {
      throw new NullPointerException("Specified configFilePath is null!");
    }

    return loadConfig(new File(configFilePath), useDefaults);
  }

  /**
   * @see net.java.dante.darknet.config.CommonConfig#load(java.util.Properties, boolean)
   */
  @Override
  protected void load(Properties p, boolean useDefaults) throws ConfigFileErrorException
  {
    super.load(p, useDefaults);

    parseMaxConnectionsAttribute(p.getProperty(ServerAttributesNames.MAX_CONNECTIONS), useDefaults);
  }


  /**
   * Loads and sets value of 'MaxConnections' attribute.
   *
   * @param value - value of 'MaxConnections' attribute.
   * @param useDefaults - specifies whether default 'MaxConnections' value
   *        should be set if any error occurred during loading.
   *
   * @throws ConfigFileErrorException if error occurred during loading
   *         'MaxConnections' attribute and second parameter was set to
   *         <code>false</code>.
   */
  private void parseMaxConnectionsAttribute(String value, boolean useDefaults) throws ConfigFileErrorException
  {
    if (value != null)
    {
      try
      {
        int tmpMaxConnections = Integer.parseInt(ValuesParser.formatPropertyValue(value));
        if (tmpMaxConnections > 0)
        {
          maxConnections = tmpMaxConnections;
        }
        else
        {
          if (!useDefaults)
          {
            throw new ConfigFileErrorException("Value of attribute 'MaxConnections' is not a positive integer!");
          }
        }
      }
      catch (NumberFormatException e)
      {
        if (!useDefaults)
        {
          throw new ConfigFileErrorException("Value of attribute 'MaxConnections' is not a valid integer!");
        }
      }
    }
    else
    {
      if (!useDefaults)
      {
        throw new ConfigFileErrorException("Attribute 'MaxConnections' not found!");
      }
    }
  }

  /**
   * Gets maximum number of connections that can be handled on one time
   * by server.
   *
   * @return Returns maximum number of connections stored in this
   *         {@link CommonConfig} object.
   */
  public int getMaxConnections()
  {
    return maxConnections;
  }
}