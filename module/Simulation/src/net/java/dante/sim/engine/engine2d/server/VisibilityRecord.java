/*
 * Created on 2006-08-09
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d.server;

import java.util.ArrayList;
import java.util.List;

import net.java.dante.sim.engine.engine2d.Engine2dObject;
import net.java.dante.sim.event.EventsRepositoryBuilder;
import net.java.dante.sim.util.math.Rect2d;


/**
 * Class storing record of seen objects that do not belong to specified group
 * of agents.
 * 
 * @author M.Olszewski
 */
class VisibilityRecord
{
  /** List with all objects seen during last update. */
  private List<Engine2dObject> seenNowObjects = new ArrayList<Engine2dObject>();
  /** List with all objects seen before last update. */  
  private List<Engine2dObject> seenBeforeObjects = new ArrayList<Engine2dObject>();
  /** Parent object for this {@link VisibilityRecord} instance. */
  private Server2dAgentsGroup parent;
  
  /**
   * Creates instance of {@link VisibilityRecord} class with specified
   * parameters.
   *
   * @param parentGroup - parent group of agents.
   */
  VisibilityRecord(Server2dAgentsGroup parentGroup)
  {
    if (parentGroup == null)
    {
      throw new NullPointerException("Specified parentGroup is null!");
    }
    
    parent = parentGroup;
  }
  
  
  /**
   * Gets identifier of this agents group.
   * 
   * @return Returns identifier of this agents group.
   */
  int getGroupId()
  {
    return parent.getGroupId();
  }
  
  /**
   * Gets events repository builder for this group.
   * 
   * @return Returns events repository builder for this group.
   */
  EventsRepositoryBuilder getEventsBuilder()
  {
    return parent.getEventsBuilder();
  }
  
  /**
   * Gets this group's statistics.
   * 
   * @return Returns this group's statistics.
   */
  GroupStatistics getStatistics()
  {
    return parent.getStatistics();
  }
  
  /**
   * Determines whether the specified object was visible during last update.
   * 
   * @param object - the specified object.
   * 
   * @return Returns <code>true</code> if the specified object was visible 
   *         during last update, <code>false</code> otherwise.
   */
  boolean isVisible(Engine2dObject object)
  {
    return seenNowObjects.contains(object);
  }
  
  /**
   * Determines whether the specified object was visible before last update.
   * 
   * @param object - the specified object.
   * 
   * @return Returns <code>true</code> if the specified object was visible 
   *         before last update, <code>false</code> otherwise.
   */
  boolean wasVisible(Engine2dObject object)
  {
    return seenBeforeObjects.contains(object);
  }
  
  /**
   * Updates this visibility record for the specified object.
   * 
   * @param object - the specified object.
   * 
   * @return Returns value indicating whether any update was performed.
   */
  boolean update(Engine2dObject object)
  {
    boolean updated = false;
    
    if (!parent.getAgents().contains(object))
    {
      Rect2d bounds = object.getObjectBounds();
      double objectX = bounds.getX() + (bounds.getWidth() >> 1);
      double objectY = bounds.getY() + (bounds.getHeight() >> 1);
      
      boolean isSeenNow = false;
      for (Server2dAgent agent : parent.getAgents())
      {
        // Check whether agent sight circle contains central point of object
        if (agent.isActive() && agent.getSightCircle().contains(objectX, objectY))
        {
          isSeenNow = true;
          break;
        }
      }
      // Update only once
      updated = updateObjectStatus(object, isSeenNow);
    }
    
    return updated;
  }
  
  /**
   * Updates this visibility record using only moved agents 
   * for the specified object.
   * 
   * @param object - the specified object.
   * 
   * @return Returns value indicating whether any update was performed.
   */
  boolean updateOnlyMoved(Engine2dObject object)
  {
    boolean updated = false;
    
    if (!parent.getAgents().contains(object))
    {
      Rect2d bounds = object.getObjectBounds();
      double objectX = bounds.getX() + (bounds.getWidth() >> 1);
      double objectY = bounds.getY() + (bounds.getHeight() >> 1);
      
      boolean movedFound = false;
      boolean isSeenNow = false;
      for (Server2dAgent agent : parent.getAgents())
      {
        if (agent.isPositionChanged())
        {
          movedFound = true;
          // Check whether object contains central point of object
          if (agent.getSightCircle().contains(objectX, objectY))
          {
            isSeenNow = true;
            break;
          }
        }
      }
      
      if (movedFound)
      {
        updated = updateObjectStatus(object, isSeenNow);
      }
    }
    
    return updated;
  }
  
  /**
   * Updates status of the specified object in this visibility record.
   * 
   * @param object - the specified object
   * @param isNowVisible - value indicating whether object is now visible or not.
   * 
   * @return Returns value indicating whether any update was performed.
   */
  private boolean updateObjectStatus(Engine2dObject object, boolean isNowVisible)
  {
    boolean updated = true;
    
    seenBeforeObjects.remove(object);
    
    if (isNowVisible)
    {
      // It was visible once before? update before list.
      if (seenNowObjects.contains(object))
      {
//        if (!seenBeforeObjects.contains(object))
        {
          seenBeforeObjects.add(object);
        }
      }
      // It is visible only now? update now list
      else
      {
        seenNowObjects.add(object);
      }
    }
    else
    {
      // It was visible before? update now list.
      if (seenNowObjects.contains(object))
      {
        seenNowObjects.remove(object);
        seenBeforeObjects.add(object);
      }
      // It was not visible before? update before list.
//      else if (seenBeforeObjects.contains(object))
//      {
//        seenBeforeObjects.remove(object);
//      }
      else
      {
        updated = false;
      }
    }
    
    return updated;
  }
}