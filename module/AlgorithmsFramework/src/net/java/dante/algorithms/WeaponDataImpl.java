/*
 * Created on 2006-09-01
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms;


import net.java.dante.algorithms.data.WeaponData;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.template.types.WeaponTemplateData;

/**
 * Implementation of {@link net.java.dante.algorithms.data.WeaponData} interface.
 *
 * @author M.Olszewski
 */
class WeaponDataImpl implements WeaponData
{
  /** This weapon's template data. */
  private WeaponTemplateData weaponData;


  /**
   * Creates instance of {@link WeaponDataImpl} class.
   *
   * @param weaponTemplateData - this weapon's template data.
   */
  WeaponDataImpl(WeaponTemplateData weaponTemplateData)
  {
    if (weaponTemplateData == null)
    {
      throw new NullPointerException("Specified weaponTemplateData is null!");
    }

    weaponData = weaponTemplateData;
  }


  /**
   * @see net.java.dante.algorithms.data.WeaponData#getExplosionRange()
   */
  public double getExplosionRange()
  {
    return weaponData.getExplosionRange();
  }

  /**
   * @see net.java.dante.algorithms.data.WeaponData#getMaxDamage()
   */
  public int getMaxDamage()
  {
    return weaponData.getMaxDamage();
  }

  /**
   * @see net.java.dante.algorithms.data.WeaponData#getMaxSpeed()
   */
  public double getMaxSpeed()
  {
    return weaponData.getMaxSpeed();
  }

  /**
   * @see net.java.dante.algorithms.data.WeaponData#getMinDamage()
   */
  public int getMinDamage()
  {
    return weaponData.getMinDamage();
  }

  /**
   * @see net.java.dante.algorithms.data.WeaponData#getProjectileSize()
   */
  public ObjectSize getProjectileSize()
  {
    return weaponData.getProjectileSize();
  }

  /**
   * @see net.java.dante.algorithms.data.WeaponData#getRange()
   */
  public double getRange()
  {
    return weaponData.getRange();
  }

  /**
   * @see net.java.dante.algorithms.data.WeaponData#getReloadTime()
   */
  public long getReloadTime()
  {
    return weaponData.getReloadTime();
  }
}