/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.server.NetworkServer;
import net.java.dante.darknet.server.NetworkServerFactory;
import net.java.dante.darknet.server.ServerStartFailedException;
import net.java.dante.darknet.session.Session;
import net.java.dante.darknet.session.SessionAdapter;
import net.java.dante.gui.common.Dbg;
import net.java.dante.gui.common.Utils;
import net.java.dante.gui.common.clients.ClientsManager;
import net.java.dante.gui.common.clients.ConnectedClient;
import net.java.dante.gui.common.clients.ConnectionsListener;
import net.java.dante.gui.common.games.GameData;
import net.java.dante.gui.common.games.GameDataChangedListener;
import net.java.dante.gui.common.games.GamesManager;
import net.java.dante.gui.common.messages.net.game.request.AbandonGameMessage;
import net.java.dante.gui.common.messages.net.game.request.CreateGameMessage;
import net.java.dante.gui.common.messages.net.game.request.GameRequestMessage;
import net.java.dante.gui.common.messages.net.game.request.JoinGameMessage;
import net.java.dante.gui.common.messages.net.game.request.NotReadyToGameStartMessage;
import net.java.dante.gui.common.messages.net.game.request.ReadyToGameStartMessage;
import net.java.dante.gui.common.messages.net.game.request.StartGameMessage;
import net.java.dante.gui.common.messages.net.game.sim.CommandsNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage;
import net.java.dante.gui.common.messages.receiver.ClientConnectionMessage;
import net.java.dante.gui.common.messages.receiver.ClientDisconnectionMessage;
import net.java.dante.gui.common.messages.receiver.ClientSessionMessage;
import net.java.dante.gui.common.messages.receiver.DarkNetExceptionMessage;
import net.java.dante.gui.common.messages.receiver.FinalizeMessage;
import net.java.dante.gui.common.messages.receiver.GameInitializationFailed;
import net.java.dante.gui.common.messages.receiver.NetworkReceiverMessage;
import net.java.dante.gui.common.messages.receiver.SimulationInputMessage;
import net.java.dante.gui.common.messages.receiver.SimulationOutputMessage;
import net.java.dante.receiver.MessageProcessingExceptionMessage;
import net.java.dante.receiver.MessagesProcessor;
import net.java.dante.receiver.Receiver;
import net.java.dante.receiver.ReceiverMessage;
import net.java.dante.receiver.RunnableReceiver;
import net.java.dante.sim.data.common.FileDataSource;
import net.java.dante.sim.data.map.MapLoadersFactory;
import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.settings.SettingsLoaderFactory;
import net.java.dante.sim.data.settings.SimulationSettings;


/**
 * Class representing server's logic with automatic processing of
 * clients connections and running simultaneous games.
 *
 * @author M.Olszewski
 */
public class ServerLogic implements MessagesProcessor
{
  /** Default name of main definition file. */
  private static final String DEFAULT_DEFINITION_FILE_NAME = "res/main.config";
  /** Server's frame title. */
  private static final String TITLE = "BattleServer v1.0";

  /** Name of file with main definitions. */
  private final String definitionFileName;
  /** Messages receiver. */
  final Receiver receiver;
  /** Network server. */
  final NetworkServer server;
  /** Frame representing server's GUI. */
  ServerFrame serverFrame;
  /** Clients manager. */
  ClientsManager clientsMan;
  /** Games manager. */
  GamesManager gamesMan;


