/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.request;

import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;

/**
 * Class representing message with request to join game with the
 * specified identifier.
 *
 * @author M.Olszewski
 */
public class JoinGameMessage extends GameRequestMessage
{
  /** Game's identifier. */
  private int gameId;
  /** Indicates whether this object is read only or not. */
  private boolean readOnly = false;


  /**
   * Creates instance of {@link JoinGameMessage} class.
   * Do not create instances of {@link JoinGameMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public JoinGameMessage()
  {
    super();

    readOnly = true;
  }

  /**
   * Creates instance of {@link JoinGameMessage} class with the specified
   * game's identifier.
   *
   * @param joinGameId - the specified game's identifier.
   */
  public JoinGameMessage(int joinGameId)
  {
    gameId = joinGameId;
  }


  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    gameId   = reader.readInt();

    readOnly = false;
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    if (readOnly)
    {
      throw new IllegalStateException("IllegalState in JoinGameMessage: object is read only!");
    }

    writer.writeInt(gameId);
  }

  /**
   * Gets the game's identifier.
   *
   * @return Returns the game's identifier.
   */
  public int getGameId()
  {
    return gameId;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + gameId;
    result = PRIME * result + (readOnly ? 1231 : 1237);

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof JoinGameMessage))
    {
      final JoinGameMessage other = (JoinGameMessage) object;
      equal = (gameId == other.gameId);
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[gameId=" + gameId + "]");
  }
}