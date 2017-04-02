/*
 * Created on 2006-09-02
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.client;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.java.dante.algorithms.AlgorithmRunner;
import net.java.dante.algorithms.BaseAlgorithmImpl;
import net.java.dante.algorithms.message.CommandsReadyMessage;
import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.session.Session;
import net.java.dante.gui.common.clients.ClientData;
import net.java.dante.gui.common.games.GameData;
import net.java.dante.gui.common.games.GameState;
import net.java.dante.gui.common.messages.net.client.update.ClientConnectedMessage;
import net.java.dante.gui.common.messages.net.client.update.ClientDisconnectedMessage;
import net.java.dante.gui.common.messages.net.client.update.GameDataChangedMessage;
import net.java.dante.gui.common.messages.net.client.update.GameInitializationFailedMessage;
import net.java.dante.gui.common.messages.net.client.update.InitDataMessage;
import net.java.dante.gui.common.messages.net.game.confirm.ClientReadyMessage;
import net.java.dante.gui.common.messages.net.game.confirm.GameConfirmationMessage;
import net.java.dante.gui.common.messages.net.game.confirm.GameCreatedMessage;
import net.java.dante.gui.common.messages.net.game.confirm.GameJoinedMessage;
import net.java.dante.gui.common.messages.net.game.confirm.GameStartedMessage;
import net.java.dante.gui.common.messages.net.game.fail.ClientStatusChangedFailedMessage;
import net.java.dante.gui.common.messages.net.game.fail.GameStartFailedMessage;
import net.java.dante.gui.common.messages.net.game.request.CreateGameMessage;
import net.java.dante.gui.common.messages.net.game.request.JoinGameMessage;
import net.java.dante.gui.common.messages.net.game.request.ReadyToGameStartMessage;
import net.java.dante.gui.common.messages.net.game.request.StartGameMessage;
import net.java.dante.gui.common.messages.net.game.sim.CommandsNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.FinishDataNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.GroupEliminatedNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.InitializationNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.StatisticsNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.TimeSyncNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.UpdateNetworkMessage;
import net.java.dante.receiver.Receiver;
import net.java.dante.receiver.time.Timer;
import net.java.dante.receiver.time.TimerManager;
import net.java.dante.receiver.time.TimerMessage;
import net.java.dante.sim.Simulation;
import net.java.dante.sim.SimulationFactory;
import net.java.dante.sim.SimulationType;
import net.java.dante.sim.data.ClientSimulationInitData;
import net.java.dante.sim.data.common.InitializationDataSource;
import net.java.dante.sim.engine.engine2d.server.GroupStatistics;
import net.java.dante.sim.io.ClientInputData;
import net.java.dante.sim.io.CommandsData;
import net.java.dante.sim.io.OutputData;
import net.java.dante.sim.io.SimulationOutput;
import net.java.dante.sim.io.TimeSyncData;
import net.java.dante.sim.io.UpdateData;
import net.java.dante.sim.io.init.InitializationData;

/**
 * Auto connection manager class.
 *
 * @author M.Olszewski
 */
class AutoConnectionManager
{
  private static final String RESULTS_FILE_PREFIX = "results_";
  /** Timer's starting connection delay. */
  private static final int TIMER_DELAY = 5000;

  /** Session of this automatic connection manager. */
  Session session;
  /** Number of maximum games that can be run simultaneously by server. */
  int maxGames;
  /** List with data of all existing games. */
  Map<Integer, GameData> gamesData = new HashMap<Integer, GameData>();
  /** Data of current client. */
  ClientData clientData;
  /** List with data of all other clients. */
  List<ClientData> clientsData = new ArrayList<ClientData>();
  /** Indicates whether this connection manager should create or join games. */
  final boolean creator;
  /** Messages receiver. */
  final Receiver receiver;
  /** Algorithm to run. */
  final BaseAlgorithmImpl algorithm;
  /** Client's frame. */
  final ClientFrame frame;
  Timer timer;
  /** Main state machine. */
  MainStateMachine stateMachine = new MainStateMachine();


