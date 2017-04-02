/*
 * Created on 2006-07-17
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.engine2d.server;

import java.util.Map;

import net.java.dante.sim.data.SimulationData;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.engine.Engine;
import net.java.dante.sim.engine.engine2d.AbstractEngine2d;
import net.java.dante.sim.engine.engine2d.Engine2dInitData;
import net.java.dante.sim.engine.time.SystemTimer;
import net.java.dante.sim.engine.time.TimeCounter;
import net.java.dante.sim.engine.time.TimeStopper;
import net.java.dante.sim.event.EventsRepository;
import net.java.dante.sim.io.CommandsData;
import net.java.dante.sim.io.EnableRenderingData;
import net.java.dante.sim.io.FinishData;
import net.java.dante.sim.io.GroupAbandonSimulationData;
import net.java.dante.sim.io.GroupEliminatedSimulationData;
import net.java.dante.sim.io.InputData;
import net.java.dante.sim.io.ServerInput;
import net.java.dante.sim.io.SimulationInput;
import net.java.dante.sim.io.SimulationOutput;
import net.java.dante.sim.io.StatisticsData;
import net.java.dante.sim.io.TimeSyncData;
import net.java.dante.sim.io.UpdateData;


/**
 * Implementation of {@link Engine} interface using Java2D rendering to render
 * agents, running on server's side.
 *
 * @author M.Olszewski
 */
public class ServerEngine2d extends AbstractEngine2d
{
  /** Input for this engine. */
  private ServerInput input;

  /** Time stopper. */
  private TimeStopper timeStopper;
  /** Current time holder. */
  private CurrentTimeHolder timeHolder = new CurrentTimeHolder();
  /** Time counter counting whether it is time to send update messages. */
  private TimeCounter sendUpdateMessagesCounter;

  /** All groups of objects manager. */
  private Server2dGroupsManager groupsManager;

  /** Indicates whether rendering should be enabled or not. */
  private boolean renderingEnabled = true;


  /**
   * Creates not initialized instance of {@link ServerEngine2d} class.
   * {@link #init(SimulationInput, SimulationOutput, SimulationData, InitData)}
   * method must be called in order to use any other methods of this object.
   */
  public ServerEngine2d()
  {
    // Intentionally left empty.
  }



  /**
   * @see net.java.dante.sim.engine.engine2d.AbstractEngine2d#performInitialization(net.java.dante.sim.io.SimulationInput, net.java.dante.sim.data.SimulationData, net.java.dante.sim.engine.engine2d.Engine2dInitData)
   */
  @Override
  protected void performInitialization(SimulationInput simInput, SimulationData simData, Engine2dInitData engineInitData)
  {
    if (!(simInput instanceof ServerInput))
    {
      throw new IllegalArgumentException("Invalid argument simInput - not an instance of ServerInput!");
    }

    // Initialize input
    input = (ServerInput)simInput;

    // Initialize timer and time stopper
    SystemTimer timer = new SystemTimer();
    timer.start();
    timeStopper = new TimeStopper(timer, updatesInterval);

    // Initialize send updates timer
    sendUpdateMessagesCounter = new TimeCounter(engineInitData.getGlobalData().getSendUpdateMessagesInterval());

    // Initialize groups manager
    groupsManager = new Server2dGroupsManager(context, spritesRepository, timeHolder);
    groupsManager.init(simData, engineInitData);
  }

  /**
   * Main method of engine - it runs whole simulation (updates object's states,
   * checks collisions, renders agents if it is requested and creates
   * updates of simulation state on output).
   *
   * @see net.java.dante.sim.engine.engine2d.AbstractEngine2d#engineUpdate()
   */
  @Override
  protected void engineUpdate()
  {
    timeStopper.startPoint();
    {
      long delta = updatesInterval;

      if (!isPaused())
      {
        // Update current time
        timeHolder.update(delta);

        // Retrieve input
        processInput();

        // Update simulation
        groupsManager.update(delta);

        // Render frame
        renderScene();

        // Check whether some group is not active and notify it about this state
        notifyNotActiveGroups();

        // Send updates if necessary
        sendUpdates(delta);

        // Check finish condition and finish simulation if condition is met
        checkFinishCondition(delta);
      }
      else
      {
        // If paused - only render current frame
        renderScene();
      }
    }
    timeStopper.endPoint();
  }

  /**
   * Processes input of this simulation engine.
   */
  private void processInput()
  {
    InputData inputData = input.retrieveInput();
    while (inputData != null)
    {
      if (inputData instanceof CommandsData)
      {
        groupsManager.processCommands((CommandsData)inputData);
      }
      else if (inputData instanceof EnableRenderingData)
      {
        renderingEnabled = ((EnableRenderingData)inputData).isRenderingEnabled();
      }
      else if (inputData instanceof GroupAbandonSimulationData)
      {
        groupsManager.groupAbandoned((GroupAbandonSimulationData)inputData);
      }

      inputData = input.retrieveInput();
    }
  }

  /**
   * Renders scene (background and objects) if rendering is enabled.
   */
  private void renderScene()
  {
    if (renderingEnabled)
    {
      context.beginRendering();
      {
        renderBackground();
        groupsManager.render();
      }
      context.endRendering();
    }
  }

  /**
   * Renders background sprite.
   */
  private void renderBackground()
  {
    spritesRepository.getBackgroundSprite().draw(0, 0);
  }

  /**
   * Sends updates if required time has passed.
   *
   * @param delta time elapsed since last update.
   */
  private void sendUpdates(long delta)
  {
    if (sendUpdateMessagesCounter.isActionTime(delta))
    {
      sendEvents();

      sendUpdateMessagesCounter.reset();
    }
  }

  /**
   * Send events to all agents groups.
   */
  private void sendEvents()
  {
    Map<Integer, EventsRepository> repositories = groupsManager.buildEventsRepositories();

    for (Integer groupId : repositories.keySet())
    {
      EventsRepository repository = repositories.get(groupId);
      if (repository != null)
      {
        output.dataReady(new UpdateData(timeHolder.getCurrentTime(), repository));
      }
      else if (groupsManager.isGroupActive(groupId))
      {
        output.dataReady(new TimeSyncData(groupId.intValue(), timeHolder.getCurrentTime()));
      }
    }
  }

  /**
   * Checks whether status of any group has changed to 'not active' and if so
   * notifies this group.
   */
  private void notifyNotActiveGroups()
  {
    Map<Integer, EventsRepository> repositories = groupsManager.buildEventsRepositoriesForEliminatedGroups();

    if (repositories != null)
    {
      for (Integer groupId : repositories.keySet())
      {
        EventsRepository repository = repositories.get(groupId);
        if (repository != null)
        {
          output.dataReady(new UpdateData(timeHolder.getCurrentTime(), repository));
        }
        output.dataReady(new GroupEliminatedSimulationData(groupId.intValue()));
      }
    }
  }

  /**
   * Checks finish condition - one or zero agents groups are active
   * or battle time has passed.
   *
   * @param delta time elapsed since last update.
   */
  private void checkFinishCondition(long delta)
  {
    if (battleDurationCounter.isActionTime(delta) ||
        (groupsManager.getActiveGroupsCount() <= 1))
    {
      // Stop further processing
      stopLoop();

      // Send events generated so far
      sendEvents();

      // Send statistics
      GroupStatistics[] statistics = groupsManager.buildGroupsStatistics();
      for (int i = 0; i < statistics.length; i++)
      {
        output.dataReady(new StatisticsData(statistics[i]));
      }

      // Send finish signal
      output.dataReady(new FinishData());
    }
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