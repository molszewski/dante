/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.object;

import net.java.dante.sim.data.object.state.ObjectBaseState;

/**
 * State machine class responsible for moving objects modifying their state.
 *
 * @author M.Olszewski
 */
public final class MovementStateMachine
{
  /** Operated object's state. */
  ObjectBaseState state;
  /** Movement tracker used by this agent. */
  DistanceTracker tracker;
  /** Current state. */
  private State currentState;

  /** 'Not moving' state representation. */
  private State notMovingState = new State() {
    /**
     * @see net.java.dante.sim.data.object.MovementStateMachine.State#startMovement(double, double, double, double)
     */
    @Override
    void startMovement(double destinationX, double destinationY, double speedX, double speedY)
    {
      double totalSpeed = Math.sqrt((speedX * speedX) + (speedY * speedY));
      tracker = new DistanceTracker(totalSpeed,
                                    state.getX(), state.getY(),
                                    destinationX, destinationY);

      MovementStateMachine.this.setMovingState();
    }
  };

  /** 'Moving' state representation. */
  private State movingState = new State() {
    /**
     * @see net.java.dante.sim.data.object.MovementStateMachine.State#update(long)
     */
    @Override
    void update(long delta)
    {
      if (!tracker.isDestinationReached(delta))
      {
        double dx = (delta * state.getSpeedX()) / 1000.0;
        double dy = (delta * state.getSpeedY()) / 1000.0;

        state.setX(state.getX() + dx);
        state.setY(state.getY() + dy);
      }
      else
      {
        MovementStateMachine.this.setNotMovingState();
      }
    }
  };

  /**
   * Creates instance of {@link MovementStateMachine} class.
   *
   * @param objectState - state of object which will be updated.
   */
  public MovementStateMachine(ObjectBaseState objectState)
  {
    if (objectState == null)
    {
      throw new NullPointerException("Specified objectState is null!");
    }

    state = objectState;
    currentState = notMovingState;
  }


  /**
   * Sets 'moving' state.
   */
  public void setMovingState()
  {
    currentState = movingState;
  }

  /**
   * Sets 'not moving' state.
   */
  public void setNotMovingState()
  {
    currentState = notMovingState;
  }

  /**
   * Starts movement towards specified destination point ('x' and 'y') with
   * specified speed's components.
   *
   * @param destinationX - destination's 'x' coordinate.
   * @param destinationY - destination's 'y' coordinate.
   * @param speedX - object's horizontal speed.
   * @param speedY - object's horizontal speed.
   */
  public void startMovement(double destinationX, double destinationY,
      double speedX, double speedY)
  {
    currentState.startMovement(destinationX, destinationY, speedX, speedY);
  }

  /**
   * Update method, moving agent in 'moving' state.
   *
   * @param delta - time elapsed since last update.
   */
  public void update(long delta)
  {
    currentState.update(delta);
  }

  /**
   * Base state class for {@link MovementStateMachine} object.
   *
   * @author M.Olszewski
   */
  abstract class State
  {
    /**
     * Starts movement towards specified destination point ('x' and 'y') with
     * specified speed's components.
     *
     * @param destinationX - destination's 'x' coordinate.
     * @param destinationY - destination's 'y' coordinate.
     * @param speedX - object's horizontal speed.
     * @param speedY - object's horizontal speed.
     */
    void startMovement(double destinationX, double destinationY,
        double speedX, double speedY)
    {
      // Intentionally left empty.
    }

    /**
     * Base update method - intentionally left empty in base class.
     *
     * @param delta - time elapsed since last update.
     */
    void update(long delta)
    {
      // Intentionally left empty.
    }
  }
}
