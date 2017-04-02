/*
 * Created on 2006-09-08
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.receiver;


import net.java.dante.darknet.config.ClientConfig;
import net.java.dante.receiver.ReceiverMessage;

/**
 * Class representing message sent when connection's configuration was
 * changed.
 *
 * @author M.Olszewski
 */
public class ConnectionConfigurationChangedMessage implements ReceiverMessage
{
  /** Client's configuration. */
  private ClientConfig config;
  /** Client's behaviour (creating or joining games). */
  private boolean creator;


  /**
   * Creates instance of {@link ConnectionConfigurationChangedMessage} class
   * with the specified parameters.
   *
   * @param clientConfig client's configuration.
   * @param creatorBehaviour client's behaviour (creating or joining games).
   */
  public ConnectionConfigurationChangedMessage(ClientConfig clientConfig,
                                               boolean creatorBehaviour)
  {
    if (clientConfig == null)
    {
      throw new NullPointerException("Specified clientConfig is null!");
    }

    config  = clientConfig;
    creator = creatorBehaviour;
  }


  /**
   * Gets the client's configuration.
   *
   * @return Returns the client's configuration.
   */
  public ClientConfig getConfig()
  {
    return config;
  }

  /**
   * Checks whether the specified client should act as games creator.
   *
   * @return Returns <code>true</code> if the specified client should act
   *         as games creator, <code>false</code> if the specified client
   *         should act as games joiner.
   */
  public boolean isCreator()
  {
    return creator;
  }
}