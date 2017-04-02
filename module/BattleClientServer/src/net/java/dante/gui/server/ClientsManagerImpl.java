/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.java.dante.darknet.session.Session;
import net.java.dante.gui.common.clients.ClientData;
import net.java.dante.gui.common.clients.ClientsManager;
import net.java.dante.gui.common.clients.ConnectedClient;
import net.java.dante.gui.common.clients.ConnectionsListener;
import net.java.dante.gui.common.messages.net.client.NoFreeClientsSlotsMessage;

/**
 * Implementation of {@link ClientsManager} interface.
 *
 * @author M.Olszewski
 */
class ClientsManagerImpl implements ClientsManager
{
  /**
   * Maximum number of clients that can be maintained
   * by this {@link ClientsManager} instance.
   */
  private final int maxClients;
  /** Server's frame. */
  private final ServerFrame frame;
  /** Mapping between {@link Session} objects and {@link ConnectedClient} objects. */
  private Map<Session, ConnectedClient> clients = new HashMap<Session, ConnectedClient>();
  /** List with all clients data. */
  private List<ClientData> clientsData = new ArrayList<ClientData>();
  /** Set with {@link ConnectionsListener} objects. */
  private Set<ConnectionsListener> listeners =
      new HashSet<ConnectionsListener>();


  /**
   * Creates instance of {@link ClientsManagerImpl} class with the specified
   * parameters.
   *
   * @param maximumClients - maximum number of clients that can be maintained
   *        by created {@link ClientsManager} instance.
   * @param serverFrame - server's frame.
   */
  ClientsManagerImpl(final int maximumClients, ServerFrame serverFrame)
  {
    if (serverFrame == null)
    {
      throw new NullPointerException("Specified serverFrame is null!");
    }

    maxClients = maximumClients;
    frame = serverFrame;
  }


  /**
   * @see net.java.dante.gui.common.clients.ClientsManager#getClientsCount()
   */
  public int getClientsCount()
  {
    return clients.size();
  }

  /**
   * @see net.java.dante.gui.common.clients.ClientsManager#getMaximumClientsCount()
   */
  public int getMaximumClientsCount()
  {
    return maxClients;
  }

  /**
   * @see net.java.dante.gui.common.clients.ClientsManager#getClient(net.java.dante.darknet.session.Session)
   */
  public ConnectedClient getClient(Session session)
  {
    return clients.get(session);
  }

  /**
   * @see net.java.dante.gui.common.clients.ClientsManager#getClientsData()
   */
  public ClientData[] getClientsData()
  {
    return clientsData.toArray(new ClientData[clientsData.size()]);
  }

  /**
   * @see net.java.dante.gui.common.clients.ClientsManager#getClients()
   */
  public ConnectedClient[] getClients()
  {
    return clients.values().toArray(new ConnectedClient[clients.values().size()]);
  }

  /**
   * @see net.java.dante.gui.common.clients.ClientsManager#clientConnected(net.java.dante.darknet.session.Session)
   */
  public void clientConnected(Session session)
  {
    if (clients.size() < maxClients)
    {
      ConnectedClientImpl client = new ConnectedClientImpl(new ClientDataImpl(), session, frame);

      frame.addClientData(client);

      // Notify other clients about this client connection
      notifyClientConnection(client);
      // Fire connection listeners
      fireConnectedListeners(client);

      clients.put(session, client);
    }
    else
    {
      if (session.isConnected())
      {
        session.send(new NoFreeClientsSlotsMessage());
      }
    }
  }

  /**
   * Notifies other clients about this client connection.
   *
   * @param client - connected client.
   */
  private void notifyClientConnection(ConnectedClient client)
  {
    ClientData clientData = client.getClientData();

    for (Session session : clients.keySet())
    {
      ConnectedClient otherClient = clients.get(session);
      otherClient.clientConnected(clientData);
    }
  }

  /**
   * @see net.java.dante.gui.common.clients.ClientsManager#clientDisconnected(net.java.dante.darknet.session.Session)
   */
  public void clientDisconnected(Session session)
  {
    ConnectedClient client = clients.get(session);
    clients.remove(session);

    frame.removeClientData((ConnectedClientImpl)client);

    // Notify other clients about this client disconnection
    notifyClientDisconnection(client);
    // Fire disconnection listeners
    fireDisconnectedListeners(client);
  }

  /**
   * Notifies other clients about this client disconnection.
   *
   * @param client - connected client.
   */
  private void notifyClientDisconnection(ConnectedClient client)
  {
    ClientData clientData = client.getClientData();

    for (Session session : clients.keySet())
    {
      ConnectedClient otherClient = clients.get(session);
      otherClient.clientDisconnected(clientData);
    }
  }

  /**
   * @see net.java.dante.gui.common.clients.ClientsManager#dispose()
   */
  public void dispose()
  {
    for (Session session : clients.keySet())
    {
      session.close(false);
    }

    clients.clear();
    listeners.clear();
  }

  /**
   * @see net.java.dante.gui.common.clients.ClientsManager#addConnectionsListener(net.java.dante.gui.common.clients.ConnectionsListener)
   */
  public void addConnectionsListener(ConnectionsListener listener)
  {
    listeners.add(listener);
  }

  /**
   * @see net.java.dante.gui.common.clients.ClientsManager#removeConnectionsListener(net.java.dante.gui.common.clients.ConnectionsListener)
   */
  public void removeConnectionsListener(ConnectionsListener listener)
  {
    listeners.remove(listener);
  }

  /**
   * Fire all registered {@link ConnectionsListener} with information that
   * the specified {@link ConnectedClient} was connected.
   *
   * @param client - the specified {@link ConnectedClient} object.
   */
  private void fireConnectedListeners(ConnectedClient client)
  {
    for (ConnectionsListener listener : listeners)
    {
      listener.clientConnected(client);
    }
  }

  /**
   * Fire all registered {@link ConnectionsListener} with information that
   * the specified {@link ConnectedClient} was disconnected.
   *
   * @param client - the specified {@link ConnectedClient} object.
   */
  private void fireDisconnectedListeners(ConnectedClient client)
  {
    for (ConnectionsListener listener : listeners)
    {
      listener.clientDisconnected(client);
    }
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[maxClients=" + maxClients + "; clients=" + clients +
        "; clientsData=" + clientsData + "; listeners=" + listeners + "]");
  }
}