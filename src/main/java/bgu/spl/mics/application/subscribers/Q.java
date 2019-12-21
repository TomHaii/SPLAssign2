package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TimeEndedBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.LinkedList;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

	private static Inventory inventoryInstance = Inventory.getInstance();
	private int qTime = 0;
	public Q() {
		super("Q");
	}

	@Override
	protected void initialize() {
		System.out.println(getName() + " started");
		subscribeBroadcast(TimeEndedBroadcast.class, b -> {
			terminate();
			inventoryInstance.printToFile("inventory.json");
			System.out.println(getName() + " terminated");
		});
		subscribeBroadcast(TickBroadcast.class, b -> {
			qTime = b.getTime();
		});
		subscribeEvent(GadgetAvailableEvent.class, ev -> {
			System.out.println(getName() + " Started handling gadgetAvailableEvent");
			ev.getReport().setQTime(qTime);
			ev.getReport().setGadgetName(ev.getGadget());
			if (!inventoryInstance.getItem(ev.getGadget())) {
				complete(ev, "fail - gadget is not available");
				print("fail - gadget is not available");
			} else {
				complete(ev, "success");
				print("success");

			}
		});
	}

	private void print(String msg){
		System.out.println(getName() + " finished handling gadgetAvailableEvent. result: " +msg);
	}

}
