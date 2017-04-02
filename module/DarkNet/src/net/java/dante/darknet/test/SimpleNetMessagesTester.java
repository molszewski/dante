/*
 * Created on 2006-08-27
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import net.java.dante.darknet.client.ClientNotConnectedException;
import net.java.dante.darknet.client.NetworkClient;
import net.java.dante.darknet.client.NetworkClientFactory;
import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.messaging.NetworkMessagesRegister;
import net.java.dante.darknet.server.NetworkServer;
import net.java.dante.darknet.server.NetworkServerFactory;
import net.java.dante.darknet.server.ServerStartFailedException;
import net.java.dante.darknet.session.Session;
import net.java.dante.darknet.session.SessionAdapter;


/**
 * Simple network messages tester, sending messages from one peer to another,
 * receives them and sends back. Finally sender compares sent and received
 * messages - if they are the same, test is considered as passed.
 *
 * @author M.Olszewski
 */
public class SimpleNetMessagesTester implements NetMessagesTester
{
  /** All registered messages providers. */
  private Set<MessagesProvider> providers = new HashSet<MessagesProvider>();
  /** Client running test. */
  private NetworkClient client = NetworkClientFactory.getInstance().initDefaultClient();
  /** Server running test. */
  private NetworkServer server = NetworkServerFactory.getInstance().initDefaultServer();
  /** Custom messages comparer. */
  MessagesComparer messagesComparer;


  /**
   * @see net.java.dante.darknet.test.NetMessagesTester#registerMessagesProvider(net.java.dante.darknet.test.MessagesProvider)
   */
  public void registerMessagesProvider(MessagesProvider messagesProvider)
  {
    providers.add(messagesProvider);
  }

  /**
   * @see net.java.dante.darknet.test.NetMessagesTester#unregisterMessagesProvider(net.java.dante.darknet.test.MessagesProvider)
   */
  public void unregisterMessagesProvider(MessagesProvider messagesProvider)
  {
    providers.remove(messagesProvider);
  }

  /**
   * @see net.java.dante.darknet.test.NetMessagesTester#test()
   */
  public void test()
  {
    test0();
  }

  /**
   * @see net.java.dante.darknet.test.NetMessagesTester#test(net.java.dante.darknet.test.MessagesComparer)
   */
  public void test(MessagesComparer comparer)
  {
    messagesComparer = comparer;

    test0();
  }

  private void test0()
  {
    Set<Class<? extends NetworkMessage>> registeredClasses = new HashSet<Class<? extends NetworkMessage>>();
    Set<NetworkMessage> messages = new HashSet<NetworkMessage>();
    int classId = 101;

    for (MessagesProvider provider : providers)
    {
      NetworkMessage[] providedMessages = provider.provide();
      for (int i = 0; i < providedMessages.length; i++)
      {
        NetworkMessage message = providedMessages[i];
        if (message != null)
        {
          if (!registeredClasses.contains(message.getClass()))
          {
            registerMessageClass(message.getClass(), classId);
            registeredClasses.add(message.getClass());
            classId++;
          }

          messages.add(message);
        }
        else
        {
          Logger.logInfo("Null message detected, skipping...");
        }
      }
    }

    if (messages.size() > 0)
    {
      runTest(messages);
    }
    else
    {
      Logger.logInfo("No messages provided. Tests not run.");
    }
  }

  /**
   * Registers the specified class inside client and server messaging systems
   * with the specified class identifier.
   *
   * @param registerClass the specified class.
   * @param classId the specified class identifier.
   */
  private void registerMessageClass(Class<? extends NetworkMessage> registerClass, int classId)
  {
    NetworkMessagesRegister clientRegister = client.getRegister();
    NetworkMessagesRegister serverRegister = server.getRegister();

    clientRegister.register(registerClass, classId);
    serverRegister.register(registerClass, classId);
  }

  /**
   * Run test for the specified set of messages.
   *
   * @param messages the specified set of messages.
   */
  private void runTest(Set<NetworkMessage> messages)
  {
    Object lock = new Object();
    ClientSessionHandler handler = new ClientSessionHandler(lock, messagesComparer);

    try
    {
      server.start(new ServerSessionHandler());
    }
    catch (ServerStartFailedException e)
    {
      Logger.logError("Server not started - tests not started.");
      throw e;
    }

    Session connection = null;
    try
    {
      connection = client.connect(handler);
    }
    catch (ClientNotConnectedException e)
    {
      Logger.logError("Client not connected - tests not started.");
      throw e;
    }

    for (NetworkMessage message : messages)
    {
      handler.startNewTest();
      connection.send(message);
      while (!handler.isTestCompleted())
      {
        synchronized(lock)
        {
          try
          {
            lock.wait();
          }
          catch (InterruptedException e)
          {
            throw new RuntimeException("InterruptedException exception caught while testing.");
          }
        }
      }
    }

    client.disconnect(false);
    server.stop(true);

    handler.logSummary(connection);
  }
}

/**
 * Simple logging class.
 *
 * @author M.Olszewski
 */
