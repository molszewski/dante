/*
 * Created on 2006-05-01
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.session;

import java.net.SocketAddress;

import net.java.dante.darknet.messaging.NetworkMessage;


/**
 * Session represents connection with specified peer (server or client).
 *
 * @author M.Olszewski
 */
public interface Session
{
  /**
   * Gets address of the remote peer represented by this {@link Session}.
   *
   * @return Returns address of the remote peer.
   */
  public SocketAddress getRemoteAddress();

  /**
   * Gets address of the remote peer represented by this {@link Session}.
   *
   * @return Returns address of the remote peer.
   */
  public SocketAddress getLocalAddress();

  /**
   * Closes this {@link Session}. If <code>quickClose</code> is set to
   * <code>true</code> then session is closed immediately, otherwise
   * it waits until processing is done.
   *
   * @param quickClose - specifies whether session should be closed immediately
   *        or not.
   */
  public void close(boolean quickClose);

  /**
   * Checks whether this session is connected to remote host.
   *
   * @return Returns <code>true</code> if this session is connected
   *         to remote host, <code>false</code> otherwise.
   */
  public boolean isConnected();

  /**
   * Sends {@link NetworkMessage} object to client represented by this {@link Session}.
   *
   * @param message - message to send.
   */
  public void send(NetworkMessage message);

  /**
   * Gets number of messages sent using this {@link Session}.
   *
   * @return Returns number of messages sent using this {@link Session}.
   */
  public long getSentMessagesCount();

  /**
   * Gets number of messages received using this {@link Session}.
   *
   * @return Returns number of messages received using this {@link Session}.
   */
  public long getReceivedMessagesCount();

  /**
   * Gets number of bytes sent using this {@link Session}.
   *
   * @return Returns number of bytes sent using this {@link Session}.
   */
  public long getSentBytesCount();

  /**
   * Gets number of bytes received using this {@link Session}.
   *
   * @return Returns number of bytes received using this {@link Session}.
   */
  public long getReceivedBytesCount();
}