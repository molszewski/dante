/*
 * Created on 2006-05-06
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.messaging;

/**
 * Interface managing process of registration any subclasses of {@link NetworkMessage}
 * class. Any subclass of {@link NetworkMessage} class that you want to send and 
 * receive using DarkNet library should register itself using this interface's 
 * {@link #register(Class, int)} or {@link #register(NetworkMessage, int)}
 * method. Both sides: sender and receiver must register specified message
 * subclass with the same identifier to perform any communication.
 * <p>
 * <b>All {@link NetworkMessage} subclasses should be registered before server/client
 * message processing threads are started. If not, some of messages could not
 * be sent/received.</b>
 * <p> 
 * Not registering message class at any side will not cause any error in
 * process of sending/receiving messages. Only specific error message 
 * can be displayed during processing of messages.
 * <p>
 * Although there is {@link #register(Class, int)} 
 * (or {@link #register(NetworkMessage, int)}) method there is no way of unregistering
 * any specific {@link NetworkMessage} subclass other than clearing whole register
 * ({@link #clear()} method). However, clearing register is the last thing
 * you should do: although performing it during running server/client message
 * processing threads is safe, almost any received message will not be processed
 * correctly. 
 * <p>
 * Implementation of this class should be thread-safe. 
 *
 * @author M.Olszewski
 */
public interface NetworkMessagesRegister
{
  /**
   * Registers specified {@link NetworkMessage} subclass represented by its object 
   * in this {@link NetworkMessagesRegister}, so it could be used in DarkNet library.
   * 
   * @param message - object of {@link NetworkMessage} subclass to be registered.
   * @param subclassId - unique subclass identifier.
   * 
   * @throws NullPointerException if <code>message</code> is null.
   * @throws SubclassIdAlreadyRegisteredException if {@link NetworkMessage} 
   *         subclass identifier is already registered.
   * @throws SubclassAlreadyRegisteredException if {@link NetworkMessage} 
   *         subclass is already registered.  
   */
  public void register(NetworkMessage message, int subclassId);
  
  /**
   * Registers specified {@link NetworkMessage} subclass in this 
   * {@link NetworkMessagesRegister}, so it could be used in DarkNet library.
   * 
   * @param msgClass - {@link NetworkMessage} subclass to be registered.
   * @param subclassId - unique subclass identifier.
   * 
   * @throws NullPointerException if <code>msgClass</code> is null.
   * @throws SubclassIdAlreadyRegisteredException if {@link NetworkMessage} 
   *         subclass identifier is already registered.
   * @throws SubclassAlreadyRegisteredException if {@link NetworkMessage} 
   *         subclass is already registered. 
   */
  public void register(Class<? extends NetworkMessage> msgClass, int subclassId);
  
  /**
   * Clears whole messages register - beware, all messages types are removed, so
   * most of the DarkNet library functionality will be disabled.
   */
  public void clear();
  
  /**
   * Checks whether specified subclass of {@link NetworkMessage} represented by its 
   * object is registered.
   * 
   * @param message - {@link NetworkMessage} subclass object to check.
   * 
   * @return Returns <code>true</code> if specified {@link NetworkMessage} subclass 
   *         is registered or <code>false</code> otherwise.
   */
  public boolean isRegistered(NetworkMessage message);
  
  /**
   * Checks whether specified subclass of {@link NetworkMessage} is registered.
   * 
   * @param subclass - subclass of {@link NetworkMessage} to check.
   * 
   * @return Returns <code>true</code> if specified class is registered
   *         or <code>false</code> otherwise.
   */
  public boolean isRegistered(Class<? extends NetworkMessage> subclass);
  
  /**
   * Checks whether specified {@link NetworkMessage} subclass identifier (integer) 
   * is already in use.
   * 
   * @param subClassId - subclass identifier to check.
   * 
   * @return Returns <code>true</code> if specified subclass identifier is registered
   *         or <code>false</code> otherwise.
   */
  public boolean isUsed(int subClassId);
  
  /**
   * Attempts to construct {@link NetworkMessage} subclass using {@link NetworkMessage} 
   * nullary constructor. 
   * If {@link NetworkMessage} subclass is not registered in this 
   * {@link NetworkMessagesRegister} or its construction failed, then <code>null</code>
   * is returned. In case of successful construction new object of specified
   * {@link NetworkMessage} subclass is returned with proper subclass identifier set.  
   * 
   * @param subclassId - {@link NetworkMessage} subclass identifier, utilized to  
   *        construct object of this subclass. 
   * 
   * @return Returns object of {@link NetworkMessage} subclass identified by 
   *         <code>subclassId</code> parameter or <code>null</code> if 
   *         construction failed.
   */
  public NetworkMessage construct(int subclassId);
  
  /**
   * Gets subclass identifier for specified object of {@link NetworkMessage} subclass.
   * 
   * @param message - message which class's identifier will be obtained. 
   * 
   * @return Returns subclass identifier for specified object of 
   *         {@link NetworkMessage} subclass.
   * @throws IllegalStateException if specified {@link NetworkMessage} subclass is not
   *         registered.
   */
  public int getSubclassId(NetworkMessage message);
  
  /**
   * Gets subclass identifier of specified {@link NetworkMessage} subclass.
   * 
   * @param msgClass - {@link NetworkMessage} subclass which identifier will be obtained. 
   * 
   * @return Returns subclass identifier of specified {@link NetworkMessage} subclass.
   * @throws IllegalStateException if specified {@link NetworkMessage} subclass is not
   *         registered.
   */
  public int getSubclassId(Class<? extends NetworkMessage> msgClass);
}