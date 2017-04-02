/*
 * Created on 2006-05-11
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.session;

import net.java.dante.darknet.messaging.NetworkMessage;

/**
 * Interface with set of callback methods invoked when specific event
 * is generated, e.g. {@link Session} object was created.
 *
 * @author M.Olszewski
 */
public interface SessionHandler
{
  /**
   * Invoked when {@link Session} object representing connection with
   * remote peer is created.
   *
   * @param session - {@link Session} object representing connection with
   *        remote peer.
   */
  public void sessionCreated(Session session);

  /**
   * Invoked when {@link Session} object representing connection with
   * remote peer is opened.
   *
   * @param session - {@link Session} object representing connection with
   *        remote peer.
   */
  public void sessionOpened(Session session);

  /**
   * Invoked when {@link Session} object representing connection with
   * remote peer is closed.
   *
   * @param session - {@link Session} object representing connection with
   *        remote peer.
   */
  public void sessionClosed(Session session);

  /**
   * Invoked when exception was thrown during any operation regarding
   * specified {@link Session}.
   *
   * @param session - {@link Session} object representing connection with
   *        remote peer.
   * @param cause - exception's cause.
   */
  public void exceptionCaught(Session session, Throwable cause);

  /**
   * Invoked when any message was received by specified {@link Session} object.
   *
   * @param session - {@link Session} object representing connection with
   *        remote peer.
   * @param message - received {@link NetworkMessage}.
   */
  public void messageReceived(Session session, NetworkMessage message);

  /**
   * Invoked when any message is sent by specified {@link Session} object.
   *
   * @param session - {@link Session} object representing connection with
   *        remote peer.
   * @param message - sent {@link NetworkMessage}.
   */
  public void messageSent(Session session, NetworkMessage message);
}