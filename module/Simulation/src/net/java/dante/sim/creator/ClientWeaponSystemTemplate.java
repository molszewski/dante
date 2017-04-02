/*
 * Created on 2006-08-07
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.creator;

import net.java.dante.sim.data.object.ClientWeaponSystem;
import net.java.dante.sim.data.object.WeaponSystem;
import net.java.dante.sim.data.template.ClassTypesDataStorage;
import net.java.dante.sim.data.template.TemplateTypeData;
import net.java.dante.sim.data.template.TemplatesTypesStorage;
import net.java.dante.sim.data.template.types.WeaponTemplateData;

/**
 * Template class creating {@link net.java.dante.sim.data.object.ClientWeaponSystem}
 * objects.
 *
 * @author M.Olszewski
 */
class ClientWeaponSystemTemplate implements WeaponSystemTemplate
{
  /** Class storage with weapon systems templates types data. */
  private ClassTypesDataStorage weaponsClassStorage;

  
  /**
   * Constructs instance of {@link ClientWeaponSystemTemplate} class 
   * with specified templates types data storage.
   * 
   * @param templatesStorage - templates types data storage.
   */
  ClientWeaponSystemTemplate(TemplatesTypesStorage templatesStorage)
  {
    weaponsClassStorage = templatesStorage.getClassStorage(WeaponTemplateData.class);
    if (weaponsClassStorage == null)
    {
      throw new IllegalArgumentException("Invalid argument templatesStorage - it must contain templates for WeaponSystem class!");
    }
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
    
    return new ClientWeaponSystem(typeName, (WeaponTemplateData)weaponTypeData.getData());
  }

  /**
   * @see net.java.dante.sim.creator.WeaponSystemTemplate#isSupported(java.lang.String)
   */
  public boolean isSupported(String typeName)
  {
    return (weaponsClassStorage.getTemplateTypeData(typeName) != null);
  }
}
