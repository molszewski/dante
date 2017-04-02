/*
 * Created on 2006-07-22
 *
 * @author M.Olszewski
 */

package net.java.dante.sim;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.java.dante.sim.command.CommandUtils;
import net.java.dante.sim.command.CommandsRepository;
import net.java.dante.sim.command.CommandsRepositoryBuilder;
import net.java.dante.sim.command.types.CommandTypesUtils;
import net.java.dante.sim.common.Dbg;
import net.java.dante.sim.data.ClientSimulationInitData;
import net.java.dante.sim.data.ServerSimulationInitData;
import net.java.dante.sim.data.common.FileDataSource;
import net.java.dante.sim.data.common.InitializationDataSource;
import net.java.dante.sim.engine.engine2d.server.GroupStatistics;
import net.java.dante.sim.event.EventsRepository;
import net.java.dante.sim.io.CommandsData;
import net.java.dante.sim.io.GroupEliminatedSimulationData;
import net.java.dante.sim.io.OutputData;
import net.java.dante.sim.io.SimulationInput;
import net.java.dante.sim.io.SimulationOutput;
import net.java.dante.sim.io.StatisticsData;
import net.java.dante.sim.io.TimeSyncData;
import net.java.dante.sim.io.UpdateData;
import net.java.dante.sim.io.init.AgentsInitData;
import net.java.dante.sim.io.init.FriendlyAgentInitData;
import net.java.dante.sim.io.init.InitializationData;


/**
 * Start point of simulation.
 *
 * @author M.Olszewski
 */
public class StartPoint
{
  private static final int SCREEN_WIDTH = 800;
  private static final int SCREEN_HEIGHT = 608;
  private static final int CLIENTS_COUNT = 2;
  private static final String MAIN_FILE_NAME = "res/main.config";

  private static int logFileCount = 0;

  /**
   * Start point of simulation.
   *
   * @param args - can contain path to file with main settings.
   */
  public static void main(String[] args)
  {
    String mainFile = MAIN_FILE_NAME;
    if (args.length >= 1)
    {
      mainFile = args[0];
    }
    else
    {
      System.out.println("Default main file selected.");
    }

    startMainThread(mainFile);
  }

  static void startMainThread(String mainFile)
  {
    startNewLogging();
    /* The frame in which we'll display our canvas */
    JFrame frame = new JFrame();
    JPanel panel = createPanel(frame, "ROBOTIX 0.6f - server");
    Simulation sim = SimulationFactory.getInstance().createSimulation(SimulationType.SERVER);
    sim.init(
        new ExampleSimOutput(mainFile, frame, sim),
        new FileDataSource(mainFile),
        new ServerSimulationInitData(CLIENTS_COUNT, panel));

    sim.start();
  }

  static void startNewLogging()
  {
    Dbg.disableLogging();
    Dbg.enableLogging("d:/log" + logFileCount + ".txt");
    logFileCount++;
  }