  /**
   * Creates instance of {@link AutoConnectionManager} class.
   *
   * @param messagesReceiver messages receiver.
   * @param creatorConnection determines whether this automatic connection
   *        manager should create or join games.
   * @param connectionSession session used in this connection.
   * @param message message with initialization data for this manager.
   * @param baseAlgorithm algorithm controlling agents.
   * @param clientFrame client's frame.
   */
  AutoConnectionManager(boolean creatorConnection,
                        Session connectionSession,
                        InitDataMessage message,
                        BaseAlgorithmImpl baseAlgorithm,
                        Receiver messagesReceiver,
                        ClientFrame clientFrame)
  {
    if (connectionSession == null)
    {
      throw new NullPointerException("Specified connectionSession is null!");
    }
    if (message == null)
    {
      throw new NullPointerException("Specified message is null!");
    }
    if (baseAlgorithm == null)
    {
      throw new NullPointerException("Specified baseAlgorithm is null!");
    }
    if (messagesReceiver == null)
    {
      throw new NullPointerException("Specified messagesReceiver is null!");
    }
    if (clientFrame == null)
    {
      throw new NullPointerException("Specified clientFrame is null!");
    }

    creator   = creatorConnection;
    session   = connectionSession;
    algorithm = baseAlgorithm;
    receiver  = messagesReceiver;
    frame     = clientFrame;

    timer = TimerManager.getInstance().createTimer(receiver);

    timer.scheduleAfter(creatorConnection ? TIMER_DELAY : (TIMER_DELAY / 2));

    initializeData(message);
  }


  /**
   * Initializes clients and games data using data from the specified
   * initialization message.
   *
   * @param message the specified initialization message.
   */
  private void initializeData(InitDataMessage message)
  {
    clientData = message.getCurrentClientData();
    for (int i = 0, clientsCount = message.getClientsDataCount(); i < clientsCount; i++)
    {
      clientsData.add(message.getClientData(i));
    }

    maxGames = message.getMaxGamesCount();
    for (int i = 0, gamesCount = message.getGamesDataCount(); i < gamesCount; i++)
    {
      GameData gameData = message.getGameData(i);
      gamesData.put(Integer.valueOf(gameData.getGameId()), gameData);
    }
  }

  /**
   * Processes the specified timer message by reseting main state machine.
   *
   * @param timerMessage timer message.
   */
  void processTimeMessage(TimerMessage timerMessage)
  {
    stateMachine.processTimeMessage(timerMessage);
  }

  /**
   * Processes the specified network message.
   *
   * @param message the specified network message.
   */
  void processNetworkMessage(NetworkMessage message)
  {
    // Update games and clients data
    if (message instanceof GameDataChangedMessage)
    {
      updateGameData(((GameDataChangedMessage)message).getGameData());
    }
    else if (message instanceof ClientConnectedMessage)
    {
      processOtherClientConnection(((ClientConnectedMessage)message).getClientData());
    }
    else if (message instanceof ClientDisconnectedMessage)
    {
      processOtherClientDisconnection(((ClientDisconnectedMessage)message).getClientData());
    }

    stateMachine.processNetworkMessage(message);
  }


  /**
   * Processes the specified commands ready message.
   *
   * @param message the specified commands ready message.
   */
  void processCommandsReadyMessage(CommandsReadyMessage message)
  {
    stateMachine.processCommandsReadyMessage(message);
  }

  /**
   * Performs disposal.
   */
  void dispose()
  {
    stateMachine.dispose();
  }

  /**
   * Updates the specified game's data.
   *
   * @param gameData the specified game's data.
   */
  private void updateGameData(GameData gameData)
  {
    Integer gameId = Integer.valueOf(gameData.getGameId());
    if (gamesData.containsKey(gameId))
    {
      if (gameData.getState() != GameState.FINISHED)
      {
        gamesData.put(gameId, gameData);
      }
      else
      {
        gamesData.remove(gameId);
      }
    }
    else
    {
      gamesData.put(gameId, gameData);
    }
  }

  /**
   * Updates the list of clients by adding the specified connected client.
   *
   * @param connectedClient the specified connected client.
   */
  private void processOtherClientConnection(ClientData connectedClient)
  {
    if (!clientsData.contains(connectedClient))
    {
      clientsData.add(connectedClient);
    }
  }

