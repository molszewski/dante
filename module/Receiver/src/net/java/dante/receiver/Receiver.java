/*
 * Created on 2006-08-22
 *
 * @author M.Olszewski
 */

package net.java.dante.receiver;

/**
 * Interface for receiver of {@link ReceiverMessage} objects.
 * Receiver's implementations should assure that each posted message
 * will be processed by the {@link MessagesProcessor} specified in
 * {@link #start(MessagesProcessor)} method and whole processing
 * will be performed in one thread (which means that {@link MessagesProcessor}
 * will receive messages one-by-one).
 *
 * @author M.Olszewski
 */
public interface Receiver
{
  /**
   * Posts specified message to the receiver.
   *
   * @param message message to post.
   */
  void postMessage(ReceiverMessage message);

  /**
   * Starts receiving messages and process them using the specified
   * {@link MessagesProcessor}.
   *
   * @param messagesProcessor messages processor.
   */
  void start(MessagesProcessor messagesProcessor);

  /**
   * Disposes this receiver and all its resources.
   * Further calls to any of receiver's methods must not have
   * any side effects and should return from the methods immediately.
   *
   * @param quickDispose determines whether thread from which this method
   *        was called should wait for finish of disposal procedure.
   *        Please note that if this method is called within receiver's thread,
   *        it must be called with <code>false</code> argument or deadlock
   *        will occur.
   */
  void dispose(boolean quickDispose);
}