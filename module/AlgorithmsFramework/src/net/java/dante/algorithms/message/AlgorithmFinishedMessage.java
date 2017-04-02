/*
 * Created on 2006-09-01
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.message;

import net.java.dante.receiver.ReceiverMessage;

/**
 * Message sent when execution of algorithm is finished.
 *
 * @author M.Olszewski
 */
public class AlgorithmFinishedMessage implements ReceiverMessage
{
  /**
   * Creates instance of {@link AlgorithmFinishedMessage} class.
   */
  public AlgorithmFinishedMessage()
  {
    // Intentionally left empty.
  }
}