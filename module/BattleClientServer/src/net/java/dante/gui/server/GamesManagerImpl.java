/*
 * Created on 2006-08-21
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import net.java.dante.gui.common.Const;
import net.java.dante.gui.common.clients.ConnectedClient;
import net.java.dante.gui.common.games.FailureReason;
import net.java.dante.gui.common.games.GameData;
import net.java.dante.gui.common.games.GameDataChangedListener;
import net.java.dante.gui.common.games.GameInitData;
import net.java.dante.gui.common.games.GameInputData;
import net.java.dante.gui.common.games.GameOutputData;
import net.java.dante.gui.common.games.GameState;
import net.java.dante.gui.common.games.GamesManager;
import net.java.dante.gui.common.messages.receiver.SimulationInputMessage;
import net.java.dante.gui.common.messages.receiver.SimulationOutputMessage;
import net.java.dante.sim.io.CommandsData;
import net.java.dante.sim.io.FinishData;
import net.java.dante.sim.io.GroupAbandonSimulationData;
import net.java.dante.sim.io.InputData;
import net.java.dante.sim.io.OutputData;
import net.java.dante.sim.io.StatisticsData;
import net.java.dante.sim.io.TimeSyncData;
import net.java.dante.sim.io.UpdateData;
import net.java.dante.sim.io.init.InitializationData;


/**
 * Implementation of {@link GamesManager} interface.
 *
 * @author M.Olszewski
 */
class GamesManagerImpl implements GamesManager
{
  /** Games storage. */
  private GamesStorage storage;
  /** Server's frame. */
  ServerFrame frame;
  /** Set with {@link GameDataChangedListener} objects. */
  private Set<GameDataChangedListener> listeners =
      new HashSet<GameDataChangedListener>();


  /**
   * Creates instance of {@link GamesManagerImpl} class with the specified
   * parameters.
   *
   * @param maxGamesCount - maximum games count.
   * @param maxClientsCount - maximum clients count per game.
   * @param serverFrame - server's frame.
   */
  GamesManagerImpl(final int maxGamesCount, final int maxClientsCount, ServerFrame serverFrame)
  {
    if (serverFrame == null)
    {
      throw new NullPointerException("Specified serverFrame is null!");
    }
    if (maxGamesCount < Const.MINIMUM_MAX_GAMES_COUNT)
    {
      throw new IllegalArgumentException("Invalid argument maxGamesCount - at least " + Const.MINIMUM_MAX_GAMES_COUNT + " games must be run simultaneously on server!");
    }
    if (maxClientsCount <= 0)
    {
      throw new IllegalArgumentException("Invalid argument maxClientsCount - it must be positive integer!");
    }

    frame   = serverFrame;
    storage = new GamesStorage(maxGamesCount, maxClientsCount);
  }