  static JPanel createPanel(JFrame frame, String title)
  {
    JPanel panel = (JPanel) frame.getContentPane();
    panel.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    panel.setLayout(null);
    // finally make the window visible
    frame.pack();
    frame.setResizable(false);
    frame.setVisible(true);
    frame.setTitle(title);

    // add a listener to respond to the user closing the window. If they
    // do we'd like to exit the game
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e)
      {
        System.exit(0);
      }
    });

    return panel;
  }


  private static class ExampleSimOutput implements SimulationOutput
  {
    DispatcherThread dispatcher;

    ExampleSimOutput(String mainDefFile, JFrame mainFrame, Simulation mainSimThread)
    {
      dispatcher = new DispatcherThread(this, mainDefFile, mainFrame, mainSimThread);
      dispatcher.start();
    }

    /**
     * @see net.java.dante.sim.io.SimulationOutput#dataReady(net.java.dante.sim.io.OutputData)
     */
    public void dataReady(OutputData data)
    {
      dispatcher.put(data);
    }

    private class DispatcherThread extends Thread
    {
      JFrame mainFrame = null;
      Simulation mainThread = null;
      String mainFile;
      ExampleSimOutput parent;
      BlockingQueue<OutputData> queue = new LinkedBlockingQueue<OutputData>();

      private int clientIdx = 0;
      private Map<Integer, Simulation> clients = new HashMap<Integer, Simulation>();
      private Map<Integer, Timer>      timers  = new HashMap<Integer, Timer>();
      private Map<Integer, JFrame>     frames  = new HashMap<Integer, JFrame>();
      private int resumeCounts = 0;

      DispatcherThread(ExampleSimOutput parentOutput, String mainDefFile,
          JFrame mainSimFrame, Simulation mainSimThread)
      {
        super("DispatcherThread");

        parent = parentOutput;
        mainFile = mainDefFile;
        mainFrame = mainSimFrame;
        mainThread = mainSimThread;
      }

      void put(OutputData data)
      {
        try
        {
          queue.put(data);
        }
        catch (InterruptedException e)
        {
          Dbg.error("InterruptedException while putting to queue");
        }
      }

      /**
       * @see java.lang.Runnable#run()
       */
      public void run()
      {
        while (true)
        {
          OutputData data = null;
          try
          {
            data = queue.take();
          }
          catch (InterruptedException e)
          {
            Dbg.error("InterruptedException while taking from queue");
          }

          if (data instanceof InitializationData)
          {
            if (Dbg.DBG1) Dbg.write("InitializationData received - OK.");

            final InitializationData initData = (InitializationData)data;

            JFrame frame = startClient(initData);
            frames.put(Integer.valueOf(initData.getGroupId()), frame);

  //          SimulationTester.getInstance().runSingleTest(
  //              new ShooterTestCase(input, initData), 50);
  //            SimulationTester.getInstance().runSingleTest(
  //              new ProjectileGoneTestCase(input, initData), 50);
            Timer timer = SimulationTester.getInstance().runCyclicTest(
                new RandomSimulationTestCase(mainThread.getInput(), initData), 50, 4000);
            timers.put(Integer.valueOf(initData.getGroupId()), timer);
//            SimulationTester.getInstance().runCyclicTest(
//                new AttackThemselvesTestCase(mainThread.getInput(), initData), 1500, 1000);
          }
          else if (data instanceof TimeSyncData)
          {
            TimeSyncData update = (TimeSyncData)data;
            Simulation sim = clients.get(Integer.valueOf(update.getGroupId()));
            sim.getInput().dataReceived(update);
          }
          else if (data instanceof UpdateData)
          {
            UpdateData update = (UpdateData)data;
            EventsRepository repository = update.getRepository();

  //          Dbg.write("UpdateData arrived with time= " + update.getTime() + " - WOW!");
  //          Dbg.write("Events for groupId=" + update.getRepository().getGroupId());
  //          Dbg.write("Events count=" + repository.getEventsCount());
  //
  //          for (int j = 0, eventsSize = repository.getEventsCount(); j < eventsSize; j++)
  //          {
  //            Event event = repository.getEvent(j);
  //            Dbg.write(j + ". event=" + event);
  //          }

            Simulation sim = clients.get(Integer.valueOf(repository.getGroupId()));
            sim.getInput().dataReceived(update);
          }
          else if (data instanceof StatisticsData)
          {
            StatisticsData resume = (StatisticsData)data;
            GroupStatistics statistics = resume.getGroupStatistics();
            Simulation sim = clients.get(Integer.valueOf(statistics.getGroupId()));

            Dbg.write("=============================================================");
            Dbg.write("FINAL STATISTICS FOR GROUP " + statistics.getGroupId() + ": ");
            Dbg.write("=============================================================");
            Dbg.write("Enemies visible  : " + statistics.getEnemyAgentsVisible());
            Dbg.write("Enemies hit      : " + statistics.getEnemyAgentsHit());
            Dbg.write("Enemies destroyed: " + statistics.getEnemyAgentsDestroyed());
            Dbg.write("Friends hit      : " + statistics.getFriendlyFireHits());
            Dbg.write("Friends destroyed: " + statistics.getFriendlyFireDestroyed());
            Dbg.write("Projectiles shot : " + statistics.getProjectilesShot());
            Dbg.write("Accuracy         : " + statistics.getAccuracy());
            Dbg.write("=============================================================");
            Dbg.write("TOTAL POINTS     : " + statistics.getTotalPoints());
            Dbg.write("=============================================================");

            sim.getInput().dataReceived(resume);
            resumeCounts++;


            if (resumeCounts == clients.size())
            {
              Dbg.write("restarting..");
              restart();
            }
          }
          else if (data instanceof GroupEliminatedSimulationData)
          {
            GroupEliminatedSimulationData eliminated = (GroupEliminatedSimulationData)data;
            Simulation sim = clients.get(Integer.valueOf(eliminated.getGroupId()));

            sim.getInput().dataReceived(eliminated);
          }
        }
      }

      private JFrame startClient(InitializationData initData)
      {
        JFrame frame = new JFrame();
        JPanel panel = createPanel(frame, "ROBOTIX 0.6f - client no." + (clientIdx + 1));
        clientIdx++;
        int groupId = initData.getGroupId();
        Simulation sim = SimulationFactory.getInstance().createSimulation(SimulationType.CLIENT);
        sim.init(
            new ExampleClientOutput(),
            new InitializationDataSource(initData),
            new ClientSimulationInitData(groupId, panel));

        clients.put(Integer.valueOf(groupId), sim);

        sim.start();

        return frame;
      }

      private void restart()
      {
        resumeCounts = 0;
        clientIdx    = 0;

        try
        {
          Thread.sleep(10000);
        }
        catch (InterruptedException e)
        {
          // Intentionally left empty.
        }

        Dbg.write("disposing timers..");
        for (Integer timerId : timers.keySet())
        {
          Timer timer = timers.get(timerId);
          timer.cancel();
        }
        timers.clear();
        Dbg.write("timers disposed.");

        Dbg.write("disposing clients..");
        for (Integer clientId : clients.keySet())
        {
          Simulation sim = clients.get(clientId);
          sim.dispose();
        }
        clients.clear();
        Dbg.write("clients disposed.");

        Dbg.write("disposing main thread..");
        mainThread.dispose();
        Dbg.write("main thread disposed.");

        Dbg.write("disposing main frame..");
        mainFrame.dispose();
        Dbg.write("main frame disposed.");

        Dbg.write("disposing frames..");
        for (Integer frameId : frames.keySet())
        {
          JFrame frame = frames.get(frameId);
          frame.dispose();
        }
        frames.clear();
        Dbg.write("frames disposed.");

        Dbg.write("starting main thread..");
        startMainThread();
      }

      private void startMainThread()
      {
        startNewLogging();

        mainFrame = new JFrame();
        JPanel panel = createPanel(mainFrame, "ROBOTIX 0.6f - server");

        mainThread = SimulationFactory.getInstance().createSimulation(SimulationType.SERVER);
        mainThread.init(
            parent,
            new FileDataSource(mainFile),
            new ServerSimulationInitData(CLIENTS_COUNT, panel));
        mainThread.start();
      }
    }


    static final class ExampleClientOutput implements SimulationOutput
    {
      /**
       * @see net.java.dante.sim.io.SimulationOutput#dataReady(net.java.dante.sim.io.OutputData)
       */
      public void dataReady(OutputData data)
      {
        Dbg.error("Clients does not generate data.. - but data ready is: " + data);
      }
    }
  }
}


