/*
 * Created on 2006-06-08
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.object;

import net.java.dante.sim.data.object.projectile.ProjectilesMediator;
import net.java.dante.sim.data.template.types.WeaponTemplateData;
import net.java.dante.sim.engine.time.TimeCounter;

/**
 * Class representing weapon system for (@link sim.data.object.agent.ServerAgent}.
 *
 * @author M.Olszewski
 */
public class ServerWeaponSystem extends WeaponSystem
{
  /** Time counter for reloading time. */
  TimeCounter counter;
  /** State machine for this weapon system. */
  WeaponSystemStateMachine stateMachine = new WeaponSystemStateMachine();
  /** Projectiles mediator. */
  ProjectilesMediator projectilesMediator;


  /**
   * Creates object of {@link ServerWeaponSystem} with specified parameters.
   *
   * @param weaponType name of weapon's type.
   * @param weaponTemplateData weapon's template data.
   * @param weaponProjectilesMediator projectiles mediator for this weapon system.
   */
  public ServerWeaponSystem(String weaponType,
      WeaponTemplateData weaponTemplateData,
      ProjectilesMediator weaponProjectilesMediator)
  {
    super(weaponType, weaponTemplateData);

    if (weaponProjectilesMediator == null)
    {
      throw new NullPointerException("Specified weaponProjectilesMediator is null!");
    }

    projectilesMediator = weaponProjectilesMediator;
    counter = new TimeCounter(weaponTemplateData.getReloadTime());
  }


  /**
   * Updates state of this weapon system.
   *
   * @param delta time elapsed since last update.
   */
  public void update(long delta)
  {
    stateMachine.update(delta);
  }

  /**
   * Tries to shoot a projectile. Returns <code>true</code> if projectile
   * was shot.
   *
   * @param ownerGroupId identifier of projectile owner's group.
   * @param ownerId identifier of projecitle's owner.
   * @param startX projectile's start 'x' coordinate.
   * @param startY projectile's start 'y' coordinate.
   * @param targetX projectile's target 'x' coordinate.
   * @param targetY projectile's target 'y' coordinate.
   * @return Returns <code>true</code> if projectile was shot, <code>false</code>
   *         otherwise.
   */
  public boolean shoot(int ownerGroupId, int ownerId,
                       double startX, double startY,
                       double targetX, double targetY)
  {
    return stateMachine.shoot(ownerGroupId, ownerId, startX, startY, targetX, targetY);
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) +
        "; stateMachine=" + stateMachine + "]");
  }


  /**
   * Class representing state machine for this weapon system.
   *
   * @author M.Olszewski
   */
  private class WeaponSystemStateMachine
  {
    /** Reloaded state. */
    private State reloadedState = new ReloadedState();
    /** Reloading state. */
    private State reloadingState = new ReloadingState();
    /** Current state. */
    private State currentState;


    /**
     * Creates instance of {@link WeaponSystemStateMachine} class. It is in
     * 'reloaded' state.
     */
    WeaponSystemStateMachine()
    {
      currentState = reloadedState;
    }


    /**
     * Sets current state to 'reloaded'.
     */
    void setReloadedState()
    {
      currentState = reloadedState;
    }

    /**
     * Sets current state to 'reloading'.
     */
    void setReloadingState()
    {
      currentState = reloadingState;
    }

    /**
     * Updates this weapon system - if it is reloading, it will check
     * whether reloading time has passed.
     *
     * @param delta time elapsed since last update.
     */
    void update(long delta)
    {
      currentState.update(delta);
    }

    /**
     * Shoots a projectile if it is possible (weapon system is not reloading).
     * Returns <code>true</code> if projectile was shot, <code>false</code>
     * otherwise.
     *
     * @param ownerGroupId identifier of projectile owner's group.
     * @param ownerId identifier of projecitle's owner.
     * @param startX projectile's start 'x' coordinate.
     * @param startY projectile's start 'y' coordinate.
     * @param targetX projectile's target 'x' coordinate.
     * @param targetY projectile's target 'y' coordinate.
     *
     * @return Returns <code>true</code> if projectile was shot, <code>false</code>
     *         otherwise.
     */
    boolean shoot(int ownerGroupId, int ownerId,
                  double startX, double startY,
                  double targetX, double targetY)
    {
      return currentState.shoot(ownerGroupId, ownerId,
                                startX, startY,
                                targetX, targetY);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      return (getClass() + "[currentState=" + currentState + "]");
    }
  }

  /**
   * Base state class for weapon system states.
   *
   * @author M.Olszewski
   */
  class State
  {
    /**
     * Update method.
     *
     * @param delta time elapsed since last update.
     */
    void update(long delta)
    {
      //
    }

    /**
     * Shoot method.
     *
     * @param ownerGroupId identifier of projectile owner's group.
     * @param ownerId identifier of projecitle's owner.
     * @param startX projectile's start 'x' coordinate.
     * @param startY projectile's start 'y' coordinate.
     * @param targetX projectile's target 'x' coordinate.
     * @param targetY projectile's target 'y' coordinate.
     *
     * @return Returns <code>false</code> by default - action not performed.
     */
    boolean shoot(int ownerGroupId, int ownerId,
                  double startX, double startY,
                  double targetX, double targetY)
    {
      return false;
    }
  }


  /**
   * Class indicating 'reloaded' state of this weapon system.
   *
   * @author M.Olszewski
   */
  final class ReloadedState extends State
  {
    /**
     * @see net.java.dante.sim.data.object.ServerWeaponSystem.State#shoot(int, int, double, double, double, double)
     */
    @Override
    boolean shoot(int ownerGroupId, int ownerId,
                  double startX, double startY,
                  double targetX, double targetY)
    {
      projectilesMediator.createProjectile(ownerGroupId, ownerId,
                                           startX, startY,
                                           targetX, targetY,
                                           ServerWeaponSystem.this);
      stateMachine.setReloadingState();
      counter.reset();
      return true;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      return getClass().toString();
    }
  }

  /**
   * Class indicating 'reloading' state of this weapon system.
   *
   * @author M.Olszewski
   */
  final class ReloadingState extends State
  {
    /**
     * @see net.java.dante.sim.data.object.ServerWeaponSystem.State#update(long)
     */
    @Override
    void update(long delta)
    {
      if (counter.isActionTime(delta))
      {
        stateMachine.setReloadedState();
      }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      return getClass().toString();
    }
  }
}