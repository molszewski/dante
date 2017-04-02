/*
 * Created on 2006-05-20
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.config;

/**
 * Class specifying set of default names specific only for client's attributes,
 * like remote host address, maximum timeout.
 *
 * @author M.Olszewski
 */
public class ClientAttributesNames
{
  /** IP address or machine name of the host to which client will try to connect. */
  public static final String HOST_ADDRESS = "HOST";
  /** Maximum time for client to wait for connection with remote host. */
  public static final String MAX_TIMEOUT = "MAX_TIMEOUT";

  /**
   * Private default constructor: no inheritance or external creation.
   */
  private ClientAttributesNames()
  {
    // Intentionally empty.
  }
}