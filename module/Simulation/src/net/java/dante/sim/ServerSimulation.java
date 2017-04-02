/*
 * Created on 2006-07-13
 *
 * @author M.Olszewski
 */

package net.java.dante.sim;

import net.java.dante.sim.data.GlobalData;
import net.java.dante.sim.data.ServerSimulationInitData;
import net.java.dante.sim.data.SimulationData;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.engine.Engine;
import net.java.dante.sim.engine.engine2d.Engine2dInitData;
import net.java.dante.sim.io.CachedSimulationInput;
import net.java.dante.sim.io.SimulationInput;
import net.java.dante.sim.io.SimulationOutput;
import net.java.dante.sim.io.init.InitializationData;

/**
 * Server side simulation.
 *
 * @author M.Olszewski
 */
class ServerSimulation extends AbstractSimulation
{
  /**
   * Creates instance of {@link ServerSimulation} class with specified
   * parameters.
   *
   * @param simEngine - simulation's engine.
   * @param simInput - simulation's input.
   */
  ServerSimulation(Engine simEngine, CachedSimulationInput simInput)
  {
    super(simEngine, simInput);
  }


  /**
   * @see net.java.dante.sim.AbstractSimulation#initEngine(net.java.dante.sim.io.SimulationInput, net.java.dante.sim.io.SimulationOutput, net.java.dante.sim.data.SimulationData, net.java.dante.sim.data.common.InitData)
   */
  @Override
  protected void initEngine(SimulationInput simInput, SimulationOutput simOutput,
      SimulationData simData, InitData initData)
  {
    if (!(initData instanceof ServerSimulationInitData))
    {
      throw new IllegalArgumentException("Invalid argument initData - not an instance of ServerSimulationInitData class!");
    }

    ServerSimulationInitData simInitData = (ServerSimulationInitData)initData;
    GlobalData globalData = data.getGlobalData();
    engine.init(input, output, data,
        new Engine2dInitData(simInitData.getParentContainer(), globalData));
  }

  /**
   * @see net.java.dante.sim.AbstractSimulation#stateChanged(net.java.dante.sim.SimulationState, net.java.dante.sim.SimulationState)
   */
  @Override
  protected void stateChanged(SimulationState oldState, SimulationState newState)
  {
    if (oldState == SimulationState.NOT_INITIALIZED)
    {
      if (newState == SimulationState.INITIALIZED)
      {
        // InitializationData is ready.
        InitializationData[] initDataArray =
            InitializationData.createInitializationData(data);
        for (int i = 0; i < initDataArray.length; i++)
        {
          output.dataReady(initDataArray[i]);
        }
      }
    }
    if (newState == SimulationState.STARTED)
    {
      engine.start();
    }
    if (newState == SimulationState.PAUSED)
    {
      engine.pause();
    }
  }
}