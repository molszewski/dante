/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.io.init;

/**
 * Interface defining friendly agent's initialization data. 
 *
 * @author M.Olszewski
 */
public interface FriendlyAgentInitData
{
  /**
   * Gets name of friendly agent's type.
   * 
   * @return Returns name of friendly agent's type.
   */
  String getType();
  
  /**
   * Gets friendly agent's identifier.
   * 
   * @return Returns friendly agent's identifier.
   */
  int getAgentId();
  
  /**
   * Gets friendly agent's start 'x' coordinate.
   * 
   * @return Returns friendly agent's start 'x' coordinate.
   */
  double getStartX();
  
  /**
   * Gets friendly agent's start 'y' coordinate.
   * 
   * @return Returns friendly agent's start 'y' coordinate.
   */
  double getStartY();
}