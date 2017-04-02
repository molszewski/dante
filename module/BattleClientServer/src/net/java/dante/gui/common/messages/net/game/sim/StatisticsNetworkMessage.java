/*
 * Created on 2006-08-28
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.sim;


import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;
import net.java.dante.sim.engine.engine2d.server.GroupStatistics;
import net.java.dante.sim.io.StatisticsData;

/**
 * Class wrapping {@link StatisticsData} object to transport it through
 * the network.
 *
 * @author M.Olszewski
 */
public class StatisticsNetworkMessage extends SimulationNetworkMessage
{
  /** Statistics data for client. */
  private StatisticsData statistics;


  /**
   * Creates instance of {@link StatisticsNetworkMessage} class.
   * Do not create instances of {@link StatisticsNetworkMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public StatisticsNetworkMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link StatisticsNetworkMessage} class with the
   * specified parameters.
   *
   * @param gameIdenifier the game's identifier.
   * @param statisticsData statistics data for client.
   */
  public StatisticsNetworkMessage(int gameIdenifier,
                                  StatisticsData statisticsData)
  {
    super(gameIdenifier);

    if (statisticsData == null)
    {
      throw new NullPointerException("Specified statisticsData is null!");
    }

    statistics = statisticsData;
  }


  /**
   * Gets {@link StatisticsData} object wrapped by this object.
   *
   * @return {@link StatisticsData} object wrapped by this object.
   */
  public StatisticsData getStatisticsData()
  {
    return statistics;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    super.fromPacket(reader);

    statistics = new StatisticsData(loadStatisticsData(reader));
  }

  private GroupStatistics loadStatisticsData(PacketReader reader)
  {
    int groupId = reader.readInt();
    long enemyVisible = reader.readLong();
    long updatesCount = reader.readLong();
    int enemyHit = reader.readInt();
    int enemyDestroyed = reader.readInt();
    int friendlyFireHit = reader.readInt();
    int friendlyFireDestroyed = reader.readInt();
    int friendlyDestroyed = reader.readInt();
    int damageTaken = reader.readInt();
    int projectilesShot = reader.readInt();
    long totalPoints = reader.readLong();

    return new GroupStatistics(groupId, enemyVisible, updatesCount,
                               enemyHit, enemyDestroyed,
                               friendlyFireHit, friendlyFireDestroyed,
                               friendlyDestroyed, damageTaken,
                               projectilesShot, totalPoints);
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    super.toPacket(writer);

    if (statistics == null)
    {
      throw new IllegalStateException("IllegalState in StatisticsNetworkMessage: object is read only!");
    }

    storeStatisticsData(writer, statistics);
  }

  private void storeStatisticsData(PacketWriter writer, StatisticsData statisticsData)
  {
    GroupStatistics groupStatistics = statisticsData.getGroupStatistics();

    writer.writeInt(groupStatistics.getGroupId());
    writer.writeLong(groupStatistics.getEnemyAgentsVisible());
    writer.writeLong(groupStatistics.getUpdatesCount());
    writer.writeInt(groupStatistics.getEnemyAgentsHit());
    writer.writeInt(groupStatistics.getEnemyAgentsDestroyed());
    writer.writeInt(groupStatistics.getFriendlyFireHits());
    writer.writeInt(groupStatistics.getFriendlyFireDestroyed());
    writer.writeInt(groupStatistics.getFriendlyAgentsDestroyed());
    writer.writeInt(groupStatistics.getDamageTaken());
    writer.writeInt(groupStatistics.getProjectilesShot());
    writer.writeLong(groupStatistics.getTotalPoints());
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = super.hashCode();

    result = PRIME * result + ((statistics == null) ? 0 : statistics.hashCode());

    return result;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof StatisticsNetworkMessage))
    {
      final StatisticsNetworkMessage other = (StatisticsNetworkMessage) object;
      equal = ((statistics == null)? (other.statistics == null) : statistics.equals(other.statistics));
    }
    return equal;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[statistics=" + statistics + "]");
  }
}