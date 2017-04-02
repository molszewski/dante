/*
 * Created on 2006-08-28
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.receiver;

import net.java.dante.receiver.ReceiverMessage;


/**
 * Message that can be sent to receiver to perform final disposal.
 *
 * @author M.Olszewski
 */
public class FinalizeMessage implements ReceiverMessage
{
  /**
   * Creates instance of {@link FinalizeMessage} class.
   */
  public FinalizeMessage()
  {
    // Intentionally left empty.
  }
}