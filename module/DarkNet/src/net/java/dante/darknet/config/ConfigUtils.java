/*
 * Created on 2006-05-20
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.config;

/**
 * Utility class for configuration packet. Contains useful methods for 
 * checking numbers of the ports against minimum and maximum port numbers. 
 *
 * @author M.Olszewski
 */
final class ConfigUtils
{
  /** Minimum allowed port number. */
  public static final int MIN_PORT_NUMBER = 1;
  /** Maximum allowed port number. */
  public static final int MAX_PORT_NUMBER = 65535;
  
  /**
   * 
   */
  private ConfigUtils()
  {
    // Intentionally left empty.
  }

  /**
   * Checks specified array of port numbers against minimum and maximum
   * allowed port numbers.
   *  
   * @param portsToCheck - array with ports numbers to check.
   * 
   * @return Returns <code>false</code> if any of the ports is not in allowed
   *         range of values, <code>true</code> otherwise.
   */
  public static boolean checkPorts(int[] portsToCheck)
  {
    boolean allInRange = true;
    for (int i = 0; i < portsToCheck.length; i++)
    {
      if (!checkPort(portsToCheck[i]))
      {
        allInRange = false;
        break;
      }
    }
    
    return allInRange;
  }
  
  /**
   * Checks single port against minimum and maximum allowed port numbers.
   *  
   * @param port - port to check.
   * 
   * @return Returns <code>false</code> if the port is not in allowed
   *         range of values, <code>true</code> otherwise.
   */
  public static boolean checkPort(int port)
  {
    return ((port >= MIN_PORT_NUMBER) && (port <= MAX_PORT_NUMBER));
  }
}