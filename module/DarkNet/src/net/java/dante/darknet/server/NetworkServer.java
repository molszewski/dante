/*
 * Created on 2006-05-01
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.server;

import net.java.dante.darknet.config.CommonConfig;
import net.java.dante.darknet.config.ServerConfig;
import net.java.dante.darknet.messaging.NetworkMessagesRegister;
import net.java.dante.darknet.session.SessionHandler;

/**
 * Server's interface, providing set of methods maintaining its life-cycle
 * (configuration, start, stop, restart). Also there is method to access
 * {@link net.java.dante.darknet.messaging.NetworkMessagesRegister} used by this server.
 * <p>
 * Server's implementation must be consistent - only one operation from
 * following list can be performed at one time:
 * <ul>
 * <li>stop,
 * <li>start,
 * <li>restart.
 * </ul>
 * <p>
 * You can obtain handle to server's implementation by using
 * {@link NetworkServerFactory}.
 *
 * @see NetworkServerFactory
 *
 * @author M.Olszewski
 */
public interface NetworkServer
{
  /**
   * Configures this {@link NetworkServer} object using configuration from
   * {@link CommonConfig} object.
   *
   * @param config - object containing this server's configuration.
   */
  public void configure(ServerConfig config);

  /**
   * Starts this server. Started server will try to listen for connections
   * on specified port(s) and performs full communication with connected
   * clients.
   * <p>Server uses specified {@link SessionHandler} to inform external
   * environment about events regarding communication with clients.
   *
   * @param handler - {@link SessionHandler} object that will be informed
   *        about any events regarding communication with clients.
   *
   * @throws ServerStartFailedException if server could not be started for
   *         some reason.
   */
  public void start(SessionHandler handler) throws ServerStartFailedException;

  /**
   * Stops this server. If parameter is set to <code>true</code> then
   * server will stop immediately, without warning clients about the shutdown.
   * If server was stopped or not started this method should return
   * immediately.
   *
   * @param quickShutdown - determines whether server should shutdown on
   *        instance (if <code>true</code>) or after warning clients.
   */
  public void stop(boolean quickShutdown);

  /**
   * Restarts server. If parameter is set to <code>true</code> then
   * server will restart immediately, without warning clients about the restart.
   *
   * @param quickRestart - determines whether server should restart on
   *        instance (if <code>true</code>) or after warning clients.
   */
  public void restart(boolean quickRestart);

  /**
   * Gets actual port number on which this server listens.
   *
   * @return Return actual port number on which this server listens.
   */
  public int getPort();

  /**
   * Gets instance of {@link NetworkMessagesRegister} utilized by this {@link NetworkServer}.
   *
   * @return Returns instance of {@link NetworkMessagesRegister} utilized by this
   *         {@link NetworkServer}.
   */
  public NetworkMessagesRegister getRegister();

  /**
   * Gets this server's configuration.
   *
   * @return Returns this server's configuration.
   */
  public ServerConfig getConfiguration();
}