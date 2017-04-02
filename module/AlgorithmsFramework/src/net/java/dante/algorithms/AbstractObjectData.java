/*
 * Created on 2006-08-31
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms;


import net.java.dante.algorithms.data.ObjectData;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.object.state.ObjectBaseState;
import net.java.dante.sim.data.object.state.ObjectBaseStateHolder;

/**
 * Implementation of {@link net.java.dante.algorithms.data.ObjectData} interface.
 *
 * @author M.Olszewski
 */
abstract class AbstractObjectData implements ObjectData
{
  private int objectId;
  private boolean alive = true;
  private double maxSpeed;
  private ObjectBaseState holder;


  /**
   * Creates instance of {@link AbstractObjectData} class.
   *
   * @param objectTypeName name of object's type.
   * @param objectIdentifier object's identifier.
   * @param objectSize object's size.
   * @param maxObjectSpeed maximum object's speed.
   */
  AbstractObjectData(String objectTypeName,
                     int objectIdentifier,
                     ObjectSize objectSize,
                     double maxObjectSpeed)
  {
    holder = new ObjectBaseStateHolder(objectTypeName, objectSize);

    maxSpeed = maxObjectSpeed;
    objectId = objectIdentifier;
  }

  /**
   * Gets name of object's type.
   *
   * @return Returns name of object's type.
   */
  public String getType()
  {
    return holder.getType();
  }

  /**
   * @see net.java.dante.algorithms.data.ObjectData#getId()
   */
  public int getId()
  {
    return objectId;
  }

  /**
   * @see net.java.dante.algorithms.data.ObjectData#getMaxSpeed()
   */
  public double getMaxSpeed()
  {
    return maxSpeed;
  }

  /**
   * @see net.java.dante.algorithms.data.ObjectData#getSize()
   */
  public ObjectSize getSize()
  {
    return holder.getSize();
  }

  /**
   * @see net.java.dante.algorithms.data.ObjectData#getSpeedX()
   */
  public double getSpeedX()
  {
    return holder.getSpeedX();
  }

  /**
   * Sets new object's horizontal speed.
   *
   * @param newSpeedX new object's horizontal speed.
   */
  void setSpeedX(double newSpeedX)
  {
    holder.setSpeedX(newSpeedX);
  }

  /**
   * @see net.java.dante.algorithms.data.ObjectData#getSpeedY()
   */
  public double getSpeedY()
  {
    return holder.getSpeedY();
  }

  /**
   * Sets new object's vertical speed.
   *
   * @param newSpeedY new object's vertical speed.
   */
  void setSpeedY(double newSpeedY)
  {
    holder.setSpeedY(newSpeedY);
  }

  /**
   * @see net.java.dante.algorithms.data.ObjectData#getX()
   */
  public double getX()
  {
    return holder.getX();
  }

  /**
   * Sets object's 'x' coordinate.
   *
   * @param newX new object's 'x' coordinate.
   */
  void setX(double newX)
  {
    holder.setX(newX);
  }

  /**
   * @see net.java.dante.algorithms.data.ObjectData#getY()
   */
  public double getY()
  {
    return holder.getY();
  }

  /**
   * Sets object's 'y' coordinate.
   *
   * @param newY new object's 'y' coordinate.
   */
  void setY(double newY)
  {
    holder.setY(newY);
  }

  /**
   * @see net.java.dante.algorithms.data.ObjectData#isAlive()
   */
  public boolean isAlive()
  {
    return alive;
  }

  /**
   * Sets operational status of object to destroyed. Further calls to
   * {@link #isAlive()} always returns <code>false</code>.
   */
  void destroyed()
  {
    alive = false;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[objectId=" + objectId + "; alive=" + alive +
        "; position=" + holder + "; maxSpeed=" + maxSpeed + "]");
  }
}