  /**
   * Creates instance of {@link ServerLogic} class.
   *
   * @param messagesReceiver - messages receiver.
   * @param networkServer - network server.
   * @param mainDefinitionFileName - main definition file name.
   */
  ServerLogic(Receiver messagesReceiver,
                     NetworkServer networkServer,
                     String mainDefinitionFileName)
  {
    if (messagesReceiver == null)
    {
      throw new NullPointerException("Specified messagesReceiver is null!");
    }
    if (networkServer == null)
    {
      throw new NullPointerException("Specified networkServer is null!");
    }
    if (mainDefinitionFileName == null)
    {
      throw new NullPointerException("Specified mainDefinitionFileName is null!");
    }

    receiver           = messagesReceiver;
    server             = networkServer;
    definitionFileName = mainDefinitionFileName;

    // Max clients count from server configuration
    int maxConnections = networkServer.getConfiguration().getMaxConnections();

    int maxClientsPerGame = getMaxClientsPerGame();
    int maxGames = (maxConnections / maxClientsPerGame) +
        (((maxConnections % maxClientsPerGame) == 0)? 0 : 1);

    serverFrame = new ServerFrame(maxGames);
    serverFrame.initialize();
    serverFrame.setTitle(TITLE);
    serverFrame.addWindowListener(new WindowAdapter() {
      /**
       * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
       */
      public void windowClosing(WindowEvent e)
      {
        receiver.postMessage(new FinalizeMessage());
      }
    });
    serverFrame.pack();
    serverFrame.setVisible(true);

    clientsMan = new ClientsManagerImpl(maxConnections, serverFrame);
    clientsMan.addConnectionsListener(new ClientConnectionListener());

    gamesMan = new GamesManagerImpl(maxGames, maxClientsPerGame, serverFrame);
    gamesMan.addGameDataChangedListener(new GamesChangesTrackerListener());
  }


  /**
   * Loads simulation map and check how many clients take part in game.
   *
   * @return Returns maximum number of clients taking part in one game.
   */
  private int getMaxClientsPerGame()
  {
    SimulationSettings simSettings = (SimulationSettings)SettingsLoaderFactory.getInstance().createLoader(new FileDataSource(definitionFileName)).loadSettings();
    SimulationMap map = MapLoadersFactory.getInstance().createLoader(new FileDataSource(simSettings.getMapFile())).loadMap();
    return map.locationGroupsCount();
  }


  /**
   * Main processing method.
   * 
   * @see net.java.dante.receiver.MessagesProcessor#processMessage(net.java.dante.receiver.ReceiverMessage)
   */
  public void processMessage(ReceiverMessage message)
  {
    if (message instanceof NetworkReceiverMessage)
    {
      processNetworkMessage((NetworkReceiverMessage)message);
    }
    else if (message instanceof SimulationOutputMessage)
    {
      processSimulationOutputMessage((SimulationOutputMessage)message);
    }
    else if (message instanceof ClientSessionMessage)
    {
      processClientSessionMessage((ClientSessionMessage)message);
    }
    else if (message instanceof GameInitializationFailed)
    {
      processGameInitializationFailedMessage((GameInitializationFailed)message);
    }
    else if (message instanceof DarkNetExceptionMessage)
    {
      processDarkNetExceptionMessage((DarkNetExceptionMessage)message);
    }
    else if (message instanceof MessageProcessingExceptionMessage)
    {
      processMessageProcessingExceptionMessage((MessageProcessingExceptionMessage)message);
    }
    else if (message instanceof FinalizeMessage)
    {
      performDisposal();
    }
  }

  /**
   * Processes {@link NetworkReceiverMessage} message.
   *
   * @param message message to process.
   */
  private void processNetworkMessage(NetworkReceiverMessage message)
  {
    NetworkMessage networkMessage = message.getMessage();
    ConnectedClient client = clientsMan.getClient(message.getSession());
    if (client != null)
    {
      if (networkMessage instanceof GameRequestMessage)
      {
        processGameRequest(client, (GameRequestMessage)networkMessage);
      }
      else if (networkMessage instanceof SimulationNetworkMessage)
      {
        processSimulationMessage(client, (SimulationNetworkMessage)networkMessage);
      }

      serverFrame.modifyClientData((ConnectedClientImpl)client);
    }
  }

  /**
   * Processes {@link GameRequestMessage} message generated by
   * the specified client.
   *
   * @param client client who generated the specified message.
   * @param gameRequestMessage message to process.
   */
  private void processGameRequest(ConnectedClient client, GameRequestMessage gameRequestMessage)
  {
    if (gameRequestMessage instanceof CreateGameMessage)
    {
      gamesMan.createGame(client);
    }
    else if (gameRequestMessage instanceof JoinGameMessage)
    {
      gamesMan.joinGame(client,
                        Integer.valueOf(((JoinGameMessage)gameRequestMessage).getGameId()));
    }
    else if (gameRequestMessage instanceof AbandonGameMessage)
    {
      gamesMan.abandonGame(client);
    }
    else if (gameRequestMessage instanceof ReadyToGameStartMessage)
    {
      gamesMan.markAsReady(client, true);
    }
    else if (gameRequestMessage instanceof NotReadyToGameStartMessage)
    {
      gamesMan.markAsReady(client, false);
    }
    else if (gameRequestMessage instanceof StartGameMessage)
    {
      gamesMan.startGame(client, new ExternalSimulationGameInitData(receiver,
                                                                    definitionFileName));
    }
  }

