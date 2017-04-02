/*
 * Created on 2006-04-19
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.messaging;

import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;


/**
 * Abstract class representing high-level abstraction for data sent via
 * Internet using DarkNet network library. Every message has specified
 * message type identifier to allow decoding it at receiver's side.
 * Messages must be registered by {@link NetworkMessagesRegister} in the DarkNet
 * network library at the client and server side to enable their almost fully
 * automated processing.
 * <p>In order to achieve automatic decoding/encoding of each message
 * all this message class subclasses must:
 * <ul>
 * <li>Be public classes with public default constructor,
 * <li>Override {@link #fromPacket(PacketReader)} and
 *     {@link #toPacket(PacketWriter)} methods with the same order
 *     of writes and reads.
 * </ul>
 *
 * @see NetworkMessagesRegister
 *
 * @author M.Olszewski
 */
public abstract class NetworkMessage
{
  /**
   * Creates object of {@link NetworkMessage} class with no specified type.
   */
  public NetworkMessage()
  {
    // Empty.
  }

  /**
   * Implementation of this method should fill all data of this
   * {@link NetworkMessage} subclass from specified {@link PacketReader}.
   * This method is called automatically during message receiving process
   * after {@link PacketReader} with specified subclass identifier was obtained.
   *
   * @param reader - reference to {@link PacketReader}, containing data that
   *        should be read by this {@link NetworkMessage} subclass.
   */
  public abstract void fromPacket(PacketReader reader);

  /**
   * This method is called automatically before any sending message.
   * Implementation of this method obtains new empty {@link PacketWriter}
   * object that should be filled with message-specific data.
   * Afterwards it should return the very same object that was passed to this
   * method - otherwise message will not be sent.
   *
   * @param writer - {@link PacketWriter} that should be filled with
   *        message-specific data.
   */
  public abstract void toPacket(PacketWriter writer);

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass().toString());
  }
}