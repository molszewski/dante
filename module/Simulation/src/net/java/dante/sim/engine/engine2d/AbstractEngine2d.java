/*
 * Created on 2006-08-18
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.engine2d;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import net.java.dante.sim.common.Dbg;
import net.java.dante.sim.data.GlobalData;
import net.java.dante.sim.data.SimulationData;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.engine.Engine;
import net.java.dante.sim.engine.graphics.java2d.Java2dContext;
import net.java.dante.sim.engine.time.TimeCounter;
import net.java.dante.sim.io.SimulationInput;
import net.java.dante.sim.io.SimulationOutput;


/**
 * Base class for all two dimensional engines that are running in separate
 * thread.
 * This class simplifies life cycle of engine: initialization, running
 * engine in 'engine loop' (by calling {@link #engineUpdate()} method),
 * pausing and resuming paused engine and engine disposal.<p>
 * It is assumed that each engine:
 * <ol>
 * <li>can perform only one operation at once even if methods are called
 *     from separate threads at the same time,
 * <li>must be initialized before performing any other operation,
 * <li>access to each resource is synchronized,
 * <li>can be paused externally - but what 'pause' means is specific for engine,
 * <li>can be disposed externally - which means that 'engine loop' is stopped,
 *     engine thread and all resources specific for subclass are disposed,
 * <li>can be stopped by subclass - e.g. if stop condition is met, subclass
 *     can stop engine from running.
 * </ol>
 *
 * Each subclass representing engine must:
 * <ol>
 * <li>Perform initialization procedure specific for subclass in
 *     {@link #performInitialization(SimulationInput, SimulationData, Engine2dInitData)}.
 * <li>Perform disposal procedure specific for subclass in
 *     {@link #performDisposal()}.
 * <li>Execute each update of 'engine loop' in {@link #engineUpdate()}
 *     method.
 * </ol>
 *
 * @author M.Olszewski
 */
public abstract class AbstractEngine2d implements Engine
{
  /** Determines whether engine was initialized. */
  private boolean initialized;
  /** Determines whether engine is paused. */
  private boolean paused;
  /** Determines whether simulation engine loop is running. */
  private AtomicBoolean loopRunning = new AtomicBoolean(false);
  /** Engine thread. */
  private Thread engineThread;

  /** Output for this engine. */
  protected SimulationOutput output;
  /** Drawing context. */
  protected Java2dContext context;
  /** Sprites repository. */
  protected SpritesRepository spritesRepository;

  /** Interval between two updates. */
  protected long updatesInterval;
  /** Time counter counting battle duration. */
  protected TimeCounter battleDurationCounter;

  /** Number of running engines. */
  private static AtomicInteger runningEnginesCount = new AtomicInteger(0);


  /**
   * Constructor of {@link AbstractEngine2d} class.
   */
  protected AbstractEngine2d()
  {
    // Intentionally left empty.
  }


  /**
   * @see net.java.dante.sim.engine.Engine#init(net.java.dante.sim.io.SimulationInput, net.java.dante.sim.io.SimulationOutput, net.java.dante.sim.data.SimulationData, net.java.dante.sim.data.common.InitData)
   */
  public final void init(SimulationInput simInput, SimulationOutput simOutput,
      SimulationData simData, InitData initData)
  {
    if (simInput == null)
    {
      throw new NullPointerException("Specified simInput is null!");
    }
    if (simOutput == null)
    {
      throw new NullPointerException("Specified simOutput is null!");
    }
    if (simData == null)
    {
      throw new NullPointerException("Specified simData is null!");
    }
    if (initData == null)
    {
      throw new NullPointerException("Specified initData is null!");
    }
    if (!(initData instanceof Engine2dInitData))
    {
      throw new IllegalArgumentException("Invalid argument initData - not an instance of FastEngineInitData!");
    }

    synchronized (this)
    {
      if (!isInitialized())
      {
        // Initialize output
        output = simOutput;

        // Get Engine2dInitData and GlobalData references
        Engine2dInitData engineInitData = (Engine2dInitData)initData;
        GlobalData global = engineInitData.getGlobalData();

        // Initialize updates interval and timers
        updatesInterval = global.getUpdatesInterval();
        battleDurationCounter = new TimeCounter(global.getBattleDuration());

        // Initialize graphics context
        context = new Java2dContext(engineInitData.getParent(), true);
        int resW = global.getMapTileSize().getWidth() * simData.getMap().getColumns();
        int resH = global.getMapTileSize().getHeight() * simData.getMap().getRows();
        context.setResolution(resW, resH);
        context.initialize();

        // Create sprites repository
        spritesRepository = new SpritesRepository(global, context);

        // Perform specific initialization
        performInitialization(simInput, simData, engineInitData);

        // Engine initialized
        initialized();
      }
    }
  }

