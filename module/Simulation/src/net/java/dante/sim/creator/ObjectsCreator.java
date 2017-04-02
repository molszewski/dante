/*
 * Created on 2006-07-12
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.creator;

import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.data.object.SimulationObject;

/**
 * Interface for classes creating objects types of {@link SimulationObject} 
 * subclasses. For each subclass can exist more than one 'type' of an object - 
 * objects types contains the same parameters but their values can be 
 * different. Object's type must be unique within one subclass (two types
 * from different subclasses must be resolved correctly).
 *
 * @author M.Olszewski
 */
public interface ObjectsCreator
{
  /**
   * Creates object of specified {@link SimulationObject} subclass. There can exist
   * different types of objects for specified subclass. Type must be unique
   * within specified subclass.
   * This method should return valid object or throw an 
   * {@link ObjectCreationFailedException} exception in other case.
   * 
   * @param simObjClass - class indicating what kind of object should be created.
   * @param type - name of object's type to create. 
   * @param initData - external initialization data for object.
   * 
   * @return Returns reference to created object.
   * @throws ObjectCreationFailedException if object cannot be created.
   */
  SimulationObject createObject(Class<? extends SimulationObject> simObjClass,
      String type, InitData initData) throws ObjectCreationFailedException;
  
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
  boolean isSupported(Class<? extends SimulationObject> simObjClass);
  
  /**
   * Checks whether specified object's type being object of specified 
   * {@link SimulationObject} subclass is supported by this {@link ObjectsCreator}. 
   * 'Supported' means that instance of specified class can be created by 
   * this {@link ObjectsCreator}.
   * 
   * @param simObjClass - subclass of {@link SimulationObject} class to check.
   * @param type - name of object's type to create.
   * 
   * @return Returns <code>true</code> if specified object's type belonging 
   *         to specified subclass is supported, <code>false</code> otherwise.
   */
  boolean isSupported(Class<? extends SimulationObject> simObjClass, String type);
}