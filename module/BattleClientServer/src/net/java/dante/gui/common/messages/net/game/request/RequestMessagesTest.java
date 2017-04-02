/*
 * Created on 2006-08-30
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.request;

import java.util.Random;

import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.test.MessagesComparer;
import net.java.dante.darknet.test.MessagesProvider;
import net.java.dante.darknet.test.SimpleNetMessagesTester;

/**
 * Tests for requests messages.
 *
 * @author M.Olszewski
 */
public class RequestMessagesTest
{
  /**
   * Tests method.
   *
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    SimpleNetMessagesTester tester = new SimpleNetMessagesTester();
    tester.registerMessagesProvider(new RequestsMessagesProvider());

    tester.test(new ClassComaparer());
  }
}

/**
 * Compares classes of objects only.
 *
 * @author M.Olszewski
 */
class ClassComaparer implements MessagesComparer
{

  /**
   * @see net.java.dante.darknet.test.MessagesComparer#compare(net.java.dante.darknet.messaging.NetworkMessage, net.java.dante.darknet.messaging.NetworkMessage)
   */
  public boolean compare(NetworkMessage sent, NetworkMessage received)
  {
    boolean result = false;
    if (sent instanceof JoinGameMessage)
    {
      result = sent.equals(received);
    }
    else
    {
      result = (sent.getClass().equals(received.getClass()));
    }

    return result;
  }
}


/**
 * Provider of network request messages.
 *
 * @author M.Olszewski
 */
class RequestsMessagesProvider implements MessagesProvider
{
  private Random rand = new Random();


  /**
   * @see net.java.dante.darknet.test.MessagesProvider#provide()
   */
  public NetworkMessage[] provide()
  {
    final int maxMessages = 10000;
    final int NUMBER_OF_TYPES = 6;

    NetworkMessage[] msg = new NetworkMessage[maxMessages];

    for (int i = 0; i < maxMessages; i++)
    {
      switch (rand.nextInt(NUMBER_OF_TYPES))
      {
        case 0:
        {
          msg[i] = buildCreateGameMessage();
          break;
        }
        case 1:
        {
          msg[i] = buildJoinGameMessage();
          break;
        }
        case 2:
        {
          msg[i] = buildAbandonGameMessage();
          break;
        }
        case 3:
        {
          msg[i] = buildStartGameMessage();
          break;
        }
        case 4:
        {
          msg[i] = buildNotReadyToGameStartMessage();
          break;
        }
        case 5:
        {
          msg[i] = buildReadyToGameStartMessage();
          break;
        }
      }
    }

    return msg;
  }

  private CreateGameMessage buildCreateGameMessage()
  {
    return new CreateGameMessage();
  }

  private JoinGameMessage buildJoinGameMessage()
  {
    return new JoinGameMessage(rand.nextInt(Integer.MAX_VALUE));
  }

  private AbandonGameMessage buildAbandonGameMessage()
  {
    return new AbandonGameMessage();
  }

  private StartGameMessage buildStartGameMessage()
  {
    return new StartGameMessage();
  }

  private NotReadyToGameStartMessage buildNotReadyToGameStartMessage()
  {
    return new NotReadyToGameStartMessage();
  }

  private ReadyToGameStartMessage buildReadyToGameStartMessage()
  {
    return new ReadyToGameStartMessage();
  }
}