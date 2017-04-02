/*
 * Created on 2006-07-11
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.creator;

import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.data.object.SimulationObject;

/**
 * Template's interface implemented by classes 
 * used to create objects of {@link SimulationObject} subclasses. 
 *
 * @author M.Olszewski
 */
interface SimulationObjectTemplate
{
  /**
   * Creates specified type of {@link SimulationObject} object using specified
   * initial data. This method should return valid object or throw an 
   * {@link ObjectCreationFailedException} exception in other case.
   * 
   * @param typeName - name of type.
   * @param objectsMediator - objects mediator.
   * @param initData - initial object's data.
   * 
   * @return Returns created object.
   * @throws ObjectCreationFailedException if object cannot be created.
   */
  SimulationObject createObject(String typeName, ObjectsMediator objectsMediator, InitData initData) throws ObjectCreationFailedException;
  
  /**
   * Checks whether specified object's type is supported by this {@link SimulationObjectTemplate}.
   * 'Supported' means that instance of specified class can be created by 
   * this {@link SimulationObjectTemplate}.
   * 
   * @param typeName - object's type to check.
   * 
   * @return Returns <code>true</code> if object's type is supported, 
   *         <code>false</code> otherwise.
   */
  boolean isSupported(String typeName);
}