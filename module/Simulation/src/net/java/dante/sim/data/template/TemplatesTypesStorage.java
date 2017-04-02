/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.template;


/**
 * Main storage for templates types data, containing {@link ClassTypesDataStorage} 
 * objects representing information about template types data for different 
 * classes. 
 * 
 * @author M.Olszewski
 */
public interface TemplatesTypesStorage
{
  /**
   * Gets size of this storage.
   * 
   * @return Returns size of this storage.
   */
  int getStorageSize();
  
  /**
   * Gets {@link ClassTypesDataStorage} object that stores templates types data
   * for specified class or <code>null</code> if respective 
   * {@link ClassTypesDataStorage} object cannot be found.
   * 
   * @param storageClass - class for which {@link ClassTypesDataStorage} object
   *        stores stores templates types data.
   * 
   * @return Returns specified {@link ClassTypesDataStorage} object or 
   *         <code>null</code> if it cannot be found.
   */
  ClassTypesDataStorage getClassStorage(Class<? extends TemplateData> storageClass);
}