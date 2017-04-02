/*
 * Created on 2006-07-14
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.settings;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import net.java.dante.sim.common.ResourceLoadFailedException;
import net.java.dante.sim.data.DataLoadingFailedException;
import net.java.dante.sim.data.common.AnimationFramesData;
import net.java.dante.sim.data.common.FileLoader;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.util.SettingsReader;


/**
 * Class loading settings from file.
 *
 * @author M.Olszewski
 */
class SettingsFileLoader extends FileLoader implements SettingsLoader
{
  /** One second in milliseconds. */
  private static final long SECOND_IN_MS = 1000L;
  /** Length of array with sizes. */
  private static final int SIZE_ARRAY_LENGTH = 2;


  /**
   * Creates object of {@link SettingsFileLoader} loading settings from
   * specified file.
   *
   * @param settingsFileName - settings file name.
   */
  SettingsFileLoader(String settingsFileName)
  {
    super(settingsFileName);
  }


  /**
   * @see net.java.dante.sim.data.settings.SettingsLoader#loadSettings()
   */
  public Settings loadSettings() throws DataLoadingFailedException
  {
    Settings settings = null;

    try
    {
      settings = readSettings(getFileName());
    }
    catch (Exception e)
    {
      throw new DataLoadingFailedException("File with settings parameters cannot be loaded! " + e.getMessage(), e);
    }

    return settings;
  }

  /**
   * Reads settings attributes from specified file and returns
   * instance of {@link Settings} implementation containing read data.
   *
   * @param settingsFileName - file with settings attributes.
   *
   * @return Returns instance of {@link Settings} implementation containing
   *         read data.
   * @throws IOException if any I/O error occurred during reading contents
   *         of settings file.
   */
  private Settings readSettings(String settingsFileName) throws IOException
  {
    URL url = getClass().getClassLoader().getResource(getFileName().trim());
    if (url == null)
    {
      throw new ResourceLoadFailedException("Cannot find or access settings file: "
          + getFileName().trim() + "!");
    }

    Properties p = SettingsReader.readSettings(url);

    SimulationSettings settings = new SimulationSettings();

    readMainSettings(p, settings);
    readMapSettings(p, settings);
    readAgentSettings(p, settings);
    readProjectileSettings(p, settings);

    return settings;
  }

  /**
   * Reads main settings from specified properties and store them in
   * {@link SimulationSettings} object.
   *
   * @param p - {@link Properties} object.
   * @param settings - {@link SimulationSettings} object.
   */
  private void readMainSettings(Properties p, SimulationSettings settings)
  {
    boolean battleRecorded = SettingsReader.readBoolean(p.getProperty(SettingsNames.RECORD_BATTLE));
    boolean battleEvaluated = SettingsReader.readBoolean(p.getProperty(SettingsNames.EVALUATE_BATTLE));
    int battleTimeSecs = SettingsReader.readInt(p.getProperty(SettingsNames.BATTLE_DURATION));
    long updatesInterval = SettingsReader.readLong(p.getProperty(SettingsNames.UPDATES_INTERVAL));
    long sendUpdatesInterval = SettingsReader.readLong(p.getProperty(SettingsNames.SEND_UPDATE_MESSAGES_INTERVAL));

    settings.setRecordBattle(battleRecorded);
    settings.setEvaluateBattle(battleEvaluated);
    // Store read seconds in milliseconds
    settings.setBattleDuration(battleTimeSecs * SECOND_IN_MS);
    settings.setUpdatesInterval(updatesInterval);
    settings.setSendUpdateMessagesInterval(sendUpdatesInterval);
  }