abstract class SimulationTestCase
{
  private SimulationInput input;

  SimulationTestCase(SimulationInput simulationInput)
  {
    if (simulationInput == null)
    {
      throw new NullPointerException("Specified simulationInput is null!");
    }

    input = simulationInput;
  }

  /**
   * Gets simulation input where all commands should be sent.
   *
   * @return Returns simulation input where all commands should be sent.
   */
  protected SimulationInput getInput()
  {
    return input;
  }

  /**
   * This method should contain body of test case.
   */
  abstract public void executeTest();
}

class SimulationTester
{
  /** The only existing instance of {@link SimulationTester}. */
  private static SimulationTester instance = new SimulationTester();


  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private SimulationTester()
  {
    //
  }


  /**
   * Gets the only instance of this singleton class.
   *
   * @return Returns the only instance of this singleton class.
   */
  public static SimulationTester getInstance()
  {
    return instance;
  }

  /**
   * Runs single test.
   *
   * @param testCase - test case to run.
   * @param startTime - start time of test.
   *
   * @return Returns timer responsible for running single test.
   */
  public Timer runSingleTest(final SimulationTestCase testCase,
      long startTime)
  {
    final Timer timer = new Timer();
    timer.schedule(createTask(testCase), startTime);
    return timer;
  }

  /**
   * Runs cyclic test.
   *
   * @param testCase - test case to run.
   * @param startTime - start time of first test.
   * @param cycleDelay - delay between two cycles of test.
   *
   * @return Returns timer responsible for running cyclic test.
   */
  public Timer runCyclicTest(final SimulationTestCase testCase,
      long startTime, long cycleDelay)
  {
    final Timer timer = new Timer();
    timer.schedule(createTask(testCase), startTime, cycleDelay);

    return timer;
  }

