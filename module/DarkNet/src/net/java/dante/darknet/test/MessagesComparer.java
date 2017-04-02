/*
 * Created on 2006-08-30
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.test;

import net.java.dante.darknet.messaging.NetworkMessage;

/**
 * Interface for class intending in comparing contents of two copies of the same
 * network message: one copy created before sending message and second
 * copy created after receiving message.
 *
 * @author M.Olszewski
 */
public interface MessagesComparer
{
  /**
   * Performs user-specific comparison of sent and received copies of
   * the same network message.
   *
   * @param sent copy of sent message.
   * @param received copy of received message.
   *
   * @return Returns <code>true</code> if messages are equal or <code>false</code>
   *         otherwise.
   */
  boolean compare(NetworkMessage sent, NetworkMessage received);
}