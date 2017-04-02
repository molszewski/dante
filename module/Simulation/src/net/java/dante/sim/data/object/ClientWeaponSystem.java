/*
 * Created on 2006-08-06
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object;

import net.java.dante.sim.data.template.types.WeaponTemplateData;

/**
 * Weapon system used by {@link net.java.dante.sim.data.object.agent.ClientFriendlyAgent} objects.
 * 
 * @author M.Olszewski
 */
public class ClientWeaponSystem extends WeaponSystem
{
  /**
   * Creates object of {@link ClientWeaponSystem} with specified parameters.
   * 
   * @param weaponType - name of weapon's type.
   * @param weaponTemplateData - weapon's template data.
   */
  public ClientWeaponSystem(String weaponType, 
      WeaponTemplateData weaponTemplateData)
  {
    super(weaponType, weaponTemplateData);
  }
}