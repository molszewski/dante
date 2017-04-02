/*
 * Created on 2006-07-16
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.template;

import java.util.HashMap;
import java.util.Map;

import net.java.dante.sim.data.DataLoadingFailedException;
import net.java.dante.sim.data.common.InitializationDataLoader;
import net.java.dante.sim.data.template.types.AgentTemplateData;
import net.java.dante.sim.data.template.types.WeaponTemplateData;
import net.java.dante.sim.io.init.InitializationData;


/**
 * Class responsible for reading contents of {@link InitializationData} object
 * and retrieving instance of {@link TemplatesTypesStorage} class from it.
 *
 * @author M.Olszewski
 */
class TemplatesInitializationDataLoader extends InitializationDataLoader
    implements TemplatesLoader
{
  /**
   * Creates instance of {@link TemplatesInitializationDataLoader}.
   *
   * @param loaderInputData - reference to {@link InitializationData} with
   *        requested data.
   */
  TemplatesInitializationDataLoader(InitializationData loaderInputData)
  {
    super(loaderInputData);
  }


  /**
   * @see net.java.dante.sim.data.template.TemplatesLoader#loadTemplates()
   */
  public TemplatesTypesStorage loadTemplates() throws DataLoadingFailedException
  {
    InitializationData initData = getInitData();

    Map<String, TemplateTypeData> agentsTypeData = new HashMap<String, TemplateTypeData>(1);
    agentsTypeData.put(initData.getAgentTemplateTypeData().getType(),
                       initData.getAgentTemplateTypeData());

    Map<String, TemplateTypeData> weaponsTypeData = new HashMap<String, TemplateTypeData>(1);
    weaponsTypeData.put(initData.getWeaponTemplateTypeData().getType(),
                       initData.getWeaponTemplateTypeData());

    Map<Class<? extends TemplateData>, ClassTypesDataStorage> classes = new HashMap<Class<? extends TemplateData>, ClassTypesDataStorage>(2);
    classes.put(WeaponTemplateData.class, new ClassTypesDataStorageImpl(WeaponTemplateData.class, weaponsTypeData));
    classes.put(AgentTemplateData.class, new ClassTypesDataStorageImpl(AgentTemplateData.class, agentsTypeData));

    return new TemplatesTypesStorageImpl(classes);
  }
}