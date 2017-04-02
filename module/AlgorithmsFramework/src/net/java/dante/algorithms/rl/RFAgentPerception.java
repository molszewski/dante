/*
 * Created on 2006-09-10
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.rl;

import net.java.dante.algorithms.common.Dbg;

import pl.gdan.elsy.qconf.Perception;


/**
 * Perception of {@link RFAgent} object.
 *
 * @author M.Olszewski
 */
public class RFAgentPerception extends Perception
{
  private static final long serialVersionUID = 1L;
  
  private RFAgent agent;
  

  /**
   * Creates instance of {@link RFAgentPerception} class.
   *
   * @param learningAgent learning agent.
   */
  public RFAgentPerception(RFAgent learningAgent)
  {
    if (learningAgent == null)
    {
      throw new NullPointerException("Specified learningAgent is null!");
    }

    agent = learningAgent;
  }

  /**
   * @see pl.gdan.elsy.qconf.Perception#getReward()
   */
  @Override
  public double getReward()
  {
    double reward = 0.0;

    switch (agent.getLastCommandType())
    {
      case ATTACK_COMMAND:
      {
        if (agent.weaponNotRealoaded())
        {
          reward = -0.1;
        }
        else if (!agent.attackedEnemyInRange())
        {
          reward = -0.3;
        }
        else
        {
          reward = 0.4;
        }

        break;
      }
      case MOVE_COMMAND:
      {
        if (agent.isEnemyInRange() && agent.isWeaponReady())
        {
          reward = -0.15;
        }
        else if (!agent.movedWell())
        {
          reward = -0.2;
        }
        else
        {
          reward = 0.2;
        }
        
        break;
      }

      default:
      {
        break;
      }
    }
    
//    if (Dbg.DBG1) 
      Dbg.write("Reward=" + reward);
    return reward;
  }

  /**
   * @see pl.gdan.elsy.qconf.Perception#updateInputValues()
   */
  @Override
  protected void updateInputValues()
  {
    setNextValue(agent.checkPossibleCollisions());
    setNextValue(agent.isWeaponReady());
    setNextValue(agent.isEnemyInRange());
  }
}