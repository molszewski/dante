/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.server;


import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.session.Session;
import net.java.dante.gui.common.clients.ClientData;
import net.java.dante.gui.common.clients.ClientInitializationData;
import net.java.dante.gui.common.clients.ConnectedClient;
import net.java.dante.gui.common.games.FailureReason;
import net.java.dante.gui.common.games.GameData;
import net.java.dante.gui.common.games.GameOutputData;
import net.java.dante.gui.common.games.GameStatusData;
import net.java.dante.gui.common.messages.net.client.update.ClientConnectedMessage;
import net.java.dante.gui.common.messages.net.client.update.ClientDisconnectedMessage;
import net.java.dante.gui.common.messages.net.client.update.ClientStatusChangedMessage;
import net.java.dante.gui.common.messages.net.client.update.GameDataChangedMessage;
import net.java.dante.gui.common.messages.net.client.update.GameInitializationFailedMessage;
import net.java.dante.gui.common.messages.net.client.update.InitDataMessage;
import net.java.dante.gui.common.messages.net.game.confirm.ClientNotReadyMessage;
import net.java.dante.gui.common.messages.net.game.confirm.ClientReadyMessage;
import net.java.dante.gui.common.messages.net.game.confirm.GameAbandonedMessage;
import net.java.dante.gui.common.messages.net.game.confirm.GameCreatedMessage;
import net.java.dante.gui.common.messages.net.game.confirm.GameJoinedMessage;
import net.java.dante.gui.common.messages.net.game.confirm.GameStartedMessage;
import net.java.dante.gui.common.messages.net.game.fail.ClientStatusChangedFailedMessage;
import net.java.dante.gui.common.messages.net.game.fail.GameAbandonFailedMessage;
import net.java.dante.gui.common.messages.net.game.fail.GameCreationFailedMessage;
import net.java.dante.gui.common.messages.net.game.fail.GameInputNotAcceptedMessage;
import net.java.dante.gui.common.messages.net.game.fail.GameJoinFailedMessage;
import net.java.dante.gui.common.messages.net.game.fail.GameStartFailedMessage;
import net.java.dante.gui.common.messages.net.game.sim.FinishDataNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.GroupEliminatedNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.InitializationNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.StatisticsNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.TimeSyncNetworkMessage;
import net.java.dante.gui.common.messages.net.game.sim.UpdateNetworkMessage;
import net.java.dante.gui.common.messages.receiver.SimulationOutputMessage;
import net.java.dante.sim.io.FinishData;
import net.java.dante.sim.io.GroupEliminatedSimulationData;
import net.java.dante.sim.io.OutputData;
import net.java.dante.sim.io.StatisticsData;
import net.java.dante.sim.io.TimeSyncData;
import net.java.dante.sim.io.UpdateData;
import net.java.dante.sim.io.init.InitializationData;

/**
 * Implementation of {@link ConnectedClient} interface.
 *
 * @author M.Olszewski
 */
class ConnectedClientImpl implements ConnectedClient
{
  /** This client's data. */
  private final  ClientData data;
  /** This server's frame. */
  private final ServerFrame frame;
  /** This client's session. */
  private final Session session;


  /**
   * Creates instance of {@link ConnectedClientImpl} connected
   * with the specified session.
   *
   * @param clientData - this client's data.
   * @param clientSession  - this client's session.
   * @param serverFrame - server's frame.
   */
  ConnectedClientImpl(ClientData clientData, Session clientSession, ServerFrame serverFrame)
  {
    if (clientData == null)
    {
      throw new NullPointerException("Specified clientData is null!");
    }
    if (clientSession == null)
    {
      throw new NullPointerException("Specified clientSession is null!");
    }
    if (serverFrame == null)
    {
      throw new NullPointerException("Specified serverFrame is null!");
    }

    data    = clientData;
    session = clientSession;
    frame   = serverFrame;
  }


  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#getClientData()
   */
  public ClientData getClientData()
  {
    return data;
  }

