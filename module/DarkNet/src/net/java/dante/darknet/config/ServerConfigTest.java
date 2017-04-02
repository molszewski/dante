/*
 * Created on 2006-05-20
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.config;

import net.java.dante.darknet.protocol.SupportedProtocol;
import net.java.dante.darknet.util.TempFilesList;

import junit.framework.TestCase;

/**
 * Test class containing tests for {@link ServerConfig} class.
 *
 * @author M.Olszewski
 */
public class ServerConfigTest extends TestCase
{
  /** Array with valid configuration files content. */
  private static final String[] VALID_CONF_FILES_CONTENT = new String[] {
    "PROTOCOL = \"TCP\"\nPORTS = {\"4444\", \"3232\", \"43\", \"445\"}\nMAX_CONNECTIONS = 9",
    "PROTOCOL= \"TCP\"\nPORTS =4444\nMAX_CONNECTIONS = 8",
    "PROTOCOL =\"TCP\"\nPORTS= \"4444\"\nMAX_CONNECTIONS = 2",
    "PROTOCOL= \"TCP\"\nPORTS = {\"4444\"}\nMAX_CONNECTIONS = 8",
    "PROTOCOL = \"TCP\"\nPORTS= {\"4444\"}\nWeirdSetting = unknown.\nMAX_CONNECTIONS = 4"
  };

  /** Array with all valid configurations. */
  private static final ServerConfig[] VALID_CONFIGS = new ServerConfig[] {
    new ServerConfig(SupportedProtocol.TCP, new int[] {4444, 3232, 43, 445}, 9),
    new ServerConfig(SupportedProtocol.TCP, new int[] {4444}, 8),
    new ServerConfig(SupportedProtocol.TCP, new int[] {4444}, 2),
    new ServerConfig(SupportedProtocol.TCP, new int[] {4444}, 8),
    new ServerConfig(SupportedProtocol.TCP, new int[] {4444}, 4),
  };

  /** Array with invalid configuration files content. */
  private static final String[] INVALID_CONF_FILES_CONTENT = new String[] {
    "PROTOCOL = \"TCP\"\nPORTS = \"4444\", \"3232\", \"43\", \"445\nMAX_CONNECTIONS  = 15",
    "PROTOCOL = \"TCP\"\nPORTS = 4444,\nMAX_CONNECTIONS  = 4",
    "PROTOCOL = \"TCP\"\nPORTS = 444xx\nMAX_CONNECTIONS  = -534",
    "PROTOCOL = \"TCP\"\nPORTS = -123, \nMAX_CONNECTIONS  = 1231",
    "PROTOCOL = \"CCP\"\nPORTS = 123",
    "PrTcol = \"TCP\"\nPORTS = 114444",
    "PROTOCOL = \"TCP\"\nPords = 4444",
  };

  /**
   * Array with all configurations obtained by reading configuration files
   * with errors.
   */
  private static final ServerConfig[] INVALID_CONFIGS = new ServerConfig[] {
    new ServerConfig(SupportedProtocol.TCP, new int[] {5335}, 8),
    new ServerConfig(SupportedProtocol.TCP, new int[] {5335}, 4),
    new ServerConfig(SupportedProtocol.TCP, new int[] {5335}, 8),
    new ServerConfig(SupportedProtocol.TCP, new int[] {5335}, 1231),
    new ServerConfig(SupportedProtocol.TCP, new int[] {123}, 8),
    new ServerConfig(SupportedProtocol.TCP, new int[] {5335}, 8),
    new ServerConfig(SupportedProtocol.TCP, new int[] {5335}, 8),
  };

  /** List with all valid configuration files. */
  private TempFilesList validFiles = new TempFilesList();

  /** List with all invalid configuration files. */
  private TempFilesList invalidFiles = new TempFilesList();


  /**
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
    validFiles.fillList(VALID_CONF_FILES_CONTENT);
    invalidFiles.fillList(INVALID_CONF_FILES_CONTENT);
  }

  /**
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception
  {
    validFiles.clear(true);
    invalidFiles.clear(true);
  }

  /**
   * Test method for {@link net.java.dante.darknet.config.ServerConfig#load(java.util.Properties, boolean)}.
   */
  public final void testLoad()
  {
    // 1. Test whether valid configurations are load properly without defaults
    for (int i = 0, size = validFiles.size(); i < size; i++)
    {
      ServerConfig config = new ServerConfig();
      ConfigLoader.getInstance().load(config, validFiles.get(i), false);
      assertEquals(config, VALID_CONFIGS[i]);
    }

    // 2. Test whether valid configurations are load properly with defaults
    for (int i = 0, size = validFiles.size(); i < size; i++)
    {
      ServerConfig config = new ServerConfig();
      ConfigLoader.getInstance().load(config, validFiles.get(i), true);
      assertEquals(config, VALID_CONFIGS[i]);
    }

    // 3. Test whether invalid configurations are load properly with defaults
    for (int i = 0, size = invalidFiles.size(); i < size; i++)
    {
      ServerConfig config = new ServerConfig();
      ConfigLoader.getInstance().load(config, invalidFiles.get(i), true);
      assertEquals(config, INVALID_CONFIGS[i]);
    }

    // 4. Test whether invalid configurations are not load properly without defaults
    for (int i = 0, size = invalidFiles.size(); i < size; i++)
    {
      ServerConfig config = new ServerConfig();
      try
      {
        ConfigLoader.getInstance().load(config, invalidFiles.get(i), false);
        fail("ConfigFileErrorException not thrown - failed.");
      }
      catch (ConfigFileErrorException e)
      {
        // OK
      }
    }
  }

  /**
   * Test method for {@link net.java.dante.darknet.config.ServerConfig#getMaxConnections()}.
   */
  public final void testGetMaxConnections()
  {
    ServerConfig defConfig = new ServerConfig();
    assertEquals(defConfig.getMaxConnections(), 8);

    ServerConfig config = new ServerConfig();
    ConfigLoader.getInstance().load(config, validFiles.get(0), false);
    assertEquals(config.getMaxConnections(), VALID_CONFIGS[0].getMaxConnections());
  }
}