/*
 * Created on 2006-09-01
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common;

import java.awt.Font;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import net.java.dante.darknet.messaging.NetworkMessagesRegister;
import net.java.dante.gui.common.messages.net.client.NoFreeClientsSlotsMessage;
import net.java.dante.gui.common.messages.net.client.update.ClientConnectedMessage;
import net.java.dante.gui.common.messages.net.client.update.ClientDataUpdateMessage;
import net.java.dante.gui.common.messages.net.client.update.ClientDisconnectedMessage;
import net.java.dante.gui.common.messages.net.client.update.ClientStatusChangedMessage;
import net.java.dante.gui.common.messages.net.client.update.GameDataChangedMessage;
import net.java.dante.gui.common.messages.net.client.update.GameInitializationFailedMessage;
import net.java.dante.gui.common.messages.net.client.update.InitDataMessage;
import net.java.dante.gui.common.messages.net.game.confirm.ClientNotReadyMessage;
import net.java.dante.gui.common.messages.net.game.confirm.ClientReadyMessage;
import net.java.dante.gui.common.messages.net.game.confirm.GameAbandonedMessage;
import net.java.dante.gui.common.messages.net.game.confirm.GameConfirmationMessage;
import net.java.dante.gui.common.messages.net.game.confirm.GameCreatedMessage;
import net.java.dante.gui.common.messages.net.game.confirm.GameFinishedMessage;
import net.java.dante.gui.common.messages.net.game.confirm.GameJoinedMessage;
import net.java.dante.gui.common.messages.net.game.confirm.GameStartedMessage;
import net.java.dante.gui.common.messages.net.game.fail.ClientStatusChangedFailedMessage;
import net.java.dante.gui.common.messages.net.game.fail.GameAbandonFailedMessage;
import net.java.dante.gui.common.messages.net.game.fail.GameCreationFailedMessage;
import net.java.dante.gui.common.messages.net.game.fail.GameInputNotAcceptedMessage;
import net.java.dante.gui.common.messages.net.game.fail.GameJoinFailedMessage;
import net.java.dante.gui.common.messages.net.game.fail.GameStartFailedMessage;
import net.java.dante.gui.common.messages.net.game.request.AbandonGameMessage;
import net.java.dante.gui.common.messages.net.game.request.CreateGameMessage;
import net.java.dante.gui.common.messages.net.game.request.JoinGameMessage;
import net.java.dante.gui.common.messages.net.game.request.NotReadyToGameStartMessage;
import net.java.dante.gui.common.messages.net.game.request.ReadyToGameStartMessage;
import net.java.dante.gui.common.messages.net.game.request.StartGameMessage;
import net.java.dante.gui.common.messages.net.game.sim.CommandsNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.FinishDataNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.GroupEliminatedNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.InitializationNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.StatisticsNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.TimeSyncNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.UpdateNetworkMessage;
import net.java.dante.receiver.Receiver;
import net.java.dante.receiver.ReceiverMessage;

/**
 * Common utility class for client and server side.
 *
 * @author M.Olszewski
 */
public final class Utils
{
  /** Error log file name. */
  private static final String ERROR_LOG_FILE_NAME = "./error.log";
  /** Identifier of the first network message. */
  private static final int BEGIN_NET_MSG_ID = 101;

  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private Utils()
  {
    // Intentionally left empty.
  }