class Logger
{
  /**
   * Logs the specified text as information.
   *
   * @param text the specified text.
   */
  static void logInfo(String text)
  {
    System.err.println("[NetTester]" + text);
  }

  /**
   * Logs the specified text as error.
   *
   * @param text the specified text.
   */
  static void logError(String text)
  {
    System.err.println("[NetTester][ERROR]" + text);
  }
}

/**
 * Simple server's session handler re-sending all received messages.
 *
 * @author M.Olszewski
 */
class ServerSessionHandler extends SessionAdapter
{
  /**
   * @see net.java.dante.darknet.session.SessionAdapter#messageReceived(net.java.dante.darknet.session.Session, net.java.dante.darknet.messaging.NetworkMessage)
   */
  @Override
  public void messageReceived(Session session, NetworkMessage message)
  {
    session.send(message);
  }

  /**
   * @see net.java.dante.darknet.session.SessionAdapter#exceptionCaught(net.java.dante.darknet.session.Session, java.lang.Throwable)
   */
  @Override
  public void exceptionCaught(Session session, Throwable cause)
  {
    Logger.logError("Exception caught: " + cause + " in session: " + session + " !");
    cause.printStackTrace();
  }
}

/**
 * Simple client's session handler.
 *
 * @author M.Olszewski
 */
class ClientSessionHandler extends SessionAdapter
{
  /** Reference to sent message. */
  private AtomicReference<NetworkMessage> sentMessage = new AtomicReference<NetworkMessage>();
  /** Value indicating whether single test has been completed. */
  private AtomicBoolean testCompleted = new AtomicBoolean(false);
  /** Lock notified when test is completed. */
  private Object lock;
  /** Performed tests count. */
  private int testsCount = 0;
  /** Failed tests count. */
  private int testsFailedCount = 0;
  /** Passed tests count. */
  private int testsPassedCount = 0;
  /** Custom messages comparer. */
  MessagesComparer comparer;


  /**
   * Creates instance of {@link ClientSessionHandler} class.
   *
   * @param lockObject lock notified when test is completed.
   * @param messagesComparer - custom messages comparer.
   */
  ClientSessionHandler(Object lockObject, MessagesComparer messagesComparer)
  {
    lock = lockObject;
    comparer = messagesComparer;
  }


  /**
   * Starts new test.
   */
  void startNewTest()
  {
    testCompleted.set(false);
  }

  /**
   * Checks whether test was completed.
   *
   * @return Returns <code>true</code> if test was completed.
   */
  boolean isTestCompleted()
  {
    return testCompleted.get();
  }

  /**
   * @see net.java.dante.darknet.session.SessionAdapter#messageSent(net.java.dante.darknet.session.Session, net.java.dante.darknet.messaging.NetworkMessage)
   */
  @Override
  public void messageSent(Session session, NetworkMessage message)
  {
    sentMessage.set(message);
  }

  /**
   * @see net.java.dante.darknet.session.SessionAdapter#messageReceived(net.java.dante.darknet.session.Session, net.java.dante.darknet.messaging.NetworkMessage)
   */
  @Override
  public void messageReceived(Session session, NetworkMessage message)
  {
    NetworkMessage oldMessage = sentMessage.getAndSet(null);

    // Performs test
    boolean testResult = (comparer == null)? oldMessage.equals(message) : comparer.compare(oldMessage, message);

    if (testResult)
    {
      testsPassedCount++;
      Logger.logInfo("Test no." + (testsCount + 1) + " is PASSED.");
    }
    else
    {
      testsFailedCount++;
      Logger.logInfo("Test no." + (testsCount + 1) + " is FAILED.");
    }
    testsCount++;

    // Test is completed
    testCompleted.set(true);

    synchronized(lock)
    {
      lock.notify();
    }
  }

  /**
   * @see net.java.dante.darknet.session.SessionAdapter#exceptionCaught(net.java.dante.darknet.session.Session, java.lang.Throwable)
   */
  @Override
  public void exceptionCaught(Session session, Throwable cause)
  {
    Logger.logError("Exception caught: " + cause + " in session: " + session + " !");
    cause.printStackTrace();
  }


  /**
   * Logs simple summary.
   *
   * @param connection session representing connection between server and client.
   */
  void logSummary(Session connection)
  {
    Logger.logInfo("##########################################################");
    Logger.logInfo("NETWORK STATISTICS");
    Logger.logInfo("------------------");
    Logger.logInfo("Sent messages count:     " + connection.getSentMessagesCount());
    Logger.logInfo("Received messages count: " + connection.getReceivedMessagesCount());
    Logger.logInfo("Sent bytes count:        " + connection.getSentBytesCount());
    Logger.logInfo("Received bytes count:    " + connection.getReceivedBytesCount());
    Logger.logInfo("##########################################################");
    Logger.logInfo("TEST STATISTICS");
    Logger.logInfo("---------------");
    Logger.logInfo("PASSED: " + testsPassedCount);
    Logger.logInfo("FAILED: " + testsFailedCount);
    Logger.logInfo("--------");
    Logger.logInfo("TOTAL:  " + testsCount);
    Logger.logInfo("##########################################################");
  }
}