  /**
   * Updates the list of clients by removing the specified disconnected client.
   *
   * @param disconnectedClient the specified disconnected client.
   */
  private void processOtherClientDisconnection(ClientData disconnectedClient)
  {
    clientsData.remove(disconnectedClient);
  }

  /**
   * Sends the specified network message to server.
   *
   * @param message the specified network message
   */
  void sendNetworkMessage(NetworkMessage message)
  {
    if (message != null)
    {
      session.send(message);
    }
  }


  /**
   * Main state machine creating or joining games, starting and running them.
   *
   * @author M.Olszewski
   */
  class MainStateMachine
  {
    /** 'Initialization' state. */
    private MainState initState = new InitState();
    /** 'Game connecting' state. */
    private MainState gameConnectingState = new GameConnectingState();
    /** 'In game' state. */
    private InGameState inGameState = new InGameState();

    /** Current state. */
    private MainState currentState = initState;


    /**
     * Sets 'initialization' state.
     */
    void setInitState()
    {
      setState(initState);
    }

    /**
     * Sets 'game connecting' state.
     */
    void setGameConnectingState()
    {
      setState(gameConnectingState);
    }

    /**
     * Sets 'in game' state.
     *
     * @param gameIdentifier - game's identifier.
     */
    void setInGameState(int gameIdentifier)
    {
      inGameState.setGameId(gameIdentifier);
      setState(inGameState);
    }

    /**
     * Sets the specified state, calling {@link MainState#exitAction()}
     * for current state and {@link MainState#entryAction()} for the
     * specified state.
     *
     * @param newState the specified state.
     */
    private void setState(MainState newState)
    {
      currentState.exitAction();

      currentState = newState;

      currentState.entryAction();
    }

    /**
     * Processes the specified network message.
     *
     * @param message the specified network message.
     */
    void processNetworkMessage(NetworkMessage message)
    {
      currentState.processNetworkMessage(message);
    }

    /**
     * Processes the specified commands ready message.
     *
     * @param message the specified commands ready message.
     */
    void processCommandsReadyMessage(CommandsReadyMessage message)
    {
      currentState.processCommandsReadyMessage(message);
    }

    /**
     * Processes the specified timer message.
     *
     * @param timerMessage timer message.
     */
    void processTimeMessage(TimerMessage timerMessage)
    {
      currentState.processTimeMessage(timerMessage);
    }

    /**
     * Performs disposal.
     */
    void dispose()
    {
      currentState.dispose();
    }
  }


  /**
   * Main game state.
   *
   * @author M.Olszewski
   */
  abstract class MainState
  {
    /**
     * Processes the specified network message.
     *
     * @param message the specified network message.
     */
    void processNetworkMessage(NetworkMessage message)
    {
      // Intentionally left empty.
    }

    /**
     * Processes the specified commands ready message.
     *
     * @param message the specified commands ready message.
     */
    void processCommandsReadyMessage(CommandsReadyMessage message)
    {
      // Intentionally left empty.
    }

    /**
     * Processes the specified timer message.
     *
     * @param timerMessage timer message.
     */
    void processTimeMessage(TimerMessage timerMessage)
    {
      // Intentionally left empty.
    }

    /**
     * Method invoked after setting current state to this one.
     */
    void entryAction()
    {
      // Intentionally left empty.
    }

    /**
     * Method invoked before performing change from this state to different one.
     */
    void exitAction()
    {
      // Intentionally left empty.
    }

    /**
     * Performs disposal.
     */
    void dispose()
    {
      // Intentionally left empty.
    }
  }


  /**
   * 'Initialization' state. Waits for timer message to change state to
   * 'game connecting'.
   *
   * @author M.Olszewski
   */
  class InitState extends MainState
  {
    /**
     * @see net.java.dante.gui.client.AutoConnectionManager.MainState#processTimeMessage(net.java.dante.receiver.time.TimerMessage)
     */
    @Override
    void processTimeMessage(TimerMessage timerMessage)
    {
      if (timer == timerMessage.getTimer())
      {
        stateMachine.setGameConnectingState();
      }
    }

    /**
     * @see net.java.dante.gui.client.AutoConnectionManager.MainState#entryAction()
     */
    @Override
    void entryAction()
    {
      timer.scheduleAfter(creator ? TIMER_DELAY : (TIMER_DELAY / 2));
    }
  }

