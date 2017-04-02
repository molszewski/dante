/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.receiver;


import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.session.Session;
import net.java.dante.receiver.ReceiverMessage;

/**
 * Class wrapping {@link net.java.dante.darknet.messaging.NetworkMessage} object.
 *
 * @author M.Olszewski
 */
public class NetworkReceiverMessage implements ReceiverMessage
{
  /** Wrapped {@link NetworkMessage} object. */
  private NetworkMessage message;
  /** Client's session from which wrapped {@link NetworkMessage} object comes from. */
  private Session session;


  /**
   * Creates instance of {@link NetworkReceiverMessage} wrapping the specified
   * {@link NetworkMessage} object.
   *
   * @param networkMessage - wrapped {@link NetworkMessage} object.
   * @param clientSession - session from which wrapped {@link NetworkMessage}
   *        object comes from.
   */
  public NetworkReceiverMessage(NetworkMessage networkMessage,
                                Session clientSession)
  {
    if (networkMessage == null)
    {
      throw new NullPointerException("Specified networkMessage is null!");
    }
    if (clientSession == null)
    {
      throw new NullPointerException("Specified clientSession is null!");
    }

    message = networkMessage;
    session = clientSession;
  }


  /**
   * Gets the wrapped {@link NetworkMessage} object.
   *
   * @return Returns the wrapped {@link NetworkMessage} object.
   */
  public NetworkMessage getMessage()
  {
    return message;
  }

  /**
   * Gets client's session from which wrapped {@link NetworkMessage} object
   * comes from.
   *
   * @return Returns client's session from which wrapped
   *         {@link NetworkMessage} object comes from.
   */
  public Session getSession()
  {
    return session;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + message.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof NetworkReceiverMessage))
    {
      final NetworkReceiverMessage other = (NetworkReceiverMessage) object;
      equal = (message.equals(other.message));
    }
    return equal;
  }


  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[message=" + message + "]");
  }
}