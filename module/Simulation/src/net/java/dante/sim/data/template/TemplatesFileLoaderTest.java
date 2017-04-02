/*
 * Created on 2006-07-13
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.template;

import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.template.types.AgentTemplateData;
import net.java.dante.sim.data.template.types.WeaponTemplateData;
import net.java.dante.sim.util.TempFilesList;
import net.java.dante.sim.util.TextUtils;

import junit.framework.TestCase;

/**
 * Test suite for {@link TemplatesFileLoader} class loading invalid and
 * valid {@link TemplateData} implementations from files.
 *
 * @author M.Olszewski
 */
public class TemplatesFileLoaderTest extends TestCase
{
  /** List with all valid agents configuration files. */
  private TempFilesList validAgentFiles = new TempFilesList();
  /** List with all valid weapons configuration files. */
  private TempFilesList validWeaponFiles = new TempFilesList();
  /** List with all invalid configuration files. */
//  private TempFilesList invalidFiles = new TempFilesList();

  private static final String[] VALID_WEAPONS_FILES = new String[] {
      "TYPE=laser\nRANGE=70.1\nMAX_SPEED=24.0\nEXPLOSION_RANGE=4.0\nMIN_DAMAGE=10\nMAX_DAMAGE=20\nRELOAD_TIME=900\nPROJECTILE_SIZE={\"8\",\"8\"}",
      "TYPE=cannon\nRANGE=42.3\nMAX_SPEED=20.0\nEXPLOSION_RANGE=2.0\nMIN_DAMAGE=5\nMAX_DAMAGE=15\nRELOAD_TIME=2000\nPROJECTILE_SIZE={\"8\",\"8\"}",
  };
  private static final String[] VALID_AGENTS_FILES = new String[] {
      "TYPE=robot\nMAX_HEALTH=100\nMAX_SPEED=10.0\nSIGHT_RANGE=80.3\nAGENT_SIZE={\"32\",\"32\"}\nWEAPON_SYSTEM_FILE=",
      "TYPE=killer\nMAX_HEALTH=150\nMAX_SPEED=18.8\nSIGHT_RANGE=94.5\nAGENT_SIZE={\"32\",\"32\"}\nWEAPON_SYSTEM_FILE=",
  };
  private static String[] agentTypes = new String[] {
    "robot",
    "killer",
  };

  private static String[] weaponTypes = new String[] {
    "laser",
    "cannon",
  };
  private static final AgentTemplateData[] AGENT_TYPE_DATA = new AgentTemplateData[] {
      new AgentTemplateData(100, 10.0, 80.3, new ObjectSize(32, 32), weaponTypes[0]),
      new AgentTemplateData(150, 18.8, 94.5, new ObjectSize(32, 32), weaponTypes[1])
  };
  private static final WeaponTemplateData[] WEAPON_TYPE_DATA = new WeaponTemplateData[] {
    new WeaponTemplateData(70.1, 24.0, 4.0, 10, 20, 900, new ObjectSize(8, 8)),
    new WeaponTemplateData(42.3, 20.0, 2.0, 5, 15,  2000, new ObjectSize(8, 8))
  };



  /**
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
    validWeaponFiles.fillList(VALID_WEAPONS_FILES);
    String[] modifiedAgentFiles = new String[VALID_AGENTS_FILES.length];
    for (int i = 0, size = validWeaponFiles.size(); i < size; i++)
    {
      modifiedAgentFiles[i] = VALID_AGENTS_FILES[i] + TextUtils.dos2unixPath(validWeaponFiles.get(i).getPath());
    }
    validAgentFiles.fillList(modifiedAgentFiles);
//    invalidFiles.fillList(INVALID_FILES);
  }

  /**
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception
  {
    validWeaponFiles.clear(true);
    validAgentFiles.clear(true);
//    invalidFiles.clear(true);
  }

  /**
   * Test method for {@link net.java.dante.sim.data.template.TemplatesFileLoader#loadTemplates()}
   * with valid files.
   */
  public final void testLoadCreator()
  {
    for (int i = 0, size = validAgentFiles.size(); i < size; i++)
    {
      TemplatesLoader loader = new TemplatesFileLoader(validAgentFiles.get(i).getName());
      TemplatesTypesStorage storage = loader.loadTemplates();
      assertNotNull(storage);

      ClassTypesDataStorage weaponClassStorage = storage.getClassStorage(WeaponTemplateData.class);
      assertNotNull(weaponClassStorage);
      assertEquals(weaponClassStorage.getStorageSize(), 1);
      assertEquals(weaponClassStorage.getStoredClass(), WeaponTemplateData.class);

      TemplateTypeData weaponTypeData = weaponClassStorage.getTemplateTypeData(weaponTypes[i]);
      assertEquals(weaponTypeData.getType(), weaponTypes[i]);
      assertEquals(weaponTypeData, weaponClassStorage.getDefault());
      WeaponTemplateData weaponData = (WeaponTemplateData)weaponTypeData.getData();
      assertTrue(weaponData.equals(WEAPON_TYPE_DATA[i]));

      ClassTypesDataStorage agentClassStorage = storage.getClassStorage(AgentTemplateData.class);
      assertNotNull(agentClassStorage);
      assertEquals(agentClassStorage.getStorageSize(), 1);
      assertEquals(agentClassStorage.getStoredClass(), AgentTemplateData.class);

      TemplateTypeData agentTypeData = agentClassStorage.getTemplateTypeData(agentTypes[i]);
      assertEquals(agentTypeData.getType(), agentTypes[i]);
      assertEquals(agentTypeData, agentClassStorage.getDefault());
      AgentTemplateData agentData = (AgentTemplateData)agentTypeData.getData();
      assertTrue(agentData.equals(AGENT_TYPE_DATA[i]));
    }
  }
}