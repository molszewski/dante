/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data;

/**
 * Enumeration defining possible formats of {@link net.java.dante.sim.data.SimulationData} 
 * objects. Simulation data can be simple, used by clients or 'full', containing
 * all necessary information to run simulation on server side.
 * 
 * @author M.Olszewski
 */
public enum SimulationDataType
{
  /** Simple simulation data format - used by clients. */
  SIMPLE,
  /** Full simulation data format - used by servers. */
  FULL
}