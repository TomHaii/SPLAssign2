package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.ReleaseAgentsEvent;
import bgu.spl.mics.application.messages.SendAgentsEvent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {

	private static Squad squadInstance = Squad.getInstance();
	private int serialNumber;
	private boolean agentSender;

	public Moneypenny(int i) {
		super("MoneyPenny");
		serialNumber = i;
		agentSender = (i%2 == 0);
	}

	public Moneypenny() {
		super("MoneyPenny");
	}


	public int getSerialNumber(){
		return serialNumber;
	}

	public boolean isAgentSender(){
		return agentSender;
	}


	@Override
	protected void initialize() {
		if (isAgentSender()) {
			subscribeEvent(SendAgentsEvent.class, ev -> {
				squadInstance.sendAgents(ev.getAgentSerialNumbers(), ev.getTime());
				complete(ev, "success");
			});
			subscribeEvent(ReleaseAgentsEvent.class, ev -> {
				squadInstance.releaseAgents(ev.getAgentSerialNumbers());
				complete(ev, "success");
			});
		}
		else{
			subscribeEvent(AgentsAvailableEvent.class, ev -> {
				while (!squadInstance.getAgents(ev.getAgentSerialNumbers())) {
					try {
						squadInstance.wait();
					} catch (Exception ignored) {
					}
				}
				complete(ev, "success");
			});
		}
	}

}
