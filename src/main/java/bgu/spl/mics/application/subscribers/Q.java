package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.KillSubsBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TimeEndedBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

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
//		subscribeBroadcast(KillSubsBroadcast.class, b -> {
//			terminate();
//			System.out.println(getName() + " terminated");
//		});
		subscribeBroadcast(TimeEndedBroadcast.class, b -> {
			terminate();
			System.out.println(getName() + " terminated");
		});
		subscribeBroadcast(TickBroadcast.class, b -> {
			qTime = b.getTime();
		});
		subscribeEvent(GadgetAvailableEvent.class, ev -> {
			System.out.println(getName() + " started handling gadgetAvailableEvent from "+ev.getSender());
			if (!inventoryInstance.getItem(ev.getGadget())) {
				complete(ev, "fail - gadget is not available");
				print("fail - gadget is not available", ev.getSender());
			} else {
				complete(ev, "success");
				print("success", ev.getSender());
				ev.getReport().setQTime(qTime);
				ev.getReport().setGadgetName(ev.getGadget());

			}
		});
	}

	private void print(String msg, String sender){
		System.out.println(getName() + " finished handling gadgetAvailableEvent for "+sender+". result: " +msg);
	}

}
