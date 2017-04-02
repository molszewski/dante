/*
 * Created on 2006-05-18
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.config;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import net.java.dante.darknet.protocol.SupportedProtocol;
import net.java.dante.darknet.util.ValuesParser;


/**
 * Class responsible for providing objects containing configuration for
 * clients loaded from specified configuration file
 * (by calling {@link #loadConfig(File, boolean)} method) or objects
 * containing default configuration.
 * <p>
 * Default client's configuration contains:
 * <ul>
 * <li>Protocol: TCP,
 * <li>Host: 127.0.0.1,
 * <li>Port: 5335,
 * <li>Maximum timeout: 10 seconds.
 * </ul>
 *
 * @author M.Olszewski
 */
public class ClientConfig extends CommonConfig
{
  /** Default maximum timeout for client waiting to connect with server. */
  private static final int DEFAULT_MAX_TIMEOUT = 10;

  /** IP address or machine name of the host to which client will try to connect. */
  private InetAddress hostAddress = null;
  /** Maximum time for client to wait for connection with remote host. */
  private int maxTimeout = DEFAULT_MAX_TIMEOUT;

  /**
   * Constructs object representing customized default client's configuration.
   * Protected constructor - for inheritance only.
   *
   * @param defaultProtocol - custom default protocol.
   * @param defaultHostAddress - custom default host address.
   * @param defaultPorts - custom default host ports.
   * @param defaultMaxTimeout - custom default maximum timeout.
   *
   * @throws NullPointerException if <code>defaultProtocol</code>,
   *         <code>defaultPorts</code>, <code>defaultHostAddress</code>
   *         or <code>defaultHostPorts</code> are <code>null</code>.
   * @throws IllegalArgumentException if any port value in
   *         <code>defaultPorts</code> or <code>defaultHostPorts</code> is
   *         not between 1 and 65535 or
   *         maximum timeout value is zero or negative integer.
   */
  protected ClientConfig(SupportedProtocol defaultProtocol, int[] defaultPorts,
      InetAddress defaultHostAddress, int defaultMaxTimeout)
  {
    super(defaultProtocol, defaultPorts);

    if (defaultHostAddress == null)
    {
      throw new NullPointerException("Specified defaultProtocol is null!");
    }

    if (defaultMaxTimeout <= 0)
    {
      throw new IllegalArgumentException("Specified defaultMaxTimeout is zero or a negative integer!");
    }

    hostAddress = defaultHostAddress;
    maxTimeout = defaultMaxTimeout;
  }

  /**
   * Constructs object representing default client's configuration.
   * Protected constructor - for inheritance only.
   */
  protected ClientConfig()
  {
    try
    {
      hostAddress = InetAddress.getLocalHost();
    }
    catch (UnknownHostException e)
    {
      throw new RuntimeException("Cannot obtain address of the local host for default client configuration!");
    }
  }


  /**
   * Creates new {@link ClientConfig} object containing default
   * configuration:
   * <ul>
   * <li>Protocol: TCP,
   * <li>Server's address: 127.0.0.1
   * <li>Port: 5335,
   * <li>Maximum timeout: 10 seconds
   * </ul>
   *
   * @return Returns new {@link ClientConfig} object containing default
   *         configuration.
   */
  public static ClientConfig defaultConfig()
  {
    return new ClientConfig();
  }

  /**
   * Creates new {@link ClientConfig} object containing the specified
   * configuration.
   *
   * @param protocol transport protocol.
   * @param serverAddress server's address.
   * @param portNumber server's port number.
   * @param connectionTimeOut connection's time out.
   *
   * @return Returns new {@link ClientConfig} object containing specified
   *         configuration.
   */
  public static ClientConfig customConfig(SupportedProtocol protocol,
                                          InetAddress serverAddress,
                                          int portNumber,
                                          int connectionTimeOut)
  {
    return new ClientConfig(protocol,
                            new int[] { portNumber },
                            serverAddress,
                            connectionTimeOut);
  }

