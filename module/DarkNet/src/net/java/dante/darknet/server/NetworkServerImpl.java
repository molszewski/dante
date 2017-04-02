/*
 * Created on 2006-05-01
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.server;

import java.io.File;
import java.io.IOException;

import net.java.dante.darknet.config.ServerConfig;
import net.java.dante.darknet.messaging.NetworkMessagesRegister;
import net.java.dante.darknet.messaging.NetworkMessagingSystem;
import net.java.dante.darknet.messaging.NetworkMessagingSystemFactory;
import net.java.dante.darknet.mina.DarkNetProtocolProvider;
import net.java.dante.darknet.session.SessionHandler;

import org.apache.mina.common.TransportType;
import org.apache.mina.protocol.ProtocolProvider;
import org.apache.mina.registry.Service;
import org.apache.mina.registry.ServiceRegistry;
import org.apache.mina.registry.SimpleServiceRegistry;


/**
 * Implementation of {@link NetworkServer} interface using MINA framework
 * to start/stop server.
 *
 * @author M.Olszewski
 */
class NetworkServerImpl implements NetworkServer
{
  /** This server's configuration. */
  private ServerConfig config = null;
  /** Messaging system used by this {@link NetworkServerImpl}. */
  private NetworkMessagingSystem msgSystem = null;
  /** Port actually used by this {@link NetworkServerImpl}. */
  private int port = 0;
  /** Services register used by all {@link NetworkServerImpl} objects. */
  private static ServiceRegistry registry = new SimpleServiceRegistry();
  /** Service used by this {@link NetworkServerImpl}. */
  private Service service = null;
  /** Session handler required to restart server properly. */
  private SessionHandler handler = null;

  /**
   * Creates object of {@link NetworkServerImpl}.
   */
  NetworkServerImpl()
  {
    // Intentionally left empty.
  }

  /**
   * @see net.java.dante.darknet.server.NetworkServer#configure(net.java.dante.darknet.config.ServerConfig)
   */
  public void configure(ServerConfig serverConfig)
  {
    if (serverConfig != null)
    {
      synchronized(this)
      {
        configure0(serverConfig);
      }
    }
    else
    {
      throw new NullPointerException("Specified serverConfig is null!");
    }
  }

  /**
   * Performs server's configuration.
   *
   * @param serverConfig - object of {@link ServerConfig} class with server's
   *        configuration.
   */
  private void configure0(ServerConfig serverConfig)
  {
    config = serverConfig;
    msgSystem = NetworkMessagingSystemFactory.getInstance().createMessagingSystem(config.getProtocol());
  }

  /**
   * Reconfigure server settings using the same file as recently.
   */
  private void reconfigure()
  {
    File path = config.getConfigPath();
    if (path != null)
    {
      configure0(ServerConfig.loadConfig(path, true));
    }
  }

  /**
   * Checks whether server is started already.
   *
   * @return Returns <code>true</code> if server is started, <code>false</code>
   *         otherwise.
   */
  private boolean isStarted()
  {
    return (service != null);
  }

  /**
   * @see net.java.dante.darknet.server.NetworkServer#start(SessionHandler)
   */
  public void start(SessionHandler sessionHandler) throws ServerStartFailedException
  {
    if (sessionHandler == null)
    {
      throw new NullPointerException("Specified handler is null!");
    }

    synchronized(this)
    {
      start0(sessionHandler);
    }
  }


  /**
   * Performs start operation.
   *
   * @param sessionHandler - external session handler.
   *
   * @throws ServerStartFailedException if server was not started because it
   *         cannot be bound to any of specified ports.
   */
  private void start0(SessionHandler sessionHandler) throws ServerStartFailedException
  {
    if (msgSystem == null)
    {
      throw new NullPointerException("MessagingSystem is null!");
    }
    if (config == null)
    {
      throw new IllegalStateException("Client was not configured!");
    }

    ProtocolProvider protocolProvider = new DarkNetProtocolProvider(msgSystem, sessionHandler);
    int[] ports = config.getPorts();

    for (int i = 0; i < ports.length; i++)
    {
      Service tmpService = new Service("DarkNet.ServerImpl", TransportType.SOCKET, ports[i]);
      try
      {
        registry.bind(tmpService, protocolProvider);
        handler = sessionHandler;
        service = tmpService;
        port = ports[i];
        break;
      }
      catch (IOException e)
      {
        // No more ports to check?
        if (i == (ports.length - 1))
        {
          throw new ServerStartFailedException("Server cannot be started: all requested ports are bound!");
        }
        // .. or don't do anything - check next port number.
      }
    }
  }

  /**
   * @see net.java.dante.darknet.server.NetworkServer#stop(boolean)
   */
  public void stop(boolean quickShutdown)
  {
    synchronized(this)
    {
      stop0(quickShutdown);
    }
  }

  /**
   * Performs stop operation.
   *
   * @param quickShutdown - determines whether messages should be sent.
   */
  private void stop0(boolean quickShutdown)
  {
    if (isStarted())
    {
      if (!quickShutdown)
      {
        // TODO send warning message to clients and wait
        // TODO not necessary. remove quickShutdown parameter.
      }
      registry.unbind(service);
      service = null;
      port = 0;
    }
  }

  /**
   * @see net.java.dante.darknet.server.NetworkServer#restart(boolean)
   */
  public void restart(boolean quickRestart)
  {
    synchronized(this)
    {
      stop0(quickRestart);
      reconfigure();
      restart();
    }
  }

  /**
   * Restarts server with most recent {@link SessionHandler}.
   */
  private void restart()
  {
    if (handler != null)
    {
      start0(handler);
    }
  }

  /**
   * @see net.java.dante.darknet.server.NetworkServer#getRegister()
   */
  public synchronized NetworkMessagesRegister getRegister()
  {
    assert (msgSystem != null) : "MessagingSystem is null!";

    return msgSystem.getRegister();
  }

  /**
   * @see net.java.dante.darknet.server.NetworkServer#getPort()
   */
  public int getPort()
  {
    return port;
  }

  /**
   * @see net.java.dante.darknet.server.NetworkServer#getConfiguration()
   */
  public ServerConfig getConfiguration()
  {
    return config;
  }
}