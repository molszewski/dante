/*
 * Created on 2006-08-21
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.clients;

import net.java.dante.darknet.session.Session;


/**
 * Interface for classes managing {@link net.java.dante.gui.common.clients.ConnectedClient} objects.
 *
 * @author M.Olszewski
 */
public interface ClientsManager
{
  /**
   * Gets number of clients stored by this {@link ClientsManager} instance.
   *
   * @return Returns number of clients stored by this {@link ClientsManager}
   *         instance.
   */
  int getClientsCount();

  /**
   * Gets maximum number of clients that can be maintained by this
   * {@link ClientsManager} instance.
   *
   * @return Returns maximum number of clients that can be maintained by this
   *         {@link ClientsManager} instance.
   */
  int getMaximumClientsCount();

  /**
   * Gets {@link ConnectedClient} object representing specified session.
   *
   * @param session - the specified session.
   *
   * @return Returns {@link ConnectedClient} object representing specified
   *         session.
   */
  ConnectedClient getClient(Session session);

  /**
   * Gets array containing data for all connected clients.
   *
   * @return Returns array containing data for all connected clients.
   */
  ClientData[] getClientsData();

  /**
   * Gets array containing all connected clients.
   *
   * @return Returns array containing all connected clients.
   */
  ConnectedClient[] getClients();

  /**
   * This method should be invoked if new client was connected to the server.
   *
   * @param session - session representing client.
   */
  void clientConnected(Session session);

  /**
   * This method should be invoked if new client was disconnected from the server.
   *
   * @param session - session representing client.
   */
  void clientDisconnected(Session session);

  /**
   * Disposes this games manager by disconnecting all the clients and
   * removing all listeners. No notification is sent.
   */
  void dispose();

  /**
   * Adds the specified {@link ConnectionsListener} listener
   * notified about clients connections and disconnections.
   *
   * @param listener - listener to add.
   */
  void addConnectionsListener(ConnectionsListener listener);

   /**
    * Removes the specified {@link ConnectionsListener} listener
    * notified about clients connections and disconnections.
    *
    * @param listener - listener to remove.
    */
  void removeConnectionsListener(ConnectionsListener listener);
}