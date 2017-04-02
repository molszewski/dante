/*
 * Created on 2006-05-18
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import net.java.dante.darknet.config.ClientConfig;
import net.java.dante.darknet.messaging.NetworkMessagesRegister;
import net.java.dante.darknet.messaging.NetworkMessagingSystem;
import net.java.dante.darknet.messaging.NetworkMessagingSystemFactory;
import net.java.dante.darknet.mina.DarkNetProtocolProvider;
import net.java.dante.darknet.mina.SessionProvider;
import net.java.dante.darknet.session.Session;
import net.java.dante.darknet.session.SessionHandler;

import org.apache.mina.io.filter.IoThreadPoolFilter;
import org.apache.mina.io.socket.SocketConnector;
import org.apache.mina.protocol.ProtocolProvider;
import org.apache.mina.protocol.ProtocolSession;
import org.apache.mina.protocol.filter.ProtocolThreadPoolFilter;
import org.apache.mina.protocol.io.IoProtocolConnector;


/**
 * Implementation of {@link NetworkClient} interface.
 *
 * @author M.Olszewski
 */
class NetworkClientImpl implements NetworkClient
{
  /** This client's configuration. */
  private ClientConfig config = null;
  /** Messaging system used by this {@link NetworkClientImpl}. */
  private NetworkMessagingSystem msgSystem = null;

  /** Address of the server to which this client is connected. */
  private SocketAddress serverAddress = null;
  /** Port actually used by this {@link NetworkClientImpl}. */
  private int port = 0;

  /** Session handler required to restart client properly. */
  private SessionHandler handler = null;

  ProtocolSession session = null;
  private IoThreadPoolFilter ioFilter = null;
  private ProtocolThreadPoolFilter protocolFilter = null;

  private Object lock = new Object();
  Object connectionLock = new Object();

  /**
   * Creates object of {@link NetworkClientImpl}.
   */
  NetworkClientImpl()
  {
    // Intentionally left empty.
  }

  /**
   * @see net.java.dante.darknet.client.NetworkClient#configure(net.java.dante.darknet.config.ClientConfig)
   */
  public void configure(ClientConfig clientConfig)
  {
    if (clientConfig != null)
    {
      synchronized(lock)
      {
        configure0(clientConfig);
      }
    }
    else
    {
      throw new NullPointerException("Specified clientConfig is null!");
    }
  }

  /**
   * Performs client's configuration.
   *
   * @param clientConfig - object of {@link ClientConfig} class with client's
   *        configuration.
   */
  private void configure0(ClientConfig clientConfig)
  {
    config = clientConfig;
    msgSystem = NetworkMessagingSystemFactory.getInstance().createMessagingSystem(config.getProtocol());
  }

  /**
   * Reconfigure client settings using the same file as recently.
   */
  private void reconfigure()
  {
    File path = config.getConfigPath();
    if (path != null)
    {
      configure0(ClientConfig.loadConfig(path, true));
    }
  }

  /**
   * @see net.java.dante.darknet.client.NetworkClient#connect(net.java.dante.darknet.session.SessionHandler)
   */
  public Session connect(SessionHandler sessionHandler)
  {
    if (sessionHandler == null)
    {
      throw new NullPointerException("Specified sessionHandler is null!");
    }

    Session retSession = null;
    synchronized (lock)
    {
      retSession = connect0(sessionHandler);
    }

    return retSession;
  }