  /**
   * 'Game connecting' state. Performs creation of game or joins some game.
   *
   * @author M.Olszewski
   */
  class GameConnectingState extends MainState
  {
    /**
     * @see net.java.dante.gui.client.AutoConnectionManager.MainState#processNetworkMessage(net.java.dante.darknet.messaging.NetworkMessage)
     */
    void processNetworkMessage(NetworkMessage message)
    {
      if (message instanceof GameDataChangedMessage)
      {
        performGameConnectionAction();
      }
      else if (((message instanceof GameCreatedMessage) && creator) ||
               ((message instanceof GameJoinedMessage) && !creator))
      {
        frame.appendLogLine("In game!");
        stateMachine.setInGameState(((GameConfirmationMessage)message).getGameId());
      }
    }

    /**
     * @see net.java.dante.gui.client.AutoConnectionManager.MainState#entryAction()
     */
    void entryAction()
    {
      performGameConnectionAction();
    }

    /**
     * Connects to game by joining some existing one or creating new one,
     * depending whether creator or joiner algorithm was chosen.
     */
    void performGameConnectionAction()
    {
      if (creator)
      {
        int gamesCount = gamesData.size();
        if (gamesCount < maxGames)
        {
          sendNetworkMessage(new CreateGameMessage());

          frame.appendLogLine("Creating game...");
        }
      }
      else
      {
        for (Integer gameId : gamesData.keySet())
        {
          GameData data = gamesData.get(gameId);
          if ((data.getState() == GameState.CREATED) ||
              (data.getState() == GameState.READY_TO_RUN))
          {
            if (data.getClientsCount() < data.getMaxClientsCount())
            {
              sendNetworkMessage(new JoinGameMessage(data.getGameId()));

              frame.appendLogLine("Joining game...");
            }
          }
        }
      }
    }
  }

  /**
   * State indicating that client is taking part in game with some game's
   * identifier.
   *
   * @author M.Olszewski
   */
  class InGameState extends MainState
  {
    /** Game's identifier. */
    int gameId;
    /** Simulation. */
    Simulation simulation;
    /**
     * Game state machine, controlling actions of client who is taking part
     * in some game.
     */
    GameStateMachine gameStateMachine = new GameStateMachine();


    /**
     * @see net.java.dante.gui.client.AutoConnectionManager.MainState#processNetworkMessage(net.java.dante.darknet.messaging.NetworkMessage)
     */
    void processNetworkMessage(NetworkMessage message)
    {
      gameStateMachine.processNetworkMessage(message);
    }

    /**
     * @see net.java.dante.gui.client.AutoConnectionManager.MainState#processCommandsReadyMessage(net.java.dante.algorithms.message.CommandsReadyMessage)
     */
    void processCommandsReadyMessage(CommandsReadyMessage message)
    {
      gameStateMachine.processCommandsReadyMessage(message);
    }

    /**
     * @see net.java.dante.gui.client.AutoConnectionManager.MainState#dispose()
     */
    @Override
    void dispose()
    {
      gameStateMachine.dispose();
    }

    /**
     * Default disposal method, disposing simulation and stopping
     * algorithm.
     */
    void defaultDisposal()
    {
      algorithm.requestStopRunning();

      if (simulation != null)
      {
        simulation.dispose();
      }
    }

    /**
     * @see net.java.dante.gui.client.AutoConnectionManager.MainState#entryAction()
     */
    @Override
    void entryAction()
    {
      gameStateMachine.reset();
    }

    /**
     * Sets current game's identifier.
     *
     * @param gameIdentifier current game's identifier.
     */
    void setGameId(int gameIdentifier)
    {
      gameId = gameIdentifier;
    }

    /**
     * Game state machine, controlling actions of client who is taking part
     * in some game.
     *
     * @author M.Olszewski
     */
    class GameStateMachine
    {
      InnerGameState clientNotReadyState = new ClientNotReadyState();
      InnerGameState clientReadyState = new ClientReadyState();
      InnerGameState gameStartedState = new GameStartedState();
      InnerGameState gameRunningState = new GameRunningState();

      InnerGameState currentState = clientNotReadyState;


