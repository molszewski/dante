/*
 * Created on 2006-07-17
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io;

/**
 * Interface for classes that caches input data they have received.
 * Implementations must deliver {@link #retrieveInput()} method returning
 * some instance of {@link InputData} implementation that was received by this
 * {@link SimulationInput}.
 *
 * @author M.Olszewski
 */
public interface CachedSimulationInput extends SimulationInput
{
  /**
   * Retrieves input data from internal cache or <code>null</code> if
   * no input data is available.
   *
   * @return Returns input data from internal cache or <code>null</code> if
   *         no input data is available.
   */
  InputData retrieveInput();

  /**
   * Disposes all resources used by {@link CachedSimulationInput} implementation.
   */
  void dispose();
}