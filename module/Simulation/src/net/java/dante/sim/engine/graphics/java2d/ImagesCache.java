/*
 * Created on 2005-10-30
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.graphics.java2d;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.java.dante.sim.common.ResourceLoadFailedException;


/**
 * Image cache managing all images loaded from file. <p>
 * Cache is divided into two areas:
 * <ul>
 * <li>Persistent storage from where images will not be removed unless {@link #clear(boolean)}
 *     method will be invoked with <code>true</code> argument or <code>ImagesCache</code> 
 *     is (re)initialized (by calling {@link #init(int)} method).
 * <li>Images cache with limited size - if it is reached, eldest entry is removed.
 *     It is also cleared every time {@link #clear(boolean)} method is invoked
 *     or images cache is (re)initialized by calling {@link #init(int)} method.
 *
 * @author M.Olszewski
 */
public final class ImagesCache 
{
  /** The only existing instance of {@link ImagesCache}. */
  private static final ImagesCache instance = new ImagesCache();
  
  /**
   * Implementation of cache of images using Most Recently Used algorithm.
   * Maximum allowed images count is 
   * {@link #DEFAULT_MAX_IMAGES_COUNT}.
   *
   * @author M.Olszewski
   */
  private class ImagesCacheMap extends LinkedHashMap<String, BufferedImage[]>
  {
    private static final long serialVersionUID = 1L;
    /** Default maximum images count. */
    private static final int DEFAULT_MAX_IMAGES_COUNT = 16;
    /** Default load factor for super class. */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /** Maximum count of images in the cache.  */
    private final int maxCount;
    
    
    /**
     * Default constructor setting initial capacity and maximum number of images
     * stored in the cache to {@link #DEFAULT_MAX_IMAGES_COUNT}.
     */
    ImagesCacheMap()
    {
      this(DEFAULT_MAX_IMAGES_COUNT);
    }
    
    /**
     * Constructor calling {@link LinkedHashMap} constructor
     * with specified initial capacity equal to maximum number of images
     * that are allowed to be stored in the cache. 
     * 
     * @param maxImagesCount - maximum number of images stored in the cache.
     */
    ImagesCacheMap(int maxImagesCount)
    {
      super(maxImagesCount, DEFAULT_LOAD_FACTOR, true);
      
      maxCount = maxImagesCount;
    }
    

    /** 
     * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
     */
    @Override
    protected boolean removeEldestEntry(Entry<String, BufferedImage[]> entry)
    {
      return (size() > maxCount);
    }
  }
  
  /** Private images cache */
  private ImagesCacheMap imagesPool = new ImagesCacheMap();
  /** Persistent storage - only for most important images! */
  private Map<String, BufferedImage[]> persistent = new HashMap<String, BufferedImage[]>();
  

  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private ImagesCache()
  {
    // Empty
  }
  
  
  /**
   * Gets the only instance of this singleton class.
   * 
   * @return Returns the only instance of this singleton class.
   */
  public static ImagesCache getInstance()
  {
    return instance;
  }
  

  /**
   * Initializes this <code>ImagePool</code> with specified maximum size of cache.
   * After calling this method no images will be available in cache and 
   * persistent storage.
   * 
   * @param maxCacheSize - maximum number of images that can be stored in cache.
   */
  public void init(int maxCacheSize)
  {
    if (maxCacheSize <= 0)
    {
      throw new IllegalArgumentException("Invalid argument maxCacheSize - it must be positive integer!");
    }
    
    imagesPool = new ImagesCacheMap(maxCacheSize);
    persistent.clear();
  }
  
  /**
   * Clears this cache and - if specified - persistent storage.
   * 
   * @param clearPersistent - specifies whether persistent storage should be cleared.
   */
  public void clear(boolean clearPersistent)
  {
    imagesPool.clear();
    if (clearPersistent)
    {
      persistent.clear();
    }
  }
  
  /**
   * Gets array with {@link BufferedImage} objects representing requested 
   * images obtained from location specified by <code>path</code>.
   * 
   * @param path - path to a file with images.
   * @param columnsCount - count of images columns in file.
   * @param rowsCount - count of images rows in file.
   * @param transparency - image's transparency type.
   * 
   * @return Returns array with {@link BufferedImage} objects.
   */
  public BufferedImage[] loadImages(String path, final int columnsCount, final int rowsCount, final int transparency)
  {
    if (path == null)
    {
      throw new NullPointerException("Specified path is null!");
    }
    
    BufferedImage[] images = null;
    
    if (persistent.containsKey(path))
    {
      images = persistent.get(path);
    }
    else if (imagesPool.containsKey(path))
    {
      images = imagesPool.get(path);
    }
    else
    {
      images = getImages(path, columnsCount, rowsCount, transparency);
      imagesPool.put(path, images);
    }
    
    return images;
  }
  
