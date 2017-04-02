/*
 * Created on 2006-07-11
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.creator;

import java.util.HashMap;
import java.util.Map;

import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.template.TemplatesTypesStorage;


/**
 * Abstract class for all subclasses that are meant to create different 
 * {@link net.java.dante.sim.data.object.SimulationObject} objects. This abstract class uses 
 * {@link net.java.dante.sim.data.template.TemplatesTypesStorage} internally to obtain
 * proper templates data {@link net.java.dante.sim.data.template.TemplateData} for objects. 
 *
 * @author M.Olszewski
 */
public abstract class AbstractObjectsCreator implements ObjectsCreator
{
  /** Templates types data storage. */
  protected TemplatesTypesStorage storage;
  /** Association between subclasses and templates. */
  protected Map<Class<? extends SimulationObject>, SimulationObjectTemplate> templates = 
      new HashMap<Class<? extends SimulationObject>, SimulationObjectTemplate>();
  
  /** Objects mediator. */
  protected ObjectsMediator mediator;

  
  /**
   * Creates instance of {@link AbstractObjectsCreator} class using specified
   * templates types data storage to create new simulation objects instances
   * and specified objects mediator to create objects that are able to
   * notify simulation's data about changes in objects states. 
   * Protected constructor - only inheritance allowed.
   *
   * @param templatesStorage - templates types data storage.
   * @param objectsMediator - objects mediator.
   */
  protected AbstractObjectsCreator(TemplatesTypesStorage templatesStorage,
      ObjectsMediator objectsMediator)
  {
    if (templatesStorage == null)
    {
      throw new NullPointerException("Specified templatesStorage is null!");
    }
    if (objectsMediator == null)
    {
      throw new NullPointerException("Specified objectsMediator is null!");
    }
    
    storage = templatesStorage;
    mediator = objectsMediator;
  }
  
  
  /**
   * Performs initialization of templates.
   */
  protected abstract void initializeTemplates();
  
  /** 
   * @see net.java.dante.sim.creator.ObjectsCreator#createObject(java.lang.Class, java.lang.String, net.java.dante.sim.data.common.InitData)
   */
  public SimulationObject createObject(Class<? extends SimulationObject> simObjClass,
      String typeName, InitData initData)
  {
    if (simObjClass == null)
    {
      throw new NullPointerException("Specified simObjClass is null!");
    }
    if (typeName == null)
    {
      throw new NullPointerException("Specified typeName is null!");
    }
    if (typeName.length() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument typeName - it cannot be empty string!");
    }
    
    SimulationObjectTemplate template = templates.get(simObjClass);
    if (template == null)
    {
      throw new ObjectCreationFailedException("Cannot find templates types data storage for " + simObjClass.getName() + " class!");
    }
    
    return template.createObject(typeName, mediator, initData);
  }
  
  /** 
   * @see net.java.dante.sim.creator.ObjectsCreator#isSupported(java.lang.Class)
   */
  public boolean isSupported(Class<? extends SimulationObject> simObjClass)
  {
    if (simObjClass == null)
    {
      throw new NullPointerException("Specified simObjClass is null!");
    }
    
    return isClassSupported(simObjClass);
  }
  
  /**
   * @see net.java.dante.sim.creator.ObjectsCreator#isSupported(java.lang.Class, java.lang.String)
   */
  public boolean isSupported(Class<? extends SimulationObject> simObjClass, 
      String typeName)
  {
    if (simObjClass == null)
    {
      throw new NullPointerException("Specified simObjClass is null!");
    }
    if (typeName == null)
    {
      throw new NullPointerException("Specified typeName is null!");
    }
    if (typeName.length() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument typeName - it cannot be empty string!");
    }
    
    boolean supported = false;
    
    if (isClassSupported(simObjClass))
    {
      supported = templates.get(simObjClass).isSupported(typeName);
    }
    
    return supported;
  }
  
  /**
   * Checks whether specified subclass of {@link SimulationObject} class
   * is supported by this {@link ObjectsCreator}. 'Supported' means that
   * instance of specified class can be created by this {@link ObjectsCreator}.
   * 
   * @param simObjClass - subclass of {@link SimulationObject} class to check.
   * 
   * @return Returns <code>true</code> if specified subclass is supported,
   *         <code>false</code> otherwise.
   */
  private boolean isClassSupported(Class<? extends SimulationObject> simObjClass)
  {
    return templates.containsKey(simObjClass);
  }
}