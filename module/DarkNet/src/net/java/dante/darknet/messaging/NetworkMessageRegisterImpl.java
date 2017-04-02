/*
 * Created on 2006-04-19
 *
 * @author M.Olszewski
 */

package net.java.dante.darknet.messaging;

import java.util.HashMap;
import java.util.Map;

import net.java.dante.darknet.common.Dbg;


/**
 * Thread-safe implementation of {@link NetworkMessagesRegister} interface.
 *
 * @author M.Olszewski
 */
class NetworkMessageRegisterImpl implements NetworkMessagesRegister
{
  /**
   * Register containing subclasses identifiers and classes associated with them.
   * Only classes extending {@link NetworkMessage} class are held.
   */
  private Map<Integer, Class<? extends NetworkMessage>> register = new HashMap<Integer, Class<? extends NetworkMessage>>();
  /**
   * Register containing classes and subclasses identifiers associated with them.
   * Only classes extending {@link NetworkMessage} class are held.
   * It is reversed version of {@link NetworkMessagesRegister} for faster
   * {@link #getSubclassId(Class)} or {@link #getSubclassId(NetworkMessage)} calls.
   */
  private Map<Class<? extends NetworkMessage>, Integer> reverseRegister = new HashMap<Class<? extends NetworkMessage>, Integer>();

  /** Internal lock for this class: to avoid any DoS attacks. */
  private Object lock = new Object();


  /**
   * Constructor of {@link NetworkMessageRegisterImpl} object.
   */
  NetworkMessageRegisterImpl()
  {
    // Empty.
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessagesRegister#register(net.java.dante.darknet.messaging.NetworkMessage, int)
   */
  public void register(NetworkMessage message, int subclassId)
  {
    if (message != null)
    {
      register(message.getClass(), subclassId);
    }
    else
    {
      throw new NullPointerException("Specified message is null!");
    }
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessagesRegister#register(java.lang.Class, int)
   */
  public void register(Class<? extends NetworkMessage> msgClass, int subclassId)
  {
    if (msgClass != null)
    {
      synchronized(lock)
      {
        Class<? extends NetworkMessage> registryClass = register.get(Integer.valueOf(subclassId));
        if (registryClass == null)
        {
          if (!reverseRegister.containsKey(msgClass))
          {
            Integer id = Integer.valueOf(subclassId);
            register.put(id, msgClass);
            reverseRegister.put(msgClass, id);
          }
          else
          {
            throw new SubclassAlreadyRegisteredException("Specified subclass of Message class: " + msgClass.getName() + " already registered!");
          }
        }
        else
        {
          if (msgClass != registryClass)
          {
            throw new SubclassIdAlreadyRegisteredException("Specified Subclass identifier: " + subclassId + " already registered for another Message subclass!");
          }
        }
      }
    }
    else
    {
      throw new NullPointerException("Specified msgClass is null!");
    }
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessagesRegister#clear()
   */
  public void clear()
  {
    synchronized(lock)
    {
      register.clear();
      reverseRegister.clear();
    }
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessagesRegister#isRegistered(net.java.dante.darknet.messaging.NetworkMessage)
   */
  public boolean isRegistered(NetworkMessage message)
  {
    boolean registered = false;
    if (message != null)
    {
      registered = isRegistered(message.getClass());
    }
    else
    {
      throw new NullPointerException("Specified message is null!");
    }

    return registered;
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessagesRegister#isRegistered(java.lang.Class)
   */
  public boolean isRegistered(Class<? extends NetworkMessage> subclass)
  {
    boolean registered = false;
    if (subclass != null)
    {
      synchronized(lock)
      {
        registered = reverseRegister.containsKey(subclass);
      }
    }
    else
    {
      throw new NullPointerException("Specified subclass is null!");
    }
    return registered;
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessagesRegister#isUsed(int)
   */
  public boolean isUsed(int subClassId)
  {
    boolean inUse = false;
    synchronized(lock)
    {
      inUse = register.containsKey(Integer.valueOf(subClassId));
    }
    return inUse;
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessagesRegister#construct(int)
   */
  public NetworkMessage construct(int subclassId)
  {
    NetworkMessage msg = null;

    Class<? extends NetworkMessage> msgClass = null;
    synchronized(lock)
    {
      msgClass = register.get(Integer.valueOf(subclassId));
    }

    if (msgClass != null)
    {
      try
      {
        // Call public nullary constructor.
        msg = msgClass.newInstance();
      }
      catch (Exception e)
      {
        if (Dbg.DBGE) Dbg.error("MessageRegisterImpl.construct(): constructing message with specified class identifier: " + subclassId + " failed.");
        e.printStackTrace();
      }
    }
    else
    {
      if (Dbg.DBGE) Dbg.error("MessageRegisterImpl.construct(): subclass identifier: " + subclassId + " is not registered in this MessageRegisterImpl!");
    }

    return msg;
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessagesRegister#getSubclassId(java.lang.Class)
   */
  public int getSubclassId(Class<? extends NetworkMessage> msgClass)
  {
    int subclassId = 0;

    if (msgClass != null)
    {
      synchronized(lock)
      {
        Integer value = reverseRegister.get(msgClass);
        if (value != null)
        {
          subclassId = value.intValue();
        }
        else
        {
          throw new IllegalStateException("Specified Message subclass: " + msgClass.getSimpleName() + " is not registered!");
        }
      }
    }
    else
    {
      throw new NullPointerException("Specified msgClass is null!");
    }

    return subclassId;
  }

  /**
   * @see net.java.dante.darknet.messaging.NetworkMessagesRegister#getSubclassId(net.java.dante.darknet.messaging.NetworkMessage)
   */
  public int getSubclassId(NetworkMessage message)
  {
    int subclassId = 0;

    if (message != null)
    {
      subclassId = getSubclassId(message.getClass());
    }
    else
    {
      throw new NullPointerException("Specified message is null!");
    }

    return subclassId;
  }
}