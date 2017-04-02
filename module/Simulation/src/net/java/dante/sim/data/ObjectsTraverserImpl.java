/*
 * Created on 2006-07-14
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data;

import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.SimulationObjectsGroup;

/**
 * Default implementation of {@link ObjectsTraverser} class.
 * 
 * @author M.Olszewski
 */
class ObjectsTraverserImpl extends ObjectsTraverser
{
  /**
   * Default constructor with package access.
   */
  ObjectsTraverserImpl()
  {
    // Intentionally left empty.
  }
  
  /**
   * Starts tree traverse procedure, performing actions for each tree node 
   * on before traversing further and after traversing through all the children.
   * 
   * @param startObject - object from which traverse should be started.
   * @param action - action listener, callback object.
   */
  public void traverse(SimulationObject startObject, ActionListener action)
  {
    if (startObject == null)
    {
      throw new NullPointerException("Specified object is null!");
    }
    if (action == null)
    {
      throw new NullPointerException("Specified action is null!");
    }
    
    traverse0(startObject, action);
  }
  
  /**
   * Recursive method, called for all elements in the tree. For each object
   * two methods from {@link ActionListener} are called: one on entering
   * current node of the tree and second when leaving it (after traversing
   * through all node's children).
   * 
   * @param object - current object. 
   * @param action - action listener, callback object.
   */
  private void traverse0(SimulationObject object, ActionListener action)
  {
    if (object != null)
    {
      action.entryAction(object);
      
      if (object instanceof SimulationObjectsGroup)
      {
        SimulationObjectsGroup group = (SimulationObjectsGroup)object;
        for (int i = 0, size = group.getChildrenCount(); i < size; i++)
        {
          traverse0(group.getChild(i), action);
        }
      }
      
      action.exitAction(object);
    }
  }
}