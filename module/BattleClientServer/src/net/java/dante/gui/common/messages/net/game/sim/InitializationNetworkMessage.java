/*
 * Created on 2006-08-27
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.sim;


import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;
import net.java.dante.sim.data.GlobalData;
import net.java.dante.sim.data.common.AnimationFramesData;
import net.java.dante.sim.data.map.MapUtils;
import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.map.StartLocation;
import net.java.dante.sim.data.map.StartLocationGroup;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.template.TemplateData;
import net.java.dante.sim.data.template.TemplateDataUtils;
import net.java.dante.sim.data.template.TemplateTypeData;
import net.java.dante.sim.data.template.types.AgentTemplateData;
import net.java.dante.sim.data.template.types.WeaponTemplateData;
import net.java.dante.sim.io.init.AgentInitDataUtils;
import net.java.dante.sim.io.init.AgentsInitData;
import net.java.dante.sim.io.init.EnemyAgentInitData;
import net.java.dante.sim.io.init.EnemyAgentsGroupInitData;
import net.java.dante.sim.io.init.FriendlyAgentInitData;
import net.java.dante.sim.io.init.InitializationData;

/**
 * Class wrapping {@link InitializationData} object to transport it through
 * the network.
 *
 * @author M.Olszewski
 */
public class InitializationNetworkMessage extends SimulationNetworkMessage
{
  /** Initialization data for client. */
  private InitializationData initialization;


  /**
   * Creates instance of {@link InitializationNetworkMessage} class.
   * Do not create instances of {@link InitializationNetworkMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public InitializationNetworkMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link InitializationNetworkMessage} class with the
   * specified parameters.
   *
   * @param gameIdenifier the game's identifier.
   * @param initializationData initialization data for client.
   */
  public InitializationNetworkMessage(int gameIdenifier, InitializationData initializationData)
  {
    super(gameIdenifier);

    if (initializationData == null)
    {
      throw new NullPointerException("Specified initializationData is null!");
    }

    initialization = initializationData;
  }


  /**
   * Gets {@link InitializationData} object wrapped by this object.
   *
   * @return {@link InitializationData} object wrapped by this object.
   */
  public InitializationData getInitializationData()
  {
    return initialization;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    super.fromPacket(reader);

    initialization = loadInitializationData(reader);
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    super.toPacket(writer);

    if (initialization == null)
    {
      throw new IllegalStateException("IllegalState in InitializationNetworkMessage: object is read only!");
    }

    storeInitializationData(writer, initialization);
  }

  private void storeInitializationData(PacketWriter writer,
                                       InitializationData initializationData)
  {
    writer.writeInt(initializationData.getGroupId());

    GlobalData globalData = initializationData.getGlobalData();
    storeGlobalData(writer, globalData);

    SimulationMap map = initializationData.getMap();
    storeSimulationMap(writer, map);

    TemplateTypeData agentTypeData = initializationData.getAgentTemplateTypeData();
    writer.writeString(agentTypeData.getType());
    storeAgentTemplateData(writer, (AgentTemplateData)agentTypeData.getData());

    TemplateTypeData weaponTypeData = initializationData.getWeaponTemplateTypeData();
    writer.writeString(weaponTypeData.getType());
    storeWeaponTemplateData(writer, (WeaponTemplateData)weaponTypeData.getData());

    AgentsInitData agentsInitData = initializationData.getAgentsInitData();
    storeAgentsInitData(writer, agentsInitData);
  }

  private void storeGlobalData(PacketWriter writer,
                               GlobalData globalData)
  {
    writer.writeBoolean(globalData.isRecordBattle());
    writer.writeBoolean(globalData.isEvaluateBattle());
    writer.writeLong(globalData.getBattleDuration());
    writer.writeLong(globalData.getUpdatesInterval());
    writer.writeLong(globalData.getSendUpdateMessagesInterval());
    writer.writeInt(globalData.getGroupsCount());
    writer.writeString(globalData.getBackgroundImageFile());
    writer.writeString(globalData.getTileImageFile());

    writer.writeInt(globalData.getAgentQueueMaxSize());
    storeAnimationFrameData(writer, globalData.getAgentMoveAnimation());
    storeAnimationFrameData(writer, globalData.getAgentExplosionAnimation());
    storeAnimationFrameData(writer, globalData.getProjectileMoveAnimation());
    storeAnimationFrameData(writer, globalData.getProjectileExplosionAnimation());

    storeObjectSize(writer, globalData.getMapTileSize());
  }

  private void storeAnimationFrameData(PacketWriter writer,
                                       AnimationFramesData frameData)
  {
    writer.writeString(frameData.getFramesFile());
    writer.writeInt(frameData.getFramesCount());
    writer.writeLong(frameData.getFramesDelay());
  }

  private void storeObjectSize(PacketWriter writer, ObjectSize sizeData)
  {
    writer.writeInt(sizeData.getWidth());
    writer.writeInt(sizeData.getHeight());
  }

