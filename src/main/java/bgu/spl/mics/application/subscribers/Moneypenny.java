package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {

	private final static Squad squadInstance = Squad.getInstance();
	private int serialNumber;
	private boolean agentSender;

	public Moneypenny(int i) {
		super("MoneyPenny");
		serialNumber = i;
		agentSender = (i%2 == 0);
	}


	public int getSerialNumber() {
		return serialNumber;
	}

	public boolean isAgentSender() {
		return agentSender;
	}


	@Override
	protected void initialize() {
		System.out.println(getName() + getSerialNumber() + " started");
		subscribeBroadcast(KillSubsBroadcast.class, b -> {
			terminate();
			System.out.println(getName() + getSerialNumber() + " terminated");


		});
		if (isAgentSender()) {
			subscribeEvent(SendAgentsEvent.class, ev -> {
				System.out.println(getName() + getSerialNumber() + " is handling a sendAgentsEvent from "+ev.getSender());
				squadInstance.sendAgents(ev.getAgentSerialNumbers(), ev.getTime());
				squadInstance.releaseAgents(ev.getAgentSerialNumbers());
				complete(ev, "success");
				print(SendAgentsEvent.class, "success", ev.getSender());
			});
			subscribeEvent(ReleaseAgentsEvent.class, ev -> {
				System.out.println(getName() + getSerialNumber() + " is handling a releaseAgentsEvent from "+ev.getSender());
				squadInstance.releaseAgents(ev.getAgentSerialNumbers());
				complete(ev, "success");
				print(ReleaseAgentsEvent.class, "success",ev.getSender());
			});
		} else {
			subscribeEvent(AgentsAvailableEvent.class, ev -> {
				System.out.println(getName() + getSerialNumber() + " is handling an AgentsAvailableEvent from "+ev.getSender());

				if (!squadInstance.getAgents(ev.getAgentSerialNumbers())) {
					complete(ev, "fail - agents requested do not exist");
					print(AgentsAvailableEvent.class, "fail - agents requested do not exist", ev.getSender());
				} else {
					complete(ev, "success");
					ev.getReport().setMoneypenny(getSerialNumber());
					ev.getReport().setAgentsSerialNumbers(ev.getAgentSerialNumbers());
					ev.getReport().setAgentsNames(squadInstance.getAgentsNames(ev.getAgentSerialNumbers()));

					print(AgentsAvailableEvent.class, "success", ev.getSender());
				}
			});
		}
	}

	private void print(Class<? extends Event<?>> cl, String msg, String sender) {
		if (cl == SendAgentsEvent.class) {
			print("SendAgentEvent", msg, sender);
		} else if (cl == ReleaseAgentsEvent.class) {
			print("ReleaseAgentsEvent", msg, sender);
		} else {
			print("AgentsAvailableEvent", msg, sender);

		}
	}

	private void print(String cl, String msg, String sender) {
		System.out.println(getName() + getSerialNumber() + " finished handling " + cl +" for " +sender + ". result: " + msg);

	}


}