  /**
   * Gets this client's session.
   *
   * @return Returns this client's session.
   */
  Session getSession()
  {
    return session;
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#gameCreated(int)
   */
  public void gameCreated(int gameId)
  {
    sendMessage(new GameCreatedMessage(gameId));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#gameJoined(int, net.java.dante.gui.common.games.GameStatusData[])
   */
  public void gameJoined(int gameId, GameStatusData[] gameStatuses)
  {
    sendMessage(new GameJoinedMessage(gameId, gameStatuses));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#gameStarted(int)
   */
  public void gameStarted(int gameId)
  {
    sendMessage(new GameStartedMessage(gameId));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#gameInitializationFailed(int)
   */
  public void gameInitializationFailed(int gameId)
  {
    sendMessage(new GameInitializationFailedMessage(gameId));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#gameAbandoned(int)
   */
  public void gameAbandoned(int gameId)
  {
    sendMessage(new GameAbandonedMessage(gameId));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#clientSetAsReady(int)
   */
  public void clientSetAsReady(int gameId)
  {
    sendMessage(new ClientReadyMessage(gameId));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#clientSetAsNotReady(int)
   */
  public void clientSetAsNotReady(int gameId)
  {
    sendMessage(new ClientNotReadyMessage(gameId));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#clientGameStatusChanged(int, int, boolean)
   */
  public void clientGameStatusChanged(int clientId, int gameId, boolean status)
  {
    sendMessage(new ClientStatusChangedMessage(clientId, gameId, status));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#gameDataChanged(net.java.dante.gui.common.games.GameData)
   */
  public void gameDataChanged(GameData gameData)
  {
    sendMessage(new GameDataChangedMessage(gameData));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#gameInputAccepted(int)
   */
  public void gameInputAccepted(int gameId)
  {
    // Intentionally left empty - clients not notified about acceptance of input.
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#gameOutputReady(GameOutputData)
   */
  public void gameOutputReady(GameOutputData outputData)
  {
    if (!(outputData instanceof SimulationOutputMessage))
    {
      throw new IllegalArgumentException("Invalid argument outputData - not an instance of SimulationOutputMessage class!");
    }

    SimulationOutputMessage simOutputData = (SimulationOutputMessage)outputData;

    SimulationNetworkMessage simNetworkMessage =
        createSimulationNetworkMessage(simOutputData.getGameId().intValue(),
                                       simOutputData.getOutputData());
    if (simNetworkMessage != null)
    {
      sendMessage(simNetworkMessage);
    }
  }

  /**
   * Creates proper instance of {@link SimulationNetworkMessage} subclass based upon
   * the specified implementation of {@link OutputData} interface.
   *
   * @param gameId game's identifier for which this message was created.
   * @param outputData output data generated by simulation.
   *
   * @return Returns created proper instance of {@link SimulationNetworkMessage}
   *         subclass.
   */
  private SimulationNetworkMessage createSimulationNetworkMessage(int gameId, OutputData outputData)
  {
    SimulationNetworkMessage message = null;

    if (outputData instanceof InitializationData)
    {
      message = new InitializationNetworkMessage(gameId, (InitializationData)outputData);
    }
    else if (outputData instanceof UpdateData)
    {
      message = new UpdateNetworkMessage(gameId, (UpdateData)outputData);
    }
    else if (outputData instanceof TimeSyncData)
    {
      message = new TimeSyncNetworkMessage(gameId, (TimeSyncData)outputData);
    }
    else if (outputData instanceof StatisticsData)
    {
      message = new StatisticsNetworkMessage(gameId, (StatisticsData)outputData);
    }
    else if (outputData instanceof GroupEliminatedSimulationData)
    {
      message = new GroupEliminatedNetworkMessage(gameId, (GroupEliminatedSimulationData)outputData);
    }
    else if (outputData instanceof FinishData)
    {
      message = new FinishDataNetworkMessage(gameId);
    }

    return message;
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#gameCreationFailed(net.java.dante.gui.common.games.FailureReason)
   */
  public void gameCreationFailed(FailureReason reason)
  {
    sendMessage(new GameCreationFailedMessage(reason));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#gameJoinFailed(net.java.dante.gui.common.games.FailureReason)
   */
  public void gameJoinFailed(FailureReason reason)
  {
    sendMessage(new GameJoinFailedMessage(reason));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#clientStatusChangedFailed(net.java.dante.gui.common.games.FailureReason)
   */
  public void clientStatusChangedFailed(FailureReason reason)
  {
    sendMessage(new ClientStatusChangedFailedMessage(reason));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#gameStartFailed(net.java.dante.gui.common.games.FailureReason)
   */
  public void gameStartFailed(FailureReason reason)
  {
    sendMessage(new GameStartFailedMessage(reason));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#gameAbandonFailed(net.java.dante.gui.common.games.FailureReason)
   */
  public void gameAbandonFailed(FailureReason reason)
  {
    sendMessage(new GameAbandonFailedMessage(reason));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#gameInputNotAccepted(net.java.dante.gui.common.games.FailureReason)
   */
  public void gameInputNotAccepted(FailureReason reason)
  {
    sendMessage(new GameInputNotAcceptedMessage(reason));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#clientConnected(net.java.dante.gui.common.clients.ClientData)
   */
  public void clientConnected(ClientData clientData)
  {
    sendMessage(new ClientConnectedMessage(clientData));
  }


  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#clientDisconnected(net.java.dante.gui.common.clients.ClientData)
   */
  public void clientDisconnected(ClientData clientData)
  {
    sendMessage(new ClientDisconnectedMessage(clientData));
  }

  /**
   * @see net.java.dante.gui.common.clients.ConnectedClient#clientInitialized(net.java.dante.gui.common.clients.ClientInitializationData)
   */
  public void clientInitialized(ClientInitializationData clientInitData)
  {
    sendMessage(new InitDataMessage(clientInitData.getCurrentClientData(),
                                    clientInitData.getClientsData(),
                                    clientInitData.getMaxGames(),
                                    clientInitData.getGamesData()));
  }

  /**
   * Sends the specified message using session object.
   *
   * @param message - message to send.
   */
  private void sendMessage(NetworkMessage message)
  {
    if (session.isConnected())
    {
      session.send(message);
      frame.modifyClientData(this);
    }
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + data.hashCode();
    result = PRIME * result + session.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof ConnectedClientImpl))
    {
      final ConnectedClientImpl other = (ConnectedClientImpl) object;
      equal = ((data.equals(other.data)) &&
               (session.equals(other.session)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[session=" + session + "; data=" + data + "]");
  }
}