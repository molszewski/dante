/*
 * Created on 2006-08-17
 *
 * @author M.Olszewski
 */

package net.java.dante.sim;

import net.java.dante.sim.data.ClientSimulationInitData;
import net.java.dante.sim.data.GlobalData;
import net.java.dante.sim.data.SimulationData;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.engine.Engine;
import net.java.dante.sim.engine.engine2d.client.ClientEngine2dInitData;
import net.java.dante.sim.io.CachedSimulationInput;
import net.java.dante.sim.io.SimulationInput;
import net.java.dante.sim.io.SimulationOutput;

/**
 * Client side simulation.
 *
 * @author M.Olszewski
 */
class ClientSimulation extends AbstractSimulation
{
  /**
   * Creates instance of {@link ClientSimulation} class with specified
   * parameters.
   *
   * @param simEngine - simulation's engine.
   * @param simInput - simulation's input.
   */
  ClientSimulation(Engine simEngine, CachedSimulationInput simInput)
  {
    super(simEngine, simInput);
  }


  /**
   * @see net.java.dante.sim.AbstractSimulation#initEngine(net.java.dante.sim.io.SimulationInput, net.java.dante.sim.io.SimulationOutput, net.java.dante.sim.data.SimulationData, net.java.dante.sim.data.common.InitData)
   */
  @Override
  protected void initEngine(SimulationInput simInput,
      SimulationOutput simOutput, SimulationData simData,
      InitData initData)
  {
    if (!(initData instanceof ClientSimulationInitData))
    {
      throw new IllegalArgumentException("Invalid argument initData - not an instance of ServerSimulationInitData class!");
    }

    ClientSimulationInitData simInitData = (ClientSimulationInitData)initData;
    GlobalData globalData = data.getGlobalData();

    engine.init(input, output, data,
        new ClientEngine2dInitData(
            simInitData.getParentContainer(),
            globalData,
            simInitData.getGroupId(),
            simData.getCreator()));
  }

  /**
   * @see net.java.dante.sim.AbstractSimulation#stateChanged(net.java.dante.sim.SimulationState, net.java.dante.sim.SimulationState)
   */
  @Override
  protected void stateChanged(SimulationState oldState, SimulationState newState)
  {
    switch (newState)
    {
      case STARTED:
      {
        engine.start();
        break;
      }

      case PAUSED:
      {
        engine.pause();
        break;
      }

      default:
      {
        // TODO sth?
        break;
      }
    }
  }
}