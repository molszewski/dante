/*
 * Created on 2006-05-21
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.java.dante.darknet.protocol.SupportedProtocol;
import net.java.dante.darknet.util.TempFilesList;

import junit.framework.TestCase;

/**
 * Test class containing tests for {@link ClientConfig} class.
 *
 * @author M.Olszewski
 */
public class ClientConfigTest extends TestCase
{
  private static InetAddress localHost = null;
  private static InetAddress javaHost = null;

  static
  {
    try
    {
      localHost = InetAddress.getLocalHost();
      javaHost = InetAddress.getByName("www.java.sun.com");
    }
    catch (UnknownHostException e)
    {
      throw new RuntimeException("Static initialization failed due to UnknownHostException.");
    }
  }

  /** Array with valid configuration files content. */
  private static final String[] VALID_CONF_FILES_CONTENT = new String[] {
    "PROTOCOL = \"TCP\"\nPORTS = {\"4444\", \"3232\", \"43\", \"445\"}\nHOST = 127.0.0.1\nHostPorts   = {\"5535\", \"5335\", \"123\"}    \n MAX_TIMEOUT = 1000",
    "PROTOCOL= \"TCP\"\nPORTS =4444\nHOST = localhost\n MAX_TIMEOUT = \"1500\"",
    "PROTOCOL= \"TCP\"\nPORTS =\"4444\"\nHOST = www.java.sun.com\n MAX_TIMEOUT = \"21500\"",
    "PROTOCOL =\"TCP\"\nPORTS= \"4444\"\nHOST = \"www.java.sun.com\"\nMAX_TIMEOUT = \"20000\"",
    "PROTOCOL =\"TCP\"\nPORTS= \"4444\"\nHOST = \"192.18.97.48\"\nMAX_TIMEOUT = \"20000\"",
    "PROTOCOL =\"TCP\"\nPORTS= \"4444\"\nWeirdSetting = unknown.\nHOST = 192.18.97.48\nAgainSthWrong = 534\nHostPorts   =   {\"35\"}\n MAX_TIMEOUT = \"20000\"",
  };

  /** Array with all valid configurations. */
  private static final ClientConfig[] VALID_CONFIGS = new ClientConfig[] {
    new ClientConfig(SupportedProtocol.TCP, new int[] {4444, 3232, 43, 445}, localHost, 1000),
    new ClientConfig(SupportedProtocol.TCP, new int[] {4444}, localHost, 1500),
    new ClientConfig(SupportedProtocol.TCP, new int[] {4444}, javaHost, 21500),
    new ClientConfig(SupportedProtocol.TCP, new int[] {4444}, javaHost, 20000),
    new ClientConfig(SupportedProtocol.TCP, new int[] {4444}, javaHost, 20000),
    new ClientConfig(SupportedProtocol.TCP, new int[] {4444}, javaHost, 20000),
  };

  /** Array with invalid configuration files content. */
  private static final String[] INVALID_CONF_FILES_CONTENT = new String[] {
    "PROTOCOL = \"TCP\"\nPORTS = 4444\nHozd = www.java.sun.com\nMAX_TIMEOUT=   1200",
    "PROTOCOL = \"TCP\"\nPORTS = 4444\nHOST = www.java.sun.com\nMaxxTimeout=   1200",
    "PROTOCOL = \"TCP\"\nPORTS = 4444\nHOST = ww.jva.sun.com\nMAX_TIMEOUT=   1200",
    "PROTOCOL = \"TCP\"\nPORTS = 4444\nHOST = 192.4344.223.23\nMAX_TIMEOUT=   -1200",
    "PROTOCOL = \"TCP\"\nPORTS = 4444\nHOST = www.java.sun.com\nMAX_TIMEOUT=   -1200",
  };

  /**
   * Array with all configurations obtained by reading configuration files
   * with errors.
   */
  private static final ClientConfig[] INVALID_CONFIGS = new ClientConfig[] {
    new ClientConfig(SupportedProtocol.TCP, new int[] {4444}, localHost, 1200),
    new ClientConfig(SupportedProtocol.TCP, new int[] {4444}, javaHost, 10000),
    new ClientConfig(SupportedProtocol.TCP, new int[] {4444}, localHost, 1200),
    new ClientConfig(SupportedProtocol.TCP, new int[] {4444}, localHost, 1200),
    new ClientConfig(SupportedProtocol.TCP, new int[] {4444}, javaHost, 10000),
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
   * Test method for {@link net.java.dante.darknet.config.ClientConfig#load(java.util.Properties, boolean)}.
   */
  public final void testLoad()
  {
    // 1. Test whether valid configurations are load properly without defaults
    for (int i = 0, size = validFiles.size(); i < size; i++)
    {
      ClientConfig config = new ClientConfig();
      ConfigLoader.getInstance().load(config, validFiles.get(i), false);
      assertEquals(config, VALID_CONFIGS[i]);
    }

    // 2. Test whether valid configurations are load properly with defaults
    for (int i = 0, size = validFiles.size(); i < size; i++)
    {
      ClientConfig config = new ClientConfig();
      ConfigLoader.getInstance().load(config, validFiles.get(i), true);
      assertEquals(config, VALID_CONFIGS[i]);
    }

    // 3. Test whether invalid configurations are load properly with defaults
    for (int i = 0, size = invalidFiles.size(); i < size; i++)
    {
      ClientConfig config = new ClientConfig();
      ConfigLoader.getInstance().load(config, invalidFiles.get(i), true);
      assertEquals(config, INVALID_CONFIGS[i]);
    }

    // 4. Test whether invalid configurations are not load properly without defaults
    for (int i = 0, size = invalidFiles.size(); i < size; i++)
    {
      ClientConfig config = new ClientConfig();
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
   * Test method for {@link net.java.dante.darknet.config.ClientConfig#getHostAddress()}.
   */
  public final void testGetHostAddress()
  {
    ClientConfig defConfig = new ClientConfig();
    assertEquals(defConfig.getHostAddress(), localHost);

    ClientConfig config = new ClientConfig();
    ConfigLoader.getInstance().load(config, validFiles.get(2), false);
    assertEquals(config.getHostAddress(), VALID_CONFIGS[2].getHostAddress());
  }

  /**
   * Test method for {@link net.java.dante.darknet.config.ClientConfig#getMaxTimeout()}.
   */
  public final void testGetMaxTimeout()
  {
    ClientConfig defConfig = new ClientConfig();
    assertEquals(defConfig.getMaxTimeout(), 10000);

    ClientConfig config = new ClientConfig();
    ConfigLoader.getInstance().load(config, validFiles.get(0), false);
    assertEquals(config.getMaxTimeout(), VALID_CONFIGS[0].getMaxTimeout());
  }
}