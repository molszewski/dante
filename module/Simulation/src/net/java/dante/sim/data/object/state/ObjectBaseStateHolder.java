/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

import net.java.dante.sim.data.object.ObjectSize;

/**
 * Class holding data defined in {@link ObjectBaseState} interface, allowing
 * to modify object's coordinates and speed.
 * 
 * @author M.Olszewski
 */
public class ObjectBaseStateHolder implements ObjectBaseState
{
  /** Name of object's type. */
  private String type;
  /** Object's 'x' coordinate. */
  private double x;
  /** Object's 'y' coordinate. */
  private double y;
  /** Object's size. */
  private ObjectSize size;
  /** Object's horizontal speed. */
  private double speedX;
  /** Object's vertical speed. */
  private double speedY;
  

  /**
   * Creates instance of {@link ObjectBaseStateHolder} class holding data 
   * specified in {@link ObjectBaseState} interface.
   *
   * @param objectTypeName - name of object's type.
   * @param objectSize - object's size.
   */
  public ObjectBaseStateHolder(String objectTypeName, ObjectSize objectSize)
  {
    if (objectTypeName == null)
    {
      throw new NullPointerException("Specified objectTypeName is null!");
    }
    if (objectSize == null)
    {
      throw new NullPointerException("Specified objectSize is null!");
    }
    if (objectTypeName.length() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument objectTypeName - it cannot be empty string!");
    }
    
    type = objectTypeName;
    size = objectSize;
  }
  

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getType()
   */
  public String getType()
  {
    return type;
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getX()
   */
  public double getX()
  {
    return x;
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#setX(double)
   */
  public void setX(double newX)
  {
    x = newX;
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getY()
   */
  public double getY()
  {
    return y;
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#setY(double)
   */
  public void setY(double newY)
  {
    y = newY;
  }
  
  /** 
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getSize()
   */
  public ObjectSize getSize()
  {
    return size;
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getSpeedX()
   */
  public double getSpeedX()
  {
    return speedX;
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#setSpeedX(double)
   */
  public void setSpeedX(double newSpeedX)
  {
    speedX = newSpeedX;
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getSpeedY()
   */
  public double getSpeedY()
  {
    return speedY;
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#setSpeedY(double)
   */
  public void setSpeedY(double newSpeedY)
  {
    speedY = newSpeedY;
  }

  /** 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[type=" + type + "; x=" + x + "; y=" + y + "; size=" +
        size + "; speedX=" + speedX + "; speedY=" + speedY + "]");
  }
}