  /**
   * Creates timer task for the specified test case.
   *
   * @param testCase - the specified test case.
   *
   * @return Returns created timer task for the specified test case.
   */
  private TimerTask createTask(final SimulationTestCase testCase)
  {
    return new TimerTask() {
      public void run()
      {
        testCase.executeTest();
      }
    };
  }
}

class RandomSimulationTestCase extends SimulationTestCase
{
  private InitializationData initData;
  Random rand = new Random();

  RandomSimulationTestCase(SimulationInput simulationInput,
      InitializationData initializationData)
  {
    super(simulationInput);

    initData = initializationData;
  }

  /**
   * @see net.java.dante.sim.SimulationTestCase#executeTest()
   */
  @Override
  public void executeTest()
  {
    CommandsRepositoryBuilder builder =
        CommandUtils.createDefaultBuilder(initData.getGroupId());

    AgentsInitData agentsInitData = initData.getAgentsInitData();

    for (int i = 0; i < agentsInitData.getFriendlyAgentsCount(); i++)
    {
      FriendlyAgentInitData agent = agentsInitData.getFriendlyAgentInitData(i);
      int agentId = agent.getAgentId();

      builder.addCommand(agentId, CommandTypesUtils.createClearQueueCommand());

      if (rand.nextBoolean())
      {
        builder.addCommand(agentId,
            CommandTypesUtils.createAttackCommand(rand.nextInt(738) + 32, rand.nextInt(538) + 32));
        builder.addCommand(agentId,
                           CommandTypesUtils.createAttackCommand(rand.nextInt(738) + 32, rand.nextInt(538) + 32));
      }
      builder.addCommand(agentId,
          CommandTypesUtils.createMoveCommand(rand.nextInt(738) + 32, rand.nextInt(538) + 32));
    }

    CommandsRepository repository = builder.build();
    if (repository != null)
    {
      CommandsData commandsData = new CommandsData(repository);
      getInput().dataReceived(commandsData);
    }
  }
}

class ProjectileGoneTestCase extends SimulationTestCase
{
  private InitializationData initData;

  ProjectileGoneTestCase(SimulationInput simulationInput,
      InitializationData initializationData)
  {
    super(simulationInput);

    initData = initializationData;
  }

  /**
   * @see net.java.dante.sim.SimulationTestCase#executeTest()
   */
  @Override
  public void executeTest()
  {
    CommandsRepositoryBuilder builder =
        CommandUtils.createDefaultBuilder(initData.getGroupId());

    AgentsInitData agentsInitData = initData.getAgentsInitData();

    for (int i = 0; i < agentsInitData.getFriendlyAgentsCount(); i++)
    {
      FriendlyAgentInitData agent = agentsInitData.getFriendlyAgentInitData(i);
      int agentId = agent.getAgentId();

      if (agent.getStartX() == 32.0)
      {
        builder.addCommand(agentId, CommandTypesUtils.createClearQueueCommand());
        builder.addCommand(agentId,
              CommandTypesUtils.createAttackCommand(64.5, 48.0));
      }
    }

    CommandsRepository repository = builder.build();
    if (repository != null)
    {
      CommandsData commandsData = new CommandsData(repository);
      getInput().dataReceived(commandsData);
    }
  }
}

class ShooterTestCase extends SimulationTestCase
{
  private InitializationData initData;
  Random rand = new Random();
  private final static int SHOOTS_COUNT = 100;

  ShooterTestCase(SimulationInput simulationInput,
      InitializationData initializationData)
  {
    super(simulationInput);

    initData = initializationData;
  }

