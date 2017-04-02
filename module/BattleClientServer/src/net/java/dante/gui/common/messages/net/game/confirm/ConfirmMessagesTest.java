/*
 * Created on 2006-09-04
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.confirm;

import java.util.Random;

import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.test.MessagesProvider;
import net.java.dante.darknet.test.SimpleNetMessagesTester;
import net.java.dante.gui.common.games.GameStatusData;

/**
 * Performs tests for network messages from this package.
 *
 * @author M.Olszewski
 */
public class ConfirmMessagesTest
{
  /**
   * Tests method.
   *
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    SimpleNetMessagesTester tester = new SimpleNetMessagesTester();
    tester.registerMessagesProvider(new ConfirmMessagesProvider());

    tester.test();
  }
}

/**
 * Provider of network confirm messages.
 *
 * @author M.Olszewski
 */
class ConfirmMessagesProvider implements MessagesProvider
{
  private Random rand = new Random();

  
  /**
   * @see net.java.dante.darknet.test.MessagesProvider#provide()
   */
  public NetworkMessage[] provide()
  {
    final int maxMessages = 10000;
    final int NUMBER_OF_TYPES = 1;

    NetworkMessage[] msg = new NetworkMessage[maxMessages];

    for (int i = 0; i < maxMessages; i++)
    {
      switch (rand.nextInt(NUMBER_OF_TYPES))
      {
        case 0:
        {
          msg[i] = buildGameJoinedMessage();
          break;
        }
//        case 1:
//        {
//          msg[i] = buildJoinGameMessage();
//          break;
//        }
//        case 2:
//        {
//          msg[i] = buildAbandonGameMessage();
//          break;
//        }
//        case 3:
//        {
//          msg[i] = buildStartGameMessage();
//          break;
//        }
//        case 4:
//        {
//          msg[i] = buildNotReadyToGameStartMessage();
//          break;
//        }
//        case 5:
//        {
//          msg[i] = buildReadyToGameStartMessage();
//          break;
//        }
      }
    }

    return msg;
  }

  private GameJoinedMessage buildGameJoinedMessage()
  {
    GameStatusDataImpl[] gs = new GameStatusDataImpl[rand.nextInt(100) + 50];
    for (int i = 0; i < gs.length; i++)
    {
      gs[i] = new GameStatusDataImpl(rand.nextBoolean(),
                                     rand.nextInt(Integer.MAX_VALUE));

    }

    return new GameJoinedMessage(rand.nextInt(Integer.MAX_VALUE), gs);
  }

  class GameStatusDataImpl implements GameStatusData
  {
    boolean status;
    int clientId;

    /**
     * Creates instance of {@link GameStatusDataImpl} class.
     *
     * @param clientStatus
     * @param clientIdentifier
     */
    public GameStatusDataImpl(boolean clientStatus, int clientIdentifier)
    {
      this.status = clientStatus;
      this.clientId = clientIdentifier;
    }

    /**
     * @see net.java.dante.gui.common.games.GameStatusData#getClientId()
     */
    public int getClientId()
    {
      return clientId;
    }

    /**
     * @see net.java.dante.gui.common.games.GameStatusData#getGameStatus()
     */
    public boolean getGameStatus()
    {
      return status;
    }
  }

//  private JoinGameMessage buildJoinGameMessage()
//  {
//    return new JoinGameMessage(rand.nextInt(Integer.MAX_VALUE));
//  }
//
//  private AbandonGameMessage buildAbandonGameMessage()
//  {
//    return new AbandonGameMessage();
//  }
//
//  private StartGameMessage buildStartGameMessage()
//  {
//    return new StartGameMessage();
//  }
//
//  private NotReadyToGameStartMessage buildNotReadyToGameStartMessage()
//  {
//    return new NotReadyToGameStartMessage();
//  }
//
//  private ReadyToGameStartMessage buildReadyToGameStartMessage()
//  {
//    return new ReadyToGameStartMessage();
//  }
}