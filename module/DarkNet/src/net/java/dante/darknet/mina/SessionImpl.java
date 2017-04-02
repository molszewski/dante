/*
 * Created on 2006-05-11
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.mina;

import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicLong;

import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.session.Session;

import org.apache.mina.protocol.ProtocolSession;


/**
 * Implementation of {@link Session} interface based upon
 * {@link ProtocolSession} interface from MINA framework.
 *
 * @author M.Olszewski
 */
class SessionImpl implements Session
{
  /** {@link ProtocolSession} object used by MINA framework. */
  private ProtocolSession session = null;
  /** Number of sent messages. */
  private AtomicLong sentMessagesCount = new AtomicLong();
  /** Number of received messages. */
  private AtomicLong receivedMessagesCount = new AtomicLong();

  /**
   * Creates {@link SessionImpl} object based upon specified
   * {@link ProtocolSession} implementation.
   *
   * @param minaSession - implementation of {@link ProtocolSession} on which
   *        this session will be based.
   */
  SessionImpl(ProtocolSession minaSession)
  {
    if (minaSession == null)
    {
      throw new NullPointerException("Specified minaSession cannot be null!");
    }

    session = minaSession;
  }

  /**
   * @see net.java.dante.darknet.session.Session#getRemoteAddress()
   */
  public SocketAddress getRemoteAddress()
  {
    return session.getRemoteAddress();
  }

  /**
   * @see net.java.dante.darknet.session.Session#getLocalAddress()
   */
  public SocketAddress getLocalAddress()
  {
    return session.getLocalAddress();
  }

  /**
   * @see net.java.dante.darknet.session.Session#close(boolean)
   */
  public void close(boolean quickClose)
  {
    session.close(!quickClose);
  }

  /**
   * @see net.java.dante.darknet.session.Session#isConnected()
   */
  public boolean isConnected()
  {
    return session.isConnected();
  }

  /**
   * @see net.java.dante.darknet.session.Session#send(net.java.dante.darknet.messaging.NetworkMessage)
   */
  public void send(NetworkMessage message)
  {
    if (message == null)
    {
      throw new NullPointerException("Specified message cannot be null!");
    }

    session.write(message);
  }

  /**
   * @see net.java.dante.darknet.session.Session#getSentMessagesCount()
   */
  public long getSentMessagesCount()
  {
    return sentMessagesCount.get();
  }

  /**
   * @see net.java.dante.darknet.session.Session#getReceivedMessagesCount()
   */
  public long getReceivedMessagesCount()
  {
    return receivedMessagesCount.get();
  }

  /**
   * Increases sent messages count by specified number.
   *
   * @param messagesCount - number of sent messages.
   */
  synchronized void increaseSentMessagesCount(int messagesCount)
  {
    sentMessagesCount.addAndGet(messagesCount);
  }

  /**
   * Increases received messages count by specified number.
   *
   * @param messagesCount - number of sent messages.
   */
  synchronized void increaseReceivedMessagesCount(int messagesCount)
  {
    receivedMessagesCount.addAndGet(messagesCount);
  }

  /**
   * @see net.java.dante.darknet.session.Session#getReceivedBytesCount()
   */
  public long getReceivedBytesCount()
  {
    return session.getReadBytes();
  }

  /**
   * @see net.java.dante.darknet.session.Session#getSentBytesCount()
   */
  public long getSentBytesCount()
  {
    return session.getWrittenBytes();
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return "SessionImpl[minaSession=" + session +
        "; sentMessagesCount=" + sentMessagesCount.get() +
        "; receivedMessagesCount=" + receivedMessagesCount.get() +
        "; sentBytesCount=" + session.getWrittenBytes() +
        "; receivedBytesCount=" + session.getReadBytes() + "]";
  }
}