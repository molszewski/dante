/*
 * Created on 2006-07-04
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object;

import java.util.ArrayList;
import java.util.List;

import net.java.dante.sim.data.object.state.ObjectState;



/**
 * Class representing group of simulation objects.
 *
 * @author M.Olszewski
 */
public class SimulationObjectsGroup extends SimulationObject
{
  /** Children belonging to this group. */
  private List<SimulationObject> children = new ArrayList<SimulationObject>();
  

  /**
   * Creates instance of {@link SimulationObjectsGroup} class.
   */
  public SimulationObjectsGroup()
  {
    // Intentionally left empty.
  }
  
  /**
   * Creates instance of {@link SimulationObjectsGroup} class.
   * 
   * @param groupId - group's identifier.
   */
  public SimulationObjectsGroup(int groupId)
  {
    super(groupId);
  }
  
  /**
   * Updates state of all children belonging to this group.
   * 
   * @see net.java.dante.sim.data.object.SimulationObject#update(long)
   */
  @Override
  public void update(long elapsedTime)
  {
    for (int i = 0, size = children.size(); i < size; i++)
    {
      children.get(i).update(elapsedTime);
    }
  }

  /**
   * Adds specified child to this group.
   * 
   * @param child - child to add.
   */
  public void addChild(SimulationObject child)
  {
    if (children.add(child))
    {
      child.setParent(this);
    }
  }
  
  /**
   * Removes specified child from this group or - if child does not belong
   * to this group - immediately returns.
   * 
   * @param child - child to remove.
   */
  public void removeChild(SimulationObject child)
  {
    if (children.remove(child))
    {
      child.setParent(null);
    }
  }
  
  /**
   * Removes all children from this group.
   */
  public void removeAllChildren()
  {
    children.clear();
  }
  
  /**
   * Gets number of children.
   * 
   * @return Returns number of children.
   */
  public int getChildrenCount()
  {
    return children.size();
  }
  
  /**
   * Gets child with specified index.
   * 
   * @param index - index of requested child.
   * 
   * @return Returns child with specified index.
   */
  public SimulationObject getChild(int index)
  {
    return children.get(index);
  }
  
  /**
   * Always returns <code>null</code>. Agent group does
   * not contain any data. 
   * 
   * @see net.java.dante.sim.data.object.SimulationObject#getData()
   */
  @Override
  public ObjectState getData()
  {
    return null;
  }
}