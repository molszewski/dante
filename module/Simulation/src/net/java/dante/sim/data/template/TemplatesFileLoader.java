/*
 * Created on 2006-07-12
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.template;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.java.dante.sim.common.ResourceLoadFailedException;
import net.java.dante.sim.data.DataLoadingFailedException;
import net.java.dante.sim.data.common.FileLoader;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.template.types.AgentAttributesNames;
import net.java.dante.sim.data.template.types.AgentTemplateData;
import net.java.dante.sim.data.template.types.WeaponAttributesNames;
import net.java.dante.sim.data.template.types.WeaponTemplateData;
import net.java.dante.sim.util.InvalidValueException;
import net.java.dante.sim.util.SettingsReader;



/**
 * Class responsible for reading contents of agent's configuration file and
 * creating instance of {@link TemplatesTypesStorage} class from it.
 *
 * @author M.Olszewski
 */
class TemplatesFileLoader extends FileLoader implements TemplatesLoader
{
  private final static int AGENT_SIZE_ARRAY_LENGTH = 2;
  private final static int PROJECTILE_SIZE_ARRAY_LENGTH = 2;
  
  /**
   * Creates instance of {@link TemplatesFileLoader}.
   * 
   * @param agentFileName - file name with agent's configuration.
   */
  TemplatesFileLoader(String agentFileName)
  {
    super(agentFileName);
  }

  
  /** 
   * @see net.java.dante.sim.data.template.TemplatesLoader#loadTemplates()
   */
  public TemplatesTypesStorage loadTemplates() throws DataLoadingFailedException
  {
    TemplatesTypesStorage storage = null;
    
    try
    {
      Map<String, TemplateTypeData> agentsTypeData = new HashMap<String, TemplateTypeData>(1);
      Map<String, TemplateTypeData> weaponsTypeData = new HashMap<String, TemplateTypeData>(1);
      
      fillMaps(agentsTypeData, weaponsTypeData);
      
      Map<Class<? extends TemplateData>, ClassTypesDataStorage> classes = new HashMap<Class<? extends TemplateData>, ClassTypesDataStorage>(2); 
      classes.put(WeaponTemplateData.class, new ClassTypesDataStorageImpl(WeaponTemplateData.class, weaponsTypeData));
      classes.put(AgentTemplateData.class, new ClassTypesDataStorageImpl(AgentTemplateData.class, agentsTypeData));
      
      storage = new TemplatesTypesStorageImpl(classes);
    }
    catch (IOException e)
    {
      throw new DataLoadingFailedException("Templates types data cannot be loaded! " + e.getMessage(), e);
    }
    catch (InvalidValueException e)
    {
      throw new DataLoadingFailedException("Templates types data cannot be loaded! " + e.getMessage(), e);
    }
    
    return storage;
  }
  
  /**
   * Fills specified maps with association between names of templates types and 
   * proper agents types data and weapons types data.
   * At first agent's configuration file is read and all agent's settings are stored.
   * Then appropriate weapon system type data is created from file specified in
   * agent's configuration file and added to map with weapons templates types data. 
   * Finally agent's template type data is created and added to list with 
   * agents templates types data.
   * 
   * @param agentsTypeData - list that should be filled with agent's templates types data.
   * @param weaponsTypesData - list that should be filled with weapon system's templates types data.
   * 
   * @throws IOException if any I/O error occurred during reading agent's
   *         configuration file.
   */
  private void fillMaps(Map<String, TemplateTypeData> agentsTypeData, Map<String, TemplateTypeData> weaponsTypesData) throws IOException
  {
    Properties p = SettingsReader.readSettings(getURL());
    
    // 1. Read agent's data
    String agentType  = SettingsReader.readString(p.getProperty(AgentAttributesNames.AGENT_TYPE));
    int    maxHealth  = SettingsReader.readInt(p.getProperty(AgentAttributesNames.MAX_HEALTH));
    double maxSpeed   = SettingsReader.readDouble(p.getProperty(AgentAttributesNames.MAX_SPEED));
    double sightRange = SettingsReader.readDouble(p.getProperty(AgentAttributesNames.SIGHT_RANGE));
    String weaponFile = SettingsReader.readString(p.getProperty(AgentAttributesNames.WEAPON_SYSTEM_FILE));
    int[] agentSizeArray = SettingsReader.readIntsArray(
        p.getProperty(AgentAttributesNames.AGENT_SIZE), AGENT_SIZE_ARRAY_LENGTH);
    
    // 2. Read weapon's data - then agent's data can be created
    TemplateTypeData weapon = createWeaponTypeData(weaponFile);
    weaponsTypesData.put(weapon.getType(), weapon);
    
    agentsTypeData.put(agentType, new TemplateTypeDataImpl(
        agentType, new AgentTemplateData(
            maxHealth, maxSpeed, sightRange, 
            new ObjectSize(agentSizeArray[0], agentSizeArray[1]), weapon.getType())));
  }
  
  /**
   * Loads weapon system's template data from specified file and stores it in appropriate
   * format in specified {@link TemplateTypeData} object.
   * 
   * @param weaponFileName - weapon system's file name.
   * 
   * @return Returns name of weapon's type.
   * @throws IOException if any I/O error occurred during reading weapon system's
   *         configuration file.
   */
  private TemplateTypeData createWeaponTypeData(String weaponFileName) throws IOException
  {
    URL url = getClass().getClassLoader().getResource(weaponFileName.trim());
    if (url == null)
    {
      throw new ResourceLoadFailedException("Cannot find or access template file: " + weaponFileName.trim() + "!");
    }
    Properties p = SettingsReader.readSettings(url);
    
    String weaponType = SettingsReader.readString(p.getProperty(WeaponAttributesNames.WEAPON_TYPE));
    
    double range          = SettingsReader.readDouble(p.getProperty(WeaponAttributesNames.RANGE));
    double maxSpeed       = SettingsReader.readDouble(p.getProperty(WeaponAttributesNames.MAX_SPEED));
    double explosionRange = SettingsReader.readDouble(p.getProperty(WeaponAttributesNames.EXPLOSION_RANGE));
    int    minDmg         = SettingsReader.readInt(p.getProperty(WeaponAttributesNames.MIN_DAMAGE));
    int    maxDmg         = SettingsReader.readInt(p.getProperty(WeaponAttributesNames.MAX_DAMAGE));
    long   reloadTime     = SettingsReader.readLong(p.getProperty(WeaponAttributesNames.RELOAD_TIME));
    int[] projectileSize  = SettingsReader.readIntsArray(
        p.getProperty(WeaponAttributesNames.PROJECTILE_SIZE), PROJECTILE_SIZE_ARRAY_LENGTH);
    
    return new TemplateTypeDataImpl(weaponType, 
        new WeaponTemplateData(
            range, maxSpeed, explosionRange, minDmg, maxDmg, reloadTime,
            new ObjectSize(projectileSize[0], projectileSize[1])));
  }
}