/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.template;

/**
 * Class containing information about single template type data:
 * name of the type and implementation of {@link TemplateData}, containing
 * data for specified template.
 * 
 * @author M.Olszewski
 */
public interface TemplateTypeData
{
  /**
   * Gets name of this template type. 
   * 
   * @return Returns name of this template type.
   */
  String getType();
  
  /**
   * Gets data of this template type.
   * 
   * @return Returns data of this template type.
   */
  TemplateData getData();
}