  /**
   * @see net.java.dante.sim.engine.Engine#start()
   */
  public final void start()
  {
    synchronized (this)
    {
      if (!isInitialized())
      {
        throw new IllegalStateException("Engine was not initialized!");
      }

      // Always resume engine
      resumed();
      if (!isRunning())
      {
        startLoop();

        String threadName = (getClass().getSimpleName() + '#' + runningEnginesCount.incrementAndGet());

        engineThread = new Thread(threadName) {
          /**
           * @see java.lang.Thread#run()
           */
          @Override
          public void run()
          {
            try
            {
              while (isLoopRunning())
              {
                engineUpdate();
              }
            }
            catch (Throwable e)
            {
              Dbg.error("Caught exception: " + e + " in engine thread: " + getName() + ", stack trace:");
              e.printStackTrace();

              throw new RuntimeException(e);
            }
          }
        };
        engineThread.start();
      }
    }
  }

  /**
   * @see net.java.dante.sim.engine.Engine#pause()
   */
  public final void pause()
  {
    synchronized (this)
    {
      if (!isInitialized())
      {
        throw new IllegalStateException("Engine was not initialized!");
      }

      paused();
    }
  }

  /**
   * @see net.java.dante.sim.engine.Engine#dispose()
   */
  public final void dispose()
  {
    synchronized (this)
    {
      if (!isInitialized())
      {
        throw new IllegalStateException("Engine was not initialized!");
      }

      stopEngineThread();
      performDisposal();
      disposed();
    }
  }

  /**
   * This method should be implemented by subclasses and should contain
   * initialization procedure specific for the concrete engine.
   * This method is called only once in
   * {@link #init(SimulationInput, SimulationOutput, SimulationData, InitData)}
   * method.
   *
   * @param simInput - simulation's input.
   * @param simData - simulation's data.
   * @param engineInitData - engine initialization data.
   */
  protected abstract void performInitialization(SimulationInput simInput,
      SimulationData simData, Engine2dInitData engineInitData);

  /**
   * This method should be implemented by subclasses and should contain
   * procedure for each simulation update. This method is called in
   * engine loop in another thread (started in {@link #start()} method)
   * until {@link #isLoopRunning()} method returns <code>false</code>.
   */
  protected abstract void engineUpdate();

  /**
   * This method should be implemented by subclasses and should contain
   * disposal procedure specific for the concrete engine.
   * This method is called only once in {@link #dispose()} method.
   */
  protected abstract void performDisposal();

  /**
   * Stops engine thread.
   */
  private void stopEngineThread()
  {
    if (isRunning())
    {
      stopLoop();

      try
      {
        engineThread.join();
      }
      catch (InterruptedException e)
      {
        if (Dbg.DBGE) Dbg.error("InterruptedException caught while waiting for engineThread!");
      }

      runningEnginesCount.decrementAndGet();
      engineThread = null;
    }
  }

  /**
   * Mark engine as initialized.
   */
  void initialized()
  {
    initialized = true;
  }

  /**
   * Mark engine as disposed (not initialized).
   */
  void disposed()
  {
    initialized = false;
  }

  /**
   * Marks simulation engine loop as paused.
   */
  void paused()
  {
    paused = true;
  }

  /**
   * Marks simulation engine loop as not paused (resumed).
   */
  void resumed()
  {
    paused = false;
  }

  /**
   * Checks whether engine was initialized.
   *
   * @return Returns <code>true</code> if engine was initialized.
   */
  boolean isInitialized()
  {
    return initialized;
  }

  /**
   * Checks whether engine thread is running.
   *
   * @return Returns <code>true</code> if engine thread is running.
   */
  boolean isRunning()
  {
    return (engineThread != null);
  }

  /**
   * Checks whether simulation loop is paused.
   * This method can be invoked by subclasses
   * of this {@link AbstractEngine2d} class to check status of engine loop.
   *
   * @return Returns <code>true</code> if simulation loop is paused.
   */
  protected synchronized final boolean isPaused()
  {
    return paused;
  }

  /**
   * Starts simulation engine loop.
   */
  void startLoop()
  {
    loopRunning.set(true);
  }

  /**
   * Stops engine loop. This method can be invoked by subclasses
   * of this {@link AbstractEngine2d} class to control engine loop.
   */
  protected final void stopLoop()
  {
    loopRunning.set(false);
  }

  /**
   * Checks whether simulation engine loop is running.
   * This method can be invoked by subclasses
   * of this {@link AbstractEngine2d} class to check status of engine loop.
   *
   * @return Returns <code>true</code> if simulation engine loop is running.
   */
  final boolean isLoopRunning()
  {
    return loopRunning.get();
  }
}