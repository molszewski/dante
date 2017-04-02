/*
 * Created on 2006-08-27
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.test;

import net.java.dante.darknet.messaging.NetworkMessage;

/**
 * Interface for classes responsible for providing network messages
 * instances used in tests run by {@link NetMessagesTester} implementations.
 *
 * @author M.Olszewski
 */
public interface MessagesProvider
{
  /**
   * Provides array of {@link NetworkMessage} instances used in tests
   * run by {@link NetMessagesTester} implementations.
   *
   * @return Returns provided array of {@link NetworkMessage} instances.
   */
  NetworkMessage[] provide();
}