  /**
   * Registers all messages used both by client and server in the specified
   * register. This method should be called once per each register.
   *
   * @param register the specified register.
   */
  public static void registerAllMessages(NetworkMessagesRegister register)
  {
    if (register == null)
    {
      throw new NullPointerException("Specified register is null!");
    }

    // Client updates messages
    register.register(NoFreeClientsSlotsMessage.class, BEGIN_NET_MSG_ID);
    register.register(ClientConnectedMessage.class, BEGIN_NET_MSG_ID           + 1);
    register.register(ClientDisconnectedMessage.class, BEGIN_NET_MSG_ID        + 2);
    register.register(ClientDataUpdateMessage.class, BEGIN_NET_MSG_ID          + 3);
    register.register(ClientStatusChangedMessage.class, BEGIN_NET_MSG_ID       + 4);
    register.register(GameDataChangedMessage.class, BEGIN_NET_MSG_ID           + 5);
    register.register(GameInitializationFailedMessage.class, BEGIN_NET_MSG_ID  + 6);
    register.register(InitDataMessage.class, BEGIN_NET_MSG_ID                  + 7);

    // Client requests messages
    register.register(AbandonGameMessage.class, BEGIN_NET_MSG_ID               + 8);
    register.register(CreateGameMessage.class, BEGIN_NET_MSG_ID                + 9);
    register.register(JoinGameMessage.class, BEGIN_NET_MSG_ID                  + 10);
    register.register(NotReadyToGameStartMessage.class, BEGIN_NET_MSG_ID       + 11);
    register.register(ReadyToGameStartMessage.class, BEGIN_NET_MSG_ID          + 12);
    register.register(StartGameMessage.class, BEGIN_NET_MSG_ID                 + 13);

    // Failure messages
    register.register(ClientStatusChangedFailedMessage.class, BEGIN_NET_MSG_ID + 14);
    register.register(GameAbandonFailedMessage.class, BEGIN_NET_MSG_ID         + 15);
    register.register(GameCreationFailedMessage.class, BEGIN_NET_MSG_ID        + 16);
    register.register(GameInputNotAcceptedMessage.class, BEGIN_NET_MSG_ID      + 17);
    register.register(GameJoinFailedMessage.class, BEGIN_NET_MSG_ID            + 18);
    register.register(GameStartFailedMessage.class, BEGIN_NET_MSG_ID           + 19);

    // Confirmation messages
    register.register(ClientNotReadyMessage.class, BEGIN_NET_MSG_ID            + 20);
    register.register(ClientReadyMessage.class, BEGIN_NET_MSG_ID               + 21);
    register.register(GameAbandonedMessage.class, BEGIN_NET_MSG_ID             + 22);
    register.register(GameConfirmationMessage.class, BEGIN_NET_MSG_ID          + 23);
    register.register(GameCreatedMessage.class, BEGIN_NET_MSG_ID               + 24);
    register.register(GameFinishedMessage.class, BEGIN_NET_MSG_ID              + 25);
    register.register(GameJoinedMessage.class, BEGIN_NET_MSG_ID                + 26);
    register.register(GameStartedMessage.class, BEGIN_NET_MSG_ID               + 27);

    // Simulation messages
    register.register(InitializationNetworkMessage.class, BEGIN_NET_MSG_ID     + 28);
    register.register(CommandsNetworkMessage.class, BEGIN_NET_MSG_ID           + 29);
    register.register(FinishDataNetworkMessage.class, BEGIN_NET_MSG_ID         + 30);
    register.register(GroupEliminatedNetworkMessage.class, BEGIN_NET_MSG_ID    + 31);
    register.register(StatisticsNetworkMessage.class, BEGIN_NET_MSG_ID         + 32);
    register.register(TimeSyncNetworkMessage.class, BEGIN_NET_MSG_ID           + 33);
    register.register(UpdateNetworkMessage.class, BEGIN_NET_MSG_ID             + 34);
  }

  /**
   * Posts the specified message to the specified receiver in different
   * thread.
   *
   * @param receiver the specified messages receiver.
   * @param message the specified message.
   */
  public static void forkMessage(final Receiver receiver, final ReceiverMessage message)
  {
    new Thread() {
      /**
       * @see java.lang.Thread#run()
       */
      @Override
      public void run()
      {
        receiver.postMessage(message);
      }
    }.start();
  }

  /**
   * Creates font with fixed width of letters and the specicified size.
   *
   * @param size the specified font's size.
   *
   * @return Returns created font with fixed width.
   */
  public static Font createFixedWidthFont(int size)
  {
    return new Font("Monospaced", Font.PLAIN, size);
  }

  /**
   * Creates default font with the specified size.
   *
   * @param size the specified font's size.
   *
   * @return Returns created default font with the specified size.
   */
  public static Font createDefaultFont(int size)
  {
    return new Font("Serif", Font.PLAIN, size);
  }

  /**
   * Logs error caused by the exception with the specified cause to file.
   *
   * @param cause the specified cause of the exception.
   */
  public static void logError(Throwable cause)
  {
    // Store all exception informations in 'error.log' file
    try
    {
      PrintWriter writer = new PrintWriter(new FileWriter(ERROR_LOG_FILE_NAME, true));

      try
      {
        writer.println("Exception caught in messages processing thread at " + (new Date()) + ":");
        cause.printStackTrace(writer);
        writer.println("");
      }
      finally
      {
        writer.close();
      }
    }
    catch (IOException e)
    {
      // Intentionally left empty.
    }
  }
}