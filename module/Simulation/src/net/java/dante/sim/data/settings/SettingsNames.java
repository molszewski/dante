/*
 * Created on 2006-07-05
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.settings;

/**
 * Names of all general settings configuring simulation.
 *
 * @author M.Olszewski
 */
public final class SettingsNames
{
  /** Attribute indicating whether battle will be recorded or not. */
  public static final String RECORD_BATTLE = "RECORD_BATTLE";
  /** Attribute indicating whether battle will be evaluated or not. */
  public static final String EVALUATE_BATTLE = "EVALUATE_BATTLE";
  /** Attribute indicating duration of single battle. */
  public static final String BATTLE_DURATION = "BATTLE_DURATION";
  /** Attribute indicating interval between two updates (in milliseconds). */
  public static final String UPDATES_INTERVAL = "UPDATES_INTERVAL";
  /** Attribute indicating interval between sending update messages (in milliseconds). */
  public static final String SEND_UPDATE_MESSAGES_INTERVAL = "SEND_UPDATE_MESSAGES_INTERVAL";

  /** Attribute indicating where file with map definition is located. */
  public static final String MAP_FILE = "MAP_FILE";
  /** Attribute indicating file with background's image. */
  public static final String BACKGROUND_IMAGE_FILE = "BACKGROUND_IMAGE_FILE";
  /** Attribute indicating file with map's tile image. */
  public static final String TILE_IMAGE_FILE = "TILE_IMAGE_FILE";
  /** Attribute indicating map tile's size. */
  public static final String TILE_SIZE = "TILE_SIZE";

  /** Attribute indicating file with agent's data. */
  public static final String AGENT_FILE = "AGENT_FILE";
  /** Attribute indicating maximum size of each agent's queue. */
  public static final String AGENT_QUEUE_MAX_SIZE = "AGENT_QUEUE_MAX_SIZE";
  /** Attribute indicating file with agent's images. */
  public static final String AGENT_IMAGE_FILE = "AGENT_IMAGE_FILE";
  /** Attribute indicating number of agent's images. */
  public static final String AGENT_IMAGES_COUNT = "AGENT_IMAGES_COUNT";
  /** Attribute indicating time after which agent's image should be switched. */
  public static final String AGENT_IMAGES_DELAY = "AGENT_IMAGES_DELAY";
  /** Attribute indicating file with agent's explosion images. */
  public static final String AGENT_EXPLOSION_IMAGE_FILE = "AGENT_EXPLOSION_IMAGE_FILE";
  /** Attribute indicating number of agent's explosion images. */
  public static final String AGENT_EXPLOSION_IMAGES_COUNT = "AGENT_EXPLOSION_IMAGES_COUNT";
  /** Attribute indicating time after which agent's explosion image should be switched. */
  public static final String AGENT_EXPLOSION_IMAGES_DELAY = "AGENT_EXPLOSION_IMAGES_DELAY";

  /** Attribute indicating file with projectile's image. */
  public static final String PROJECTILE_IMAGE_FILE = "PROJECTILE_IMAGE_FILE";
  /** Attribute indicating number of projectile's images. */
  public static final String PROJECTILE_IMAGES_COUNT = "PROJECTILE_IMAGES_COUNT";
  /** Attribute indicating time after which projectile's image should be switched. */
  public static final String PROJECTILE_IMAGES_DELAY = "PROJECTILE_IMAGES_DELAY";
  /** Attribute indicating file with projectile's explosion images. */
  public static final String PROJECTILE_EXPLOSION_IMAGE_FILE = "PROJECTILE_EXPLOSION_IMAGE_FILE";
  /** Attribute indicating number of projectile's explosion images. */
  public static final String PROJECTILE_EXPLOSION_IMAGES_COUNT = "PROJECTILE_EXPLOSION_IMAGES_COUNT";
  /** Attribute indicating time after which projectile's explosion image should be switched. */
  public static final String PROJECTILE_EXPLOSION_IMAGES_DELAY = "PROJECTILE_EXPLOSION_IMAGES_DELAY";


  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private SettingsNames()
  {
    //
  }
}