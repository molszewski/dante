/*
 * Created on 2006-08-27
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.sim;


import net.java.dante.darknet.protocol.packet.PacketReader;
import net.java.dante.darknet.protocol.packet.PacketWriter;
import net.java.dante.sim.event.Event;
import net.java.dante.sim.event.EventType;
import net.java.dante.sim.event.EventUtils;
import net.java.dante.sim.event.EventsRepository;
import net.java.dante.sim.event.types.EnemyAgentDestroyedEvent;
import net.java.dante.sim.event.types.EnemyAgentEvent;
import net.java.dante.sim.event.types.EnemyAgentHitEvent;
import net.java.dante.sim.event.types.EventTypesUtils;
import net.java.dante.sim.event.types.FriendlyAgentBlockedEvent;
import net.java.dante.sim.event.types.FriendlyAgentDestroyedEvent;
import net.java.dante.sim.event.types.FriendlyAgentEvent;
import net.java.dante.sim.event.types.FriendlyAgentHitEvent;
import net.java.dante.sim.event.types.FriendlyAgentMoveEvent;
import net.java.dante.sim.event.types.FriendlyAgentMoveFinishedEvent;
import net.java.dante.sim.event.types.ObjectMoveEventParams;
import net.java.dante.sim.event.types.ProjectileDestroyedEvent;
import net.java.dante.sim.event.types.ProjectileEvent;
import net.java.dante.sim.event.types.ProjectileShotEvent;
import net.java.dante.sim.event.types.ProjectileTypeEvent;
import net.java.dante.sim.io.UpdateData;

/**
 * Class wrapping {@link UpdateData} object to transport it through
 * the network.
 *
 * @author M.Olszewski
 */
public class UpdateNetworkMessage extends SimulationNetworkMessage
{
  /** Wrapped {@link UpdateData} object */
  private UpdateData update;


  /**
   * Creates instance of {@link UpdateNetworkMessage} class.
   * Do not create instances of {@link UpdateNetworkMessage} class by calling this
   * constructor - it is intended to be called by DarkNet while
   * encoding this message.
   */
  public UpdateNetworkMessage()
  {
    super();
  }

  /**
   * Creates instance of {@link UpdateNetworkMessage} class with the
   * specified parameters.
   *
   * @param gameIdenifier the game's identifier.
   * @param updateData update data with events repository.
   */
  public UpdateNetworkMessage(int gameIdenifier, UpdateData updateData)
  {
    super(gameIdenifier);

    if (updateData == null)
    {
      throw new NullPointerException("Specified updateData is null!");
    }

    update = updateData;
  }


