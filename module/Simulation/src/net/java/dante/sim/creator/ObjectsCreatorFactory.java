/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.creator;

import net.java.dante.sim.data.SimulationData;

/**
 * Factory class delivering instances of classes implementing 
 * {@link ObjectsCreator} interface.
 *
 * @author M.Olszewski
 */
public class ObjectsCreatorFactory
{
  /** The only existing instance of {@link ObjectsCreatorFactory}. */
  private static final ObjectsCreatorFactory instance = new ObjectsCreatorFactory();
  
  
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private ObjectsCreatorFactory()
  {
    // Intentionally left empty.
  }
  
  
  /**
   * Gets the only instance of this singleton class.
   * 
   * @return Returns the only instance of this singleton class.
   */
  public static ObjectsCreatorFactory getInstance()
  {
    return instance;
  }

  
  /**
   * Creates instance of class implementing {@link ObjectsCreator} interface
   * using specified templates types data storage.
   * 
   * @param simData - simulation's data.
   * 
   * @return Returns newly created instance of class implementing 
   *         {@link ObjectsCreator} interface using specified templates types 
   *         data storage.
   */
  public ObjectsCreator createObjectsCreator(SimulationData simData)
  {
    if (simData == null)
    {
      throw new NullPointerException("Specified simData is null!");
    }
    
    AbstractObjectsCreator creator = null;
    switch (simData.getType())
    {
      case FULL:
      {
        creator = new ServerObjectsCreatorImpl(simData.getTemplatesStorage(), 
            simData.getMediator());
        break;
      }
      
      case SIMPLE:
      {
        creator = new ClientObjectsCreatorImpl(simData.getTemplatesStorage(), 
            simData.getMediator());
        break;
      }

      default:
      {
        throw new IllegalArgumentException("Invalid argument type - type: '" + simData.getType().name() + "' is not supported!");
      }
    }
    
    // Initialize templates.
    creator.initializeTemplates();
    
    return creator;
  }
}