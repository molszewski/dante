/*
 * Created on 2006-07-21
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.collision;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.java.dante.sim.engine.EngineObject;



/**
 * Implementation of collision detector, using simplified brute force method
 * to check all possible collisions. Only active objects are checked.
 *
 * @author M.Olszewski
 */
public class CollisionDetector
{
  /** List of objects checked against collisions with each other. */
  private List<EngineObject> checkedObjects = new ArrayList<EngineObject>();
  /** Set of collision checking modules. */
  private Set<CollisionModule> modules = new HashSet<CollisionModule>();


  /**
   * Creates instance of {@link CollisionDetector} class.
   */
  public CollisionDetector()
  {
    // Intentionally left empty.
  }


  /**
   * Removes all registered modules and added objects from this
   * {@link CollisionDetector} object.
   */
  public void clear()
  {
    modules.clear();
    checkedObjects.clear();
  }

  /**
   * Registers collision module. If collision module is already registered,
   * this method will return immediately.
   *
   * @param module - collision checking module.
   *
   * @throws NullPointerException if <code>module</code> is <code>null</code>.
   */
  public void registerModule(CollisionModule module)
  {
    if (module == null)
    {
      throw new NullPointerException("Specified module is null!");
    }

    modules.add(module);
  }

  /**
   * Unregisters collision module. If collision module is not registered,
   * this method will return immediately.
   *
   * @param module - collision checking module.
   *
   * @throws NullPointerException if <code>module</code> is <code>null</code>.
   */
  public void unregisterModule(CollisionModule module)
  {
    if (module == null)
    {
      throw new NullPointerException("Specified module is null!");
    }

    modules.remove(module);
  }

  /**
   * Adds object to list of objects that are checked against collisions.
   * The same object cannot be added more than once.
   *
   * @param object - object that will be checked against collisions.
   *
   * @throws NullPointerException if specified <code>object</code> is <code>null</code>.
   */
  public void addObject(EngineObject object)
  {
    if (object == null)
    {
      throw new NullPointerException("Specified object is null!");
    }

    if (!checkedObjects.contains(object))
    {
      checkedObjects.add(object);
    }
  }

  /**
   * Removes object from list of objects that are checked against collisions.
   * If object is already not on the list, this method returns immediately.
   *
   * @param object - object that will be removed from list of objects checked
   *        against collisions.
   *
   * @throws NullPointerException if specified <code>object</code> is <code>null</code>.
   */
  public void removeObject(EngineObject object)
  {
    if (object == null)
    {
      throw new NullPointerException("Specified object is null!");
    }

    checkedObjects.remove(object);
  }

  /**
   * Checks all possible collisions, using all registered modules and invoking
   * proper listeners.
   *
   * @throws IllegalStateException if no collision modules were registered.
   */
  public void checkCollisions()
  {
    if (modules.size() <= 0)
    {
      throw new IllegalStateException("No collision modules are registered!");
    }

    for (Iterator<CollisionModule> iter = modules.iterator(); iter.hasNext();)
    {
      checkCollisions0(iter.next());
    }
  }

  /**
   * Checks all possible collisions, using only one specified collision module
   * and invoking only listener associated with it. Specified collision module
   * does not have to be registered in this {@link CollisionDetector}.
   *
   * @param module - collision module that will check collisions.
   *
   * @throws NullPointerException if specified <code>module</code> is <code>null</code>.
   */
  public void checkCollisions(CollisionModule module)
  {
    if (module == null)
    {
      throw new NullPointerException("Specified module is null!");
    }

    checkCollisions0(module);
  }

  /**
   * Checks collisions using specified collision module.
   *
   * @param module - collision module checking collisions.
   */
  private void checkCollisions0(CollisionModule module)
  {
    CollisionListener[] listeners = module.getListeners();

    for (int i = 0, size = checkedObjects.size(); i < size; i++)
    {
      EngineObject object1 = checkedObjects.get(i);
      for (int j = (i + 1); j < size; j++)
      {
        EngineObject object2 = checkedObjects.get(j);
        if (module.checkCollision(object1, object2))
        {
          invokeListeners(listeners, object1, object2);
        }
      }
    }
  }

  /**
   * Checks all possible collisions between all objects stored in this
   * {@link CollisionDetector} and specified object,
   * using only one specified collision module and invoking only listener
   * associated with it. Specified collision module
   * does not have to be registered in this {@link CollisionDetector}.
   * Specified object does not have to be added to this {@link CollisionDetector}
   * before calling this method.
   *
   * @param module - collision module that will check collisions.
   * @param object - object checked against collisions with all objects
   *        stored inside this {@link CollisionDetector}.
   *
   * @throws NullPointerException if specified <code>module</code> is <code>null</code>.
   * @throws IllegalArgumentException if collision module was not registered.
   */
  public void checkCollisions(CollisionModule module, EngineObject object)
  {
    if (module == null)
    {
      throw new NullPointerException("Specified module is null!");
    }
    if (object == null)
    {
      throw new NullPointerException("Specified object is null!");
    }

    checkCollisions0(module, object);
  }

  /**
   * Checks collisions between all objects stored in this
   * {@link CollisionDetector} and specified object,
   * using specified collision module.
   *
   * @param module - collision module checking collisions.
   * @param object - object object checked against collisions with all objects
   *        stored inside this {@link CollisionDetector}.
   */
  private void checkCollisions0(CollisionModule module, EngineObject object)
  {
    CollisionListener[] listeners = module.getListeners();

    // Do not check inactive objects
    if (object.isActive())
    {
      for (int i = 0, size = checkedObjects.size(); i < size; i++)
      {
        EngineObject checkedObject = checkedObjects.get(i);
        if ((checkedObject != object) && module.checkCollision(object, checkedObject))
        {
          invokeListeners(listeners, object, checkedObject);
        }
      }
    }
  }

  /**
   * Invokes all listeners obtained from {@link CollisionModule} object.
   *
   * @param listeners - listeners obtained from {@link CollisionModule} object.
   * @param object1 - first collided object.
   * @param object2 - second collided object.
   */
  private void invokeListeners(CollisionListener[] listeners, EngineObject object1, EngineObject object2)
  {
    for (int i = 0; i < listeners.length; i++)
    {
      listeners[i].collisionDetected(object1, object2);
    }
  }
}