/*
 * Created on 2006-07-05
 *
 * @author M.Olszewski
 */

package net.java.dante.sim;

import net.java.dante.sim.engine.engine2d.client.ClientEngine2d;
import net.java.dante.sim.engine.engine2d.server.ServerEngine2d;
import net.java.dante.sim.io.ClientInput;
import net.java.dante.sim.io.ServerInput;


/**
 * Factory class delivering different implementations of {@link Simulation}
 * interface.
 *
 * @author M.Olszewski
 */
public class SimulationFactory
{
  /** The only existing instance of {@link SimulationFactory}. */
  private static final SimulationFactory instance = new SimulationFactory();


  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private SimulationFactory()
  {
    // Intentionally left empty.
  }


  /**
   * Gets the only instance of this singleton class.
   *
   * @return Returns the only instance of this singleton class.
   */
  public static SimulationFactory getInstance()
  {
    return instance;
  }


  /**
   * Creates instance of requested {@link Simulation} interface implementation.
   * Every time this method is invoked, new object is created.
   *
   * @param type - type of requested {@link Simulation} interface implementation.
   *
   * @return Returns new instance of {@link Simulation} interface implementation
   *         or <code>null</code> if requested type is not supported.
   */
  public Simulation createSimulation(SimulationType type)
  {
    Simulation sim = null;

    switch (type)
    {
      case SERVER:
      {
        sim = new ServerSimulation(new ServerEngine2d(), new ServerInput());
        break;
      }

      case CLIENT:
      {
        sim = new ClientSimulation(new ClientEngine2d(), new ClientInput());
        break;
      }

      default:
      {
        break;
      }
    }

    return sim;
  }
}