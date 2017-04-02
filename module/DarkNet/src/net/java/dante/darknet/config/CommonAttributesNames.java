/*
 * Created on 2006-05-13
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.config;

/**
 * Class specifying set of default names of client or server's attributes,
 * like used protocol or list of ports on which server will try to
 * listen for connections or client will try to connect.
 *
 * @author M.Olszewski
 */
public class CommonAttributesNames
{
  /** Protocol used by server to communicate with clients. */
  public static String PROTOCOL = "PROTOCOL";
  /**
   * Ports on which server will try to listen. If server cannot listen on
   * first specified port, then it tries second one, third one etc., until
   * it finds free port or list is finished.
   */
  public static String PORTS = "PORTS";


  /**
   * Private default constructor: no inheritance or external creation.
   */
  private CommonAttributesNames()
  {
    // Intentionally empty.
  }
}