  /**
   * Processes {@link SimulationNetworkMessage} message generated by
   * the specified client.
   *
   * @param client client who generated the specified message.
   * @param message message to process.
   */
  private void processSimulationMessage(ConnectedClient client, SimulationNetworkMessage message)
  {
    if (message instanceof CommandsNetworkMessage)
    {
      CommandsNetworkMessage commandsMsg = (CommandsNetworkMessage)message;
      gamesMan.processGameInput(client,
                                new SimulationInputMessage(commandsMsg.getGameId(),
                                                           commandsMsg.getCommandsData()));
    }
  }

  /**
   * Processes {@link SimulationOutputMessage} message.
   *
   * @param message message to process.
   */
  private void processSimulationOutputMessage(SimulationOutputMessage message)
  {
    gamesMan.processGameOutput(message);
  }

  /**
   * Processes {@link ClientSessionMessage} message.
   *
   * @param message message to process.
   */
  private void processClientSessionMessage(ClientSessionMessage message)
  {
    Session session = message.getSession();
    if (message instanceof ClientConnectionMessage)
    {
      clientsMan.clientConnected(session);
    }
    else if (message instanceof ClientDisconnectionMessage)
    {
      clientsMan.clientDisconnected(session);
    }
  }

  /**
   * Processes {@link GameInitializationFailed} message.
   *
   * @param message message to process.
   */
  private void processGameInitializationFailedMessage(GameInitializationFailed message)
  {
    gamesMan.gameInitializationFailed(message.getGameId());

    serverFrame.appendLogLine("Initialization of game with identifier " + message.getGameId() + " has failed! Printing stack trace...");
    logStackTrace(message.getFailureCause());

    if (Dbg.DBGE)
    {
      Dbg.error("Initialization of game with identifier " + message.getGameId() + " has failed! Printing stack trace...");
      message.getFailureCause().printStackTrace();
    }
  }

  /**
   * Processes {@link DarkNetExceptionMessage} message.
   *
   * @param message message to process.
   */
  private void processDarkNetExceptionMessage(DarkNetExceptionMessage message)
  {
    serverFrame.appendLogLine("Exception was caught in network session: " + message.getSource() + "! Printing stack trace...");
    logStackTrace(message.getCause());
  }

  /**
   * Processes the specified {@link MessageProcessingExceptionMessage} message.
   *
   * @param message message to process.
   */
  private void processMessageProcessingExceptionMessage(MessageProcessingExceptionMessage message)
  {
    Throwable cause = message.getCause();

    // Show error dialog and perform disposal
    JOptionPane.showMessageDialog(serverFrame,
                                  "Exception caught in messages processing thread. Exception message:\n" +
                                  cause.getMessage() +
                                  "\nFull stack trace should be available in 'error.log' file placed in current working directory.\nPress OK to exit.",
                                  "BattleServer internal error",
                                  JOptionPane.ERROR_MESSAGE);
    Utils.logError(cause);
    performDisposal();
  }

  /**
   * Performs disposal.
   */
  private void performDisposal()
  {
    clientsMan.dispose();
    gamesMan.dispose();

    server.stop(false);

    serverFrame.dispose();

    receiver.dispose(false);
  }

  /**
   * Logs full stack trace in GUI.
   *
   * @param throwable - stack trace's provider.
   */
  private void logStackTrace(Throwable throwable)
  {
    StackTraceElement[] elements = throwable.getStackTrace();
    for (int i = 0; i < elements.length; i++)
    {
      serverFrame.appendLogLine(elements[i].toString());
    }
  }


  /**
   * Listener sending initialization messages for connected clients and informing
   * games manager about disconnected clients. It also updates clients
   * information displayed on server's frame.
   *
   * @author M.Olszewski
   */
  final class ClientConnectionListener implements ConnectionsListener
  {
    /**
     * @see net.java.dante.gui.common.clients.ConnectionsListener#clientConnected(net.java.dante.gui.common.clients.ConnectedClient)
     */
    public void clientConnected(ConnectedClient client)
    {
      // Inform client that it was connected and initialized
      client.clientInitialized(new ClientInitializationDataImpl(client.getClientData(),
                                                                clientsMan.getClientsData(),
                                                                gamesMan.getMaxGamesCount(),
                                                                gamesMan.getGamesData()));
    }

