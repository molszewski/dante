/*
 * Created on 2006-08-18
 *
 * @author M.Olszewski
 */


package net.java.dante.sim.engine.engine2d.client;

import net.java.dante.sim.data.SimulationData;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.engine.Engine;
import net.java.dante.sim.engine.engine2d.AbstractEngine2d;
import net.java.dante.sim.engine.engine2d.Engine2dInitData;
import net.java.dante.sim.engine.time.SystemTimer;
import net.java.dante.sim.engine.time.TimeStopper;
import net.java.dante.sim.io.ClientInput;
import net.java.dante.sim.io.FinishData;
import net.java.dante.sim.io.GroupEliminatedSimulationData;
import net.java.dante.sim.io.InputData;
import net.java.dante.sim.io.SimulationInput;
import net.java.dante.sim.io.SimulationOutput;
import net.java.dante.sim.io.TimeSyncData;
import net.java.dante.sim.io.UpdateData;

/**
 * Implementation of {@link Engine} interface using Java2D rendering to render
 * agents, running on client's side.
 *
 * @author M.Olszewski
 */
public class ClientEngine2d extends AbstractEngine2d
{
  /** Input for this engine. */
  private ClientInput input;

  /** Object managing all groups of objects belonging to this engine. */
  private Client2dGroupsManager groupsManager;

  private int updatesCount = 0;
  private long lastUpdateTime = 0;

  private TimeStopper timeStopper;


  /**
   * Creates not initialized instance of {@link ClientEngine2d} class.
   * {@link #init(SimulationInput, SimulationOutput, SimulationData, InitData)}
   * method must be called in order to use any other methods of this object.
   */
  public ClientEngine2d()
  {
    // Intentionally left empty.
  }


  /**
   * @see net.java.dante.sim.engine.engine2d.AbstractEngine2d#performInitialization(net.java.dante.sim.io.SimulationInput, net.java.dante.sim.data.SimulationData, net.java.dante.sim.engine.engine2d.Engine2dInitData)
   */
  @Override
  protected void performInitialization(SimulationInput simInput, SimulationData simData, Engine2dInitData engineInitData)
  {
    if (!(simInput instanceof ClientInput))
    {
      throw new IllegalArgumentException("Invalid argument simInput - not an instance of ClientInput!");
    }
    if (!(engineInitData instanceof ClientEngine2dInitData))
    {
      throw new IllegalArgumentException("Invalid argument engineInitData - not an instance of ClientEngine2dInitData!");
    }

    // Initialize input
    input = (ClientInput)simInput;

    // Initialize groups
    groupsManager = new Client2dGroupsManager(context, spritesRepository);
    groupsManager.init(simData, (ClientEngine2dInitData)engineInitData);

    // Initialize time stopper
    SystemTimer timer = new SystemTimer();
    timer.start();
    timeStopper = new TimeStopper(timer, updatesInterval);
  }


  /**
   * Main method of engine - it runs whole simulation (updates object's states
   * and renders agents if it is requested).
   *
   * @see net.java.dante.sim.engine.engine2d.AbstractEngine2d#engineUpdate()
   */
  @Override
  protected void engineUpdate()
  {
    if (!isPaused())
    {
      processInput();

      if (updatesCount > 0)
      {
        timeStopper.startPoint();
        {
          long delta = updatesInterval;

          for (int i = 0; i < updatesCount; i++)
          {
            groupsManager.update(delta);

            renderScene();
          }
        }
        timeStopper.endPoint();
      }
      else
      {
        renderOnlyScene();
      }
    }
    else
    {
      renderOnlyScene();
    }
  }

  private void processInput()
  {
    updatesCount = 0;

    InputData inputData = input.retrieveInput();
    if (inputData instanceof UpdateData)
    {
      UpdateData updateData = (UpdateData)inputData;
      groupsManager.processInput(updateData);

      updatesCount = (int)((updateData.getTime() - lastUpdateTime) / updatesInterval);
      lastUpdateTime = updateData.getTime();
    }
    else if (inputData instanceof TimeSyncData)
    {
      TimeSyncData syncData = (TimeSyncData)inputData;

      updatesCount = (int)((syncData.getTime() - lastUpdateTime) / updatesInterval);
      lastUpdateTime = syncData.getTime();
    }
    else if ((inputData instanceof GroupEliminatedSimulationData) ||
             (inputData instanceof FinishData))
    {
      stopLoop();
    }
  }

  /**
   * Renders only scene and sleeps for some time if rendering took less than
   * update time interval.
   */
  private void renderOnlyScene()
  {
    timeStopper.startPoint();
    {
      renderScene();
    }
    timeStopper.endPoint();
  }

  /**
   * Renders scene: background and objects.
   */
  private void renderScene()
  {
    context.beginRendering();
    {
      renderBackground();
      groupsManager.render();
    }
    context.endRendering();
  }


  /**
   * Renders background sprite.
   */
  private void renderBackground()
  {
    spritesRepository.getBackgroundSprite().draw(0, 0);
  }

  /**
   * @see net.java.dante.sim.engine.engine2d.AbstractEngine2d#performDisposal()
   */
  @Override
  protected void performDisposal()
  {
    context.dispose();
  }
}