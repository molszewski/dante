/*
 * Created on 2006-05-13
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.client;

import java.net.SocketAddress;

import net.java.dante.darknet.config.ClientConfig;
import net.java.dante.darknet.messaging.NetworkMessagesRegister;
import net.java.dante.darknet.session.Session;
import net.java.dante.darknet.session.SessionHandler;


/**
 * Interface for classes representing network clients in server/client
 * architecture.
 *
 * @author M.Olszewski
 */
public interface NetworkClient
{
  /**
   * Configures this {@link NetworkClient} object using configuration from
   * {@link ClientConfig} object.
   *
   * @param config - object containing this client's configuration.
   */
  public void configure(ClientConfig config);

  /**
   * Connects this client to host with specified address. Connected
   * client is able to sent and receive messages.
   * <p>Client uses specified {@link SessionHandler} to inform external
   * environment about events regarding communication with host.
   *
   * @param handler - {@link SessionHandler} object that will be informed
   *        about any events regarding communication with clients.
   *
   * @return Returns {@link Session} object that can be used to send messages.
   * @throws ClientNotConnectedException if client could not be connected with
   *         desired server for some reason.
   */
  public Session connect(SessionHandler handler) throws ClientNotConnectedException;

  /**
   * Determines whether this {@link NetworkClient} is still connected to server.
   *
   * @return Returns <code>true</code> if client is connected to server,
   *         <code>false</code> otherwise.
   */
  public boolean isConnected();

  /**
   * Disconnects this client. If client is already disconnected
   * or not connected this method should return immediately.
   *
   * @param quickDisconnect determines whether this method's caller
   *        will wait until disconnection will be performed. If
   *        this parameter is <code>true</code> than disconnection will be
   *        performed as fast as possible.
   */
  public void disconnect(boolean quickDisconnect);

  /**
   * Restarts and reconnects client. If parameter is set to <code>true</code>
   * then client will restart immediately, without warning clients about the restart.
   *
   * @param quickReconnect determines whether this method's caller
   *        will wait until disconnection will be performed. If
   *        this parameter is <code>true</code> than reconnection will be
   *        performed as fast as possible.
   */
  public void reconnect(boolean quickReconnect);

  /**
   * Gets address of the server to which client is connected.
   * This method may return <code>null</code> if client is not connected to any
   * server.
   *
   * @return Return address of the server to which client is connected or <code>null</code>
   *         if client is not connected.
   */
  public SocketAddress getServerAddress();

  /**
   * Gets actual port number which is used by this {@link NetworkClient}.
   *
   * @return Return actual port number which is used by this {@link NetworkClient}.
   */
  public int getPort();

  /**
   * Gets instance of {@link NetworkMessagesRegister} utilized by this {@link NetworkClient}.
   *
   * @return Returns instance of {@link NetworkMessagesRegister} utilized by this
   *         {@link NetworkClient}.
   */
  public NetworkMessagesRegister getRegister();

  /**
   * Gets this client's configuration.
   *
   * @return Returns this client's configuration.
   */
  ClientConfig getConfiguration();
}