  /**
   * Performs connect operation.
   *
   * @param sessionHandler - external session handler.
   *
   * @return Returns {@link Session} object representing connection with remote
   *         peer.
   * @throws ClientNotConnectedException if client could not be connected with
   *         desired server for some reason.
   */
  private Session connect0(SessionHandler sessionHandler)
  {
    if (msgSystem == null)
    {
      throw new NullPointerException("MessagingSystem is null!");
    }
    if (config == null)
    {
      throw new IllegalStateException("Client was not configured!");
    }

    // MINA components initialization
    IoThreadPoolFilter ioThreadPoolFilter = new IoThreadPoolFilter();
    ProtocolThreadPoolFilter protocolThreadPoolFilter = new ProtocolThreadPoolFilter();

    ioThreadPoolFilter.start();
    protocolThreadPoolFilter.start();

    IoProtocolConnector connector = new IoProtocolConnector(new SocketConnector());
    connector.getIoConnector().getFilterChain().addFirst("clientIoThreadPool", ioThreadPoolFilter);
    connector.getFilterChain().addFirst("clientProtocolThreadPool", protocolThreadPoolFilter);

    ProtocolProvider protocolProvider = new DarkNetProtocolProvider(msgSystem, sessionHandler);

    int maxTimeout = config.getMaxTimeout();
    InetAddress hostAddress = config.getHostAddress();
    int[] ports = config.getPorts();

    Session retSession = null;

    try
    {
      // Try to connect to host on one of specified ports.
      for (int i = 0; i < ports.length; i++)
      {
        InetSocketAddress remoteAddress = new InetSocketAddress(hostAddress, ports[i]);
        try
        {
          ProtocolSession tmpSession = connector.connect(remoteAddress, maxTimeout, protocolProvider);
          SocketAddress socketAddress = tmpSession.getLocalAddress();

          if (socketAddress instanceof InetSocketAddress)
          {
            port = ((InetSocketAddress)socketAddress).getPort();
          }

          synchronized(connectionLock)
          {
            session = tmpSession;
          }
          retSession = SessionProvider.getInstance().getSession(tmpSession);
          serverAddress = remoteAddress;
          ioFilter = ioThreadPoolFilter;
          protocolFilter = protocolThreadPoolFilter;
          handler = sessionHandler;
          break;
        }
        catch (IOException e)
        {
          // No more ports to check?
          if (i == (ports.length - 1))
          {
            throw new ClientNotConnectedException("Client cannot be connected to specified host using any of defined port numbers!");
          }
          // .. or don't do anything - check next port number.
        }
      }
    }
    finally
    {
      if (retSession == null)
      {
        protocolThreadPoolFilter.stop();
        ioThreadPoolFilter.stop();
      }
    }

    return retSession;
  }

  /**
   * @see net.java.dante.darknet.client.NetworkClient#isConnected()
   */
  public boolean isConnected()
  {
    boolean connected = false;

    synchronized(connectionLock)
    {
      connected = ((session != null) && (session.isConnected()));
    }

    return connected;
  }

  /**
   * @see net.java.dante.darknet.client.NetworkClient#disconnect(boolean)
   */
  public void disconnect(boolean quickDisconnect)
  {
    synchronized(lock)
    {
      disconnect0(quickDisconnect);
    }
  }

  /**
   * Performs disconnection operation.
   *
   * @param quickDisconnect - determines whether MINA framework should wait
   *        until disconnection is completed.
   */
  private void disconnect0(final boolean quickDisconnect)
  {
    if ((ioFilter != null) && (protocolFilter != null))
    {
      synchronized(connectionLock)
      {
        if (session != null)
        {
          session.close(!quickDisconnect);
        }
        session = null;
      }

      ioFilter.stop();
      protocolFilter.stop();

      ioFilter = null;
      protocolFilter = null;

      port = 0;
      serverAddress = null;
    }
    else
    {
      assert ((ioFilter == null) && (protocolFilter == null)) : "ioFilter and protocolFilter references not synchronized!";
    }
  }

  /**
   * @see net.java.dante.darknet.client.NetworkClient#getServerAddress()
   */
  public SocketAddress getServerAddress()
  {
    return serverAddress;
  }

  /**
   * @see net.java.dante.darknet.client.NetworkClient#getPort()
   */
  public int getPort()
  {
    return port;
  }

  /**
   * @see net.java.dante.darknet.client.NetworkClient#getRegister()
   */
  public NetworkMessagesRegister getRegister()
  {
    assert (msgSystem != null) : "MessagingSystem is null!";

    return msgSystem.getRegister();
  }

  /**
   * @see net.java.dante.darknet.client.NetworkClient#getConfiguration()
   */
  public ClientConfig getConfiguration()
  {
    return config;
  }

  /**
   * Non-blocking implementation.
   *
   * @see net.java.dante.darknet.client.NetworkClient#reconnect(boolean)
   */
  public void reconnect(final boolean quickReconnect)
  {
    synchronized(lock)
    {
      disconnect0(quickReconnect);
      reconfigure();
      reconnect();
    }
  }

  /**
   * Reconnects with server using the most recent {@link SessionHandler}.
   */
  void reconnect()
  {
    if (handler != null)
    {
      connect0(handler);
    }
  }
}