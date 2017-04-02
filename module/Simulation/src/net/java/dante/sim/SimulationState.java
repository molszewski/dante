/*
 * Created on 2006-07-15
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim;

/**
 * Enumeration representing all possible simulation states.
 *
 * @author M.Olszewski
 */
public enum SimulationState 
{
  /** Simulation not initialized - it must be initialized. */
  NOT_INITIALIZED,
  /** Simulation initialized and ready to start. */
  INITIALIZED,
  /** Simulation started. */
  STARTED,
  /** Simulation paused. */
  PAUSED,
  /** Simulation reset and ready to start. */
  RESET;
}