  /**
   * Gets wrapped {@link UpdateData} object.
   *
   * @return Returns wrapped {@link UpdateData} object.
   */
  public UpdateData getUpdateData()
  {
    return update;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#fromPacket(net.java.dante.darknet.protocol.packet.PacketReader)
   */
  @Override
  public void fromPacket(PacketReader reader)
  {
    super.fromPacket(reader);

    long time = reader.readLong();
    EventsRepository repository = loadRepository(reader);

    update = new UpdateData(time, repository);
  }

  /**
   * Loads stored events repository using the specified {@link PacketReader}
   * object.
   *
   * @param reader the specified {@link PacketReader} object.
   *
   * @return Returns read events repository.
   */
  private EventsRepository loadRepository(PacketReader reader)
  {
    int groupId     = reader.readInt();
    int eventsCount = reader.readInt();
    Event[] events  = new Event[eventsCount];

    for (int i = 0; i < eventsCount; i++)
    {
      int       eventId   = reader.readInt();
      EventType eventType = EventType.values()[reader.readInt()];
      long      eventTime = reader.readLong();

      events[i] = readEvent(reader, eventId, eventType, eventTime);
    }

    return EventUtils.createGroupEventsRepository(groupId, events);
  }

  /**
   * Loads stored event using the specified {@link PacketReader} object.
   *
   * @param reader the specified {@link PacketReader} object.
   * @param eventId the specified event's identifier.
   * @param eventType the specified event's type.
   * @param eventTime the specified event's time.
   *
   * @return Returns read event.
   */
  Event readEvent(PacketReader reader, int eventId, EventType eventType,
                  long eventTime)
  {
    Event event = null;
    switch (eventType)
    {
      case ENEMY_AGENT_SEEN:
      {
        event = EventTypesUtils.createEnemyAgentSeenEvent(eventId, eventTime,
                                                          reader.readInt(),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()));
        break;
      }
      case ENEMY_AGENT_GONE:
      {
        event = EventTypesUtils.createEnemyAgentGoneEvent(eventId, eventTime,
                                                          reader.readInt(),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()));
        break;
      }
      case ENEMY_AGENT_MOVE:
      {
        event = EventTypesUtils.createEnemyAgentMoveEvent(eventId, eventTime,
                                                          reader.readInt(),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()));
        break;
      }
      case ENEMY_AGENT_HIT:
      {
        event = EventTypesUtils.createEnemyAgentHitEvent(eventId, eventTime,
                                                         reader.readInt(),
                                                         reader.readInt());
        break;
      }
      case ENEMY_AGENT_DESTROYED:
      {
        event = EventTypesUtils.createEnemyAgentDestroyedEvent(eventId, eventTime,
                                                               reader.readInt(),
                                                               reader.readInt());
        break;
      }
      case FRIENDLY_AGENT_MOVE:
      {
        event = EventTypesUtils.createFriendlyAgentMoveEvent(eventId, eventTime,
                                                             reader.readInt(),
                                                             Double.longBitsToDouble(reader.readLong()),
                                                             Double.longBitsToDouble(reader.readLong()),
                                                             Double.longBitsToDouble(reader.readLong()),
                                                             Double.longBitsToDouble(reader.readLong()));
        break;
      }
      case FRIENDLY_AGENT_MOVE_FINISHED:
      {
        event = EventTypesUtils.createFriendlyAgentMoveFinishedEvent(eventId, eventTime,
                                                                     reader.readInt());
        break;
      }
      case FRIENDLY_AGENT_BLOCKED:
      {
        event = EventTypesUtils.createFriendlyAgentBlockedEvent(eventId, eventTime,
                                                                reader.readInt(),
                                                                Double.longBitsToDouble(reader.readLong()),
                                                                Double.longBitsToDouble(reader.readLong()));
        break;
      }
      case FRIENDLY_AGENT_HIT:
      {
        event = EventTypesUtils.createFriendlyAgentHitEvent(eventId, eventTime,
                                                            reader.readInt(),
                                                            reader.readInt());
        break;
      }
      case FRIENDLY_AGENT_DESTROYED:
      {
        event = EventTypesUtils.createFriendlyAgentDestroyedEvent(eventId, eventTime,
                                                                  reader.readInt());
        break;
      }
      case PROJECTILE_SHOT:
      {
        event = EventTypesUtils.createProjectileShotEvent(eventId, eventTime,
                                                          reader.readString(), reader.readInt(),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          reader.readInt());
        break;
      }
      case PROJECTILE_SEEN:
      {
        event = EventTypesUtils.createProjectileSeenEvent(eventId, eventTime,
                                                          reader.readString(), reader.readInt(),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()));
        break;
      }
      case PROJECTILE_GONE:
      {
        event = EventTypesUtils.createProjectileGoneEvent(eventId, eventTime,
                                                          reader.readInt(),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()));
        break;
      }
      case PROJECTILE_MOVE:
      {
        event = EventTypesUtils.createProjectileMoveEvent(eventId, eventTime,
                                                          reader.readInt(),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()),
                                                          Double.longBitsToDouble(reader.readLong()));
        break;
      }
      case PROJECTILE_DESTROYED:
      {
        int projectileId = reader.readInt();
        double x = Double.longBitsToDouble(reader.readLong());
        double y = Double.longBitsToDouble(reader.readLong());

        event = EventTypesUtils.createProjectileDestroyedEvent(eventId, eventTime,
                                                               projectileId,
                                                               x, y);
        break;
      }
    }

    return event;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = super.hashCode();

    result = PRIME * result + ((update == null) ? 0 : update.hashCode());

    return result;
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#toPacket(net.java.dante.darknet.protocol.packet.PacketWriter)
   */
  @Override
  public void toPacket(PacketWriter writer)
  {
    super.toPacket(writer);

    if (update == null)
    {
      throw new IllegalStateException("IllegalState in UpdateNetworkMessage: object is read only!");
    }

    writer.writeLong(update.getTime());
    storeRepository(writer);
  }

  /**
   * Stores whole events repository using the specified {@link PacketWriter}
   * object.
   *
   * @param writer the specified {@link PacketWriter} object.
   */
  private void storeRepository(PacketWriter writer)
  {
    EventsRepository repository = update.getRepository();

    writer.writeInt(repository.getGroupId());
    int eventsCount = repository.getEventsCount();
    writer.writeInt(eventsCount);

    for (int i = 0; i < eventsCount; i++)
    {
      Event event = repository.getEvent(i);

      writer.writeInt(event.getId());
      writer.writeInt(event.getEventType().ordinal());
      writer.writeLong(event.getTime());

      if (event instanceof FriendlyAgentEvent)
      {
        storeFriendlyAgentEvent(writer, (FriendlyAgentEvent)event);
      }
      else if (event instanceof EnemyAgentEvent)
      {
        storeEnemyAgentEvent(writer, (EnemyAgentEvent)event);
      }
      else if (event instanceof ProjectileEvent)
      {
        storeProjectileEvent(writer, (ProjectileEvent)event);
      }
    }
  }

  /**
   * Stores the specified event with {@link FriendlyAgentEvent} super interface
   * using the specified {@link PacketWriter} object.
   *
   * @param event the specified event with {@link FriendlyAgentEvent} super
   *        interface.
   * @param writer the specified {@link PacketWriter} object.
   */
  private void storeFriendlyAgentEvent(PacketWriter writer, FriendlyAgentEvent event)
  {
    writer.writeInt(event.getFriendlyAgentId());

    if (event instanceof FriendlyAgentMoveEvent)
    {
      FriendlyAgentMoveEvent moveEvent = (FriendlyAgentMoveEvent)event;

      writer.writeLong(Double.doubleToLongBits(moveEvent.getDestinationX()));
      writer.writeLong(Double.doubleToLongBits(moveEvent.getDestinationY()));
      writer.writeLong(Double.doubleToLongBits(moveEvent.getSpeedX()));
      writer.writeLong(Double.doubleToLongBits(moveEvent.getSpeedY()));
    }
    else if (event instanceof FriendlyAgentBlockedEvent)
    {
      FriendlyAgentBlockedEvent blockedEvent = (FriendlyAgentBlockedEvent)event;

      writer.writeLong(Double.doubleToLongBits(blockedEvent.getBlockedX()));
      writer.writeLong(Double.doubleToLongBits(blockedEvent.getBlockedY()));
    }
    else if (event instanceof FriendlyAgentHitEvent)
    {
      FriendlyAgentHitEvent hitEvent = (FriendlyAgentHitEvent)event;

      writer.writeInt(hitEvent.getDamage());
    }
    else if (event instanceof FriendlyAgentDestroyedEvent)
    {
      // Intentionally left empty.
    }
    else if (event instanceof FriendlyAgentMoveFinishedEvent)
    {
      // Intentionally left empty.
    }
  }

  /**
   * Stores the specified event with {@link EnemyAgentEvent} super interface
   * using the specified {@link PacketWriter} object.
   *
   * @param event the specified event with {@link EnemyAgentEvent} super
   *        interface.
   * @param writer the specified {@link PacketWriter} object.
   */
  private void storeEnemyAgentEvent(PacketWriter writer, EnemyAgentEvent event)
  {
    writer.writeInt(event.getEnemyAgentId());

    if (event instanceof ObjectMoveEventParams)
    {
      ObjectMoveEventParams moveParams = (ObjectMoveEventParams)event;

      writer.writeLong(Double.doubleToLongBits(moveParams.getDestinationX()));
      writer.writeLong(Double.doubleToLongBits(moveParams.getDestinationY()));
      writer.writeLong(Double.doubleToLongBits(moveParams.getSpeedX()));
      writer.writeLong(Double.doubleToLongBits(moveParams.getSpeedY()));
    }
    else if (event instanceof EnemyAgentHitEvent)
    {
      EnemyAgentHitEvent hitEvent = (EnemyAgentHitEvent)event;
      writer.writeInt(hitEvent.getShooterId());
    }
    else if (event instanceof EnemyAgentDestroyedEvent)
    {
      EnemyAgentDestroyedEvent hitEvent = (EnemyAgentDestroyedEvent)event;
      writer.writeInt(hitEvent.getShooterId());
    }
  }

  /**
   * Stores the specified event with {@link ProjectileEvent} super interface
   * using the specified {@link PacketWriter} object.
   *
   * @param event the specified event with {@link ProjectileEvent} super
   *        interface.
   * @param writer the specified {@link PacketWriter} object.
   */
  private void storeProjectileEvent(PacketWriter writer, ProjectileEvent event)
  {
    if (event instanceof ProjectileTypeEvent)
    {
      ProjectileTypeEvent typeEvent = (ProjectileTypeEvent)event;
      writer.writeString(typeEvent.getProjectileType());
    }

    writer.writeInt(event.getProjectileId());

    if (event instanceof ObjectMoveEventParams)
    {
      ObjectMoveEventParams moveParams = (ObjectMoveEventParams)event;

      writer.writeLong(Double.doubleToLongBits(moveParams.getDestinationX()));
      writer.writeLong(Double.doubleToLongBits(moveParams.getDestinationY()));
      writer.writeLong(Double.doubleToLongBits(moveParams.getSpeedX()));
      writer.writeLong(Double.doubleToLongBits(moveParams.getSpeedY()));

      if (event instanceof ProjectileShotEvent)
      {
        ProjectileShotEvent shotEvent = (ProjectileShotEvent)event;
        writer.writeInt(shotEvent.getShooterId());
      }
    }
    else if (event instanceof ProjectileDestroyedEvent)
    {
      ProjectileDestroyedEvent destroyedEvent = (ProjectileDestroyedEvent)event;

      writer.writeLong(Double.doubleToLongBits(destroyedEvent.getProjectileX()));
      writer.writeLong(Double.doubleToLongBits(destroyedEvent.getProjectileY()));
    }
  }

  /**
   * @see net.java.dante.gui.common.messages.net.game.sim.SimulationNetworkMessage#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof UpdateNetworkMessage))
    {
      final UpdateNetworkMessage other = (UpdateNetworkMessage) object;
      equal = ((update == null)? (other.update == null) : (update.equals(other.update)));
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
        "; updateData=" + update + "]");
  }
}