/*
 * Created on 2006-08-21
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.server;


import net.java.dante.gui.common.games.Game;
import net.java.dante.gui.common.games.GameData;
import net.java.dante.gui.common.games.GameInitData;
import net.java.dante.gui.common.games.GameInputData;
import net.java.dante.gui.common.messages.receiver.GameInitializationFailed;
import net.java.dante.gui.common.messages.receiver.SimulationInputMessage;
import net.java.dante.gui.common.messages.receiver.SimulationOutputMessage;
import net.java.dante.receiver.Receiver;
import net.java.dante.sim.Simulation;
import net.java.dante.sim.SimulationFactory;
import net.java.dante.sim.SimulationType;
import net.java.dante.sim.data.ServerSimulationInitData;
import net.java.dante.sim.data.common.FileDataSource;
import net.java.dante.sim.io.GroupAbandonSimulationData;
import net.java.dante.sim.io.InputData;
import net.java.dante.sim.io.OutputData;
import net.java.dante.sim.io.SimulationOutput;

/**
 * Implementation of {@link Game} interface.
 *
 * @author M.Olszewski
 */
class GameImpl implements Game
{
  /** Base for name of initialization thread. */
  private final static String INIT_THREAD_NAME_BASE = "GameInitializer#";

  /** Game's data. */
  GameDataImpl data;
  /** Simulation. */
  Simulation simulation;
  /** Game's state machine. */
  StateMachine stateMachine = new StateMachine();


  /**
   * Creates instance of {@link GameImpl} class.
   *
   * @param gameData - this game's data.
   */
  GameImpl(GameDataImpl gameData)
  {
    if (gameData == null)
    {
      throw new NullPointerException("Specified gameData is null!");
    }

    data = gameData;
  }


  /**
   * Creates name of game initialization thread using
   * the specified game's identifier.
   *
   * @param gameId - the specified game's identifier.
   *
   * @return Returns created name of game initialization thread.
   */
  static String createInitThreadName(Integer gameId)
  {
    return (INIT_THREAD_NAME_BASE + gameId);
  }

  /**
   * @see net.java.dante.gui.common.games.Game#getData()
   */
  public GameData getData()
  {
    return data;
  }

  /**
   * @see net.java.dante.gui.common.games.Game#create()
   */
  public void create()
  {
    stateMachine.create();
  }

  /**
   * @see net.java.dante.gui.common.games.Game#ready()
   */
  public void ready()
  {
    stateMachine.ready();
  }

  /**
   * @see net.java.dante.gui.common.games.Game#notReady()
   */
  public void notReady()
  {
    stateMachine.notReady();
  }

  /**
   * @see net.java.dante.gui.common.games.Game#init(net.java.dante.gui.common.games.GameInitData)
   */
  public void init(GameInitData initData)
  {
    stateMachine.init(initData);
  }

  /**
   * @see net.java.dante.gui.common.games.Game#start()
   */
  public void start()
  {
    stateMachine.start();
  }

  /**
   * @see net.java.dante.gui.common.games.Game#pause()
   */
  public void pause()
  {
    stateMachine.pause();
  }

  /**
   * @see net.java.dante.gui.common.games.Game#resume()
   */
  public void resume()
  {
    stateMachine.resume();
  }

  /**
   * @see net.java.dante.gui.common.games.Game#finish()
   */
  public void finish()
  {
    stateMachine.finish();
  }

  /**
   * @see net.java.dante.gui.common.games.Game#acceptInput(net.java.dante.gui.common.games.GameInputData)
   */
  public void acceptInput(GameInputData inputData)
  {
    stateMachine.acceptInput(inputData);
  }


