/*
 * Created on 2006-08-13
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.common;


/**
 * Class holding information about data regarding animation frames (file name,
 * frames count, time between switching two frames).
 *
 * @author M.Olszewski
 */
public class AnimationFramesData
{
  /** Name of file with animation frames. */
  private String framesFile;
  /** Number of frames in file. */
  private int framesCount;
  /** Time after which frame should be switched. */
  private long framesDelay;


  /**
   * Creates instance of {@link AnimationFramesData} class with specified
   * parameters.
   *
   * @param animationFramesFile - the name of file with animation frames.
   * @param animationFramesCount - the number of animation frames.
   * @param animationFramesDelay - the time after which current frame should be
   *        switched.
   */
  public AnimationFramesData(String animationFramesFile,
      int animationFramesCount, long animationFramesDelay)
  {
    if (animationFramesFile == null)
    {
      throw new NullPointerException("Specified animationFramesFile is null!");
    }
    if (animationFramesCount <= 0)
    {
      throw new IllegalArgumentException("Invalid argument animationFramesCount - it must be positive integer!");
    }
    if (animationFramesDelay <= 0)
    {
      throw new IllegalArgumentException("Invalid argument spriteChangeDelay - it must be positive long integer!");
    }

    framesFile = animationFramesFile;
    framesCount = animationFramesCount;
    framesDelay = animationFramesDelay;
  }


  /**
   * Gets the name of file with animation frames.
   *
   * @return Returns the name of file with animation frames.
   */
  public String getFramesFile()
  {
    return framesFile;
  }

  /**
   * Gets the number of animation frames.
   *
   * @return Returns the number of animation frames.
   */
  public int getFramesCount()
  {
    return framesCount;
  }


  /**
   * Gets the time after which current frame should be switched.
   *
   * @return Returns the time after which current frame should be switched.
   */
  public long getFramesDelay()
  {
    return framesDelay;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + framesCount;
    result = PRIME * result + (int) (framesDelay ^ (framesDelay >>> 32));
    result = PRIME * result + framesFile.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof AnimationFramesData))
    {
      final AnimationFramesData other = (AnimationFramesData) object;
      equal = ((framesCount == other.framesCount) &&
               (framesDelay == other.framesDelay) &&
               (framesFile.equals(other.framesFile)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[framesFile=" + framesFile +
        "; framesCount=" + framesCount +
        "; framesDelay=" + framesDelay + "]");
  }
}