/*
 * Created on 2006-05-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.protocol;

/**
 * Protocol delivers two types of objects: 
 * <ul>
 * <li>{@link PacketsFetcher} by method {@link #createFetcher()},
 * <li>{@link PacketsSeparator} by method {@link #createSeparator()}. 
 * </ul>
 * Each invocation of methods listed above must provide new instance of 
 * specified class.
 *
 * @author M.Olszewski
 */
public interface Protocol
{
  /**
   * Creates new instance of {@link PacketsFetcher} class.
   * 
   * @return Returns new instance of {@link PacketsFetcher} class.
   */
  PacketsFetcher createFetcher();
  
  /**
   * Creates new instance of {@link PacketsSeparator} class.
   * 
   * @return Returns new instance of {@link PacketsSeparator} class.
   */
  PacketsSeparator createSeparator();
}