/*
 * Created on 2006-04-21
 *
 * @author M.Olszewski 
 */

package net.java.dante.darknet.protocol.packet;

/**
 * Interface for strategy pattern used to calculate new capacity of buffer
 * contained in e.g. {@link Packet} object.
 * <p>Please remember that result of {@link #newCapacity(int, int)} method
 * can be discarded by calling method under some circumstances e.g. 
 * when new capacity is not big enough to held required bytes.
 *
 * @author M.Olszewski
 */
public interface BufferResizeStrategy
{
  /**
   * Calculates new capacity of buffer that should be enough to hold
   * <code>requiredBytesNumber</code>. <code>oldCapacity</code> value
   * is redundant, but usually useful information.
   * In case when there is no possibility to return capacity that is
   * able to held required bytes number, zero or negative integer should
   * be returned. 
   * 
   * @param requiredBytesNumber - number of bytes that new capacity should be
   *        able to hold.
   * @param oldCapacity - old (current) capacity of buffer.
   * 
   * @return Returns new capacity of buffer that should be enough to hold
   *         required bytes number.
   */
  public int newCapacity(int requiredBytesNumber, int oldCapacity);
}