  /**
   * Reads map settings from specified properties and store them in
   * {@link SimulationSettings} object.
   *
   * @param p - {@link Properties} object.
   * @param settings - {@link SimulationSettings} object.
   */
  private void readMapSettings(Properties p, SimulationSettings settings)
  {
    String mapFile = SettingsReader.readString(p.getProperty(SettingsNames.MAP_FILE));
    String backgroundImgFile = SettingsReader.readString(p.getProperty(SettingsNames.BACKGROUND_IMAGE_FILE));

    String tileImageFile = SettingsReader.readString(
        p.getProperty(SettingsNames.TILE_IMAGE_FILE));
    int[] tileSizeArray = SettingsReader.readIntsArray(
        p.getProperty(SettingsNames.TILE_SIZE), SIZE_ARRAY_LENGTH);

    settings.setMapFile(mapFile);
    settings.setBackgroundImageFile(backgroundImgFile);
    settings.setTileImageFile(tileImageFile);
    settings.setMapTileSize(new ObjectSize(tileSizeArray[0], tileSizeArray[1]));
  }

  /**
   * Reads agent settings from specified properties and store them in
   * {@link SimulationSettings} object.
   *
   * @param p - {@link Properties} object.
   * @param settings - {@link SimulationSettings} object.
   */
  private void readAgentSettings(Properties p, SimulationSettings settings)
  {
    String agentFile = SettingsReader.readString(p.getProperty(SettingsNames.AGENT_FILE));
    settings.setAgentFile(agentFile);

    int queueSize = SettingsReader.readInt(p.getProperty(SettingsNames.AGENT_QUEUE_MAX_SIZE));
    settings.setAgentQueueMaxSize(queueSize);

    String agentImgFile = SettingsReader.readString(p.getProperty(SettingsNames.AGENT_IMAGE_FILE));
    int agentImgsCount = SettingsReader.readInt(p.getProperty(SettingsNames.AGENT_IMAGES_COUNT));
    long agentImgsDelay = SettingsReader.readLong(p.getProperty(SettingsNames.AGENT_IMAGES_DELAY));
    settings.setAgentMoveAnimation(
        new AnimationFramesData(agentImgFile, agentImgsCount, agentImgsDelay));

    String agentExploImgFile = SettingsReader.readString(p.getProperty(SettingsNames.AGENT_EXPLOSION_IMAGE_FILE));
    int agentExploImgsCount = SettingsReader.readInt(p.getProperty(SettingsNames.AGENT_EXPLOSION_IMAGES_COUNT));
    long agentExploImgsDelay = SettingsReader.readLong(p.getProperty(SettingsNames.AGENT_EXPLOSION_IMAGES_DELAY));
    settings.setAgentExplosionAnimation(
        new AnimationFramesData(agentExploImgFile, agentExploImgsCount, agentExploImgsDelay));
  }

  /**
   * Reads projectile settings from specified properties and store them in
   * {@link SimulationSettings} object.
   *
   * @param p - {@link Properties} object.
   * @param settings - {@link SimulationSettings} object.
   */
  private void readProjectileSettings(Properties p, SimulationSettings settings)
  {
    String projectileImgFile = SettingsReader.readString(p.getProperty(SettingsNames.PROJECTILE_IMAGE_FILE));
    int projectileImgsCount = SettingsReader.readInt(p.getProperty(SettingsNames.PROJECTILE_IMAGES_COUNT));
    long projectileImgsDelay = SettingsReader.readLong(p.getProperty(SettingsNames.PROJECTILE_IMAGES_DELAY));
    settings.setProjectileMoveAnimation(
        new AnimationFramesData(projectileImgFile, projectileImgsCount, projectileImgsDelay));

    String projectileExploImgFile = SettingsReader.readString(p.getProperty(SettingsNames.PROJECTILE_EXPLOSION_IMAGE_FILE));
    int projectileExploImgsCount = SettingsReader.readInt(p.getProperty(SettingsNames.PROJECTILE_EXPLOSION_IMAGES_COUNT));
    long projectileExploImgsDelay = SettingsReader.readLong(p.getProperty(SettingsNames.PROJECTILE_EXPLOSION_IMAGES_DELAY));
    settings.setProjectileExplosionAnimation(
        new AnimationFramesData(projectileExploImgFile, projectileExploImgsCount, projectileExploImgsDelay));
  }
}