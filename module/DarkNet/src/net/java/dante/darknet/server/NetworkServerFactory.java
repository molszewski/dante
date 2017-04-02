/*
 * Created on 2006-05-01
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.server;

import java.io.File;

import net.java.dante.darknet.config.ConfigFileErrorException;
import net.java.dante.darknet.config.ServerConfig;


/**
 * Factory class creating configured instances of classes implementing
 * {@link NetworkServer} interface.
 *
 * @author M.Olszewski
 */
public class NetworkServerFactory
{
  /** The only existing instance of this class. */
  private static NetworkServerFactory instance = new NetworkServerFactory();

  /**
   * Private constructor - no inheritance or external objects creation
   * (singleton).
   */
  private NetworkServerFactory()
  {
    // Intentionally left empty.
  }

  /**
   * Standard singleton method - gets the only one existing instance of
   * {@link NetworkServerFactory} class.
   *
   * @return Returns the only one existing instance of {@link NetworkServerFactory}
   *         class.
   */
  public static NetworkServerFactory getInstance()
  {
    return instance;
  }

  /**
   * Creates new {@link NetworkServer}, loads its configuration from specified file
   * and configures server with loaded data.
   * <p>
   * If second parameter is set to <code>true</code> then in case of any
   * error default configuration will be used. Also any error in values
   * of attributes will result in setting invalid values with default values.
   * <p>
   * If second parameter is set to <code>false</code> then in case of any
   * error {@link ConfigFileErrorException} will be thrown.
   *
   * @param configFilePath - file name with server's configuration.
   * @param useDefaults - indicates whether {@link NetworkServer} with default
   *        configuration should be returned if there was error during reading
   *        configuration file.
   *
   * @return Returns new {@link NetworkServer} configured from specified file.
   * @throws ConfigFileErrorException if any error occurred during reading
   *         and second parameter was set to <code>false</code>.
   * @throws NullPointerException if specified <code>configFilePath</code> is
   *         <code>null</code>.
   */
  public NetworkServer initServer(File configFilePath, boolean useDefaults)
      throws ConfigFileErrorException
  {
    NetworkServer server = null;
    if (configFilePath != null)
    {
      ServerConfig config = null;

      try
      {
        config = ServerConfig.loadConfig(configFilePath, useDefaults);
      }
      catch (ConfigFileErrorException e)
      {
        if (!useDefaults)
        {
          // re-throw: client doesn't want default settings
          throw e;
        }
        //else: set default configuration
        config = ServerConfig.defaultConfig();
      }

      server = new NetworkServerImpl();
      server.configure(config);
    }
    else
    {
      throw new NullPointerException("Specified configFilePath is null!");
    }

    return server;
  }

  /**
   * Creates new {@link NetworkServer}, loads its configuration from specified file
   * and configures server with loaded data.
   * <p>
   * If second parameter is set to <code>true</code> then in case of any
   * error default configuration will be used. Also any error in values
   * of attributes will result in setting invalid values with default values.
   * <p>
   * If second parameter is set to <code>false</code> then in case of any
   * error {@link ConfigFileErrorException} will be thrown.
   *
   * @param configFilePath - file name with server's configuration.
   * @param useDefaults - specifies whether
   *
   * @return Returns new {@link NetworkServer} configured from specified file.
   * @throws ConfigFileErrorException if any error occurred during reading
   *         and second parameter was set to <code>false</code>.
   * @throws NullPointerException if specified <code>configFilePath</code> is
   *         <code>null</code>.
   */
  public NetworkServer initServer(String configFilePath, boolean useDefaults)
      throws ConfigFileErrorException
  {
    return initServer(new File(configFilePath), useDefaults);
  }

  /**
   * Creates new {@link NetworkServer} and configures it with default configuration.
   *
   * @return Returns new {@link NetworkServer} with default configuration.
   */
  public NetworkServer initDefaultServer()
  {
    NetworkServer server = new NetworkServerImpl();
    server.configure(ServerConfig.defaultConfig());
    return server;
  }

  /**
   * Creates new {@link NetworkServer} and configures it with default configuration.
   *
   * @param config - server's configuration.
   *
   * @return Returns new {@link NetworkServer} with default configuration.
   */
  public NetworkServer initCustomServer(ServerConfig config)
  {
    NetworkServer server = new NetworkServerImpl();
    server.configure(config);
    return server;
  }
}