  private void storeSimulationMap(PacketWriter writer, SimulationMap map)
  {
    int rowsCount = map.getRows();
    writer.writeInt(rowsCount);
    int columnsCount = map.getColumns();
    writer.writeInt(columnsCount);
    for (int i = 0; i < rowsCount; i++)
    {
      for (int j = 0; j < columnsCount; j++)
      {
        writer.writeInt(map.getTile(i, j).ordinal());
      }
    }

    int groupLocationsCount = map.locationGroupsCount();
    writer.writeInt(groupLocationsCount);

    for (int i = 0; i < groupLocationsCount; i++)
    {
      StartLocationGroup group = map.getStartLocationGroup(i);
      int locationsCount = group.locationsCount();
      writer.writeInt(locationsCount);
      for (int j = 0; j < locationsCount; j++)
      {
        StartLocation location = group.getLocation(j);
        writer.writeInt(location.getRow());
        writer.writeInt(location.getColumn());
      }
    }
  }

  private void storeAgentTemplateData(PacketWriter writer, AgentTemplateData agentData)
  {
    writer.writeInt(agentData.getMaxHealth());
    writer.writeLong(Double.doubleToLongBits(agentData.getMaxSpeed()));
    writer.writeLong(Double.doubleToLongBits(agentData.getSightRange()));

    storeObjectSize(writer, agentData.getAgentSize());

    writer.writeString(agentData.getWeaponSystemType());

  }

  private void storeWeaponTemplateData(PacketWriter writer, WeaponTemplateData weaponData)
  {

    writer.writeLong(Double.doubleToLongBits(weaponData.getRange()));
    writer.writeLong(Double.doubleToLongBits(weaponData.getMaxSpeed()));
    writer.writeLong(Double.doubleToLongBits(weaponData.getExplosionRange()));
    writer.writeInt(weaponData.getMinDamage());
    writer.writeInt(weaponData.getMaxDamage());
    writer.writeLong(weaponData.getReloadTime());

    storeObjectSize(writer, weaponData.getProjectileSize());
  }

  private void storeAgentsInitData(PacketWriter writer, AgentsInitData agentsInitData)
  {
    int enemyGroupsCount = agentsInitData.getEnemyGroupsCount();
    writer.writeInt(enemyGroupsCount);

    for (int i = 0; i < enemyGroupsCount; i++)
    {
      EnemyAgentsGroupInitData enemyGroup = agentsInitData.getEnemyGroup(i);
      int enemyAgentsCount = enemyGroup.getEnemyAgentsCount();
      writer.writeInt(enemyAgentsCount);
      for (int j = 0; j < enemyAgentsCount; j++)
      {
        EnemyAgentInitData enemyData = enemyGroup.getEnemyAgentInitData(j);
        writer.writeString(enemyData.getType());
        writer.writeInt(enemyData.getAgentId());
      }
    }

    int friendlyAgentsCount = agentsInitData.getFriendlyAgentsCount();
    writer.writeInt(friendlyAgentsCount);

    for (int i = 0; i < friendlyAgentsCount; i++)
    {
      FriendlyAgentInitData friendlyData = agentsInitData.getFriendlyAgentInitData(i);
      writer.writeString(friendlyData.getType());
      writer.writeInt(friendlyData.getAgentId());
      writer.writeLong(Double.doubleToLongBits(friendlyData.getStartX()));
      writer.writeLong(Double.doubleToLongBits(friendlyData.getStartY()));
    }
  }

  private InitializationData loadInitializationData(PacketReader reader)
  {
    int groupId = reader.readInt();

    GlobalData globalData = loadGlobalData(reader);

    SimulationMap simMap = loadSimulationMap(reader);

    TemplateTypeData agentTemplateTypeData = loadAgentTemplateTypeData(reader);
    TemplateTypeData weaponTemplateTypeData = loadWeaponTemplateTypeData(reader);

    AgentsInitData agentsInitData = loadAgentsInitData(reader);

    return InitializationData.createInitializationData(groupId,
                                                       simMap,
                                                       agentTemplateTypeData,
                                                       weaponTemplateTypeData,
                                                       agentsInitData,
                                                       globalData);
  }

  private GlobalData loadGlobalData(PacketReader reader)
  {
    boolean battleRecorded = reader.readBoolean();
    boolean battleEvaluated = reader.readBoolean();
    long  battleDuration = reader.readLong();
    long updatesInterval = reader.readLong();
    long sendMessagesInterval = reader.readLong();
    int groupsCount = reader.readInt();
    String backgroundFile = reader.readString();
    String tileFile = reader.readString();

    int queueSize = reader.readInt();
    AnimationFramesData agentMoveFrames = loadAnimationFramesData(reader);
    AnimationFramesData agentExploFrames = loadAnimationFramesData(reader);
    AnimationFramesData projMoveFrames = loadAnimationFramesData(reader);
    AnimationFramesData projExploFrames = loadAnimationFramesData(reader);

    ObjectSize tileSize = loadObjectSize(reader);

    return new GlobalData(battleRecorded,
                          battleEvaluated,
                          battleDuration,
                          updatesInterval,
                          sendMessagesInterval,
                          groupsCount,
                          backgroundFile,
                          tileFile,
                          queueSize,
                          agentMoveFrames,
                          agentExploFrames,
                          projMoveFrames,
                          projExploFrames,
                          tileSize);
  }

