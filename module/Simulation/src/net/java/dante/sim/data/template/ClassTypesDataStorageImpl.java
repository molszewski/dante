/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.template;

import java.util.Map;


/**
 * Implementation of {@link ClassTypesDataStorage} storing 
 * {@link TemplateTypeData} objects as {@link Map} object with associations 
 * between {@link TemplateTypeData} objects names and objects.
 * 
 * @author M.Olszewski
 */
class ClassTypesDataStorageImpl implements ClassTypesDataStorage
{
  /** Class for which templates types data will be stored. */
  private Class<? extends TemplateData> stored;
  /** Default {@link TemplateTypeData} object. */
  private TemplateTypeData defaultObject;
  /** 
   * Map with associations between types names and {@link TemplateTypeData} 
   * objects stored by this {@link ClassTypesDataStorageImpl} object.
   */
  private Map<String, TemplateTypeData> templatesTypesData;
  
  
  /**
   * Creates instance of {@link ClassTypesDataStorageImpl} class with specified
   * arguments.
   *
   * @param storedClass - templates types data will be stored for this class. 
   * @param templatesTypesDataMap - map with associations between types names 
   *        and {@link TemplateTypeData} objects that will be stored by 
   *        this {@link ClassTypesDataStorageImpl} object.
   */
  ClassTypesDataStorageImpl(Class<? extends TemplateData> storedClass, 
      Map<String, TemplateTypeData> templatesTypesDataMap)
  {
    if (storedClass == null)
    {
      throw new NullPointerException("Specified storedClass is null!");
    }
    if (templatesTypesDataMap == null)
    {
      throw new NullPointerException("Specified templatesTypesDataList is null!");
    }
    if (templatesTypesDataMap.size() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument templatesTypesDataList - it must contain at least 1 element!");
    }
    
    stored = storedClass;
    templatesTypesData = templatesTypesDataMap;
    defaultObject = templatesTypesDataMap.values().iterator().next();
  }

  
  /**
   * @see net.java.dante.sim.data.template.ClassTypesDataStorage#getStoredClass()
   */
  public Class<? extends TemplateData> getStoredClass()
  {
    return stored;
  }

  /**
   * @see net.java.dante.sim.data.template.ClassTypesDataStorage#getStorageSize()
   */
  public int getStorageSize()
  {
    return templatesTypesData.size();
  }

  /**
   * @see net.java.dante.sim.data.template.ClassTypesDataStorage#getTemplateTypeData(String)
   */
  public TemplateTypeData getTemplateTypeData(String typeName)
  {
    if (typeName == null)
    {
      throw new NullPointerException("Specified typeName is null!");
    }
    if (typeName.length() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument typeName - it cannot be empty string!");
    }
    
    return templatesTypesData.get(typeName);
  }

  /**
   * @see net.java.dante.sim.data.template.ClassTypesDataStorage#getDefault()
   */
  public TemplateTypeData getDefault()
  {
    return defaultObject;
  }

  /**
   * @see net.java.dante.sim.data.template.ClassTypesDataStorage#setDefault(String)
   */
  public void setDefault(String typeName)
  {
    if (typeName == null)
    {
      throw new NullPointerException("Specified typeName is null!");
    }
    if (typeName.length() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument typeName - it cannot be empty string!");
    }
    if (!templatesTypesData.containsKey(typeName))
    {
      throw new IllegalArgumentException("Invalid argument typeName - it must be one of supported names of template types data!");
    }
    
    defaultObject = templatesTypesData.get(typeName);
  }
}