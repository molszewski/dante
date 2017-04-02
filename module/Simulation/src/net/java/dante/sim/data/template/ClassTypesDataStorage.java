/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.template;


/**
 * Classes storage containing {@link TemplateTypeData} objects representing
 * information about templates data for types specific for the class that
 * can be obtained by call to {@link #getStoredClass()} method.
 * 
 * @author M.Olszewski
 */
public interface ClassTypesDataStorage
{
  /**
   * Gets name of the class for which templates type data is stored 
   * by this {@link ClassTypesDataStorage}.
   * 
   * @return Returns name of the class for which templates type data is stored 
   *         by this {@link ClassTypesDataStorage}.
   */
  Class<? extends TemplateData> getStoredClass();
  
  /**
   * Gets size of this storage.
   * 
   * @return Returns size of this storage.
   */
  int getStorageSize();
  
  /**
   * Gets specified {@link TemplateTypeData} object.
   * 
   * @param type - type's name of {@link TemplateTypeData} object.
   * 
   * @return Returns specified {@link TemplateTypeData} object.
   */
  TemplateTypeData getTemplateTypeData(String type);
  
  /**
   * Sets specified {@link TemplateTypeData} object as default one, accessible
   * via {@link #getDefault()} method.
   * 
   * @param type - type's name of new default {@link TemplateTypeData} object.
   */
  void setDefault(String type);
  
  /**
   * Gets default {@link TemplateTypeData} object. Default object can be set
   * by call to {@link #setDefault(String)} method. 
   * If {@link #setDefault(String)} method was not called, 
   * implementations must assure that first available object is returned.
   * 
   * @return Returns default {@link TemplateTypeData} object.
   */
  TemplateTypeData getDefault();
}