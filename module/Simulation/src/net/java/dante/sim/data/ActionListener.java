/*
 * Created on 2006-07-14
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data;

import net.java.dante.sim.data.object.SimulationObject;

/**
 * Generic interface for actions performed during traversing tree of
 * {@link SimulationObject} instances.
 *
 * @author M.Olszewski
 */
public interface ActionListener
{
  /**
   * Entry action performed before traversing children of current
   * tree node.
   * 
   * @param object - current tree node.
   */
  void entryAction(SimulationObject object);
  
  /**
   * Exit action performed after traversing children of current
   * tree node.
   * 
   * @param object - current tree node.
   */
  void exitAction(SimulationObject object);
}