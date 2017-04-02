/*
 * Created on 2006-05-01
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.config;

/**
 * Class specifying set of default names specific only for server's attributes,
 * like maximum connections at one time.
 *
 * @author M.Olszewski
 */
public class ServerAttributesNames
{
  /** Maximum number of connections that can be handled by server at one time. */
  public static String MAX_CONNECTIONS = "MAX_CONNECTIONS";


  /**
   * Private default constructor: no inheritance or external creation.
   */
  private ServerAttributesNames()
  {
    // Intentionally empty.
  }
}