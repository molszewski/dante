/*
 * Created on 2006-07-04
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data;

import net.java.dante.sim.creator.ObjectsCreator;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.SimulationObjectsGroup;
import net.java.dante.sim.data.template.TemplatesTypesStorage;

/**
 * Generic interface with minimal set of methods for all classes
 * representing data of {@link net.java.dante.sim.Simulation}.
 *
 * @author M.Olszewski
 */
public interface SimulationData
{
  /**
   * Gets root object: first object in hierarchy, containing all other
   * objects.
   *
   * @return Returns root object.
   */
  SimulationObjectsGroup getRoot();

  /**
   * Adds specified {@link SimulationObject} as child of specified
   * {@link SimulationObjectsGroup} to this {@link SimulationData}.
   * Specified <code>object</code> and <code>parent</code> cannot be
   * <code>null</code> - in this case implementations of this method
   * should throw {@link NullPointerException}.
   *
   * @param object - instance of {@link SimulationObject} to be added to
   *        this {@link SimulationData}.
   * @param parent - parent of added object.
   *
   * @throws NullPointerException if <code>object</code> or <code>parent</code>
   *         are <code>null</code>s.
   */
  void addObject(SimulationObject object, SimulationObjectsGroup parent);

  /**
   * Removes specified {@link SimulationObject} from this {@link SimulationData}.
   * If specified object does not belong to this {@link SimulationData} this
   * method should return immediately.
   * Specified <code>object</code> cannot be <code>null</code> - in this case
   * implementations of this method should throw {@link NullPointerException}.
   *
   * @param object - instance of {@link SimulationObject} to be removed from
   *        this {@link SimulationData}.
   *
   * @throws NullPointerException if <code>object</code> or <code>parent</code>
   *         are <code>null</code>s.
   */
  void removeObject(SimulationObject object);

  /**
   * Gets representation of map on which simulation takes place.
   *
   * @return Returns representation of map on which simulation takes place.
   */
  SimulationMap getMap();

  /**
   * Gets templates types data storage containing initial data for newly
   * created simulation objects.
   *
   * @return Returns templates types data storage containing initial data for
   *         newly created simulation objects.
   */
  TemplatesTypesStorage getTemplatesStorage();

  /**
   * Gets objects mediator.
   *
   * @return Returns objects mediator.
   */
  ObjectsMediator getMediator();

  /**
   * Gets objects creator.
   *
   * @return Returns objects creator.
   */
  ObjectsCreator getCreator();

  /**
   * Gets instance of {@link GlobalData} representing global simulation
   * data.
   *
   * @return Returns instance of {@link GlobalData} representing global
   *         simulation data.
   */
  GlobalData getGlobalData();

  /**
   * Initializes simulation data - all required data is created.
   *
   * @param initData - initialization data.
   */
  void init(InitData initData);

  /**
   * Resets state of all objects in this {@link SimulationData} to initial
   * state.
   */
  void reset();

  /**
   * Adds listener notified about added objects.
   *
   * @param listener - listener to add.
   */
  void addObjectAddedListener(ObjectStateChangedListener listener);

  /**
   * Removes listener notified about added objects.
   *
   * @param listener - listener to remove.
   */
  void removeObjectAddedListener(ObjectStateChangedListener listener);

  /**
   * Gets type of this simulation data.
   *
   * @return Returns type of this simulation data.
   */
  SimulationDataType getType();
}