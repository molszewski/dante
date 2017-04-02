/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.creator;

import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.projectile.ClientProjectile;
import net.java.dante.sim.data.object.projectile.ClientProjectileInitData;
import net.java.dante.sim.data.object.projectile.ClientProjectileState;
import net.java.dante.sim.data.template.ClassTypesDataStorage;
import net.java.dante.sim.data.template.TemplateTypeData;
import net.java.dante.sim.data.template.TemplatesTypesStorage;
import net.java.dante.sim.data.template.types.WeaponTemplateData;

/**
 * Class creating instances of {@link ClientProjectile} class using appropriate 
 * {@link WeaponTemplateData} objects obtained from 
 * {@link net.java.dante.sim.data.template.TemplatesTypesStorage}.
 *
 * @author M.Olszewski
 */
final class ClientProjectileTemplate implements SimulationObjectTemplate
{
  /** Class storage with weapon systems templates types data. */
  private ClassTypesDataStorage weaponsClassStorage;

  /**
   * @param templatesStorage
   */
  ClientProjectileTemplate(TemplatesTypesStorage templatesStorage)
  {
    if (templatesStorage == null)
    {
      throw new NullPointerException("Specified templatesStorage is null!");
    }
    
    weaponsClassStorage = templatesStorage.getClassStorage(WeaponTemplateData.class);
    if (weaponsClassStorage == null)
    {
      throw new IllegalArgumentException("Invalid argument weaponsClassStorage - it must contain templates for WeaponSystem class!");
    }
  }
  

  /**
   * @see net.java.dante.sim.creator.SimulationObjectTemplate#createObject(java.lang.String, net.java.dante.sim.data.ObjectsMediator, net.java.dante.sim.data.common.InitData)
   */
  public SimulationObject createObject(String typeName, ObjectsMediator objectsMediator, 
      InitData initData) throws ObjectCreationFailedException
  {
    if (!(initData instanceof ClientProjectileInitData))
    {
      throw new IllegalArgumentException("Invalid argument initData - not an instance of ClientProjectileInitData class!");
    }
    
    TemplateTypeData weaponTypeData = weaponsClassStorage.getTemplateTypeData(typeName);
    if (weaponTypeData == null)
    {
      throw new ObjectCreationFailedException("Cannot find template type data for '" + typeName + "' type!");
    }
    
    return createProjectile(typeName, objectsMediator, 
        (WeaponTemplateData)weaponTypeData.getData(), 
        (ClientProjectileInitData)initData);
  }
  
  /**
   * Creates instance of {@link ClientProjectile} class, using specified 
   * template data and initialization data (e.g. start position).
   * 
   * @param typeName - name of weapon system's type.
   * @param objectsMediator - objects mediator.
   * @param templateData - projectile's template data.
   * @param initData - projectile's initialization data.
   * 
   * @return Returns created instance of {@link ClientProjectile} class.
   */
  private ClientProjectile createProjectile(String typeName, 
      ObjectsMediator objectsMediator, WeaponTemplateData templateData, 
      ClientProjectileInitData initData)
  {
    ClientProjectileState projectileState = 
        new ClientProjectileState(
            typeName, templateData.getProjectileSize(),
            templateData.getExplosionRange(), templateData.getMaxSpeed());
    
    projectileState.setX(initData.getX());
    projectileState.setY(initData.getY());
    projectileState.setSpeedX(initData.getSpeedX());
    projectileState.setSpeedY(initData.getSpeedY());
    
    return new ClientProjectile(initData.getId(), projectileState, 
        objectsMediator, initData.getCurrentTime());
  }
  
  /**
   * @see net.java.dante.sim.creator.SimulationObjectTemplate#isSupported(java.lang.String)
   */
  public boolean isSupported(String typeName)
  {
    return (weaponsClassStorage.getTemplateTypeData(typeName) != null);
  }
}