/*
 * Created on 2006-08-21
 *
 * @author M.Olszewski 
 */

package net.java.dante.receiver;

/**
 * Receiver messages processor interface.
 * 
 * @author M.Olszewski
 */
public interface MessagesProcessor
{
  /**
   * Processes specified message.
   * 
   * @param message - message to process.
   */
  void processMessage(ReceiverMessage message);
}