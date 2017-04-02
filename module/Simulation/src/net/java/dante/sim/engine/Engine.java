/*
 * Created on 2006-07-04
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine;

import net.java.dante.sim.data.SimulationData;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.io.SimulationInput;
import net.java.dante.sim.io.SimulationOutput;


/**
 * Engine interface, defining methods for updating state of 
 * simulation objects in time and for rendering them.
 *
 * @author M.Olszewski
 */
public interface Engine
{
  /**
   * Initializes this {@link Engine} with specified {@link SimulationData} object
   * and external initialization data.
   * 
   * @param simInput - simulation's input.
   * @param simOutput - simulation's output.
   * @param simData - simulation's data.
   * @param initData - initialization data.
   */
  void init(SimulationInput simInput, SimulationOutput simOutput, 
      SimulationData simData, InitData initData);
  
  /**
   * Starts engine. Started engine updates states of objects and renders
   * them if it is requested.
   */
  void start();
  
  /**
   * Pauses this engine - paused engine does not update state of objects
   * but can render them.
   */
  void pause();
  
  /**
   * Disposes all engine's resources that requires disposal. 
   */
  void dispose();
}