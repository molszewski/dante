/*
 * Created on 2006-08-31
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import net.java.dante.algorithms.BaseAlgorithmImpl;
import net.java.dante.algorithms.message.AlgorithmExceptionMessage;
import net.java.dante.algorithms.message.CommandsReadyMessage;
import net.java.dante.darknet.client.ClientNotConnectedException;
import net.java.dante.darknet.client.NetworkClient;
import net.java.dante.darknet.client.NetworkClientFactory;
import net.java.dante.darknet.config.ClientConfig;
import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.session.Session;
import net.java.dante.darknet.session.SessionAdapter;
import net.java.dante.gui.common.Dbg;
import net.java.dante.gui.common.Utils;
import net.java.dante.gui.common.messages.net.client.update.InitDataMessage;
import net.java.dante.gui.common.messages.receiver.AlgorithmSelectedMessage;
import net.java.dante.gui.common.messages.receiver.ClientConnectionRequestMessage;
import net.java.dante.gui.common.messages.receiver.ClientDisconnectionMessage;
import net.java.dante.gui.common.messages.receiver.ClientDisconnectionRequestMessage;
import net.java.dante.gui.common.messages.receiver.ConnectionConfigurationChangedMessage;
import net.java.dante.gui.common.messages.receiver.DarkNetExceptionMessage;
import net.java.dante.gui.common.messages.receiver.FinalizeMessage;
import net.java.dante.gui.common.messages.receiver.NetworkReceiverMessage;
import net.java.dante.receiver.MessageProcessingExceptionMessage;
import net.java.dante.receiver.MessagesProcessor;
import net.java.dante.receiver.Receiver;
import net.java.dante.receiver.ReceiverMessage;
import net.java.dante.receiver.RunnableReceiver;
import net.java.dante.receiver.time.TimerMessage;


/**
 * Class representing client's logic with automatic processing of
 * client's connection and responsible for displaying simulation's graphical
 * output.
 *
 * @author M.Olszewski
 */
public class ClientLogic implements MessagesProcessor
{
  /** Client's frame title prefix. */
  private static final String TITLE_PREFIX = "BattleClient v1.0";
  /** Client's frame title suffix - if client is behaving as creator. */
  private static final String TITLE_CREATOR_SUFFIX = " - Creator";
  /** Client's frame title suffix - if client is behaving as joiner. */
  private static final String TITLE_JOINER_SUFFIX = " - Joiner";

  /** Indicates whether this client creates or joins games. */
  boolean creatorBehaviour = true;
  /** Messages receiver. */
  final Receiver receiver;

  /** Client's configuration. */
  ClientConfig clientConfig;
  /** Network client. */
  NetworkClient client = NetworkClientFactory.getInstance().initDefaultClient();
  /** Client's frame. */
  final ClientFrame clientFrame;

  /** Client's state machine. */
  final ClientStateMachine stateMachine = new ClientStateMachine();
  /** Current network session. */
  Session session;
  /** Selected algorithm. */
  BaseAlgorithmImpl algorithm;
  /** Automatic connection manager. */
  AutoConnectionManager autoMan;



  /**
   * Creates instance of {@link ClientLogic} class.
   *
   * @param messagesReceiver messages receiver.
   * @param clientConfiguration client's initial configuration.
   */
  public ClientLogic(Receiver messagesReceiver, ClientConfig clientConfiguration)
  {
    if (messagesReceiver == null)
    {
      throw new NullPointerException("Specified messagesReceiver is null!");
    }
    if (clientConfiguration == null)
    {
      throw new NullPointerException("Specified clientConfiguration is null!");
    }

    receiver     = messagesReceiver;
    clientConfig = clientConfiguration;

    clientFrame = new ClientFrame(receiver);
    clientFrame.initialize();
    clientFrame.setConfigConnectionDialogValues(clientConfig);

    if (creatorBehaviour)
    {
      clientFrame.setTitle(TITLE_PREFIX + TITLE_CREATOR_SUFFIX);
    }
    else
    {
      clientFrame.setTitle(TITLE_PREFIX + TITLE_JOINER_SUFFIX);
    }

    clientFrame.addWindowListener(new WindowAdapter() {
      /**
       * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
       */
      public void windowClosing(WindowEvent e)
      {
        receiver.postMessage(new FinalizeMessage());
      }
    });
    clientFrame.pack();
    clientFrame.setVisible(true);
  }

