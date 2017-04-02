/*
 * Created on 2006-07-17
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data;

import java.util.HashSet;
import java.util.Set;

import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.SimulationObjectsGroup;
import net.java.dante.sim.data.template.TemplatesTypesStorage;


/**
 * Abstract simulation data storing simulation map, templates storage and
 * simulation objects in tree structure.
 *
 * @author M.Olszewski
 */
public abstract class AbstractSimulationData implements SimulationData
{
  /**
   * Root object - all other objects belonging to this {@link SimulationData}
   * are added to it or its children.
   */
  private SimulationObjectsGroup root = new SimulationObjectsGroup();

  /** Reference to object representing simulation map. */
  private SimulationMap map;
  /** Templates types data storage.  */
  private TemplatesTypesStorage storage;

  /** Set with {@link ObjectStateChangedListener} objects. */
  private Set<ObjectStateChangedListener> listeners = new HashSet<ObjectStateChangedListener>();


  /**
   * Creates instance of {@link ServerSimulationDataImpl} class.
   *
   * @param simMap - object representing simulation map.
   * @param templatesStorage - templates types data storage.
   *
   * @throws NullPointerException if any of arguments is <code>null</code>.
   */
  public AbstractSimulationData(SimulationMap simMap, TemplatesTypesStorage templatesStorage)
  {
    if (simMap == null)
    {
      throw new NullPointerException("Specified simMap is null!");
    }
    if (templatesStorage == null)
    {
      throw new NullPointerException("Specified objtemplatesStorageCreator is null!");
    }

    map     = simMap;
    storage = templatesStorage;
  }


  /**
   * @see net.java.dante.sim.data.SimulationData#getRoot()
   */
  public SimulationObjectsGroup getRoot()
  {
    return root;
  }

  /**
   * @see net.java.dante.sim.data.SimulationData#addObject(net.java.dante.sim.data.object.SimulationObject, net.java.dante.sim.data.object.SimulationObjectsGroup)
   */
  public void addObject(SimulationObject object, SimulationObjectsGroup parent)
  {
    if (object == null)
    {
      throw new NullPointerException("Specified object is null!");
    }
    if (parent == null)
    {
      throw new NullPointerException("Specified parent is null!");
    }

    parent.addChild(object);

    fireObjectAddedListeners(object);
  }

  /**
   * @see net.java.dante.sim.data.SimulationData#removeObject(net.java.dante.sim.data.object.SimulationObject)
   */
  public void removeObject(SimulationObject object)
  {
    if (object == null)
    {
      throw new NullPointerException("Specified object is null!");
    }

    SimulationObject parent = object.getParent();
    if (parent instanceof SimulationObjectsGroup)
    {
      ((SimulationObjectsGroup)parent).removeChild(object);
    }
    object.dispose();

    fireObjectRemovedListeners(object);
  }

  /**
   * @see net.java.dante.sim.data.SimulationData#getMap()
   */
  public SimulationMap getMap()
  {
    return map;
  }

  /**
   * @see net.java.dante.sim.data.SimulationData#getTemplatesStorage()
   */
  public TemplatesTypesStorage getTemplatesStorage()
  {
    return storage;
  }

  /**
   * @see net.java.dante.sim.data.SimulationData#addObjectAddedListener(net.java.dante.sim.data.ObjectStateChangedListener)
   */
  public void addObjectAddedListener(ObjectStateChangedListener listener)
  {
    if (listener == null)
    {
      throw new NullPointerException("Specified listener is null!");
    }

    listeners.add(listener);
  }

  /**
   * @see net.java.dante.sim.data.SimulationData#removeObjectAddedListener(net.java.dante.sim.data.ObjectStateChangedListener)
   */
  public void removeObjectAddedListener(ObjectStateChangedListener listener)
  {
    if (listener == null)
    {
      throw new NullPointerException("Specified listener is null!");
    }

    listeners.remove(listener);
  }

  /**
   * Fires listeners - object was added.
   *
   * @param object - added object.
   */
  protected void fireObjectAddedListeners(SimulationObject object)
  {
    for (ObjectStateChangedListener listener : listeners)
    {
      listener.objectAdded(object);
    }
  }

  /**
   * Fires listeners - object was removed.
   *
   * @param object - removed object.
   */
  protected void fireObjectRemovedListeners(SimulationObject object)
  {
    for (ObjectStateChangedListener listener : listeners)
    {
      listener.objectRemoved(object);
    }
  }

  /**
   * @see net.java.dante.sim.data.SimulationData#reset()
   */
  public void reset()
  {
    ObjectsTraverser.getDefault().traverse(root, new ResetActionListener());
  }


  /**
   * Class representing action setting data of all objects to their
   * initial state (reseting their data).
   *
   * @author M.Olszewski
   */
  class ResetActionListener implements ActionListener
  {
    /**
     * @see net.java.dante.sim.data.ActionListener#entryAction(net.java.dante.sim.data.object.SimulationObject)
     */
    public void entryAction(SimulationObject object)
    {
      // TODO Don't do anything?
    }

    /**
     * @see net.java.dante.sim.data.ActionListener#exitAction(net.java.dante.sim.data.object.SimulationObject)
     */
    public void exitAction(SimulationObject object)
    {
      // TODO reset state
    }
  }
}