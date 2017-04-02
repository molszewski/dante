/*
 * Created on 2006-07-19
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.graphics.java2d;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import net.java.dante.sim.common.ResourceLoadFailedException;


/**
 * Class with utilities for loading and splitting images from 
 * {@link URL} objects with specified types of transparency.
 *
 * @author M.Olszewski
 */
public final class ImageUtil
{
  /** 
   * Empty array with {@link BufferedImage} objects. 
   * Returned by {@link #splitImage(BufferedImage, int, int)}. 
   */
  private static final BufferedImage[] EMPTY_IMAGES_ARRAY = new BufferedImage[0];
  
  
  /**
   * Reads image from specified URL.
   * 
   * @param url - URL with image.
   * @param transparency - type of transparency.
   * 
   * @return Returns loaded image.
   * @throws ResourceLoadFailedException if image cannot be read
   *         from specified URL.
   */
  public static BufferedImage getImage(URL url, int transparency)
  {
    if (url == null)
    {
      throw new NullPointerException("Specified URL is null!");
    }
    if ((transparency != Transparency.BITMASK) && (transparency != Transparency.OPAQUE) && (transparency != Transparency.TRANSLUCENT))
    {
      throw new IllegalArgumentException("Invalid argument transparency - its value must be equal to Transparency.BITMASK, Transparency.OPAQUE or Transparency.TRANSLUCENT!");
    }
    
    return loadCompatibleImage(url, transparency);
  }
  
  /**
   * Reads image from specified URL and splits it into array of images. Images
   * will be split according to specified number of columns. 
   * This method can also return: 
   * <ul>
   * <li>empty array if <code>columnsCount</code> or 
   *     images dimensions are equal to zero or lesser than it,
   * <li>array with one element, original image if image is too small
   *     to be split or specified <code>columnsCount</code> are equal to one.
   * </ul>
   * 
   * @param url - URL with image.
   * @param columnsCount - number of columns.
   * @param transparency - type of transparency.
   * 
   * @return Returns loaded and split images.
   * @throws ResourceLoadFailedException if image cannot be read
   *         from specified URL.
   */
  public static BufferedImage[] getImages(URL url, int columnsCount, int transparency)
  {
    if (url == null)
    {
      throw new NullPointerException("Specified URL is null!");
    }
    if (columnsCount <= 0)
    {
      throw new IllegalArgumentException("Invalid argument columnsCount - it must be positive integer!");
    }
    if ((transparency != Transparency.BITMASK) && (transparency != Transparency.OPAQUE) && (transparency != Transparency.TRANSLUCENT))
    {
      throw new IllegalArgumentException("Invalid argument transparency - its value must be equal to Transparency.BITMASK, Transparency.OPAQUE or Transparency.TRANSLUCENT!");
    }
    
    return splitImage(loadCompatibleImage(url, transparency), columnsCount, 1);
  }
  
  /**
   * Reads image from specified URL and splits it into array of images. Images
   * will be split according to specified number of columns and rows. 
   * This method can also return: 
   * <ul>
   * <li>empty array if <code>columnsCount</code>, <code>rowsCount</code> or 
   *     images dimensions are equal to zero or lesser than it,
   * <li>array with one element, original image if image is too small
   *     to be split or specified <code>columnsCount</code> or 
   *     <code>rowsCount</code> are equal to one.
   * </ul>
   * 
   * @param url - URL with image.
   * @param columnsCount - number of columns.
   * @param rowsCount - number of rows.
   * @param transparency - type of transparency.
   * 
   * @return Returns loaded and split images.
   * @throws ResourceLoadFailedException if image cannot be read
   *         from specified URL.
   */
  public static BufferedImage[] getImages(URL url, int columnsCount, int rowsCount, int transparency)
  {
    if (url == null)
    {
      throw new NullPointerException("Specified URL is null!");
    }
    if (columnsCount <= 0)
    {
      throw new IllegalArgumentException("Invalid argument columnsCount - it must be positive integer!");
    }
    if (rowsCount <= 0)
    {
      throw new IllegalArgumentException("Invalid argument rowsCount - it must be positive integer!");
    }
    if ((transparency != Transparency.BITMASK) && (transparency != Transparency.OPAQUE) && (transparency != Transparency.TRANSLUCENT))
    {
      throw new IllegalArgumentException("Invalid argument transparency - its value must be equal to Transparency.BITMASK, Transparency.OPAQUE or Transparency.TRANSLUCENT!");
    }
    
    return splitImage(loadCompatibleImage(url, transparency), columnsCount, rowsCount);
  }
  
  /**
   * Loads image compatible with default configuration of screen device 
   * from specified URL with desired transparency type. 
   * 
   * @param url - URL with image.
   * @param transparency - transparency type of image.
   * 
   * @return Returns loaded image.
   * @throws ResourceLoadFailedException if image cannot be read
   *         from specified URL.
   */
  private static BufferedImage loadCompatibleImage(URL url, int transparency)
  {
    BufferedImage sourceImage = null;
    
    try
    {
      sourceImage = ImageIO.read(url);
    }
    catch (IOException e)
    {
      throw new ResourceLoadFailedException("Cannot read image: " + url + "!", e);
    }
    
    // Create an accelerated image of the right size to store our sprite in
    GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    BufferedImage image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), transparency);
    image.getGraphics().drawImage(sourceImage, 0, 0, null);
    
    return image;
  }
  
  /**
   * Splits specified instance of {@link BufferedImage} class into <code>columnsCount * rowsCount</code>
   * chunks and returns array with them. This method can return empty array if 
   * <code>columnsCount</code>, <code>rowsCount</code> or images dimensions
   * are equal to zero or lesser or array with one, original image if 
   * image is too small to be split or specified <code>columnsCount</code> or
   * <code>rowsCount</code> are equal to one.
   * 
   * @param image - image to be split.
   * @param columnsCount - number of chunks columns.
   * @param rowsCount - number of chunks rows.
   * 
   * @return Returns array with split images.
   */
  private static BufferedImage[] splitImage(BufferedImage image, int columnsCount, int rowsCount)
  {
    BufferedImage[] images = EMPTY_IMAGES_ARRAY;
    
    int imgWidth = image.getWidth();
    int imgHeight = image.getHeight();
    if ((imgWidth > 0) && (imgHeight > 0) && (columnsCount > 0) && (rowsCount > 0))
    {
      int chunkWidth = imgWidth / columnsCount;
      int chunkHeight = imgHeight / rowsCount;
      if (((chunkWidth > 0) && (chunkHeight > 0)) || ((columnsCount == 1) && (rowsCount == 1)))
      {
        images = new BufferedImage[columnsCount * rowsCount];
        
        for (int i = 0, counter = 0; i < columnsCount; i++)
        {
          for (int j = 0; j < rowsCount; j++)
          {
            images[counter] = image.getSubimage((i * chunkWidth), (j * chunkHeight), chunkWidth, chunkHeight);
            counter++;
          }
        }
      }
      else
      {
        images = new BufferedImage[] { image };
      }
    }
    
    return images;
  }

  
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private ImageUtil()
  {
    // Intentionally left empty.
  }
}