/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.fail;


import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;
import net.java.dante.gui.common.games.FailureReason;

/**
 * Class used to denote messages that contains reason of failure of request
 * controlling games (e.g. failed to create, join, abandon game).
 *
 * @author M.Olszewski
 */
public abstract class GameRequestFailedMessage extends NetworkMessage
{
  /** Failure reason. */
  private FailureReason reason;


  /**
   * Creates instance of {@link GameRequestFailedMessage} class.
   * Do not create instances of {@link GameRequestFailedMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public GameRequestFailedMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link GameRequestFailedMessage} class with the
   * specified failure's reason.
   *
   * @param failureReason - the specified failure's reason.
   */
  public GameRequestFailedMessage(FailureReason failureReason)
  {
    if (failureReason == null)
    {
      throw new NullPointerException("Specified failureReason is null!");
    }

    reason = failureReason;
  }


  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    reason   = FailureReason.values()[reader.readInt()];
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    if (reason == null)
    {
      throw new IllegalStateException("IllegalState in GameRequestFailedMessage: object is read only!");
    }

    writer.writeInt(reason.ordinal());
  }

  /**
   * Gets the failure's reason.
   *
   * @return Returns the failure's reason.
   */
  public FailureReason getFailureReason()
  {
    return reason;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + ((reason == null) ? 0 : reason.hashCode());

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof GameRequestFailedMessage))
    {
      final GameRequestFailedMessage other = (GameRequestFailedMessage) object;
      equal = (reason == other.reason);
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[reason=" + reason + "]");
  }
}