      /**
       * Resets current state to 'not ready' state and performs entry action
       * for it.
       */
      void reset()
      {
        currentState = clientNotReadyState;
        currentState.entryAction();
      }

      /**
       * Processes the specified network message.
       *
       * @param message the specified network message.
       */
      void processNetworkMessage(NetworkMessage message)
      {
        currentState.processNetworkMessage(message);
      }

      /**
       * Processes the specified network message.
       *
       * @param message the specified network message.
       */
      void processCommandsReadyMessage(CommandsReadyMessage message)
      {
        currentState.processCommandsReadyMessage(message);
      }

      /**
       * Performs disposal.
       */
      void dispose()
      {
        currentState.dispose();
      }

      /**
       * Sets 'client not ready' state.
       */
      void setClientNotReadyState()
      {
        setState(clientNotReadyState);
      }

      /**
       * Sets 'client ready' state.
       */
      void setClientReadyState()
      {
        setState(clientReadyState);
      }

      /**
       * Sets 'started' state.
       */
      void setStartedState()
      {
        setState(gameStartedState);
      }

      /**
       * Sets 'running' state.
       */
      void setRunningState()
      {
        setState(gameRunningState);
      }

      /**
       * Sets the specified state, calling {@link InnerGameState#exitAction()}
       * for current state and {@link InnerGameState#entryAction()} for the
       * specified state.
       *
       * @param newState the specified state.
       */
      private void setState(InnerGameState newState)
      {
        currentState.exitAction();

        currentState = newState;

        currentState.entryAction();
      }
    }


    /**
     * Game's state.
     *
     * @author M.Olszewski
     */
    class InnerGameState
    {
      /**
       * Processes the specified network message.
       *
       * @param message the specified network message.
       */
      void processNetworkMessage(NetworkMessage message)
      {
        // Intentionally left empty.
      }

      /**
       * Processes the specified network message.
       *
       * @param message the specified network message.
       */
      void processCommandsReadyMessage(CommandsReadyMessage message)
      {
        // Intentionally left empty.
      }

      /**
       * Method invoked after setting current state to this one.
       */
      void entryAction()
      {
        // Intentionally left empty.
      }

      /**
       * Method invoked before performing change from this state to different one.
       */
      void exitAction()
      {
        // Intentionally left empty.
      }

      /**
       * Performs disposal.
       */
      void dispose()
      {
        // Intentionally left empty.
      }
    }

    /**
     * 'Client not ready' state - client tries to change its status to
     * 'ready'.
     *
     * @author M.Olszewski
     */
    class ClientNotReadyState extends InnerGameState
    {
      /**
       * @see net.java.dante.gui.client.AutoConnectionManager.InGameState.InnerGameState#processNetworkMessage(net.java.dante.darknet.messaging.NetworkMessage)
       */
      @Override
      void processNetworkMessage(NetworkMessage message)
      {
        if (message instanceof ClientStatusChangedFailedMessage)
        {
          frame.appendLogLine("Marking as ready to start game...");
          sendNetworkMessage(new ReadyToGameStartMessage());
        }
        else if (message instanceof ClientReadyMessage)
        {
          frame.appendLogLine("Marked as ready to start game.");
          gameStateMachine.setClientReadyState();
        }
      }

      /**
       * @see net.java.dante.gui.client.AutoConnectionManager.InGameState.InnerGameState#entryAction()
       */
      @Override
      void entryAction()
      {
        sendNetworkMessage(new ReadyToGameStartMessage());

        frame.appendLogLine("Marking as ready to start game...");
      }
    }