  /**
   * @see net.java.dante.sim.SimulationTestCase#executeTest()
   */
  @Override
  public void executeTest()
  {
    CommandsRepositoryBuilder builder =
        CommandUtils.createDefaultBuilder(initData.getGroupId());

    AgentsInitData agentsInitData = initData.getAgentsInitData();

    for (int i = 0; i < agentsInitData.getFriendlyAgentsCount(); i++)
    {
      FriendlyAgentInitData agent = agentsInitData.getFriendlyAgentInitData(i);
      int agentId = agent.getAgentId();

      builder.addCommand(agentId, CommandTypesUtils.createClearQueueCommand());

      for (int j = 0; j < SHOOTS_COUNT; j++)
      {
        builder.addCommand(agentId,
            CommandTypesUtils.createAttackCommand(rand.nextInt(738) + 32, rand.nextInt(538) + 32));
      }
    }

    CommandsRepository repository = builder.build();
    if (repository != null)
    {
      CommandsData commandsData = new CommandsData(repository);
      getInput().dataReceived(commandsData);
    }
  }
}

class AttackThemselvesTestCase extends SimulationTestCase
{
  private InitializationData initData;
  Random rand = new Random();

  AttackThemselvesTestCase(SimulationInput simulationInput,
      InitializationData initializationData)
  {
    super(simulationInput);

    initData = initializationData;
  }

  /**
   * @see net.java.dante.sim.SimulationTestCase#executeTest()
   */
  @Override
  public void executeTest()
  {
    CommandsRepositoryBuilder builder =
        CommandUtils.createDefaultBuilder(initData.getGroupId());

    AgentsInitData agentsInitData = initData.getAgentsInitData();

    double firstX = 0.0;
    double firstY = 0.0;

    for (int i = 0; i < agentsInitData.getFriendlyAgentsCount(); i++)
    {
      FriendlyAgentInitData agent = agentsInitData.getFriendlyAgentInitData(i);
      if (i == 1)
      {
        firstX = agent.getStartX();
        firstY = agent.getStartY();
      }
    }

    for (int i = 0; i < agentsInitData.getFriendlyAgentsCount(); i++)
    {
      FriendlyAgentInitData agent = agentsInitData.getFriendlyAgentInitData(i);
      int agentId = agent.getAgentId();

      builder.addCommand(agentId, CommandTypesUtils.createClearQueueCommand());

      if (i != 1)
      {
        builder.addCommand(agentId,
              CommandTypesUtils.createAttackCommand(firstX, firstY));
      }
      builder.addCommand(agentId,
         CommandTypesUtils.createMoveCommand(rand.nextInt(738) + 32, rand.nextInt(538) + 32));
    }

    CommandsRepository repository = builder.build();
    if (repository != null)
    {
      CommandsData commandsData = new CommandsData(repository);
      getInput().dataReceived(commandsData);
    }
  }
}

class QueueFillTestCase extends SimulationTestCase
{
  private InitializationData initData;
  Random rand = new Random();
  private final static int FILL_COUNT = 10000;

  QueueFillTestCase(SimulationInput simulationInput,
      InitializationData initializationData)
  {
    super(simulationInput);

    initData = initializationData;
  }

  /**
   * @see net.java.dante.sim.SimulationTestCase#executeTest()
   */
  @Override
  public void executeTest()
  {
    CommandsRepositoryBuilder builder =
        CommandUtils.createDefaultBuilder(initData.getGroupId());

    AgentsInitData agentsInitData = initData.getAgentsInitData();

    for (int i = 0; i < agentsInitData.getFriendlyAgentsCount(); i++)
    {
      FriendlyAgentInitData agent = agentsInitData.getFriendlyAgentInitData(i);
      int agentId = agent.getAgentId();

      for (int j = 0; j < FILL_COUNT; j++)
      {
        if (rand.nextBoolean())
        {
          builder.addCommand(agentId,
              CommandTypesUtils.createAttackCommand(rand.nextInt(738) + 32, rand.nextInt(538) + 32));
        }
        else
        {
          builder.addCommand(agentId,
                             CommandTypesUtils.createMoveCommand(rand.nextInt(738) + 32, rand.nextInt(538) + 32));
        }
      }
    }

    CommandsRepository repository = builder.build();
    if (repository != null)
    {
      CommandsData commandsData = new CommandsData(repository);
      getInput().dataReceived(commandsData);
    }
  }
}