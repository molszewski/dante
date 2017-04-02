/*
 * Created on 2006-05-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.protocol;

/**
 * Enumeration of protocols that are supported by DarkNet library.
 * At first only TCP will be supported, then support for UDP
 * (more suitable for games) will be added.
 *
 * @author M.Olszewski
 */
public enum SupportedProtocol
{
  /** Transmission Control Protocol */
  TCP;
  
  /**
   * Checks whether protocol specified by name is supported. 
   * 
   * @param protocolName - name of the protocol to check.
   * 
   * @return Returns <code>true</code> if protocol is supported, 
   *         <code>false</code> otherwise.
   *         
   * @throws NullPointerException if specified <code>protocolName</code>
   *         is <code>null</code>.
   */
  public static boolean isSupported(String protocolName)
  {
    boolean supported = false;
    
    if (protocolName != null)
    {
      for (SupportedProtocol sp : SupportedProtocol.values())
      {
        if (sp.name().equals(protocolName))
        {
          supported = true;
          break;
        }
      }
    }
    else
    {
      throw new NullPointerException("Specified protocolName is null!");
    }
    
    return supported;
  }
}