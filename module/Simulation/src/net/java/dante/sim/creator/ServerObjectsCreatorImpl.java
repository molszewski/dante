/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.creator;

import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.object.WeaponSystem;
import net.java.dante.sim.data.object.agent.ServerAgent;
import net.java.dante.sim.data.object.projectile.ProjectilesMediator;
import net.java.dante.sim.data.object.projectile.ServerProjectile;
import net.java.dante.sim.data.object.projectile.ServerProjectileInitData;
import net.java.dante.sim.data.template.TemplatesTypesStorage;
import net.java.dante.sim.util.math.MathUtils;
import net.java.dante.sim.util.math.Vector2d;

/**
 * Implementation of {@link AbstractObjectsCreator} creating objects at
 * the server's side.
 *
 * @author M.Olszewski
 */
class ServerObjectsCreatorImpl extends AbstractObjectsCreator
{
  /** Projectiles mediator. */
  private ProjectilesMediator projectilesMediator;


  /**
   * Creates instance of {@link ServerObjectsCreatorImpl} class creating
   * objects at the server's side.
   *
   * @param templatesStorage - templates types data storage.
   * @param objectsMediator - objects mediator.
   */
  ServerObjectsCreatorImpl(TemplatesTypesStorage templatesStorage,
      ObjectsMediator objectsMediator)
  {
    super(templatesStorage, objectsMediator);

    projectilesMediator = new ProjectilesMediator() {
      /**
       * @see net.java.dante.sim.data.object.projectile.ProjectilesMediator#createProjectile(int, int, double, double, double, double, net.java.dante.sim.data.object.WeaponSystem)
       */
      public void createProjectile(int ownerGroupId, int ownerId,
                                   double startX, double startY,
                                   double targetX, double targetY,
                                   WeaponSystem weaponSystem)
      {
        Vector2d velocity = MathUtils.calculateVelocity(
            weaponSystem.getMaxSpeed(), startX, startY, targetX, targetY);
        double requestedDistance = MathUtils.calculateVectorMagnitude(targetX - startX, targetY - startY);

        mediator.objectCreated(createObject(ServerProjectile.class, weaponSystem.getType(),
            new ServerProjectileInitData(ownerGroupId, ownerId,
                                         startX, startY,
                                         velocity.getX(), velocity.getY(),
                                         requestedDistance)));
      }
    };
  }


  /**
   * @see net.java.dante.sim.creator.AbstractObjectsCreator#initializeTemplates()
   */
  protected void initializeTemplates()
  {
    templates.put(ServerAgent.class, new ServerAgentTemplate(storage,
        new ServerWeaponSystemTemplate(storage, projectilesMediator)));
    templates.put(ServerProjectile.class, new ServerProjectileTemplate(storage));
  }
}