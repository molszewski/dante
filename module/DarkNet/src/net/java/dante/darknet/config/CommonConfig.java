/*
 * Created on 2006-05-01
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.config;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;

import net.java.dante.darknet.protocol.SupportedProtocol;
import net.java.dante.darknet.util.InvalidValueException;
import net.java.dante.darknet.util.ValuesParser;


/**
 * Class responsible for providing objects containing configuration common for
 * client or server end-point and loaded from specified configuration file.
 * <p>
 * Each subclass of CommonConfig should provide default constructor that
 * creates default configuration for specified subclass.
 * <p>
 * Default common configuration contains:
 * <ul>
 * <li>Protocol: TCP,
 * <li>Port: 5335.
 * </ul>
 *
 * @author M.Olszewski
 */
public class CommonConfig
{
  /** Default protocol used by end-point. */
  private static final SupportedProtocol DEFAULT_PROTOCOL = SupportedProtocol.TCP;
  /**
   * Default port used by server to listen for connections or
   * default port for client connection.
   */
  private static final int[] DEFAULT_PORTS = new int[]{ 5335 };

  /** Name of the protocol used by end-point to communicate with other end-point. */
  private SupportedProtocol protocol = DEFAULT_PROTOCOL;
  /**
   * Ports on which server will try to listen for connections or
   * local ports used by client.
   */
  private int[] ports = DEFAULT_PORTS.clone();

  /**
   * File from which configuration was obtained. It is set to <code>null</code>
   * in default configuration.
   */
  private File configPath = null;

  /**
   * Constructs object representing customized default common configuration.
   * Protected constructor - for inheritance only.
   *
   * @param defaultProtocol - custom default protocol.
   * @param defaultPorts - custom default ports.
   *
   * @throws NullPointerException if <code>defaultProtocol</code> or
   *         <code>defaultPorts</code> are <code>null</code>.
   * @throws IllegalArgumentException if any port value in
   *         <code>defaultPorts</code> is not between 1 and 65535.
   */
  protected CommonConfig(SupportedProtocol defaultProtocol, int[] defaultPorts)
  {
    if (defaultProtocol == null)
    {
      throw new NullPointerException("Specified defaultProtocol is null!");
    }

    if (defaultPorts == null)
    {
      throw new NullPointerException("Specified defaultPorts are null!");
    }

    if (!ConfigUtils.checkPorts(defaultPorts))
    {
      throw new IllegalArgumentException("One of the specified default ports has not allowed number!");
    }

    protocol = defaultProtocol;
    ports = new int[defaultPorts.length];
    System.arraycopy(defaultPorts, 0, ports, 0, defaultPorts.length);
  }

  /**
   * Constructs object representing default common configuration.
   * Protected constructor - for inheritance only.
   */
  protected CommonConfig()
  {
    // Intentionally left empty.
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj)
  {
    boolean equal = (obj == this);

    if (!equal && (obj instanceof CommonConfig))
    {
      CommonConfig config = (CommonConfig)obj;
      equal = (Arrays.equals(ports, config.ports) && (protocol == config.protocol));
    }

    return equal;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    int result = 19;
    result = 37 * result + protocol.hashCode();
    result = 37 * result + Arrays.hashCode(ports);

    return result;
  }

  /**
   * Loads common configuration from specified {@link Properties} object
   * to this {@link CommonConfig} object.
   *
   * @param p - properties containing common configuration.
   * @param useDefaults - indicates whether in case of an error during reading
   *        attribute's value its default value should be set.
   *
   * @throws ConfigFileErrorException if error occurred during loading
   *         properties and second parameter was set to <code>false</code>.
   */
  protected void load(Properties p, boolean useDefaults) throws ConfigFileErrorException
  {
    loadProtocolAttribute(p.getProperty(CommonAttributesNames.PROTOCOL), useDefaults);
    parsePortsAttribute(p.getProperty(CommonAttributesNames.PORTS), useDefaults);
  }