  /**
   * @see net.java.dante.receiver.MessagesProcessor#processMessage(net.java.dante.receiver.ReceiverMessage)
   */
  public void processMessage(ReceiverMessage message)
  {
    if (message instanceof NetworkReceiverMessage)
    {
      stateMachine.processNetworkMessage(((NetworkReceiverMessage)message).getMessage());
    }
    else if (message instanceof TimerMessage)
    {
      stateMachine.processTimerMessage((TimerMessage)message);
    }
    else if (message instanceof CommandsReadyMessage)
    {
      stateMachine.processCommandsReadyMessage((CommandsReadyMessage)message);
    }
    else if (message instanceof AlgorithmSelectedMessage)
    {
      stateMachine.processAlgorithmSelectedMessage((AlgorithmSelectedMessage)message);
    }
    else if (message instanceof ConnectionConfigurationChangedMessage)
    {
      stateMachine.processConnectionConfigurationChangedMessage((ConnectionConfigurationChangedMessage)message);
    }
    else if (message instanceof ClientConnectionRequestMessage)
    {
      stateMachine.processConnectionRequestMessage((ClientConnectionRequestMessage)message);
    }
    else if (message instanceof ClientDisconnectionRequestMessage)
    {
      stateMachine.processDisconnectionRequestMessage((ClientDisconnectionRequestMessage)message);
    }
    else if (message instanceof ClientDisconnectionMessage)
    {
      stateMachine.disconnected();
    }
    else if (message instanceof DarkNetExceptionMessage)
    {
      processDarkNetExceptionMessage((DarkNetExceptionMessage)message);
    }
    else if (message instanceof AlgorithmExceptionMessage)
    {
      processAlgorithmExceptionMessage((AlgorithmExceptionMessage)message);
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
   * Processes {@link DarkNetExceptionMessage} message.
   *
   * @param message message to process.
   */
  private void processDarkNetExceptionMessage(DarkNetExceptionMessage message)
  {
    clientFrame.appendLogLine("Exception was caught in network session: " + message.getSource() + "! Printing stack trace...");
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
    JOptionPane.showMessageDialog(clientFrame,
                                  "Exception caught in messages processing thread. Exception message:\n" +
                                  cause.getMessage() +
                                  "\nFull stack trace should be available in 'error.log' file placed in current working directory.\nPress OK to exit.",
                                  "BattleClient internal error",
                                  JOptionPane.ERROR_MESSAGE);
    Utils.logError(cause);
    performDisposal();
  }

  /**
   * Processes {@link AlgorithmExceptionMessage} message.
   *
   * @param message message to process.
   */
  private void processAlgorithmExceptionMessage(AlgorithmExceptionMessage message)
  {
    clientFrame.appendLogLine("Exception was caught while running algorithm! Printing stack trace...");
    logStackTrace(message.getCause());
  }

  /**
   * Default disconnection procedure.
   */
  void defaultDisconnectRequest()
  {
    clientFrame.appendLogLine("Disconnecting from the server with address: " + client.getServerAddress() + "...");

    client.disconnect(false);

    clientFrame.appendLogLine("Disconnected from the server with address: " + client.getConfiguration().getHostAddress());
    clientFrame.disconnected();
  }

  /**
   * Default procedure applied when server disconnects from this client.
   */
  void defaultDisconnection()
  {
    client.disconnect(false);

    clientFrame.appendLogLine("Connection with server with address: " + client.getConfiguration().getHostAddress() + " was terminated!");
    clientFrame.disconnected();
  }

  /**
   * Performs disposal.
   */
  private void performDisposal()
  {
    client.disconnect(false);

    stateMachine.dispose();

    clientFrame.dispose();

    receiver.dispose(false);
  }

  /**
   * Logs full stack trace in GUI.
   *
   * @param throwable - stack trace's provider.
   */
  private void logStackTrace(Throwable throwable)
  {
    clientFrame.appendLogLine(throwable.getMessage());
    StackTraceElement[] elements = throwable.getStackTrace();
    for (int i = 0; i < elements.length; i++)
    {
      clientFrame.appendLogLine(elements[i].toString());
    }
  }

  /**
   * Updates current connection configuration using the specified message.
   *
   * @param connectionConfig the specified message with new connection
   *        configuration.
   */
  void updateConnectionConfig(ConnectionConfigurationChangedMessage connectionConfig)
  {
    clientConfig     = connectionConfig.getConfig();
    creatorBehaviour = connectionConfig.isCreator();

    clientFrame.setTitle(TITLE_PREFIX + (creatorBehaviour ? (TITLE_CREATOR_SUFFIX) : (TITLE_JOINER_SUFFIX)));
  }


  /**
   * Main client's state machine.
   *
   * @author M.Olszewski
   */
  class ClientStateMachine
  {
    /** Initial state. */
    ClientState initialState = new InitialState();
    /** Algorithm selected state. */
    ClientState algorithmState = new AlgorithmSelectedState();
    /** Connection not initialized state. */
    ClientState connectionNotInitState = new ConnectionNotInitState();
    /** Connection initialized state. */
    ClientState connectionInitState = new ConnectionInitState();

    /** Current state. */
    ClientState currentState = initialState;


    /**
     * Sets 'initial' state.
     */
    void setInitialState()
    {
      currentState = initialState;
    }

    /**
     * Sets 'algorithm selected' state.
     */
    void setAlgorithmSelectedState()
    {
      currentState = algorithmState;
    }

    /**
     * Sets 'connection not initialized' state.
     */
    void setConnectionNotInitState()
    {
      currentState = connectionNotInitState;
    }

    /**
     * Sets 'connection initialized' state.
     */
    void setConnectionInitState()
    {
      currentState = connectionInitState;
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
     * @param message the specified timer message.
     */
    void processTimerMessage(TimerMessage message)
    {
      currentState.processTimerMessage(message);
    }

    /**
     * Processes the specified connection's configuration change message.
     *
     * @param connectionConfig the specified connection's configuration
     *        change message.
     */
    void processConnectionConfigurationChangedMessage(ConnectionConfigurationChangedMessage connectionConfig)
    {
      currentState.processConnectionConfigurationChangedMessage(connectionConfig);
    }

    /**
     * Processes the specified connection request message.
     *
     * @param connectionRequest the specified connection request message.
     */
    void processConnectionRequestMessage(ClientConnectionRequestMessage connectionRequest)
    {
      currentState.processConnectionRequestMessage(connectionRequest);
    }

    /**
     * Processes the specified disconnection request message.
     *
     * @param disconnectionRequest the specified disconnection request message.
     */
    void processDisconnectionRequestMessage(ClientDisconnectionRequestMessage disconnectionRequest)
    {
      currentState.processDisconnectionRequestMessage(disconnectionRequest);
    }

    /**
     * Processes the specified message containing notification about selected
     * algorithm.
     *
     * @param algorithmMessage the specified message containing
     *        notification about selected algorithm.
     */
    void processAlgorithmSelectedMessage(AlgorithmSelectedMessage algorithmMessage)
    {
      currentState.processAlgorithmSelectedMessage(algorithmMessage);
    }

    /**
     * Method invoked when server was disconnected from the client.
     */
    void disconnected()
    {
      currentState.disconnected();
    }

    /**
     * Performs disposal of resources specific for the state.
     */
    void dispose()
    {
      currentState.dispose();
    }
  }

  /**
   * Base class for all main client states.
   *
   * @author M.Olszewski
   */
  abstract class ClientState
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
     * @param message the specified timer message.
     */
    void processTimerMessage(TimerMessage message)
    {
      // Intentionally left empty.
    }

    /**
     * Processes the specified connection's configuration change message.
     *
     * @param connectionConfig the specified connection's configuration
     *        change message.
     */
    void processConnectionConfigurationChangedMessage(ConnectionConfigurationChangedMessage connectionConfig)
    {
      // Intentionally left empty.
    }

    /**
     * Processes the specified connection request message.
     *
     * @param connectionRequest the specified connection request message.
     */
    void processConnectionRequestMessage(ClientConnectionRequestMessage connectionRequest)
    {
      // Intentionally left empty.
    }

    /**
     * Processes the specified disconnection request message.
     *
     * @param disconnectionRequest the specified disconnection request message.
     */
    void processDisconnectionRequestMessage(ClientDisconnectionRequestMessage disconnectionRequest)
    {
      // Intentionally left empty.
    }

    /**
     * Processes the specified message containing notification about selected
     * algorithm.
     *
     * @param algorithmMessage the specified message containing
     *        notification about selected algorithm.
     */
    void processAlgorithmSelectedMessage(AlgorithmSelectedMessage algorithmMessage)
    {
      // Intentionally left empty.
    }

    /**
     * Method invoked when server was disconnected from the client.
     */
    void disconnected()
    {
      // Intentionally left empty.
    }

    /**
     * Performs disposal of resources specific for the state.
     */
    void dispose()
    {
      // Intentionally left empty.
    }
  }

  /**
   * 'Initial' state. No algorithm selected, not connected.
   *
   * @author M.Olszewski
   */
  class InitialState extends ClientState
  {
    /**
     * @see net.java.dante.gui.client.ClientLogic.ClientState#processAlgorithmSelectedMessage(net.java.dante.gui.common.messages.receiver.AlgorithmSelectedMessage)
     */
    @Override
    void processAlgorithmSelectedMessage(AlgorithmSelectedMessage algorithmMessage)
    {
      algorithm = algorithmMessage.getAlgorithm();

      clientFrame.appendLogLine("Loaded algorithm: " + algorithm);
      clientFrame.algorithmLoaded();

      stateMachine.setAlgorithmSelectedState();
    }

    /**
     * @see net.java.dante.gui.client.ClientLogic.ClientState#processConnectionConfigurationChangedMessage(net.java.dante.gui.common.messages.receiver.ConnectionConfigurationChangedMessage)
     */
    @Override
    void processConnectionConfigurationChangedMessage(ConnectionConfigurationChangedMessage connectionConfig)
    {
      updateConnectionConfig(connectionConfig);
    }
  }

  /**
   * 'Algorithm selected' state. Algorithm was selected, but client is
   * not connected to server.
   *
   * @author M.Olszewski
   */
  class AlgorithmSelectedState extends ClientState
  {
    /**
     * @see net.java.dante.gui.client.ClientLogic.ClientState#processAlgorithmSelectedMessage(net.java.dante.gui.common.messages.receiver.AlgorithmSelectedMessage)
     */
    @Override
    void processAlgorithmSelectedMessage(AlgorithmSelectedMessage algorithmMessage)
    {
      algorithm = algorithmMessage.getAlgorithm();

      clientFrame.appendLogLine("Loaded algorithm: " + algorithm);
      clientFrame.algorithmLoaded();
    }

    /**
     * @see net.java.dante.gui.client.ClientLogic.ClientState#processConnectionConfigurationChangedMessage(net.java.dante.gui.common.messages.receiver.ConnectionConfigurationChangedMessage)
     */
    @Override
    void processConnectionConfigurationChangedMessage(ConnectionConfigurationChangedMessage connectionConfig)
    {
      updateConnectionConfig(connectionConfig);
    }

    /**
     * @see net.java.dante.gui.client.ClientLogic.ClientState#processConnectionRequestMessage(net.java.dante.gui.common.messages.receiver.ClientConnectionRequestMessage)
     */
    @Override
    void processConnectionRequestMessage(ClientConnectionRequestMessage connectionRequest)
    {
      clientFrame.connectionStarted();

      // Create client, register messages
      client = NetworkClientFactory.getInstance().initCustomClient(clientConfig);
      Utils.registerAllMessages(client.getRegister());

      clientFrame.appendLogLine("Connecting to server with address: " + client.getConfiguration().getHostAddress() + "...");

      try
      {
        // Perform connection
        session = client.connect(new ClientSessionHandler(receiver));
        if (session != null)
        {
          clientFrame.appendLogLine("Connected to server with address: " + client.getServerAddress());
          clientFrame.connected();

          stateMachine.setConnectionNotInitState();
        }
        else
        {
          clientFrame.appendLogLine("Connection to server with address: " + client.getConfiguration().getHostAddress() + " has failed! Server does not response.");
          clientFrame.connectionFailed();
        }
      }
      catch (ClientNotConnectedException e)
      {
        clientFrame.appendLogLine("Connection to server with address: " + client.getConfiguration().getHostAddress() + " has failed! Server does not response.");
        clientFrame.connectionFailed();
      }
    }
  }

  /**
   * 'Connection not initialized' state. Algorithm was selected, client is
   * connected to server, but it is waiting for initialization data.
   *
   * @author M.Olszewski
   */
  class ConnectionNotInitState extends ClientState
  {
    /**
     * @see net.java.dante.gui.client.ClientLogic.ClientState#processNetworkMessage(net.java.dante.darknet.messaging.NetworkMessage)
     */
    @Override
    void processNetworkMessage(NetworkMessage message)
    {
      if (message instanceof InitDataMessage)
      {
        autoMan = new AutoConnectionManager(creatorBehaviour, session,
                                            (InitDataMessage)message,
                                            algorithm,
                                            receiver, clientFrame);
        stateMachine.setConnectionInitState();
      }
    }

    /**
     * @see net.java.dante.gui.client.ClientLogic.ClientState#processDisconnectionRequestMessage(net.java.dante.gui.common.messages.receiver.ClientDisconnectionRequestMessage)
     */
    @Override
    void processDisconnectionRequestMessage(ClientDisconnectionRequestMessage disconnectionRequest)
    {
      defaultDisconnectRequest();
      stateMachine.setAlgorithmSelectedState();
    }

    /**
     * @see net.java.dante.gui.client.ClientLogic.ClientState#disconnected()
     */
    @Override
    void disconnected()
    {
      stateMachine.setAlgorithmSelectedState();
    }
  }

  /**
   * 'Connection initialized' state. Algorithm was selected, client is
   * connected to server and initialization data was received and processed.
   *
   * @author M.Olszewski
   */
  class ConnectionInitState extends ConnectionNotInitState
  {
    /**
     * @see net.java.dante.gui.client.ClientLogic.ConnectionNotInitState#processNetworkMessage(net.java.dante.darknet.messaging.NetworkMessage)
     */
    @Override
    void processNetworkMessage(NetworkMessage message)
    {
      autoMan.processNetworkMessage(message);
    }

    /**
     * @see net.java.dante.gui.client.ClientLogic.ClientState#processCommandsReadyMessage(net.java.dante.algorithms.message.CommandsReadyMessage)
     */
    void processCommandsReadyMessage(CommandsReadyMessage message)
    {
      autoMan.processCommandsReadyMessage(message);
    }

    /**
     * @see net.java.dante.gui.client.ClientLogic.ClientState#processTimerMessage(net.java.dante.receiver.time.TimerMessage)
     */
    @Override
    void processTimerMessage(TimerMessage message)
    {
      autoMan.processTimeMessage(message);
    }

    /**
     * @see net.java.dante.gui.client.ClientLogic.ConnectionNotInitState#processDisconnectionRequestMessage(net.java.dante.gui.common.messages.receiver.ClientDisconnectionRequestMessage)
     */
    @Override
    void processDisconnectionRequestMessage(ClientDisconnectionRequestMessage disconnectionRequest)
    {
      defaultDisconnectRequest();
      disposeAutomaticManager();
      stateMachine.setAlgorithmSelectedState();
    }

    /**
     * @see net.java.dante.gui.client.ClientLogic.ClientState#disconnected()
     */
    @Override
    void disconnected()
    {
      defaultDisconnection();
      disposeAutomaticManager();
      stateMachine.setAlgorithmSelectedState();
    }

    /**
     * @see net.java.dante.gui.client.ClientLogic.ConnectionNotInitState#dispose()
     */
    @Override
    void dispose()
    {
      disposeAutomaticManager();
      stateMachine.setInitialState();
    }

    private void disposeAutomaticManager()
    {
      autoMan.dispose();
      autoMan = null;
    }
  }


  /**
   * Starts client.
   *
   * @param args - first argument can contain optional path to file with
   *        client's configuration.
   */
  public static void main(String[] args)
  {
    ClientConfig clientConfig = ClientConfig.defaultConfig();
    if (args.length > 0)
    {
       clientConfig = ClientConfig.loadConfig(args[0], true);
    }

    Receiver receiver = new RunnableReceiver();
    ClientLogic logic = new ClientLogic(receiver, clientConfig);
    receiver.start(logic);
  }
}

/**
 * Client's session handler.
 *
 * @author M.Olszewski
 */
final class ClientSessionHandler extends SessionAdapter
{
  /** Messages receiver. */
  private Receiver receiver;

  /**
   * Creates instance of {@link ClientSessionHandler} class.
   *
   * @param messagesReceiver - messages receiver.
   */
  public ClientSessionHandler(Receiver messagesReceiver)
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
  public void sessionClosed(final Session session)
  {
    Utils.forkMessage(receiver, new ClientDisconnectionMessage(session));
  }
}