package bgu.spl.mics.application.passiveObjects;
import javax.swing.*;
import java.util.*;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {
	private static class SquadSingletonHolder {
		private static Squad instance = new Squad();
	}
	private Map<String, Agent> agents;

	private Squad(){
		agents = new HashMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return SquadSingletonHolder.instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for (Agent agent : agents) {
			(this.agents).put(agent.getSerialNumber(), agent);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials) {
		synchronized (this) {
			for (String s : serials) {
				agents.get(s).release();
			}
			notifyAll();
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		try {
			Thread.sleep(time * 100);
			releaseAgents(serials);
		}
		catch (Exception ignored){}
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials) {
		synchronized (this) {
			LinkedList<Agent> acquiredAgents = new LinkedList<>();
			for (String s : serials) {
				Agent agent = agents.get(s);
				if (agent.isAvailable()) {
					acquiredAgents.add(agent);
					agent.acquire();
				} else {
					for (Agent a : acquiredAgents) {
						a.release();
					}
					return false;
				}
			}
			return true;
		}
	}


	/**
	 * gets the agents names
	 * @param serials the serial numbers of the agents
	 * @return a list of the names of the agents with the specified serials.
	 */
	public List<String> getAgentsNames(List<String> serials){
		List<String> list = new LinkedList<>();
		for (String s : serials){
			list.add(agents.get(s).getName());
		}
		return list;
	}

}