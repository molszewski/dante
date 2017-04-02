/*
 * Created on 2006-05-20
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.config;

import java.util.Arrays;

import net.java.dante.darknet.protocol.SupportedProtocol;
import net.java.dante.darknet.util.TempFilesList;

import junit.framework.TestCase;

/**
 * Test class containing tests for {@link CommonConfig} class.
 *
 * @author M.Olszewski
 */
public class CommonConfigTest extends TestCase
{
  /** Array with valid configuration files content. */
  private static final String[] VALID_CONF_FILES_CONTENT = new String[] {
    "PROTOCOL = \"TCP\"\nPORTS = {\"4444\", \"3232\", \"43\", \"445\"}",
    "PROTOCOL = \"TCP\"\nPORTS = 4444",
    "PROTOCOL = \"TCP\"\nPORTS = \"4444\"",
    "PROTOCOL = \"TCP\"\nPORTS = {\"4444\"}",
    "PROTOCOL = \"TCP\"\nPORTS = {\"4444\"}\nWeirdSetting = unknown."
  };

  /** Array with all valid configurations. */
  private static final CommonConfig[] VALID_CONFIGS = new CommonConfig[] {
    new CommonConfig(SupportedProtocol.TCP, new int[] {4444, 3232, 43, 445}),
    new CommonConfig(SupportedProtocol.TCP, new int[] {4444}),
    new CommonConfig(SupportedProtocol.TCP, new int[] {4444}),
    new CommonConfig(SupportedProtocol.TCP, new int[] {4444}),
    new CommonConfig(SupportedProtocol.TCP, new int[] {4444}),
  };

  /** Array with invalid configuration files content. */
  private static final String[] INVALID_CONF_FILES_CONTENT = new String[] {
    "Protocol = \"TCP\"\nPORTS = \"4444\", \"3232\", \"43\", \"445",
    "Protocol = \"TCP\"\nPORTS = 314444",
    "Protocol = \"TCP\"\nPORTS = 4444,",
    "Protocol = \"TCP\"\nPORTS = 444xx",
    "Protocol = \"TCP\"\nPORTS = -123, ",
    "Protocol = \"CCP\"\nPORTS = 123",
    "PrTcol = \"TCP\"\nPORTS = 4444",
    "Protocol = \"TCP\"\nPords = 4444",
  };

  /**
   * Array with all configurations obtained by reading configuration files
   * with errors.
   */
  private static final CommonConfig[] INVALID_CONFIGS = new CommonConfig[] {
    new CommonConfig(SupportedProtocol.TCP, new int[] {5335}),
    new CommonConfig(SupportedProtocol.TCP, new int[] {5335}),
    new CommonConfig(SupportedProtocol.TCP, new int[] {5335}),
    new CommonConfig(SupportedProtocol.TCP, new int[] {5335}),
    new CommonConfig(SupportedProtocol.TCP, new int[] {5335}),
    new CommonConfig(SupportedProtocol.TCP, new int[] {123}),
    new CommonConfig(SupportedProtocol.TCP, new int[] {4444}),
    new CommonConfig(SupportedProtocol.TCP, new int[] {5335}),
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
   * Test method for {@link net.java.dante.darknet.config.CommonConfig#load(java.util.Properties, boolean)}.
   */
  public final void testLoad()
  {
    // 1. Test whether valid configurations are load properly without defaults
    for (int i = 0, size = validFiles.size(); i < size; i++)
    {
      CommonConfig config = new CommonConfig();
      ConfigLoader.getInstance().load(config, validFiles.get(i), false);
      assertEquals(config, VALID_CONFIGS[i]);
    }

    // 2. Test whether valid configurations are load properly with defaults
    for (int i = 0, size = validFiles.size(); i < size; i++)
    {
      CommonConfig config = new CommonConfig();
      ConfigLoader.getInstance().load(config, validFiles.get(i), true);
      assertEquals(config, VALID_CONFIGS[i]);
    }

    // 3. Test whether invalid configurations are load properly with defaults
    for (int i = 0, size = invalidFiles.size(); i < size; i++)
    {
      CommonConfig config = new CommonConfig();
      ConfigLoader.getInstance().load(config, invalidFiles.get(i), true);
      assertEquals(config, INVALID_CONFIGS[i]);
    }

    // 4. Test whether invalid configurations are not load properly without defaults
    for (int i = 0, size = invalidFiles.size(); i < size; i++)
    {
      CommonConfig config = new CommonConfig();
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
   * Test method for {@link net.java.dante.darknet.config.CommonConfig#getProtocol()}.
   */
  public final void testGetProtocol()
  {
    CommonConfig defConfig = new CommonConfig();
    assertEquals(defConfig.getProtocol(), SupportedProtocol.TCP);

    CommonConfig config = new CommonConfig();
    ConfigLoader.getInstance().load(config, validFiles.get(0), false);
    assertEquals(config.getProtocol(), VALID_CONFIGS[0].getProtocol());
  }

  /**
   * Test method for {@link net.java.dante.darknet.config.CommonConfig#getPorts()}.
   */
  public final void testGetPorts()
  {
    CommonConfig defConfig = new CommonConfig();
    assertTrue(Arrays.equals(defConfig.getPorts(), new int[]{5335}));

    CommonConfig config = new CommonConfig();
    ConfigLoader.getInstance().load(config, validFiles.get(0), false);
    assertTrue(Arrays.equals(config.getPorts(), VALID_CONFIGS[0].getPorts()));
  }

  /**
   * Test method for {@link net.java.dante.darknet.config.CommonConfig#getConfigPath()}.
   */
  public final void testGetConfigPath()
  {
    CommonConfig defConfig = new CommonConfig();
    assertNull(defConfig.getConfigPath());

    CommonConfig config = new CommonConfig();
    config.setConfigPath(validFiles.get(0));
    assertEquals(config.getConfigPath(), validFiles.get(0));
  }
}