  private AnimationFramesData loadAnimationFramesData(PacketReader reader)
  {
    String framesFile = reader.readString();
    int framesCount = reader.readInt();
    long framesDelay = reader.readLong();

    return new AnimationFramesData(framesFile, framesCount, framesDelay);
  }

  private ObjectSize loadObjectSize(PacketReader reader)
  {
    int width = reader.readInt();
    int height = reader.readInt();
    return new ObjectSize(width, height);
  }

  private SimulationMap loadSimulationMap(PacketReader reader)
  {
    int rowsCount = reader.readInt();
    int columnsCount = reader.readInt();

    int[][] tileTypes = new int[rowsCount][columnsCount];

    for (int i = 0; i < rowsCount; i++)
    {
      for (int j = 0; j < columnsCount; j++)
      {
        tileTypes[i][j] = reader.readInt();
      }
    }

    int groupLocationsCount = reader.readInt();
    StartLocationGroup[] groups = new StartLocationGroup[groupLocationsCount];
    for (int i = 0; i < groupLocationsCount; i++)
    {
      int locationsCount = reader.readInt();

      StartLocation[] locations = new StartLocation[locationsCount];
      for (int j = 0; j < locationsCount; j++)
      {
        int locRow = reader.readInt();
        int locCol = reader.readInt();

        StartLocation location = MapUtils.createStartLocation(locRow, locCol);
        locations[j] = location;
      }
      groups[i] = MapUtils.createStartLocationGroup(locations);
    }

    return MapUtils.createSimulationMap(tileTypes, groups);
  }

  private TemplateTypeData loadAgentTemplateTypeData(PacketReader reader)
  {
    String agentType = reader.readString();

    int maxHealth = reader.readInt();
    double maxSpeed = Double.longBitsToDouble(reader.readLong());
    double sightRange = Double.longBitsToDouble(reader.readLong());
    ObjectSize agentSize = loadObjectSize(reader);
    String weaponType = reader.readString();

    TemplateData agentData = new AgentTemplateData(maxHealth, maxSpeed,
                                                   sightRange, agentSize,
                                                   weaponType);

    return TemplateDataUtils.createTemplateTypeData(agentType, agentData);
  }

  private TemplateTypeData loadWeaponTemplateTypeData(PacketReader reader)
  {
    String weaponType = reader.readString();

    double range = Double.longBitsToDouble(reader.readLong());
    double maxSpeed = Double.longBitsToDouble(reader.readLong());
    double exploRange = Double.longBitsToDouble(reader.readLong());

    int minDmg = reader.readInt();
    int maxDmg = reader.readInt();
    long reloadTime = reader.readLong();

    ObjectSize projSize = loadObjectSize(reader);

    TemplateData weaponData = new WeaponTemplateData(range, maxSpeed,
                                                     exploRange, minDmg, maxDmg,
                                                     reloadTime, projSize);

    return TemplateDataUtils.createTemplateTypeData(weaponType, weaponData);
  }

  private AgentsInitData loadAgentsInitData(PacketReader reader)
  {
    int enemyGroupsCount = reader.readInt();
    EnemyAgentsGroupInitData[] enemyGroupsInitData = new EnemyAgentsGroupInitData[enemyGroupsCount];

    for (int i = 0; i < enemyGroupsCount; i++)
    {
      int enemyAgentsCount = reader.readInt();
      EnemyAgentInitData[] enemyGroupInitData = new EnemyAgentInitData[enemyAgentsCount];

      for (int j = 0; j < enemyAgentsCount; j++)
      {
        String enemyType = reader.readString();
        int enemyId = reader.readInt();

        enemyGroupInitData[j] =
            AgentInitDataUtils.createEnemyAgentInitData(enemyType, enemyId);
      }

      enemyGroupsInitData[i] = AgentInitDataUtils.createEnemyAgentsGroupInitData(enemyGroupInitData);
    }

    int friendlyAgentsCount = reader.readInt();
    FriendlyAgentInitData[] friendlyInitData = new FriendlyAgentInitData[friendlyAgentsCount];

    for (int i = 0; i < friendlyAgentsCount; i++)
    {
      String agentType = reader.readString();
      int agentId = reader.readInt();
      double startX = Double.longBitsToDouble(reader.readLong());
      double startY = Double.longBitsToDouble(reader.readLong());

      friendlyInitData[i] =
        AgentInitDataUtils.createFriendlyAgentInitData(agentType, agentId,
                                                       startX, startY);
    }

    return AgentInitDataUtils.createAgentsGroupsInitData(friendlyInitData,
                                                         enemyGroupsInitData);
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;

    int result = super.hashCode();
    result = PRIME * result + ((initialization == null) ? 0 : initialization.hashCode());

    return result;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof InitializationNetworkMessage))
    {
      final InitializationNetworkMessage other = (InitializationNetworkMessage) object;
      equal = ((initialization == null) ? (other.initialization == null) : initialization.equals(other.initialization));
    }
    return equal;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#toString()
   */
  @Override
  public String toString()
  {
    final String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) +
        "; initialization=" + initialization + "]");
  }
}