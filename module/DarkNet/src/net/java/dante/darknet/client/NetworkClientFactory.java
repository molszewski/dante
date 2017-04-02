/*
 * Created on 2006-05-18
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.client;

import java.io.File;

import net.java.dante.darknet.config.ClientConfig;
import net.java.dante.darknet.config.ConfigFileErrorException;


/**
 * Factory class creating configured instances of classes implementing
 * {@link NetworkClient} interface.
 *
 * @author M.Olszewski
 */
public class NetworkClientFactory
{
  /** The only existing instance of this class. */
  private static NetworkClientFactory instance = new NetworkClientFactory();


  /**
   * Private constructor - no inheritance or external objects creation
   * (singleton).
   */
  private NetworkClientFactory()
  {
    // Intentionally left empty.
  }


  /**
   * Standard singleton method - gets the only one existing instance of
   * {@link NetworkClientFactory} class.
   *
   * @return Returns the only one existing instance of {@link NetworkClientFactory}
   *         class.
   */
  public static NetworkClientFactory getInstance()
  {
    return instance;
  }

  /**
   * Creates new {@link NetworkClient}, loads its configuration from specified file
   * and configures client with loaded data.
   * <p>
   * If second parameter is set to <code>true</code> then in case of any
   * error default configuration will be used. Also any error in values
   * of attributes will result in setting invalid values with default values.
   * <p>
   * If second parameter is set to <code>false</code> then in case of any
   * error {@link ConfigFileErrorException} will be thrown.
   *
   * @param configFilePath - file name with client's configuration.
   * @param useDefaults - indicates whether {@link NetworkClient} with default
   *        configuration should be returned if there was error during reading
   *        configuration file.
   *
   * @return Returns new {@link NetworkClient} configured from specified file.
   * @throws ConfigFileErrorException if any error occurred during reading
   *         and second parameter was set to <code>false</code>.
   * @throws NullPointerException if specified <code>configFilePath</code> is
   *         <code>null</code>.
   */
  public NetworkClient initClient(File configFilePath, boolean useDefaults)
      throws ConfigFileErrorException
  {
    NetworkClient client = null;
    if (configFilePath != null)
    {
      ClientConfig config = null;

      try
      {
        config = ClientConfig.loadConfig(configFilePath, useDefaults);
      }
      catch (ConfigFileErrorException e)
      {
        if (!useDefaults)
        {
          // re-throw: client doesn't want default settings
          throw e;
        }
        //else: set default configuration
        config = ClientConfig.defaultConfig();
      }

      client = new NetworkClientImpl();
      client.configure(config);
    }
    else
    {
      throw new NullPointerException("Specified configFilePath is null!");
    }

    return client;
  }

  /**
   * Creates new {@link NetworkClient}, loads its configuration from specified file
   * and configures client with loaded data.
   * <p>
   * If second parameter is set to <code>true</code> then in case of any
   * error default configuration will be used. Also any error in values
   * of attributes will result in setting invalid values with default values.
   * <p>
   * If second parameter is set to <code>false</code> then in case of any
   * error {@link ConfigFileErrorException} will be thrown.
   *
   * @param configFilePath - file name with client's configuration.
   * @param useDefaults - indicates whether {@link NetworkClient} with default
   *        configuration should be returned if there was error during reading
   *        configuration file.
   *
   * @return Returns new {@link NetworkClient} configured from specified file.
   * @throws ConfigFileErrorException if any error occurred during reading
   *         and second parameter was set to <code>false</code>.
   * @throws NullPointerException if specified <code>configFilePath</code> is
   *         <code>null</code>.
   */
  public NetworkClient initClient(String configFilePath, boolean useDefaults)
      throws ConfigFileErrorException
  {
    return initClient(new File(configFilePath), useDefaults);
  }

  /**
   * Creates new {@link NetworkClient} and configures it with default configuration.
   *
   * @return Returns new {@link NetworkClient} with default configuration.
   */
  public NetworkClient initDefaultClient()
  {
    NetworkClient client = new NetworkClientImpl();
    client.configure(ClientConfig.defaultConfig());
    return client;
  }

  /**
   * Creates new {@link NetworkClient} and configures it with default configuration.
   *
   * @param config - server's configuration.
   *
   * @return Returns new {@link NetworkClient} with default configuration.
   */
  public NetworkClient initCustomClient(ClientConfig config)
  {
    NetworkClient client = new NetworkClientImpl();
    client.configure(config);
    return client;
  }
}