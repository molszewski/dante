/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.clients;

/**
 * Interface specifying listener invoked in case of clients connection
 * and disconnection events.
 *
 * @author M.Olszewski
 */
public interface ConnectionsListener
{
  /**
   * Method invoked when specified client was connected to
   * server.
   *
   * @param client - connected client.
   */
  void clientConnected(ConnectedClient client);

  /**
   * Method invoked when specified client was disconnected from
   * server. Further operations on client will have not any effect.
   *
   * @param client - disconnected client.
   */
  void clientDisconnected(ConnectedClient client);
}