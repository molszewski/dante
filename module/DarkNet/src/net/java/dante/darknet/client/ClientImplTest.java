/*
 * Created on 2006-05-22
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.client;

import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.server.NetworkServer;
import net.java.dante.darknet.server.NetworkServerFactory;
import net.java.dante.darknet.session.Session;
import net.java.dante.darknet.session.SessionAdapter;
import net.java.dante.darknet.session.SessionHandler;

import junit.framework.TestCase;

/**
 * Test class containing tests for {@link NetworkClientImpl} class.
 *
 * @author M.Olszewski
 */
public class ClientImplTest extends TestCase
{
  /** Server used for client connections in this test. */
  private NetworkServer server = null;
  
  static final class ServerSessionHandler extends SessionAdapter
  {
    /** 
     * @see net.java.dante.darknet.session.SessionHandler#messageReceived(net.java.dante.darknet.session.Session, net.java.dante.darknet.messaging.NetworkMessage)
     */
    public void messageReceived(Session session, NetworkMessage message)
    {
      System.out.println("Session=" + session);
      System.out.println("Received: " + message);
      
      session.send(message);
    }

    /** 
     * @see net.java.dante.darknet.session.SessionHandler#sessionClosed(net.java.dante.darknet.session.Session)
     */
    public void sessionClosed(Session session)
    {
      System.out.println("Server.Closed: " + session + " at time: " + System.currentTimeMillis());
    }

    /** 
     * @see net.java.dante.darknet.session.SessionHandler#sessionOpened(net.java.dante.darknet.session.Session)
     */
    public void sessionOpened(Session session)
    {
      System.out.println("Server.Opened: " + session + " at time: " + System.currentTimeMillis());
    }
  }
  
  static final class ClientSessionHandler extends SessionAdapter
  {
    /** 
     * @see net.java.dante.darknet.session.SessionHandler#messageReceived(net.java.dante.darknet.session.Session, net.java.dante.darknet.messaging.NetworkMessage)
     */
    public void messageReceived(Session session, NetworkMessage message)
    {
      System.out.println("Session=" + session);
      System.out.println("Received: " + message);
    }

    /** 
     * @see net.java.dante.darknet.session.SessionHandler#sessionClosed(net.java.dante.darknet.session.Session)
     */
    public void sessionClosed(Session session)
    {
      System.out.println("Client.Closed: " + session + " at time: " + System.currentTimeMillis());
    }

    /** 
     * @see net.java.dante.darknet.session.SessionHandler#sessionOpened(net.java.dante.darknet.session.Session)
     */
    public void sessionOpened(Session session)
    {
      System.out.println("Client.Opened: " + session + " at time: " + System.currentTimeMillis());
    }
  }
  
  /**
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
    server = NetworkServerFactory.getInstance().initDefaultServer();
    server.start(new ServerSessionHandler());
    server.getRegister().register(TestMessage.class, 1);
    server.getRegister().register(TestMessage2.class, 2);
    log("Server started");
  }

  /**
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception
  {
    server.stop(false);
  }

  /**
   * Test method for {@link net.java.dante.darknet.client.NetworkClientImpl#connect(SessionHandler)}.
   */
  public final void testConnect()
  {
    NetworkClient client = NetworkClientFactory.getInstance().initDefaultClient();
    client.getRegister().register(TestMessage.class, 1);
    client.getRegister().register(TestMessage2.class, 2);
    log("Client connected = " + client.isConnected());
    Session session = client.connect(new ClientSessionHandler());
    log("Client connected = " + client.isConnected());
    session.send(new TestMessage());
    session.send(new TestMessage2());
    
    try
    {
      Thread.sleep(25000);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
    
    client.disconnect(false);
  }

  /**
   * Test method for {@link net.java.dante.darknet.client.NetworkClientImpl#disconnect(boolean)}.
   */
  public final void testDisconnect()
  {
    // TODO Auto-generated method stub
  }

  /**
   * Test method for {@link net.java.dante.darknet.client.NetworkClientImpl#reconnect(boolean)}.
   */
  public final void testReconnectBoolean()
  {
    // TODO Auto-generated method stub
  }
  
  /**
   * Logs the specified text.
   * 
   * @param txt - the specified text.
   */
  public static void log(String txt)
  {
    System.out.println(txt);
    System.out.flush();
  }
}