  /**
   * Loads client's configuration from specified configuration file to new
   * {@link CommonConfig} object.
   *
   * @param configFilePath - path to client's configuration file.
   * @param useDefaults - indicates whether default client's configuration
   *        should be returned if there was error during reading
   *        configuration file.
   *
   * @return Returns new {@link ClientConfig} object with client's configuration loaded
   *         from file or default client's configuration in case of an error
   *         (if second parameter was set to <code>true</code>).
   * @throws ConfigFileErrorException if any error occurred during reading
   *         and second parameter was set to <code>false</code>.
   * @throws NullPointerException if specified <code>configFilePath</code>
   *         is <code>null</code>.
   */
  public static ClientConfig loadConfig(File configFilePath, boolean useDefaults)
      throws ConfigFileErrorException
  {
    if (configFilePath == null)
    {
      throw new NullPointerException("Specified configFilePath is null!");
    }

    ClientConfig config = new ClientConfig();
    ConfigLoader.getInstance().load(config, configFilePath, useDefaults);
    return config;
  }

  /**
   * Loads client's configuration from specified configuration file to new
   * {@link CommonConfig} object.
   *
   * @param configFilePath - path to client's configuration file.
   * @param useDefaults - indicates whether default client's configuration
   *        should be returned if there was error during reading
   *        configuration file.
   *
   * @return Returns new {@link ClientConfig} object with client's configuration loaded
   *         from file or default client's configuration in case of an error
   *         (if second parameter was set to <code>true</code>).
   * @throws ConfigFileErrorException if any error occurred during reading
   *         and second parameter was set to <code>false</code>.
   * @throws NullPointerException if specified <code>configFilePath</code>
   *         is <code>null</code>.
   */
  public static ClientConfig loadConfig(String configFilePath, boolean useDefaults)
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

    parseHostAddressAttribute(p.getProperty(ClientAttributesNames.HOST_ADDRESS), useDefaults);
    parseMaxTimeoutAttribute(p.getProperty(ClientAttributesNames.MAX_TIMEOUT), useDefaults);
  }

  /**
   * Loads and sets value of 'Host' attribute.
   *
   * @param value - value of 'Host' attribute.
   * @param useDefaults - specifies whether default 'Host' value should
   *        be set if any error occurred during loading.
   *
   * @throws ConfigFileErrorException if error occurred during loading
   *         'Host' attribute and second parameter was set to
   *         <code>false</code>.
   */
  private void parseHostAddressAttribute(String value, boolean useDefaults)
      throws ConfigFileErrorException
  {
    if (value != null)
    {
      String hostStr = ValuesParser.formatPropertyValue(value);
      try
      {
        InetAddress tmpHostAddress = InetAddress.getByName(hostStr);
        hostAddress = tmpHostAddress;
      }
      catch (UnknownHostException e)
      {
        if (!useDefaults)
        {
          throw new ConfigFileErrorException("Cannot obtain IP address for host specified in 'HOST' attribute or malformed IP address!");
        }
      }
    }
    else
    {
      if (!useDefaults)
      {
        throw new ConfigFileErrorException("Attribute 'HOST' not found!");
      }
    }
  }

  /**
   * Loads and sets value of 'MaxTimeout' attribute.
   *
   * @param value - value of 'HostPorts' attribute.
   * @param useDefaults - MaxTimeout whether default 'MaxTimeout' value should
   *        be set if any error occurred during loading.
   *
   * @throws ConfigFileErrorException if error occurred during loading
   *         'MaxTimeout' attribute and second parameter was set to
   *         <code>false</code>.
   */
  private void parseMaxTimeoutAttribute(String value, boolean useDefaults)
      throws ConfigFileErrorException
  {
    if (value != null)
    {
      try
      {
        int tmpMaxTimeout = Integer.parseInt(ValuesParser.formatPropertyValue(value));
        if (tmpMaxTimeout > 0)
        {
          maxTimeout = tmpMaxTimeout;
        }
        else
        {
          if (!useDefaults)
          {
            throw new ConfigFileErrorException("Value of attribute 'MAX_TIMEOUT' is not a positive integer!");
          }
        }
      }
      catch (NumberFormatException e)
      {
        if (!useDefaults)
        {
          throw new ConfigFileErrorException("Value of attribute 'MAX_TIMEOUT' is not a valid integer!");
        }
      }
    }
    else
    {
      if (!useDefaults)
      {
        throw new ConfigFileErrorException("Attribute 'MAX_TIMEOUT' not found!");
      }
    }
  }

  /**
   * Gets the address of the host to which client will try to connect.
   *
   * @return Returns the address of the target host.
   */
  public InetAddress getHostAddress()
  {
    return hostAddress;
  }

  /**
   * Gets the maximum time client will wait for connection.
   *
   * @return Returns the maximum time client will wait for connection.
   */
  public int getMaxTimeout()
  {
    return maxTimeout;
  }
}