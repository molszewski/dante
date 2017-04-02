/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.template;

import java.util.List;
import java.util.Map;


/**
 * Implementation of {@link TemplatesTypesStorage} storing 
 * {@link ClassTypesDataStorage} objects in the {@link List} object.
 * 
 * @author M.Olszewski
 */
class TemplatesTypesStorageImpl implements TemplatesTypesStorage
{
  /** 
   * Map with association between {@link Class} objects and 
   * {@link ClassTypesDataStorage} objects stored by this 
   * {@link TemplatesTypesStorageImpl} object.
   */
  private Map<Class<? extends TemplateData>, ClassTypesDataStorage> classTypesData;

  
  /**
   * Creates instance of {@link TemplatesTypesStorageImpl} class with specified
   * arguments.
   *
   * @param classTypesDataList - map with associations between {@link Class} 
   *        objects and {@link ClassTypesDataStorage} objects 
   *        that will be stored by this {@link TemplatesTypesStorageImpl} object.
   */
  TemplatesTypesStorageImpl(Map<Class<? extends TemplateData>, ClassTypesDataStorage> classTypesDataList)
  {
    if (classTypesDataList == null)
    {
      throw new NullPointerException("Specified classTypesDataList is null!");
    }
    if (classTypesDataList.size() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument classTypesDataList - it must contain at least 1 element!");
    }
    
    classTypesData = classTypesDataList;
  }

  /**
   * @see net.java.dante.sim.data.template.TemplatesTypesStorage#getStorageSize()
   */
  public int getStorageSize()
  {
    return classTypesData.size();
  }

  /**
   * @see net.java.dante.sim.data.template.TemplatesTypesStorage#getClassStorage(Class)
   */
  public ClassTypesDataStorage getClassStorage(Class<? extends TemplateData> storedClass)
  {
    return classTypesData.get(storedClass);
  }
}