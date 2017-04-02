/*
 * Created on 2006-07-30
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.creator;

import net.java.dante.sim.data.object.ServerWeaponSystem;
import net.java.dante.sim.data.object.WeaponSystem;
import net.java.dante.sim.data.object.projectile.ProjectilesMediator;
import net.java.dante.sim.data.template.ClassTypesDataStorage;
import net.java.dante.sim.data.template.TemplateTypeData;
import net.java.dante.sim.data.template.TemplatesTypesStorage;
import net.java.dante.sim.data.template.types.WeaponTemplateData;

/**
 * Template class creating {@link net.java.dante.sim.data.object.ServerWeaponSystem} objects.
 *
 * @author M.Olszewski
 */
class ServerWeaponSystemTemplate implements WeaponSystemTemplate
{
  /** Class storage with weapon systems templates types data. */
  private ClassTypesDataStorage weaponsClassStorage;
  /** Projectiles mediator. */
  private ProjectilesMediator projectilesMediator;


  /**
   * Constructs instance of {@link ServerWeaponSystemTemplate} class with specified
   * templates types data storage.
   * 
   * @param templatesStorage - templates types data storage.
   * @param weaponProjectilesMediator - projectiles mediator for this weapon system.
   */
  ServerWeaponSystemTemplate(TemplatesTypesStorage templatesStorage, 
      ProjectilesMediator weaponProjectilesMediator)
  {
    if (weaponProjectilesMediator == null)
    {
      throw new NullPointerException("Specified weaponProjectilesMediator is null!");
    }
    
    weaponsClassStorage = templatesStorage.getClassStorage(WeaponTemplateData.class);
    if (weaponsClassStorage == null)
    {
      throw new IllegalArgumentException("Invalid argument templatesStorage - it must contain templates for WeaponSystem class!");
    }
    
    projectilesMediator = weaponProjectilesMediator;
  }
  

  /**
   * @see net.java.dante.sim.creator.WeaponSystemTemplate#createWeaponSystem(java.lang.String)
   */
  public WeaponSystem createWeaponSystem(String typeName) throws ObjectCreationFailedException
  {
    TemplateTypeData weaponTypeData = weaponsClassStorage.getTemplateTypeData(typeName);
    if (weaponTypeData == null)
    {
      throw new ObjectCreationFailedException("Cannot find template type data for '" + typeName + "' type!");
    }
    
    return new ServerWeaponSystem(typeName, (WeaponTemplateData)weaponTypeData.getData(), 
        projectilesMediator);
  }
  
  /**
   * @see net.java.dante.sim.creator.WeaponSystemTemplate#isSupported(java.lang.String)
   */
  public boolean isSupported(String typeName)
  {
    return (weaponsClassStorage.getTemplateTypeData(typeName) != null);
  }
}