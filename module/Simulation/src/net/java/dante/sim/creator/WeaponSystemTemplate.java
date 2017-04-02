/*
 * Created on 2006-08-07
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.creator;

import net.java.dante.sim.data.object.WeaponSystem;

/**
 * Template's interface implemented by classes 
 * used to create objects of {@link WeaponSystem} subclasses.
 * These templates are embedded into appropriate simulation objects templates. 
 *
 * @author M.Olszewski
 */
public interface WeaponSystemTemplate
{
  /**
   * Creates {@link WeaponSystem} object with specified type, using proper 
   * template data.
   * 
   * @param typeName - name of weapon system type.
   *  
   * @return Returns created {@link WeaponSystem} object with specified type, 
   *         using proper template data.
   * 
   * @throws ObjectCreationFailedException if {@link WeaponSystem} cannot be 
   *         created.
   */
  WeaponSystem createWeaponSystem(String typeName) throws ObjectCreationFailedException;
  
  /**
   * Checks whether specified weapon system's type is supported by this 
   * {@link WeaponSystemTemplate}.
   * 'Supported' means that instance of specified class can be created by 
   * this {@link WeaponSystemTemplate}.
   * 
   * @param typeName - weapon system's type to check.
   * 
   * @return Returns <code>true</code> if weapon system's type is supported, 
   *         <code>false</code> otherwise.
   */
  boolean isSupported(String typeName);
}