  /**
   * Gets {@link GameDataImpl} object stored by this {@link GameImpl} instance.
   *
   * @return Returns {@link GameDataImpl} object stored by this
   *         {@link GameImpl} instance.
   */
  GameDataImpl getDataImpl()
  {
    return data;
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

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof GameImpl))
    {
      final GameImpl other = (GameImpl) object;
      equal = (data.equals(other.data));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[data=" + data + "; simulation=" + simulation +
        "; stateMachine=" + stateMachine + "]");
  }


  /**
   * State machine for this game.
   *
   * @author M.Olszewski
   */
  final class StateMachine
  {
    /** Initial state. */
    private final State initialState = new InitialState();
    /** Created and not ready to start state. */
    private final State notReadyState = new NotReadyState();
    /** Ready to start state. */
    private final State readyState = new ReadyState();
    /** Ready to start state. */
    private final State initializingState = new InitializingState();
    /** Running state. */
    private final State runningState = new RunningState();
    /** Paused state. */
    private final State pausedState  = new PausedState();
    /** Current state. */
    private State currentState = initialState;


    /**
     * Creates instance of {@link StateMachine} class.
     */
    StateMachine()
    {
      // Intentionally left empty.
    }

    /**
     * Sets current state to 'initial'.
     */
    void setInitialState()
    {
      currentState = initialState;
    }

    /**
     * Sets current state to 'not ready'.
     */
    void setNotReadyState()
    {
      currentState = notReadyState;
    }

    /**
     * Sets current state to 'ready'.
     */
    void setReadyState()
    {
      currentState = readyState;
    }

    /**
     * Sets current state to 'initializing'.
     */
    void setInitializingState()
    {
      currentState = initializingState;
    }

    /**
     * Sets current state to 'running'.
     */
    void setRunningState()
    {
      currentState = runningState;
    }

    /**
     * Sets current state to 'paused'.
     */
    void setPausedState()
    {
      currentState = pausedState;
    }

    /**
     * @see net.java.dante.gui.common.games.Game#create()
     */
    void create()
    {
      currentState.create();
    }

    /**
     * @see net.java.dante.gui.common.games.Game#ready()
     */
    public void ready()
    {
      currentState.ready();
    }

    /**
     * @see net.java.dante.gui.common.games.Game#notReady()
     */
    public void notReady()
    {
      currentState.notReady();
    }

    /**
     * @see net.java.dante.gui.common.games.Game#init(net.java.dante.gui.common.games.GameInitData)
     *
     * @param initData - initialization data for this game.
     */
    void init(GameInitData initData)
    {
      currentState.init(initData);
    }

    /**
     * @see net.java.dante.gui.common.games.Game#start()
     */
    void start()
    {
      currentState.start();
    }

    /**
     * @see net.java.dante.gui.common.games.Game#pause()
     */
    void pause()
    {
      currentState.pause();
    }

    /**
     * @see net.java.dante.gui.common.games.Game#resume()
     */
    void resume()
    {
      currentState.resume();
    }

    /**
     * @see net.java.dante.gui.common.games.Game#finish()
     */
    void finish()
    {
      currentState.finish();
    }

    /**
     * @see net.java.dante.gui.common.games.Game#acceptInput(net.java.dante.gui.common.games.GameInputData)
     *
     * @param inputData - input to accept by this game.
     */
    void acceptInput(GameInputData inputData)
    {
      currentState.acceptInput(inputData);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      return (getClass() + "[currentState=" + currentState + "]");
    }
  }


  /**
   * Base class for all game's states.
   *
   * @author M.Olszewski
   */
  abstract class State
  {
    /**
     * This method should be implemented in state that performs
     * actions when game is created.
     */
    void create()
    {
      // Intentionally left empty.
    }

    /**
     * This method should be implemented in state that performs
     * actions when game is marked as ready to start
     */
    void ready()
    {
      // Intentionally left empty.
    }

    /**
     * This method should be implemented in state that performs
     * actions when game is marked as not ready to start.
     */
    void notReady()
    {
      // Intentionally left empty.
    }

    /**
     * This method should be implemented in state that performs
     * actions when game is started.
     *
     * @param initData - initialization data for this game.
     */
    void init(GameInitData initData)
    {
      // Intentionally left empty.
    }

    /**
     * This method should be implemented in state that performs
     * actions when game is initialized and should be started.
     */
    void start()
    {
      // Intentionally left empty.
    }

    /**
     * This method should be implemented in state that performs
     * actions when game is paused.
     */
    void pause()
    {
      // Intentionally left empty.
    }

    /**
     * This method should be implemented in state that performs
     * actions when game is resumed.
     */
    void resume()
    {
      // Intentionally left empty.
    }

    /**
     * This method should be implemented in state that performs
     * actions when game is finished.
     */
    void finish()
    {
      // Intentionally left empty.
    }

    /**
     * This method should be implemented in state that performs
     * actions when the specified input should be accepted.
     *
     * @param inputData - input to accept by this game.
     */
    void acceptInput(GameInputData inputData)
    {
      // Intentionally left empty.
    }

    /**
     * Default procedure invoked when {@link #finish()} method is called.
     */
    final void defaultFinish()
    {
      data.gameFinished();

      stateMachine.setInitialState();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      return (getClass().toString());
    }
  }

  /**
   * Initial state of game.
   *
   * @author M.Olszewski
   */
  final class InitialState extends State
  {
    /**
     * Creates game.
     *
     * @see net.java.dante.gui.server.GameImpl.State#create()
     */
    void create()
    {
      simulation = SimulationFactory.getInstance().createSimulation(SimulationType.SERVER);
      data.gameCreated();
      stateMachine.setNotReadyState();
    }
  }

  /**
   * Game was created but it is not ready to start.
   *
   * @author M.Olszewski
   */
  final class NotReadyState extends State
  {
    /**
     * @see net.java.dante.gui.server.GameImpl.State#ready()
     */
    @Override
    void ready()
    {
      data.gameReady();
      stateMachine.setReadyState();
    }

    /**
     * Finishes game.
     *
     * @see net.java.dante.gui.server.GameImpl.State#finish()
     */
    @Override
    void finish()
    {
      defaultFinish();
    }
  }

  /**
   * Game is ready to be started.
   *
   * @author M.Olszewski
   */
  final class ReadyState extends State
  {
    /**
     * @see net.java.dante.gui.server.GameImpl.State#notReady()
     */
    @Override
    void notReady()
    {
      data.gameNotReady();
      stateMachine.setNotReadyState();
    }

    /**
     * Initializes and starts this game.
     *
     * @see net.java.dante.gui.server.GameImpl.State#init(net.java.dante.gui.common.games.GameInitData)
     */
    @Override
    void init(GameInitData initData)
    {
      if (!(initData instanceof SimulationGameInitData))
      {
        throw new IllegalArgumentException("Invalid argument initData - not an instance of SimulationGameInitData class!");
      }

      final SimulationGameInitData gameInitData = (SimulationGameInitData) initData;
      final Receiver receiver = gameInitData.getReceiver();
      final Integer gameId = Integer.valueOf(data.getGameId());

      // Initialize simulation in other thread - until it generates proper
      // initialization messages game will stay in 'initializing' state
      new Thread(createInitThreadName(gameId)) {
        /**
         * @see java.lang.Thread#run()
         */
        @Override
        public void run()
        {
          try
          {
            simulation.init(new InternalSimulationOutput(gameId, receiver),
                            new FileDataSource(gameInitData.getDefinitionFile()),
                            new ServerSimulationInitData(data.getClientsCount(), gameInitData.getParent()));
          }
          catch (Throwable e)
          {
            receiver.postMessage(new GameInitializationFailed(gameId, e));
          }
        }
      }.start();

      data.gameInitializing();
      stateMachine.setInitializingState();
    }

    /**
     * Finishes game.
     *
     * @see net.java.dante.gui.server.GameImpl.State#finish()
     */
    @Override
    void finish()
    {
      simulation.dispose();

      defaultFinish();
    }
  }

  /**
   * Game is being initialized.
   *
   * @author M.Olszewski
   */
  final class InitializingState extends State
  {
    /**
     * @see net.java.dante.gui.server.GameImpl.State#start()
     */
    @Override
    void start()
    {
      simulation.start();
      data.gameStarted();

      stateMachine.setRunningState();
    }

    /**
     * @see net.java.dante.gui.server.GameImpl.State#finish()
     */
    @Override
    void finish()
    {
      simulation.dispose();

      defaultFinish();
    }

    /**
     * @see net.java.dante.gui.server.GameImpl.State#acceptInput(net.java.dante.gui.common.games.GameInputData)
     */
    @Override
    void acceptInput(GameInputData inputData)
    {
      if (inputData == null)
      {
        throw new NullPointerException("Specified inputData is null!");
      }
      if (!(inputData instanceof SimulationInputMessage))
      {
        throw new IllegalArgumentException("Invalid argument initData - not an instance of SimulationInputMessage class!");
      }

      // Only notifications about group abandon events are accepted
      InputData simInputData = ((SimulationInputMessage)inputData).getInputData();

      if (simInputData instanceof GroupAbandonSimulationData)
      {
        simulation.getInput().dataReceived(simInputData);
      }
    }
  }

  /**
   * Game is running.
   *
   * @author M.Olszewski
   */
  final class RunningState extends State
  {
    /**
     * @see net.java.dante.gui.server.GameImpl.State#pause()
     */
    void pause()
    {
      simulation.pause();
      stateMachine.setPausedState();
    }

    /**
     * Finishes game.
     *
     * @see net.java.dante.gui.server.GameImpl.State#finish()
     */
    @Override
    void finish()
    {
      simulation.dispose();

      defaultFinish();
    }

    /**
     * @see net.java.dante.gui.server.GameImpl.State#acceptInput(net.java.dante.gui.common.games.GameInputData)
     */
    @Override
    void acceptInput(GameInputData inputData)
    {
      if (inputData == null)
      {
        throw new NullPointerException("Specified inputData is null!");
      }
      if (!(inputData instanceof SimulationInputMessage))
      {
        throw new IllegalArgumentException("Invalid argument initData - not an instance of SimulationInputMessage class!");
      }

      simulation.getInput().dataReceived(((SimulationInputMessage)inputData).getInputData());
    }
  }

  /**
   * Game is paused.
   *
   * @author M.Olszewski
   */
  final class PausedState extends State
  {
    /**
     * @see net.java.dante.gui.server.GameImpl.State#pause()
     */
    @Override
    void resume()
    {
      simulation.start();
      stateMachine.setRunningState();
    }

    /**
     * Finishes game.
     *
     * @see net.java.dante.gui.server.GameImpl.State#finish()
     */
    @Override
    void finish()
    {
      simulation.dispose();

      defaultFinish();
    }
  }


  /**
   * Simulation output posting {@link SimulationOutputMessage}
   * messages to the specified receiver.
   *
   * @author M.Olszewski
   */
  final class InternalSimulationOutput implements SimulationOutput
  {
    /** Running game's identifier. */
    private Integer gameId;
    /** Receiver where messages will be posted. */
    private Receiver receiver;


    /**
     * Creates instance of {@link InternalSimulationOutput} class with
     * the specified parameters.
     *
     * @param runningGameId - this game's identifier.
     * @param messagesReceiver - receiver where messages will be posted.
     */
    InternalSimulationOutput(Integer runningGameId, Receiver messagesReceiver)
    {
      if (runningGameId == null)
      {
        throw new NullPointerException("Specified runningGameId is null!");
      }
      if (messagesReceiver == null)
      {
        throw new NullPointerException("Specified messagesReceiver is null!");
      }

      gameId = runningGameId;
      receiver = messagesReceiver;
    }


    /**
     * @see net.java.dante.sim.io.SimulationOutput#dataReady(net.java.dante.sim.io.OutputData)
     */
    public void dataReady(OutputData outputData)
    {
      receiver.postMessage(new SimulationOutputMessage(gameId, outputData));
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      return (getClass() + "[gameId=" + gameId + "; receiver=" + receiver + "]");
    }
  }
}