  /**
   * Gets array with {@link BufferedImage} objects representing requested 
   * images obtained from location specified by <code>path</code>. 
   * 
   * @param path - path to a file with image(s).
   * @param columnsCount - count of images in file.
   * @param transparency - image's transparency type.
   * 
   * @return Returns array with {@link BufferedImage} objects.
   */
  public BufferedImage[] loadImages(String path, final int columnsCount, final int transparency)
  {
    return loadImages(path, columnsCount, 1, transparency);
  }
  
  /**
   * Gets single {@link BufferedImage} object representing requested 
   * image obtained from location specified by <code>path</code>.
   * 
   * @param path - path to a file with image.
   * @param transparency - image's transparency type.
   * 
   * @return Returns loaded {@link BufferedImage} object.
   */
  public BufferedImage loadImage(String path, final int transparency)
  {
    return (loadImages(path, 1, 1, transparency))[0];
  }
  
  /**
   * Adds to persistent storage array with {@link BufferedImage} objects 
   * representing images obtained from location specified by <code>path</code>. 
   * Images can be obtained afterwards by call to {@link #loadImages(String, int, int, int)}
   * method. If images from specified path are already stored they are removed from
   * persistent storage, reloaded and stored again.
   * This method returns obtained images.
   * 
   * @param path - path to image to store.
   * @param columnsCount - number of image columns.
   * @param rowsCount - number of image rows.
   * @param transparency - image's transparency type.
   * 
   * @return Returns array with {@link BufferedImage} objects.
   */
  public BufferedImage[] storeImage(String path, final int columnsCount, final int rowsCount, final int transparency)
  {
    if (path == null)
    {
      throw new NullPointerException("Specified path is null!");
    }
   
    if (persistent.containsKey(path))
    {
      persistent.remove(path);
    }
    
    BufferedImage[] images = getImages(path, columnsCount, rowsCount, transparency);
    persistent.put(path, images);
    
    return images;
  }
  
  /**
   * Adds to persistent storage array with {@link BufferedImage} objects 
   * representing images obtained from location specified by <code>path</code>. 
   * Images can be obtained afterwards by call to {@link #loadImages(String, int, int)}
   * method. If images from specified path are already stored they are removed from
   * persistent storage, reloaded and stored again.
   * This method returns obtained images.
   * 
   * @param path - path to image to store.
   * @param columnsCount - number of image columns.
   * @param transparency - image's transparency type.
   * 
   * @return Returns array with {@link BufferedImage} objects.
   */
  public BufferedImage[] storeImage(String path, final int columnsCount, final int transparency)
  {
    return storeImage(path, columnsCount, 1, transparency);
  }
  
  /**
   * Adds to persistent storage {@link BufferedImage} object 
   * representing image obtained from location specified by <code>path</code>. 
   * Image can be obtained afterwards by call to {@link #loadImage(String, int)}
   * method. If image from specified path is already stored it is removed from
   * persistent storage, reloaded and stored again.
   * This method returns obtained image.
   * 
   * @param path - path to image to store.
   * @param transparency - image's transparency type.
   * 
   * @return Returns loaded {@link BufferedImage} object.
   */
  public BufferedImage storeImage(String path, final int transparency)
  {
    return storeImage(path, 1, 1, transparency)[0];
  }
  
  /**
   * Converts specified path into URL and retrieves images from it.
   * 
   * @param path - path to image to store.
   * @param columnsCount - number of image columns.
   * @param rowsCount - number of image rows.
   * @param transparency - image's transparency type.
   * 
   * @return Returns obtained images.
   */
  private BufferedImage[] getImages(String path, final int columnsCount, final int rowsCount, final int transparency)
  {
    URL url = getClass().getClassLoader().getResource(path.trim());
    if (url == null)
    {
      throw new ResourceLoadFailedException("Cannot find or access image: " + path.trim() + "!");
    }
    
    BufferedImage[] images = ImageUtil.getImages(url, columnsCount, rowsCount, transparency);
    return images;
  }
}