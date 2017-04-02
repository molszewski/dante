/*
 * Created on 2006-08-27
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.test;

/**
 * Interface for classes that are meant to test network messages.
 *
 * @author M.Olszewski
 */
public interface NetMessagesTester
{
  /**
   * Registers the specified messages provider.
   *
   * @param messagesProvider the specified messages provider.
   */
  void registerMessagesProvider(MessagesProvider messagesProvider);

  /**
   * Unregisters the specified messages provider.
   *
   * @param messagesProvider the specified messages provider.
   */
  void unregisterMessagesProvider(MessagesProvider messagesProvider);

  /**
   * Performs all the tests, using messages created by registered
   * messages providers.
   */
  void test();

  /**
   * /**
   * Performs all the tests, using messages created by registered
   * messages providers and comparing messages by using the specified
   * comparer.
   *
   * @param comparer the specified comparer.
   */
  void test(MessagesComparer comparer);
}