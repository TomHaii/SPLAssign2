package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.ReleaseAgentsEvent;
import bgu.spl.mics.application.messages.SendAgentsEvent;
import bgu.spl.mics.application.messages.TimeEndedBroadcast;
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

	private final static Squad squadInstance = Squad.getInstance();
	private int serialNumber;
	private boolean agentSender;

	public Moneypenny(int i) {
		super("MoneyPenny");
		serialNumber = i;
		agentSender = (i % 2 == 0);
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
		subscribeBroadcast(TimeEndedBroadcast.class, b -> {
			terminate();
			System.out.println(getName() + getSerialNumber() + " terminated");

		});
		if (isAgentSender()) {
			subscribeEvent(SendAgentsEvent.class, ev -> {
				System.out.println(getName() + getSerialNumber() + " is handling a sendAgentsEvent");
				squadInstance.sendAgents(ev.getAgentSerialNumbers(), ev.getTime());
				complete(ev, "success");
				print(SendAgentsEvent.class, "success");
			});
			subscribeEvent(ReleaseAgentsEvent.class, ev -> {
				System.out.println(getName() + getSerialNumber() + " is handling a releaseAgentsEvent");
				squadInstance.releaseAgents(ev.getAgentSerialNumbers());
				complete(ev, "success");
				print(ReleaseAgentsEvent.class, "success");

			});
		} else {
			subscribeEvent(AgentsAvailableEvent.class, ev -> {

				System.out.println(getName() + getSerialNumber() + " is handling an AgentsAvailableEvent");
				ev.getReport().setMoneypenny(getSerialNumber());
				ev.getReport().setAgentsSerialNumbers(ev.getAgentSerialNumbers());
				ev.getReport().setAgentsNames(squadInstance.getAgentsNames(ev.getAgentSerialNumbers()));
				synchronized (squadInstance) {
					while (!squadInstance.getAgents(ev.getAgentSerialNumbers())) {
						try {
							squadInstance.wait();
							System.out.println("dddd");
						} catch (Exception ignored) {
						}
					}
				}
				complete(ev, "success");
				print(AgentsAvailableEvent.class, "success");
			});
		}
	}

	private void print(Class<? extends Event<?>> cl, String msg) {
		if (cl == SendAgentsEvent.class) {
			print("SendAgentEvent", msg);
		} else if (cl == ReleaseAgentsEvent.class) {
			print("ReleaseAgentsEvent", msg);
		} else {
			print("AgentsAvailableEvent", msg);

		}
	}

	private void print(String cl, String msg) {
		System.out.println(getName() + getSerialNumber() + " finished handling " + cl + " result: " + msg);

	}


}
