/*
 * Created on 2006-07-23
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object;

import java.util.Arrays;

/**
 * Simple class storing information about changed data 
 * between two updates of some system. This class stores all changes
 * record in <code>boolean</code> array - each element in the array determines
 * whether parameter with element's index was changed or not.
 *
 * @author M.Olszewski
 */
public class ChangeableData
{
  /** Stores changed parameters. */
  private boolean[] changes;
  /** Stores information whether any parameter was changed. */
  private int changedCount;
  
  
  /**
   * Creates instance of {@link ChangeableData} class.
   * 
   * @param changeableParamsCount - number of changeable parameters.
   */
  public ChangeableData(int changeableParamsCount)
  {
    if (changeableParamsCount <= 0)
    {
      throw new IllegalArgumentException("Invalid argument changedParamsCount - it must be positive integer!");
    }
    
    changes = new boolean[changeableParamsCount];
    resetChanges();
  }
  
  
  /**
   * Checks whether parameter with specified index has been changed since
   * last update.
   * 
   * @param index - index of parameter to check.
   * 
   * @return Returns <code>true</code> if parameter has been changed since 
   *         last update, <code>false</code> otherwise.
   */
  public boolean isChanged(int index)
  {
    return changes[index];
  }
  
  /**
   * Checks whether any parameter has been changed since last update.
   * 
   * @return Returns <code>true</code> if any parameter has been changed since 
   *         last update, <code>false</code> otherwise.
   */
  public boolean isAnythingChanged()
  {
    return (changedCount > 0);
  }
  
  /**
   * Gets number of changeable parameters.
   * 
   * @return Returns number of changeable parameters.
   */
  public int getParametersCount()
  {
    return changes.length;
  }
  
  /**
   * Marks specified parameter as changed.
   * 
   * @param index - index of parameter that has changed.
   */
  public void markAsChanged(int index)
  {
    if (!changes[index])
    {
      changes[index] = true;
      changedCount++;
      
      if (changedCount > changes.length)
      {
        throw new IllegalStateException("Illegal state of ChangeableData - changedCount greater than number of changeable parameters!");
      }
    }
  }
  
  /**
   * Unmarks specified parameter as changed.
   * 
   * @param index - index of parameter that is no longer considered as changed.
   */
  public void unmarkAsChanged(int index)
  {
    if (changes[index])
    {
      changes[index] = false;
      changedCount--;
      
      if (changedCount < 0)
      {
        throw new IllegalStateException("Illegal state of ChangeableData - changedCount less than zero!");
      }
    }
  }
  
  /**
   * This method should be invoked when update is completed and information
   * about changes was read. This method must be called before performing 
   * new update.
   */
  public void updateCompleted()
  {
    resetChanges();
  }
  
  /**
   * Resets all changes.
   */
  private void resetChanges()
  {
    Arrays.fill(changes, false);
    changedCount = 0;
  }

  /** 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[changedCount=" + changedCount + "; changes=" + changes + "]");
  }
}