  /**
   * Loads and sets value of 'Protocol' attribute.
   *
   * @param value - value of 'Protocol' attribute.
   * @param useDefaults - specifies whether default 'Protocol' value
   *        should be set if any error occurred during loading.
   *
   * @throws ConfigFileErrorException if error occurred during loading
   *         'Protocol' attribute and second parameter was set to
   *         <code>false</code>.
   */
  private void loadProtocolAttribute(String value, boolean useDefaults) throws ConfigFileErrorException
  {
    if (value != null)
    {
      String formattedValue = ValuesParser.formatPropertyValue(value).toUpperCase();
      if (SupportedProtocol.isSupported(formattedValue))
      {
        protocol = SupportedProtocol.valueOf(formattedValue);
      }
      else
      {
        if (!useDefaults)
        {
          throw new ConfigFileErrorException("Value of 'Protocol' attribute (" + formattedValue + ") is not valid name of supported protocol!");
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
   * Loads and sets value of 'Ports' attribute.
   *
   * @param value - value of 'Ports' attribute.
   * @param useDefaults - specifies whether default 'Ports' value should
   *        be set if any error occurred during loading.
   *
   * @throws ConfigFileErrorException if error occurred during loading
   *         'Ports' attribute and second parameter was set to
   *         <code>false</code>.
   */
  private void parsePortsAttribute(String value, boolean useDefaults)
      throws ConfigFileErrorException
  {
    if (value != null)
    {
      if (ValuesParser.isArrayOfValues(value))
      {
        try
        {
          int[] tmpPorts = ValuesParser.getIntegerArray(value);

          if (ConfigUtils.checkPorts(tmpPorts))
          {
            ports = tmpPorts;
          }
          else if (!useDefaults)
          {
            throw new ConfigFileErrorException("One of values from 'Ports' attribute is not a valid port number!");
          }
        }
        catch (InvalidValueException e)
        {
          if (!useDefaults)
          {
            throw new ConfigFileErrorException("Syntax error in array of values in attribute 'Ports'!");
          }
        }
      }
      else
      {
        try
        {
          int port = Integer.parseInt(ValuesParser.formatPropertyValue(value));
          if (ConfigUtils.checkPort(port))
          {
            ports[0] = port;
          }
          else
          {
            if (!useDefaults)
            {
              throw new ConfigFileErrorException("Value of attribute 'Ports' is not a valid port number!");
            }
          }
        }
        catch (NumberFormatException e)
        {
          if (!useDefaults)
          {
            throw new ConfigFileErrorException("Value of attribute 'Ports' is not a valid port number!");
          }
        }
      }
    }
    else
    {
      if (!useDefaults)
      {
        throw new ConfigFileErrorException("Attribute 'Ports' not found!");
      }
    }
  }

  /**
   * Gets protocol used by end-point to communicate with other end-point(s).
   *
   * @return Returns protocol stored in this {@link CommonConfig} object.
   */
  public SupportedProtocol getProtocol()
  {
    return protocol;
  }

  /**
   * Gets array holding list of ports on which server should start listening
   * for connections or client is bound to.
   * End-point checks whether it is possible to listen on/bind to
   * these ports starting from the first one and if port is free, server
   * listens for connections on this port (not checking further) and client
   * connects to specified port.
   *
   * @return Returns array holding number of ports stored in this
   *         {@link CommonConfig} object.
   */
  public int[] getPorts()
  {
    int[] tmpPorts = new int[ports.length];
    System.arraycopy(ports, 0, tmpPorts, 0, ports.length);
    return tmpPorts;
  }

  /**
   * Sets the path to configuration file. Specified path cannot be <code>null</code>.
   *
   * @param path - path to configuration file.
   *
   * @throws NullPointerException if specified path is null.
   */
  void setConfigPath(File path)
  {
    if (path != null)
    {
      configPath = path;
    }
    else
    {
      throw new NullPointerException("Specified path is null!");
    }
  }

  /**
   * Gets the path to configuration file or <code>null</code> if it is
   * default configuration.
   *
   * @return Returns the path to configuration file or <code>null</code> if it is
   *         default configuration.
   */
  public File getConfigPath()
  {
    return configPath;
  }
}