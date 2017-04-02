/*
 * Created on 2006-08-10
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.engine2d.server;

import java.util.HashMap;
import java.util.Map;

import net.java.dante.sim.data.ActionListener;
import net.java.dante.sim.data.ObjectsTraverser;
import net.java.dante.sim.data.SimulationData;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.agent.ServerAgent;
import net.java.dante.sim.engine.engine2d.SpritesRepository;


/**
 * Helper class building groups of agents from specified root of simulation
 * objects tree.
 *
 * @author M.Olszewski
 */
class Server2dAgentsGroupsBuilder
{
  /** The only existing instance of {@link Server2dAgentsGroupsBuilder}. */
  private static Server2dAgentsGroupsBuilder instance = new Server2dAgentsGroupsBuilder();


  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private Server2dAgentsGroupsBuilder()
  {
    // Intentionally left empty.
  }


  /**
   * Gets the only instance of this singleton class.
   *
   * @return Returns the only instance of this singleton class.
   */
  static Server2dAgentsGroupsBuilder getInstance()
  {
    return instance;
  }

  /**
   * Builds array of agents groups using root of simulation objects tree.
   *
   * @param root - root object of simulation objects tree.
   * @param spritesRepository - sprites repository.
   *
   * @return Returns array with found agents groups.
   */
  Server2dAgentsGroup[] build(SimulationObject root, SpritesRepository spritesRepository)
  {
    if (root == null)
    {
      throw new NullPointerException("Specified root is null!");
    }
    if (spritesRepository == null)
    {
      throw new NullPointerException("Specified spritesRepository is null!");
    }

    AgentsGroupFetcher fetcher = new AgentsGroupFetcher(spritesRepository);
    ObjectsTraverser.getDefault().traverse(root, fetcher);
    return fetcher.createGroups();
  }


  /**
   * Implementation of {@link ActionListener} fetching agents from
   * {@link SimulationData} and creating instances of {@link Server2dAgent}
   * class used by this engine.
   *
   * @author M.Olszewski
   */
  private class AgentsGroupFetcher implements ActionListener
  {
    /** Sprites repository with agents sprites. */
    private SpritesRepository sprites;
    /**
     * Map representing agents groups with group identifiers, containing
     * maps with agents identifiers and agents.
     */
    private Map<Integer, Map<Integer, Server2dAgent>> groups =
      new HashMap<Integer, Map<Integer, Server2dAgent>>();


    /**
     * Creates instance of {@link AgentsGroupFetcher} class  with specified arguments.
     *
     * @param spritesRepository - sprites repository.
     */
    AgentsGroupFetcher(SpritesRepository spritesRepository)
    {
      if (spritesRepository == null)
      {
        throw new NullPointerException("Specified spritesRepository is null!");
      }

      sprites = spritesRepository;
    }


    /**
     * @see net.java.dante.sim.data.ActionListener#entryAction(net.java.dante.sim.data.object.SimulationObject)
     */
    public void entryAction(SimulationObject object)
    {
      if (object instanceof ServerAgent)
      {
        Integer groupId = Integer.valueOf(object.getParent().getId());
        Map<Integer, Server2dAgent> group = groups.get(groupId);
        if (group == null)
        {
          group = new HashMap<Integer, Server2dAgent>();
          groups.put(groupId, group);
        }

        Server2dAgent agent = new Server2dAgent((ServerAgent)object,
            sprites.getAgentMoveSprites(), sprites.getAgentMoveDelay());
        group.put(Integer.valueOf(object.getId()), agent);
      }
    }

    /**
     * @see net.java.dante.sim.data.ActionListener#exitAction(net.java.dante.sim.data.object.SimulationObject)
     */
    public void exitAction(SimulationObject object)
    {
      // Intentionally left empty.
    }

    /**
     * Creates array with agents groups using data fetched earlier.
     *
     * @return Returns array containing all found agents groups.
     */
    Server2dAgentsGroup[] createGroups()
    {
      Server2dAgentsGroup[] agentsGroups = new Server2dAgentsGroup[groups.size()];

      int i = 0;
      for (Integer groupId : groups.keySet())
      {
        agentsGroups[i] =
            new Server2dAgentsGroup(groupId.intValue(), groups.get(groupId));
        i++;
      }

      return agentsGroups;
    }
  }
}