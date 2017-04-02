/*
 * Created on 2006-06-08
 *
 * @author M.Olszewski
 */

package net.java.dante.sim;

import net.java.dante.sim.data.common.DataSource;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.io.SimulationInput;
import net.java.dante.sim.io.SimulationOutput;

/*
 * MAJOR & CRITICALS FIXED: [record for glory & for the record :P]
 * 1. Problem with NaN values - FIXED: MathUtils.calculateVelocity calculates
 *    angle also for vectors with zero magnitudes, generating angle=NaN.
 *    It is fixed now - length of vectors are checked before calculating angle
 *    and proper velocity is returned based also on this information.
 * 2. Problem with not existing intersection point defining projectile start
 *    point - it was stopping agent from doing further commands, but agent
 *    should perform further actions - this action is 'done' but really it
 *    failed because of invalid arguments. Invalid actions will not be performed!
 */

/**
 * Interface for any classes that are intended to run simulation.
 *
 * @author M.Olszewski
 */
public interface Simulation
{
  /**
   * Starts simulation. Simulation must be initialized first - otherwise
   * this method call may fail or return immediately.
   */
  public void start();

  /**
   * Initializes this {@link Simulation}.
   *
   * @param output - output for all data generated during simulation.
   * @param dataSource - source of {@link net.java.dante.sim.data.SimulationData}.
   * @param initData - external initialization data for simulation.
   */
  void init(SimulationOutput output, DataSource dataSource, InitData initData);

  /**
   * Pauses simulation. Simulation must be started first - otherwise
   * this method call may fail or return immediately.
   */
  void pause();

  /**
   * Resets simulation - state of all simulation data is reset to initial state.
   * Simulation must be started or paused first - otherwise
   * this method call may fail or return immediately.
   */
  void reset();

  /**
   * Disposes all resources specific for this {@link Simulation}.
   */
  void dispose();

  /**
   * Gets current state of this {@link Simulation}.
   *
   * @return Returns current state of this {@link Simulation}.
   */
  SimulationState getState();

  /**
   * Gets reference to {@link SimulationInput} used by this {@link Simulation}
   * to retrieve input data that affects simulation.
   *
   * @return Returns source of input data for this {@link Simulation}.
   */
  SimulationInput getInput();
}