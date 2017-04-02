/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.template;

import java.util.HashMap;
import java.util.Map;


/**
 * Utility class capable of creating instances of 
 * {@link TemplateTypeData}, {@link ClassTypesDataStorage} and
 * {@link TemplatesTypesStorage} classes. This class can be used
 * externally.
 *
 * @author M.Olszewski
 */
public class TemplateDataUtils
{
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private TemplateDataUtils()
  {
    // Intentionally left empty.
  }
  
  
  /**
   * Creates instance of {@link TemplateTypeDataImpl} with specified arguments.
   *
   * @param typeName - name of the template type.
   * @param templateData - data for the template type.
   * 
   * @return Returns instance of {@link TemplateTypeData} class with specified 
   *         parameters.
   */
  public static TemplateTypeData createTemplateTypeData(String typeName, 
      TemplateData templateData)
  {
    return new TemplateTypeDataImpl(typeName, templateData);
  }
  
  /**
   * Creates instance of {@link ClassTypesDataStorage} class with specified
   * arguments.
   *
   * @param storedClass - templates types data will be stored for this class. 
   * @param templateTypesData - array with {@link TemplateTypeData} objects.

   * @return Returns instance of {@link ClassTypesDataStorage} class with 
   *         specified parameters.
   */
  public static ClassTypesDataStorage createClassTypesDataStorage(
      Class<? extends TemplateData> storedClass, 
      TemplateTypeData[] templateTypesData)
  {
    Map<String, TemplateTypeData> templateTypesDataMap = 
        new HashMap<String, TemplateTypeData>();
    
    for (int i = 0; i < templateTypesData.length; i++)
    {
      templateTypesDataMap.put(templateTypesData[i].getType(), templateTypesData[i]);
    }
    
    return new ClassTypesDataStorageImpl(storedClass, templateTypesDataMap);
  }
  
  /**
   * Creates instance of {@link TemplatesTypesStorage} class with specified
   * arguments.
   *
   * @param classTypesDataList - array with {@link ClassTypesDataStorage} objects.
   * 
   * @return Returns instance of {@link TemplatesTypesStorage} class with specified 
   *         parameters.
   */
  public static TemplatesTypesStorage createTemplatesTypesStorage(
      ClassTypesDataStorage[] classTypesDataList)
  {
    Map<Class<? extends TemplateData>, ClassTypesDataStorage> templateTypesDataMap = 
      new HashMap<Class<? extends TemplateData>, ClassTypesDataStorage>();
  
    for (int i = 0; i < classTypesDataList.length; i++)
    {
      templateTypesDataMap.put(classTypesDataList[i].getStoredClass(), classTypesDataList[i]);
    }
    
    return new TemplatesTypesStorageImpl(templateTypesDataMap);
  }
}