    /**
     * 'Client ready to start game' state. If client created game and
     * game is ready to start, client will start the game.
     *
     * @author M.Olszewski
     */
    class ClientReadyState extends InnerGameState
    {
      /**
       * @see net.java.dante.gui.client.AutoConnectionManager.InGameState.InnerGameState#processNetworkMessage(net.java.dante.darknet.messaging.NetworkMessage)
       */
      @Override
      void processNetworkMessage(NetworkMessage message)
      {
        if (creator)
        {
          if (message instanceof GameDataChangedMessage)
          {
            GameData gameData = ((GameDataChangedMessage)message).getGameData();
            if ((gameId == gameData.getGameId()) && (gameData.getState() == GameState.READY_TO_RUN))
            {
              frame.appendLogLine("Starting game...");
              sendNetworkMessage(new StartGameMessage());
            }
          }
          else if (message instanceof GameStartedMessage)
          {
            frame.appendLogLine("Game started...");
            gameStateMachine.setStartedState();
          }
          else if (message instanceof GameStartFailedMessage)
          {
            // Failed, wait for next game data changed..
          }
        }
        else
        {

          if (message instanceof GameDataChangedMessage)
          {
            GameData gameData = ((GameDataChangedMessage)message).getGameData();
            if ((gameId == gameData.getGameId()) && (gameData.getState() == GameState.INITIALIZING))
            {
              frame.appendLogLine("Game started...");
              gameStateMachine.setStartedState();
            }
          }
        }
      }
    }

    /**
     * 'Game started' state. Means that client is waiting for initialization
     * message. If initialization message arrives,
     *
     * @author M.Olszewski
     */
    class GameStartedState extends InnerGameState
    {
      /**
       * @see net.java.dante.gui.client.AutoConnectionManager.InGameState.InnerGameState#processNetworkMessage(net.java.dante.darknet.messaging.NetworkMessage)
       */
      @Override
      void processNetworkMessage(NetworkMessage message)
      {
        if (message instanceof InitializationNetworkMessage)
        {
          frame.appendLogLine("Initializing game...");

          InitializationData initData = ((InitializationNetworkMessage)message).getInitializationData();

          frame.appendLogLine("Creating simulation...");

          frame.getGamePanel().setVisible(true);
          InitializationDataSource dataSource = new InitializationDataSource(initData);
          ClientSimulationInitData simInitData =
              new ClientSimulationInitData(initData.getGroupId(),
                                           frame.getGamePanel());

          simulation = SimulationFactory.getInstance().createSimulation(SimulationType.CLIENT);
          frame.appendLogLine("Simulation created.");

          frame.appendLogLine("Initializing simulation...");
          simulation.init(new ClientSimulationOutput(), dataSource, simInitData);
          frame.appendLogLine("Simulation initialized.");

          frame.appendLogLine("Initializing algorithm...");
          algorithm.initAlgorithm(receiver, initData);
          frame.appendLogLine("Algorithm initialized.");

          frame.appendLogLine("Starting algorithm...");
          
          AlgorithmRunner runner = new AlgorithmRunner(algorithm);
          
          // prepare loader
          runner.setContextClassLoader(algorithm.getClass().getClassLoader());
          
          runner.start();
          
          frame.appendLogLine("Algorithm started.");

          frame.appendLogLine("Game initialized.");

          frame.appendLogLine("Game starting...");
          simulation.start();
          frame.appendLogLine("Game started.");

          gameStateMachine.setRunningState();
        }
        else if (message instanceof GameInitializationFailedMessage)
        {
          // exit parent state
          stateMachine.setInitState();

          frame.appendLogLine("Game initialization failed.");
        }
      }

      /**
       * @see net.java.dante.gui.client.AutoConnectionManager.InGameState.InnerGameState#dispose()
       */
      void dispose()
      {
        defaultDisposal();
      }
    }

    /**
     * 'Game running' state. Game is running until finish message arrives.
     *
     * @author M.Olszewski
     */
    class GameRunningState extends InnerGameState
    {
      /**
       * @see net.java.dante.gui.client.AutoConnectionManager.InGameState.InnerGameState#processNetworkMessage(net.java.dante.darknet.messaging.NetworkMessage)
       */
      @Override
      void processNetworkMessage(NetworkMessage message)
      {
        if (message instanceof SimulationNetworkMessage)
        {
          ClientInputData input = null;
          if (message instanceof TimeSyncNetworkMessage)
          {
            TimeSyncData timeData = ((TimeSyncNetworkMessage)message).getSync();

            algorithm.addTimeSyncData(timeData);

            input = timeData;
          }
          else if (message instanceof UpdateNetworkMessage)
          {
            UpdateData updateData = ((UpdateNetworkMessage)message).getUpdateData();

            algorithm.addUpdateData(updateData);

            input = updateData;
          }
          else if (message instanceof GroupEliminatedNetworkMessage)
          {
            input = ((GroupEliminatedNetworkMessage)message).getGroupEliminatedData();

            algorithm.requestStopRunning();
          }

          if (input != null)
          {
            simulation.getInput().dataReceived(input);
          }
          else
          {
            if (message instanceof StatisticsNetworkMessage)
            {
              algorithm.requestStopRunning();

              logStatistics(((StatisticsNetworkMessage)message).getStatisticsData().getGroupStatistics());
            }
            else if (message instanceof FinishDataNetworkMessage)
            {
              algorithm.requestStopRunning();

              simulation.dispose();

              frame.getGamePanel().repaint();

              // exit parent state
              stateMachine.setInitState();
            }
          }
        }
      }