    /**
     * @see net.java.dante.gui.common.clients.ConnectionsListener#clientDisconnected(net.java.dante.gui.common.clients.ConnectedClient)
     */
    public void clientDisconnected(ConnectedClient client)
    {
      gamesMan.clientDisconnected(client);
    }
  }

  /**
   * Listener sending messages about changed game's data and updating
   * information displayed on server's frame.
   *
   * @author M.Olszewski
   */
  final class GamesChangesTrackerListener implements GameDataChangedListener
  {
    /**
     * @see net.java.dante.gui.common.games.GameDataChangedListener#gameDataChanged(net.java.dante.gui.common.games.GameData)
     */
    public void gameDataChanged(GameData gameData)
    {
      for (ConnectedClient client : clientsMan.getClients())
      {
        client.gameDataChanged(gameData);
      }
    }
  }

  /**
   * Starts server with the optional arguments.
   *
   * @param args first entry in array defines path to main definition file,
   *        second one defines path to configuration file for server.
   */
  public static void main(String[] args)
  {
    String definitionFile = DEFAULT_DEFINITION_FILE_NAME;
    NetworkServer server = NetworkServerFactory.getInstance().initDefaultServer();

    if (args.length > 0)
    {
      definitionFile = args[0];
      if (args.length > 1)
      {
        server = NetworkServerFactory.getInstance().initServer(args[1], true);
      }
    }

    Utils.registerAllMessages(server.getRegister());

    Receiver receiver = new RunnableReceiver();
    ServerLogic logic = new ServerLogic(receiver, server, definitionFile);

    receiver.start(logic);
    try
    {
      server.start(new ServerSessionHandler(receiver));
      logic.serverFrame.appendLogLine("Started listening on port: " + server.getPort());
    }
    catch (ServerStartFailedException e)
    {
      // Show error dialog and post 'finalize' message
      JOptionPane.showMessageDialog(logic.serverFrame,
                                    e.getMessage() + "\nPress OK to exit.",
                                    "Server start error",
                                    JOptionPane.ERROR_MESSAGE);
      receiver.postMessage(new FinalizeMessage());
    }
  }
}

/**
 * Server's session handler.
 *
 * @author M.Olszewski
 */
class ServerSessionHandler extends SessionAdapter
{
  /** Messages receiver. */
  private Receiver receiver;

  /**
   * Creates instance of {@link ServerSessionHandler} class.
   *
   * @param messagesReceiver - messages receiver.
   */
  public ServerSessionHandler(Receiver messagesReceiver)
  {
    receiver = messagesReceiver;
  }

  /**
   * @see net.java.dante.darknet.session.SessionAdapter#exceptionCaught(net.java.dante.darknet.session.Session, java.lang.Throwable)
   */
  @Override
  public void exceptionCaught(Session session, Throwable cause)
  {
    Utils.forkMessage(receiver, new DarkNetExceptionMessage(session, cause));

    if (session.isConnected())
    {
      session.close(true);
    }

    if (Dbg.DBGE)
    {
      Dbg.error("Exception caught in session: " + session);
      cause.printStackTrace();
    }
  }

  /**
   * @see net.java.dante.darknet.session.SessionAdapter#messageReceived(net.java.dante.darknet.session.Session, net.java.dante.darknet.messaging.NetworkMessage)
   */
  @Override
  public void messageReceived(Session session, NetworkMessage message)
  {
    Utils.forkMessage(receiver, new NetworkReceiverMessage(message, session));
  }

  /**
   * @see net.java.dante.darknet.session.SessionAdapter#sessionClosed(net.java.dante.darknet.session.Session)
   */
  @Override
  public void sessionClosed(Session session)
  {
    Utils.forkMessage(receiver, new ClientDisconnectionMessage(session));
  }

  /**
   * @see net.java.dante.darknet.session.SessionAdapter#sessionOpened(net.java.dante.darknet.session.Session)
   */
  @Override
  public void sessionOpened(Session session)
  {
    Utils.forkMessage(receiver, new ClientConnectionMessage(session));
  }
}