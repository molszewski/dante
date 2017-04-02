/*
 * Created on 2006-07-04
 *
 * @author M.Olszewski
 */

package net.java.dante.sim;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.java.dante.sim.common.InvalidTransitionException;
import net.java.dante.sim.data.DataLoader;
import net.java.dante.sim.data.LoadersFactory;
import net.java.dante.sim.data.SimulationData;
import net.java.dante.sim.data.common.DataSource;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.engine.Engine;
import net.java.dante.sim.io.CachedSimulationInput;
import net.java.dante.sim.io.SimulationInput;
import net.java.dante.sim.io.SimulationOutput;


/**
 * Base class for all classes implementing {@link Simulation} interface.
 *
 * @author M.Olszewski
 */
abstract class AbstractSimulation implements Simulation
{
  /**
   * Map containing associations between each simulation state and states to which
   * transition is possible.
   */
  private static final Map<SimulationState, Set<SimulationState>> transitionsMap =
      new HashMap<SimulationState, Set<SimulationState>>();

  /** Current simulation state. */
  private SimulationState currentState = SimulationState.NOT_INITIALIZED;
  /** Reference to simulation's engine. */
  protected Engine engine;
  /** Reference to simulation's output. */
  protected SimulationOutput output;
  /** Reference to simulation's input. */
  protected CachedSimulationInput input;
  /** Reference to simulation's data. */
  protected SimulationData data;


  // Static initialization of transitionsMap
  static
  {
    transitionsMap.put(SimulationState.NOT_INITIALIZED,
                       EnumSet.of(SimulationState.INITIALIZED));
    transitionsMap.put(SimulationState.INITIALIZED,
                       EnumSet.of(SimulationState.STARTED));
    transitionsMap.put(SimulationState.STARTED,
                       EnumSet.of(SimulationState.STARTED, SimulationState.PAUSED, SimulationState.RESET));
    transitionsMap.put(SimulationState.PAUSED,
                       EnumSet.of(SimulationState.PAUSED, SimulationState.STARTED, SimulationState.RESET));
    transitionsMap.put(SimulationState.RESET,
                       EnumSet.of(SimulationState.STARTED));
  }


  /**
   * Protected constructor - only for inheritance.
   *
   * @param simEngine - simulation engine running all the simulation.
   * @param simInput - simulation's input.
   */
  protected AbstractSimulation(Engine simEngine, CachedSimulationInput simInput)
  {
    if (simEngine == null)
    {
      throw new NullPointerException("Specified simEngine is null!");
    }
    if (simInput == null)
    {
      throw new NullPointerException("Specified simInput is null!");
    }

    input = simInput;
    engine = simEngine;
  }

  /**
   * @throws NullPointerException if <code>simOutput</code>, <code>dataSource</code>
   *         or <code>initData</code> are <code>null</code>s.
   *
   * @see net.java.dante.sim.Simulation#init(net.java.dante.sim.io.SimulationOutput, net.java.dante.sim.data.common.DataSource, net.java.dante.sim.data.common.InitData)
   */
  public void init(SimulationOutput simOutput, DataSource dataSource, InitData initData)
  {
    if (simOutput == null)
    {
      throw new NullPointerException("Specified simOutput is null!");
    }
    if (dataSource == null)
    {
      throw new NullPointerException("Specified dataSource is null!");
    }
    if (initData == null)
    {
      throw new NullPointerException("Specified initData is null!");
    }

    output = simOutput;

    // Load data
    DataLoader loader = LoadersFactory.getInstance().createLoader(dataSource);
    if (loader != null)
    {
      data = loader.loadData();
      data.init(initData);
    }

    initEngine(input, output, data, initData);

    transitionTo(SimulationState.INITIALIZED);
  }

  /**
   * Initializes {@link Engine} object used by object of {@link AbstractSimulation}
   * subclass.
   *
   * @param simInput - simulation's input.
   * @param simOutput - simulation's output.
   * @param simData - simulation's data.
   * @param initData - initialization data.
   */
  protected abstract void initEngine(
      SimulationInput simInput, SimulationOutput simOutput,
      SimulationData simData, InitData initData);

  /**
   * @see net.java.dante.sim.Simulation#start()
   */
  public void start()
  {
    transitionTo(SimulationState.STARTED);
  }

  /**
   * @see net.java.dante.sim.Simulation#pause()
   */
  public void pause()
  {
    transitionTo(SimulationState.PAUSED);
  }

  /**
   * @see net.java.dante.sim.Simulation#reset()
   */
  public void reset()
  {
    transitionTo(SimulationState.RESET);
  }

  /**
   * @see net.java.dante.sim.Simulation#dispose()
   */
  public void dispose()
  {
    engine.dispose();
    input.dispose();
  }

  /**
   * @see net.java.dante.sim.Simulation#getState()
   */
  public SimulationState getState()
  {
    return currentState;
  }

  /**
   * @see net.java.dante.sim.Simulation#getInput()
   */
  public SimulationInput getInput()
  {
    return input;
  }

  /**
   * Performs transition from current state to one specified in
   * <code>targetState</code> argument.
   *
   * @param targetState - target state.
   */
  protected final void transitionTo(SimulationState targetState)
  {
    if (targetState == null)
    {
      throw new NullPointerException("Specified targetState is null!");
    }

    if (transitionsMap.get(currentState).contains(targetState))
    {
      SimulationState oldState = currentState;
      currentState = targetState;
      stateChanged(oldState, currentState);
    }
    else
    {
      throw new InvalidTransitionException("Cannot perform transition to: " + targetState.name() + " from: " + currentState.name());
    }
  }

  /**
   * Callback method invoked when current state is changed from
   * <code>oldState</code> to <code>newState</code>.
   *
   * @param oldState - state before change.
   * @param newState - state after change (current state).
   */
  protected abstract void stateChanged(SimulationState oldState, SimulationState newState);
}