      /**
       * @see net.java.dante.gui.client.AutoConnectionManager.InGameState.InnerGameState#processCommandsReadyMessage(net.java.dante.algorithms.message.CommandsReadyMessage)
       */
      void processCommandsReadyMessage(CommandsReadyMessage message)
      {
        session.send(new CommandsNetworkMessage(gameId, new CommandsData(message.getCommands())));
      }

      /**
       * @see net.java.dante.gui.client.AutoConnectionManager.InGameState.InnerGameState#dispose()
       */
      @Override
      void dispose()
      {
        defaultDisposal();
      }

      /**
       * Logs statistics.
       *
       * @param groupStats agents group statistics.
       */
      private void logStatistics(GroupStatistics groupStats)
      {
        frame.appendLogLine("Updates count:                   " + groupStats.getUpdatesCount());
        frame.appendLogLine("Enemy agents visible per update: " + groupStats.getEnemyAgentsAverageVisibility());
        frame.appendLogLine("Enemy agents hit:                " + groupStats.getEnemyAgentsHit());
        frame.appendLogLine("Enemy agents destroyed:          " + groupStats.getEnemyAgentsDestroyed());
        frame.appendLogLine("Projectiles shot:                " + groupStats.getProjectilesShot());
        frame.appendLogLine("Accuracy:                        " + groupStats.getAccuracy());
        frame.appendLogLine("Damage taken:                    " + groupStats.getDamageTaken());
        frame.appendLogLine("Friendly fire agents hits:       " + groupStats.getFriendlyFireHits());
        frame.appendLogLine("Friendly fire agents destroyed:  " + groupStats.getFriendlyFireDestroyed());
        frame.appendLogLine("Friendly agents destroyed:       " + groupStats.getFriendlyAgentsDestroyed());
        frame.appendLogLine("Total points:                    " + groupStats.getTotalPoints());

        try
        {
          PrintWriter writer = new PrintWriter(new FileWriter(RESULTS_FILE_PREFIX + algorithm.getClass().getSimpleName() + ".log", true));

          try
          {
            writer.println("##################################################################");
            writer.println("Updates count:                   \t" + groupStats.getUpdatesCount());
            writer.println("Enemy agents visible per update: \t" + groupStats.getEnemyAgentsAverageVisibility());
            writer.println("Enemy agents hit:                \t" + groupStats.getEnemyAgentsHit());
            writer.println("Enemy agents destroyed:          \t" + groupStats.getEnemyAgentsDestroyed());
            writer.println("Projectiles shot:                \t" + groupStats.getProjectilesShot());
            writer.println("Accuracy:                        \t" + groupStats.getAccuracy());
            writer.println("Damage taken:                    \t" + groupStats.getDamageTaken());
            writer.println("Friendly fire agents hits:       \t" + groupStats.getFriendlyFireHits());
            writer.println("Friendly fire agents destroyed:  \t" + groupStats.getFriendlyFireDestroyed());
            writer.println("Friendly agents destroyed:       \t" + groupStats.getFriendlyAgentsDestroyed());
            writer.println("Total points:                    \t" + groupStats.getTotalPoints());
            writer.println();
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
  }

  class ClientSimulationOutput implements SimulationOutput
  {
    /**
     * @see net.java.dante.sim.io.SimulationOutput#dataReady(net.java.dante.sim.io.OutputData)
     */
    public void dataReady(OutputData data)
    {
      // Intentionally left empty.
    }
  }
}