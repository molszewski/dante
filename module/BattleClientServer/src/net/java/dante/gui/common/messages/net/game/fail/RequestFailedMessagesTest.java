/*
 * Created on 2006-08-30
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.fail;

import java.util.Random;

import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.test.MessagesProvider;
import net.java.dante.darknet.test.SimpleNetMessagesTester;
import net.java.dante.gui.common.games.FailureReason;

/**
 * /**
 * Performs tests for network messages from this package.
 *
 * @author M.Olszewski
 */
class RequestFailedMessagesTest
{
  /**
   * Tests method.
   *
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    SimpleNetMessagesTester tester = new SimpleNetMessagesTester();
    tester.registerMessagesProvider(new RequestFailedMessagesProvider());

    tester.test();
  }
}


/**
 * Provider of network request messages.
 *
 * @author M.Olszewski
 */
class RequestFailedMessagesProvider implements MessagesProvider
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
          msg[i] = buildClientStatusChangedFailedMessage();
          break;
        }
        case 1:
        {
          msg[i] = buildGameAbandonFailedMessage();
          break;
        }
        case 2:
        {
          msg[i] = buildGameCreationFailedMessage();
          break;
        }
        case 3:
        {
          msg[i] = buildGameInputNotAcceptedMessage();
          break;
        }
        case 4:
        {
          msg[i] = buildGameJoinFailedMessage();
          break;
        }
        case 5:
        {
          msg[i] = buildGameStartFailedMessage();
          break;
        }
      }
    }

    return msg;
  }

  private ClientStatusChangedFailedMessage buildClientStatusChangedFailedMessage()
  {
    return new ClientStatusChangedFailedMessage(FailureReason.values()[rand.nextInt(FailureReason.values().length)]);
  }

  private GameAbandonFailedMessage buildGameAbandonFailedMessage()
  {
    return new GameAbandonFailedMessage(FailureReason.values()[rand.nextInt(FailureReason.values().length)]);
  }

  private GameCreationFailedMessage buildGameCreationFailedMessage()
  {
    return new GameCreationFailedMessage(FailureReason.values()[rand.nextInt(FailureReason.values().length)]);
  }

  private GameInputNotAcceptedMessage buildGameInputNotAcceptedMessage()
  {
    return new GameInputNotAcceptedMessage(FailureReason.values()[rand.nextInt(FailureReason.values().length)]);
  }

  private GameJoinFailedMessage buildGameJoinFailedMessage()
  {
    return new GameJoinFailedMessage(FailureReason.values()[rand.nextInt(FailureReason.values().length)]);
  }

  private GameStartFailedMessage buildGameStartFailedMessage()
  {
    return new GameStartFailedMessage(FailureReason.values()[rand.nextInt(FailureReason.values().length)]);
  }
}