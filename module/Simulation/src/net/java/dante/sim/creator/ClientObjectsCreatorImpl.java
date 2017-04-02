/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.creator;

import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.object.agent.ClientEnemyAgent;
import net.java.dante.sim.data.object.agent.ClientFriendlyAgent;
import net.java.dante.sim.data.object.projectile.ClientProjectile;
import net.java.dante.sim.data.template.TemplatesTypesStorage;

/**
 * Implementation of {@link AbstractObjectsCreator} creating objects at
 * the client's side.
 *
 * @author M.Olszewski
 */
class ClientObjectsCreatorImpl extends AbstractObjectsCreator
{
  /**
   * Creates instance of {@link ClientObjectsCreatorImpl} class creating 
   * objects at the client's side.
   *
   * @param templatesStorage - templates types data storage.
   * @param objectsMediator - objects mediator.
   */
  ClientObjectsCreatorImpl(TemplatesTypesStorage templatesStorage,
      ObjectsMediator objectsMediator)
  {
    super(templatesStorage, objectsMediator);
  }

  
  /** 
   * @see net.java.dante.sim.creator.AbstractObjectsCreator#initializeTemplates()
   */
  @Override
  protected void initializeTemplates()
  {
    ClientWeaponSystemTemplate weaponTemplate = 
        new ClientWeaponSystemTemplate(storage);
    
    templates.put(ClientFriendlyAgent.class, 
        new ClientFriendlyAgentTemplate(storage, weaponTemplate));
    templates.put(ClientEnemyAgent.class, 
        new ClientEnemyAgentTemplate(storage, weaponTemplate));
    templates.put(ClientProjectile.class, new ClientProjectileTemplate(storage));
  }
}