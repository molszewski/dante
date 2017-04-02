/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.creator;

import java.util.Random;

import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.common.InitData;
import net.java.dante.sim.data.object.ServerWeaponSystem;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.projectile.ServerProjectile;
import net.java.dante.sim.data.object.projectile.ServerProjectileInitData;
import net.java.dante.sim.data.object.projectile.ServerProjectileState;
import net.java.dante.sim.data.template.ClassTypesDataStorage;
import net.java.dante.sim.data.template.TemplateTypeData;
import net.java.dante.sim.data.template.TemplatesTypesStorage;
import net.java.dante.sim.data.template.types.WeaponTemplateData;


/**
 * Class creating instances of {@link ServerProjectile} class with appropriate
 * {@link ServerWeaponSystem} objects using template data obtained from
 * {@link net.java.dante.sim.data.template.TemplatesTypesStorage}.
 *
 * @author M.Olszewski
 */
final class ServerProjectileTemplate implements SimulationObjectTemplate
{
  /** Class storage with weapon systems templates types data. */
  private ClassTypesDataStorage weaponsClassStorage;
  /** Random generator for damage. */
  private Random random = new Random();


  /**
   * Creates instance of {@link ServerProjectileTemplate} class.
   *
   * @param templatesStorage templates types data storage.
   */
  ServerProjectileTemplate(TemplatesTypesStorage templatesStorage)
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
    if (initData == null)
    {
      throw new NullPointerException("Specified initData is null!");
    }
    if (!(initData instanceof ServerProjectileInitData))
    {
      throw new IllegalArgumentException("Invalid argument initData - not an instance of ProjectileInitData class!");
    }

    TemplateTypeData weaponTypeData = weaponsClassStorage.getTemplateTypeData(typeName);
    if (weaponTypeData == null)
    {
      throw new ObjectCreationFailedException("Cannot find template type data for '" + typeName + "' type!");
    }

    return createProjectile(typeName, objectsMediator,
        (WeaponTemplateData)weaponTypeData.getData(), (ServerProjectileInitData)initData);
  }

  /**
   * Creates instance of {@link ServerProjectile} class, using specified
   * template data and initialization data (e.g. start position).
   *
   * @param typeName name of projectile's type.
   * @param objectsMediator objects mediator.
   * @param templateData weapon system's template data.
   * @param projectileData projectile's initialization data.
   *
   * @return Returns created instance of {@link ServerProjectile} class.
   */
  private ServerProjectile createProjectile(String typeName,
      ObjectsMediator objectsMediator, WeaponTemplateData templateData,
      ServerProjectileInitData projectileData)
  {
    int damage =
      random.nextInt(templateData.getMaxDamage() - templateData.getMinDamage() + 1) +
      templateData.getMinDamage();

    double distance = (projectileData.getRequestedDistance() < templateData.getRange())?
        projectileData.getRequestedDistance() : templateData.getRange();

    ServerProjectileState projectileState = new ServerProjectileState(
        typeName, projectileData.getOwnerGroupId(), projectileData.getOwnerId(),
        distance, templateData.getExplosionRange(), damage,
        templateData.getMaxSpeed(), templateData.getProjectileSize());

    projectileState.setX(projectileData.getX());
    projectileState.setY(projectileData.getY());
    projectileState.setSpeedX(projectileData.getSpeedX());
    projectileState.setSpeedY(projectileData.getSpeedY());

    return new ServerProjectile(projectileState, objectsMediator);
  }

  /**
   * @see net.java.dante.sim.creator.SimulationObjectTemplate#isSupported(java.lang.String)
   */
  public boolean isSupported(String typeName)
  {
    return (weaponsClassStorage.getTemplateTypeData(typeName) != null);
  }
}