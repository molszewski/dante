/*
 * Created on 2006-08-28
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.client.update;

import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;

/**
 * Network message indicating that initialization of game with the identifier
 * specified in {@link #GameInitializationFailedMessage(int)} constructor 
 * has failed.
 * 
 * @author M.Olszewski
 */
public class GameInitializationFailedMessage extends NetworkMessage
{
  /** Game's identifier. */
  private int gameId;
  /** Indicates whether this object is read only or not. */
  private boolean readOnly = false;
  
  
  /**
   * Creates instance of {@link GameInitializationFailedMessage} class.
   * Do not create instances of {@link GameInitializationFailedMessage} 
   * class by calling this constructor - it is intended to be called by 
   * DarkNet while encoding this message.
   */
  public GameInitializationFailedMessage()
  {
    super();
  
    readOnly = true;
  }

  /**
   * Creates instance of {@link GameInitializationFailedMessage} class with the
   * specified game's identifier.
   *
   * @param gameIdentifier - game's identifier denoting game for which message
   *        was generated.
   */
  public GameInitializationFailedMessage(int gameIdentifier)
  {
    gameId = gameIdentifier;
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
      throw new IllegalStateException("IllegalState in GameConfirmationMessage: object is read only!");
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

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof GameInitializationFailedMessage))
    {
      final GameInitializationFailedMessage other = (GameInitializationFailedMessage) object;
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