  /**
   * @see net.java.dante.gui.common.games.GamesManager#getMaxGamesCount()
   */
  public int getMaxGamesCount()
  {
    return storage.getMaxGamesCount();
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#getGamesCount()
   */
  public int getGamesCount()
  {
    return storage.getGamesCount();
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#getGamesData()
   */
  public GameData[] getGamesData()
  {
    return storage.getGamesData();
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#createGame(net.java.dante.gui.common.clients.ConnectedClient)
   */
  public void createGame(ConnectedClient client)
  {
    if (client == null)
    {
      throw new NullPointerException("Specified client is null!");
    }

    Integer gameId = storage.getGameId(client);

    if (gameId == null)
    {
      GameImpl game = storage.createNewGame();
      if (game != null)
      {
        storage.gameCreated(client, Integer.valueOf(game.getData().getGameId()));
      }
      else
      {
        client.gameCreationFailed(FailureReason.NO_FREE_GAMES);
      }
    }
    else
    {
      client.gameCreationFailed(FailureReason.CLIENT_IN_OTHER_GAME);
    }
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#joinGame(net.java.dante.gui.common.clients.ConnectedClient, Integer)
   */
  public void joinGame(ConnectedClient client, Integer gameId)
  {
    if (client == null)
    {
      throw new NullPointerException("Specified client is null!");
    }
    if (gameId == null)
    {
      throw new NullPointerException("Specified gameId is null!");
    }

    Integer clientGameId = storage.getGameId(client);

    if (clientGameId == null)
    {
      GameImpl game = storage.getGame(gameId);
      if (game != null)
      {
        GameDataImpl data = game.getDataImpl();
        GameState state = data.getState();

        switch (state)
        {
          case CREATED:
          case READY_TO_RUN:
          {
            if (data.getClientsCount() < data.getMaxClientsCount())
            {
              storage.clientJoined(client, gameId);
            }
            else
            {
              client.gameJoinFailed(FailureReason.NO_FREE_SLOTS);
            }
            break;
          }
          case FREE:
          {
            client.gameJoinFailed(FailureReason.GAME_NOT_CREATED);
            break;
          }
          case INITIALIZING:
          case RUNNING:
          case PAUSED:
          {
            client.gameJoinFailed(FailureReason.GAME_RUNNING);
            break;
          }
          case FINISHED:
          {
            client.gameJoinFailed(FailureReason.GAME_FINISHED);
            break;
          }
        }
      }
      else
      {
        client.gameJoinFailed(FailureReason.GAME_NOT_EXIST);
      }
    }
    else
    {
      if (clientGameId.equals(gameId))
      {
        client.gameJoinFailed(FailureReason.CLIENT_IN_THIS_GAME);
      }
      else
      {
        client.gameJoinFailed(FailureReason.CLIENT_IN_OTHER_GAME);
      }
    }
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#markAsReady(net.java.dante.gui.common.clients.ConnectedClient, boolean)
   */
  public void markAsReady(ConnectedClient client, boolean readyStatus)
  {
    if (client == null)
    {
      throw new NullPointerException("Specified client is null!");
    }

    Integer clientGameId = storage.getGameId(client);

    if (clientGameId != null)
    {
      GameImpl game = storage.getGame(clientGameId);
      if (game != null)
      {
        GameDataImpl data = game.getDataImpl();
        GameState state = data.getState();
        Integer gameId = Integer.valueOf(data.getGameId());

        switch (state)
        {
          case CREATED:
          case READY_TO_RUN:
          {
            boolean changed = (readyStatus ?
                storage.clientReady(client, gameId) :
                storage.clientNotReady(client, gameId));

            if (changed)
            {
              if (data.isReadyState())
              {
                storage.gameReady(gameId);
              }
              else
              {
                storage.gameNotReady(gameId);
              }
            }

            break;
          }
          case FREE:
          {
            client.clientStatusChangedFailed(FailureReason.GAME_NOT_CREATED);
            break;
          }
          case INITIALIZING:
          case RUNNING:
          case PAUSED:
          {
            client.clientStatusChangedFailed(FailureReason.GAME_RUNNING);
            break;
          }
          case FINISHED:
          {
            client.gameJoinFailed(FailureReason.GAME_FINISHED);
            break;
          }
        }
      }
      else
      {
        throw new IllegalStateException("Illegal state in GamesManager: game with the identifier=" + clientGameId + " should exist!");
      }
    }
    else
    {
      client.clientStatusChangedFailed(FailureReason.CLIENT_NOT_IN_GAME);
    }
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#startGame(net.java.dante.gui.common.clients.ConnectedClient, net.java.dante.gui.common.games.GameInitData)
   */
  public void startGame(ConnectedClient client, GameInitData initData)
  {
    if (client == null)
    {
      throw new NullPointerException("Specified client is null!");
    }
    if (initData == null)
    {
      throw new NullPointerException("Specified initData is null!");
    }
    if (!(initData instanceof ExternalSimulationGameInitData))
    {
      throw new IllegalArgumentException("Invalid argument initData - not an instance of SimulationGameInitData class!");
    }

    Integer clientGameId = storage.getGameId(client);
    if (clientGameId != null)
    {
      GameImpl game = storage.getGame(clientGameId);
      if (game != null)
      {
        GameDataImpl data = game.getDataImpl();
        GameState state = data.getState();

        switch (state)
        {
          case CREATED:
          {
            client.gameStartFailed(FailureReason.GAME_NOT_READY);
            break;
          }
          case READY_TO_RUN:
          {
            storage.gameStarted(client, clientGameId, initData);
            break;
          }
          case FREE:
          {
            client.gameStartFailed(FailureReason.GAME_NOT_CREATED);
            break;
          }
          case INITIALIZING:
          case RUNNING:
          case PAUSED:
          {
            client.gameStartFailed(FailureReason.GAME_RUNNING);
            break;
          }
          case FINISHED:
          {
            client.gameJoinFailed(FailureReason.GAME_FINISHED);
            break;
          }
        }
      }
      else
      {
        throw new IllegalStateException("Illegal state in GamesManager: game with the identifier=" + clientGameId + " should exist!");
      }
    }
    else
    {
      client.gameStartFailed(FailureReason.CLIENT_NOT_IN_GAME);
    }
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#gameInitializationFailed(java.lang.Integer)
   */
  public void gameInitializationFailed(Integer gameId)
  {
    if (gameId == null)
    {
      throw new NullPointerException("Specified gameId is null!");
    }

    GameImpl game = storage.getGame(gameId);
    if (game != null)
    {
      GameDataImpl data = game.getDataImpl();
      GameState state = data.getState();

      switch (state)
      {
        case INITIALIZING:
        {
          storage.gameInitializationFailed(gameId);
          break;
        }
        case FREE:
        case CREATED:
        case READY_TO_RUN:
        case RUNNING:
        case PAUSED:
        case FINISHED:
        {
          throw new IllegalStateException("Illegal state in GamesManager: game with the identifier=" + gameId + " should be in 'initializing' state!");
        }
      }
    }
    else
    {
      throw new IllegalStateException("Illegal state in GamesManager: game with the identifier=" + gameId + " should exist!");
    }
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#abandonGame(net.java.dante.gui.common.clients.ConnectedClient)
   */
  public void abandonGame(ConnectedClient client)
  {
    if (client == null)
    {
      throw new NullPointerException("Specified client is null!");
    }

    Integer clientGameId = storage.getGameId(client);

    if (clientGameId != null)
    {
      GameImpl game = storage.getGame(clientGameId);
      if (game != null)
      {
        GameDataImpl data = game.getDataImpl();
        GameState state = data.getState();

        switch (state)
        {
          case CREATED:
          case READY_TO_RUN:
          case INITIALIZING:
          case RUNNING:
          case PAUSED:
          {
            storage.clientAbandoned(client, clientGameId);
            break;
          }
          case FREE:
          {
            client.gameAbandonFailed(FailureReason.GAME_NOT_CREATED);
            break;
          }
          case FINISHED:
          {
            client.gameAbandonFailed(FailureReason.GAME_FINISHED);
            break;
          }
        }
      }
      else
      {
        throw new IllegalStateException("Illegal state in GamesManager: game with the identifier=" + clientGameId + " should exist!");
      }
    }
    else
    {
      client.gameAbandonFailed(FailureReason.CLIENT_NOT_IN_GAME);
    }
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#processGameInput(net.java.dante.gui.common.clients.ConnectedClient, net.java.dante.gui.common.games.GameInputData)
   */
  public void processGameInput(ConnectedClient client, GameInputData inputData)
  {
    if (client == null)
    {
      throw new NullPointerException("Specified client is null!");
    }
    if (inputData == null)
    {
      throw new NullPointerException("Specified inputData is null!");
    }
    if (!(inputData instanceof SimulationInputMessage))
    {
      throw new IllegalArgumentException("Invalid argument initData - not an instance of SimulationInputMessage class!");
    }

    Integer clientGameId = storage.getGameId(client);

    if (clientGameId != null)
    {
      GameImpl game = storage.getGame(clientGameId);
      if (game != null)
      {
        if (clientGameId.equals(inputData.getGameId()))
        {
          GameState state = game.getDataImpl().getState();

          switch (state)
          {
            case FREE:
            case CREATED:
            case READY_TO_RUN:
            {
              client.gameInputNotAccepted(FailureReason.GAME_NOT_RUNNIG);
              break;
            }
            case INITIALIZING:
            {
              SimulationInputMessage simInputMessage = (SimulationInputMessage)inputData;
              if (simInputMessage.getInputData() instanceof GroupAbandonSimulationData)
              {
                storage.processGameInput(client, (SimulationInputMessage)inputData);
              }
              else
              {
                client.gameInputNotAccepted(FailureReason.GAME_NOT_RUNNIG);
              }
              break;
            }
            case RUNNING:
            {
              storage.processGameInput(client, (SimulationInputMessage)inputData);
              break;
            }
            case PAUSED:
            {
              client.gameInputNotAccepted(FailureReason.GAME_PAUSED);
              break;
            }
            case FINISHED:
            {
              client.gameInputNotAccepted(FailureReason.GAME_FINISHED);
              break;
            }
          }
        }
        else
        {
          client.gameInputNotAccepted(FailureReason.CLIENT_IN_OTHER_GAME);
        }
      }
      else
      {
        throw new IllegalStateException("Illegal state in GamesManager: game with the identifier=" + clientGameId + " should exist!");
      }
    }
    else
    {
      client.gameInputNotAccepted(FailureReason.CLIENT_NOT_IN_GAME);
    }
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#processGameOutput(net.java.dante.gui.common.games.GameOutputData)
   */
  public void processGameOutput(GameOutputData outputData)
  {
    if (outputData == null)
    {
      throw new NullPointerException("Specified inputData is null!");
    }
    if (!(outputData instanceof SimulationOutputMessage))
    {
      throw new IllegalArgumentException("Invalid argument outputData - not an instance of SimulationOutputMessage class!");
    }

    GameImpl game = storage.getGame(outputData.getGameId());
    if (game != null)
    {
      GameState state = game.getDataImpl().getState();

      switch (state)
      {
        case FREE:
        case CREATED:
        case READY_TO_RUN:
        case FINISHED:
        {
          throw new IllegalStateException("Illegal state detected in GamesManagerImpl: game with the identifier=" + outputData.getGameId() + " should not generate output!");
        }
        case INITIALIZING:
        {
          storage.processInitializationData((SimulationOutputMessage)outputData);
          break;
        }
        case RUNNING:
        case PAUSED:
        {
          storage.processGameOutput((SimulationOutputMessage)outputData);
          break;
        }
      }
    }
    else
    {
      throw new IllegalStateException("Illegal state detected in GamesManagerImpl: game with the identifier=" + outputData.getGameId() + " should exist!");
    }
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#clientDisconnected(net.java.dante.gui.common.clients.ConnectedClient)
   */
  public void clientDisconnected(ConnectedClient client)
  {
    if (client == null)
    {
      throw new NullPointerException("Specified client is null!");
    }

    storage.clientDisconnected(client);
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#isInGame(net.java.dante.gui.common.clients.ConnectedClient)
   */
  public boolean isInGame(ConnectedClient client)
  {
    return (storage.getGameId(client) != null);
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#getGameIdentifier(net.java.dante.gui.common.clients.ConnectedClient)
   */
  public Integer getGameIdentifier(ConnectedClient client)
  {
    return storage.getGameId(client);
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#dispose()
   */
  public void dispose()
  {
    listeners.clear();

    storage.dispose();
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#addGameDataChangedListener(net.java.dante.gui.common.games.GameDataChangedListener)
   */
  public void addGameDataChangedListener(GameDataChangedListener listener)
  {
    if (listener == null)
    {
      throw new NullPointerException("Specified listener is null!");
    }

    listeners.add(listener);
  }

  /**
   * @see net.java.dante.gui.common.games.GamesManager#removeGameDataChangedListener(net.java.dante.gui.common.games.GameDataChangedListener)
   */
  public void removeGameDataChangedListener(GameDataChangedListener listener)
  {
    if (listener == null)
    {
      throw new NullPointerException("Specified listener is null!");
    }

    listeners.remove(listener);
  }

  /**
   * Fires all stored {@link GameDataChangedListener} with the specified
   * changed {@link GameData} object.
   *
   * @param gameData - changed {@link GameData} object.
   */
  void fireListeners(GameData gameData)
  {
    for (GameDataChangedListener listener : listeners)
    {
      listener.gameDataChanged(gameData);
    }
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[storage=" + storage +
        "; listeners=" + listeners + "]");
  }

  /**
   * Class storing all games.
   *
   * @author M.Olszewski
   */
  private class GamesStorage
  {
    /** Maximum games count. */
    private final int maxGames;
    /** Maximum  clients count per game. */
    private final int maxClients;

    /** Games and their panels. */
    private Map<Integer, JPanel> gameIdToPanel = new HashMap<Integer, JPanel>();

    /** Mapping between clients and games identifiers. */
    private Map<ConnectedClient, Integer> clientsToGames =
        new HashMap<ConnectedClient, Integer>();
    /** All games. */
    private Map<Integer, GameImpl> games;
    /** All games data. */
    private List<GameDataImpl> gamesData;


    /**
     * Creates instance of {@link GamesStorage} class with the specified
     * parameters.
     *
     * @param maxGamesCount - maximum games count.
     * @param maxClientsCount - maximum clients count per game.
     */
    GamesStorage(final int maxGamesCount, final int maxClientsCount)
    {
      maxGames     = maxGamesCount;
      maxClients   = maxClientsCount;

      games     = new HashMap<Integer, GameImpl>(maxGamesCount);
      gamesData = new ArrayList<GameDataImpl>(maxGamesCount);
    }


    /**
     * Gets game's identifier for the specified client. This method may return
     * <code>null</code> value if the specified client is not in any game.
     *
     * @param client - the specified {@link ConnectedClient} object.
     *
     * @return Return game's identifier for the specified client or
     *         <code>null</code> value if the specified client is not
     *         in any game.
     */
    Integer getGameId(ConnectedClient client)
    {
      return clientsToGames.get(client);
    }

    /**
     * Creates new {@link GameImpl} object if it is possible (current games
     * count is less than maximum games count) and returns reference to it
     * or returns <code>null</code> if creation of new game is not possible.
     *
     * @return Returns reference to created {@link GameImpl} object
     *         or <code>null</code> if creation of new game was not possible.
     */
    GameImpl createNewGame()
    {
      GameImpl newGame = null;

      if (games.size() < maxGames)
      {
        GameDataImpl data = new GameDataImpl(maxClients);
        newGame = new GameImpl(data);

        games.put(Integer.valueOf(newGame.getData().getGameId()), newGame);
        gamesData.add(data);
      }

      return newGame;
    }

    /**
     * Gets array containing all games data.
     *
     * @return Returns array containing all games data.
     */
    GameDataImpl[] getGamesData()
    {
      return gamesData.toArray(new GameDataImpl[gamesData.size()]);
    }

    /**
     * Gets {@link GameImpl} object with the specified identifier.
     *
     * @param gameId - the specified game's identifier.
     *
     * @return Returns {@link GameImpl} object with the specified identifier.
     */
    GameImpl getGame(Integer gameId)
    {
      return games.get(gameId);
    }

    /**
     * Gets maximum number of games.
     *
     * @return Returns maximum number of games.
     */
    int getMaxGamesCount()
    {
      return maxGames;
    }

    /**
     * Gets number of all stored games.
     *
     * @return Returns number of all stored games.
     */
    int getGamesCount()
    {
      return games.size();
    }

    /**
     * Marks game with the specified identifier as created.
     *
     * @param creator - client who created the specified game.
     * @param gameId - the specified game's identifier.
     */
    void gameCreated(ConnectedClient creator, Integer gameId)
    {
      GameImpl game     = games.get(gameId);
      GameDataImpl data = game.getDataImpl();

      clientsToGames.put(creator, gameId);
      game.create();
      data.addClient(creator);

      creator.gameCreated(gameId.intValue());

      updateGameData(data);
    }

    /**
     * This method should be invoked if the specified client should join
     * game with the specified identifier.
     *
     * @param joinedClient - the specified client who joined the game.
     * @param gameId - the specified game's identifier.
     */
    void clientJoined(ConnectedClient joinedClient, Integer gameId)
    {
      GameImpl game     = games.get(gameId);
      GameDataImpl data = game.getDataImpl();

      clientsToGames.put(joinedClient, gameId);
      data.addClient(joinedClient);

      joinedClient.gameJoined(gameId.intValue(), data.getGameClientsData());

      if (data.getState() == GameState.READY_TO_RUN)
      {
        if (!data.isReadyState())
        {
          gameNotReady(gameId);
        }
      }

      updateGameData(data);
    }

    /**
     * This method should be invoked if the specified client should abandon
     * game with the specified identifier.
     *
     * @param abandonedClient - the specified client who abandoned the game.
     * @param gameId - the specified game's identifier.
     */
    void clientAbandoned(ConnectedClient abandonedClient, Integer gameId)
    {
      GameImpl game     = games.get(gameId);
      GameDataImpl data = game.getDataImpl();

      clientsToGames.remove(abandonedClient);
      clientAbandonedGame(abandonedClient, gameId);
      data.removeClient(abandonedClient);

      if (data.getState() == GameState.READY_TO_RUN)
      {
        if (!data.isReadyState())
        {
          gameNotReady(gameId);
        }
      }
      else if (data.getState() == GameState.CREATED)
      {
        if (data.isReadyState())
        {
          gameReady(gameId);
        }
      }

      abandonedClient.gameAbandoned(gameId.intValue());

      if (data.getClientsCount() <= 0)
      {
        gameFinished(gameId);
      }
      else
      {
        updateGameData(data);
      }
    }

    /**
     * Notifies game with the specified identifier that the specified client
     * abandoned it.
     *
     * @param client - the specified client.
     * @param gameId - the specified game's identifier.
     */
    private void clientAbandonedGame(ConnectedClient client, Integer gameId)
    {
      GameImpl     game = games.get(gameId);
      GameDataImpl data = game.getDataImpl();
      Integer groupId   = data.getAgentsGroupId(client);

      if (groupId != null)
      {
        game.acceptInput(new SimulationInputMessage(
            gameId.intValue(),
            new GroupAbandonSimulationData(groupId.intValue())));
      }
    }

    /**
     * This method should be invoked if the specified client
     * marked itself as ready for game start.
     *
     * @param readyClient - the specified client.
     * @param gameId - the specified game's identifier.
     *
     * @return Returns <code>true</code> if data has changed after marking
     *         client as ready, <code>false</code> otherwise.
     */
    boolean clientReady(ConnectedClient readyClient, Integer gameId)
    {
      boolean changed = false;

      GameImpl game = games.get(gameId);
      GameDataImpl data = game.getDataImpl();

      if (data.clientReady(readyClient))
      {
        notifyClientGameStatusChanged(readyClient.getClientData().getClientId(),
                                      gameId,
                                      true);
        readyClient.clientSetAsReady(gameId.intValue());

        changed = true;
      }

      return changed;
    }

    /**
     * This method should be invoked if the specified client
     * marked itself as not ready for game start.
     *
     * @param notReadyClient - the specified client.
     * @param gameId - the specified game's identifier.
     *
     * @return Returns <code>true</code> if data has changed after marking
     *         client as ready, <code>false</code> otherwise.
     */
    boolean clientNotReady(ConnectedClient notReadyClient, Integer gameId)
    {
      boolean changed = false;

      GameImpl game = games.get(gameId);
      GameDataImpl data = game.getDataImpl();

      if (data.clientNotReady(notReadyClient))
      {
        notifyClientGameStatusChanged(notReadyClient.getClientData().getClientId(),
                                      gameId,
                                      false);
        notReadyClient.clientSetAsNotReady(gameId.intValue());

        changed = true;
      }

      return changed;
    }

    /**
     * Notify all clients who are in game with the specified identifier that
     * client with the specified identifier changed its readiness status
     * to the specified one.
     *
     * @param clientId - the specified client's identifier.
     * @param gameId - the specified game's identifier.
     * @param status - the specified status.
     */
    private void notifyClientGameStatusChanged(int clientId,
                                               Integer gameId,
                                               boolean status)
    {
      for (ConnectedClient client : clientsToGames.keySet())
      {
        int otherClientId = client.getClientData().getClientId();
        if ((otherClientId != clientId) && (gameId.equals(clientsToGames.get(client))))
        {
          client.clientGameStatusChanged(clientId, gameId.intValue(), status);
        }
      }
    }

    /**
     * Marks game with the specified identifier as ready to start.
     *
     * @param gameId - the specified game's identifier.
     */
    void gameReady(Integer gameId)
    {
      GameImpl game = games.get(gameId);
      GameDataImpl data = game.getDataImpl();

      game.ready();

      updateGameData(data);
    }

    /**
     * Marks game with the specified identifier as not ready to start.
     *
     * @param gameId - the specified game's identifier.
     */
    void gameNotReady(Integer gameId)
    {
      GameImpl game = games.get(gameId);
      GameDataImpl data = game.getDataImpl();

      game.notReady();

      updateGameData(data);
    }

    /**
     * Marks game with the specified identifier as started.
     *
     * @param startingClient - the client starting this game.
     * @param gameId - the specified game's identifier.
     * @param initData - initialization data for this game.
     */
    void gameStarted(ConnectedClient startingClient, Integer gameId, GameInitData initData)
    {
      GameImpl game = games.get(gameId);
      GameDataImpl data = game.getDataImpl();

      // Confirm client that its request was successful
      startingClient.gameStarted(gameId.intValue());

      // Initializing
      game.init(new SimulationGameInitData((ExternalSimulationGameInitData)initData,
                                           getPanel(gameId)));

      // Notify all clients that game started (game data contains 'initializing' state)
      updateGameData(data);
    }

    /**
     * Gets free panel for the game with the specified identifier.
     *
     * @param gameId the specified game's identifier.
     *
     * @return Returns free panel for the game with the specified identifier.
     */
    private JPanel getPanel(Integer gameId)
    {
      JPanel panel = frame.getFreePanel();

      if (panel == null)
      {
        throw new IllegalStateException("Illegal state in GamesManager: game with the identifier=" + gameId + " cannot be created because there are no free game panels!");
      }

      gameIdToPanel.put(gameId, panel);

      return panel;
    }

    /**
     * Method called when initialization of game with the specified identifier
     * has failed.
     *
     * @param gameId - the specified game's identifier.
     */
    void gameInitializationFailed(Integer gameId)
    {
      for (ConnectedClient client : clientsToGames.keySet())
      {
        if (gameId.equals(clientsToGames.get(client)))
        {
          client.gameInitializationFailed(gameId.intValue());
        }
      }

      // Remove resources
      removeGameResources(gameId);
    }

    /**
     * Processes game input generated by the specified client.
     *
     * @param client - the client that generated game's input.
     * @param inputData - generated input data.
     */
    void processGameInput(ConnectedClient client, SimulationInputMessage inputData)
    {
      GameImpl game = getGame(inputData.getGameId());
      GameDataImpl data = game.getDataImpl();

      InputData simInputData = inputData.getInputData();

      if (simInputData instanceof CommandsData)
      {
        CommandsData commandsData = (CommandsData) simInputData;
        int groupId = commandsData.getRepository().getGroupId();
        int clientGroupId = data.getAgentsGroupId(client).intValue();
        if (clientGroupId == groupId)
        {
          game.acceptInput(inputData);
        }
        else
        {
          client.gameInputNotAccepted(FailureReason.GAME_INPUT_INVALID);
        }
      }
    }

    /**
     * Processes simulation initialization data wrapped by the specified
     * {@link SimulationOutputMessage} output message.
     *
     * @param outputSimMessage - the specified simulation output message.
     */
    void processInitializationData(SimulationOutputMessage outputSimMessage)
    {
      OutputData outputData = outputSimMessage.getOutputData();

      if (!(outputData instanceof InitializationData))
      {
        throw new IllegalStateException("Illegal state detected in GamesManagerImpl: OutputData different than InitializationData generated during initialization phase!");
      }

      InitializationData initData = (InitializationData)outputData;
      GameImpl     game = games.get(outputSimMessage.getGameId());
      GameDataImpl data = game.getDataImpl();

      boolean startGame = data.addAgentsGroupMapping(initData.getGroupId());

      // Send initialization data to client
      sendOutputData(initData.getGroupId(), outputSimMessage);

      // Start game
      if (startGame)
      {
        // All mappings added -> game started
        game.start();

        updateGameData(data);
      }
    }

    /**
     * Processes the specified simulation output.
     *
     * @param outputSimMessage - the specified simulation output message.
     */
    void processGameOutput(SimulationOutputMessage outputSimMessage)
    {
      OutputData outputData = outputSimMessage.getOutputData();

      // Check integrity of the system
      if (outputData instanceof InitializationData)
      {
        throw new IllegalStateException("Illegal state detected in GamesManagerImpl: InitializationData generated after initialization phase!");
      }

      if (outputData instanceof UpdateData)
      {
        int groupId = ((UpdateData)outputData).getRepository().getGroupId();
        sendOutputData(groupId, outputSimMessage);
      }
      else if (outputData instanceof TimeSyncData)
      {
        int groupId = ((TimeSyncData)outputData).getGroupId();
        sendOutputData(groupId, outputSimMessage);
      }
      else if (outputData instanceof StatisticsData)
      {
        int groupId = ((StatisticsData)outputData).getGroupStatistics().getGroupId();
        sendOutputData(groupId, outputSimMessage);
      }
      else if (outputData instanceof FinishData)
      {
        gameFinished(outputSimMessage.getGameId());
      }
      else
      {
        throw new IllegalArgumentException("Invalid argument outputData - the specified class=" + getClass() + " is not supported by GamesManagerImpl!");
      }
    }

    /**
     * Sends the specified {@link SimulationOutputMessage} message to the client
     * with the specified agents group identifier.
     *
     * @param groupId - the specified agents group identifier.
     * @param outputData - the specified {@link SimulationOutputMessage} message
     */
    private void sendOutputData(int groupId, SimulationOutputMessage outputData)
    {
      GameDataImpl data = games.get(outputData.getGameId()).getDataImpl();
      ConnectedClient client = data.getClientWithGroupId(groupId);

      // Client can be null here - e.g. when connection with it was lost
      // suddenly and some messages were prepared for him
      if (client != null)
      {
        client.gameOutputReady(outputData);
      }
    }

    /**
     * Marks game with the specified identifier as finished.
     *
     * @param gameId - the specified game's identifier.
     */
    private void gameFinished(Integer gameId)
    {
      GameImpl game = games.get(gameId);

      game.finish();

      for (ConnectedClient client : clientsToGames.keySet())
      {
        if (gameId.equals(clientsToGames.get(client)))
        {
          client.gameOutputReady(new SimulationOutputMessage(gameId, new FinishData()));
        }
      }

      removeGameResources(gameId);
    }

    /**
     * Removes all resources connected with the specified game's identifier.
     *
     * @param gameId - the specified game's identifier.
     */
    private void removeGameResources(Integer gameId)
    {
      // Mark panel as free
      JPanel panel = gameIdToPanel.get(gameId);
      if (panel == null)
      {
        throw new IllegalStateException("Illegal state in GamesManager: there should exist panel assigned to a game with the identifier=" + gameId + "!");
      }
      frame.freePanel(panel);
      // Remove from 'game id to panel' map
      gameIdToPanel.remove(gameId);

      // Remove all clients from the game
      for (Iterator<ConnectedClient> it = clientsToGames.keySet().iterator(); it.hasNext(); )
      {
        Integer clientGameId = clientsToGames.get(it.next());

        if (gameId.equals(clientGameId))
        {
          it.remove();
        }
      }

      removeGameData(gameId);
    }
    
    private void removeGameData(Integer gameId)
    {
      GameDataImpl data = games.get(gameId).getDataImpl();
      
      // Remove data
      gamesData.remove(data);
      // Remove game itself
      games.remove(gameId);
      // Remove from the frame
      frame.removeGameData(data);
      
      fireListeners(data);
    }

    /**
     * Client was disconnected - clears all data connected with the specified
     * client.
     *
     * @param client - disconnected client.
     */
    void clientDisconnected(ConnectedClient client)
    {
      Integer gameId = clientsToGames.get(client);
      if (gameId != null)
      {
        GameImpl game = games.get(gameId);

        if (game != null)
        {
          GameDataImpl data = game.getDataImpl();

          clientAbandonedGame(client, Integer.valueOf(data.getGameId()));
          data.removeClient(client);
          
          if (data.getClientsCount() <= 0)
          {
            if ((data.getState() == GameState.INITIALIZING) ||
                (data.getState() == GameState.RUNNING) ||
                (data.getState() == GameState.PAUSED))
            {
              gameFinished(gameId);
            }
            else
            {
              removeGameData(gameId);
            }
          }
          else
          {
            updateGameData(data);
          }
        }
        else
        {
          throw new IllegalStateException("Illegal state in GamesManager: game with the identifier=" + gameId + " should exist!");
        }

        clientsToGames.remove(client);
      }
    }

    void updateGameData(GameDataImpl gameData)
    {
      if (frame.containsGameData(gameData))
      {
        frame.modifyGameData(gameData);
      }
      else
      {
        frame.addGameData(gameData);
      }

      fireListeners(gameData);
    }

    /**
     * Disposes all running games.
     */
    void dispose()
    {
      // Finish all games
      for (Integer gameId : games.keySet())
      {
        GameImpl game = games.get(gameId);
        game.finish();
      }

      // Clear games storage
      games.clear();
      gamesData.clear();

      // Set panels free
      for (Integer gameId : gameIdToPanel.keySet())
      {
        JPanel panel = gameIdToPanel.get(gameId);
        frame.freePanel(panel);
      }

      // Clear associations
      gameIdToPanel.clear();
      clientsToGames.clear();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      return (getClass() + "[maxClients=" + maxClients +
          "; maxGames=" + maxGames + "; clientsToGames=" + clientsToGames +
          "; games=" + games + "; gamesData=" + gamesData + "]");
    }
  }
}