package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.concurrent.TimeUnit;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private Diary diary = Diary.getInstance();
	private int serialNumber;
	private int mTime;


	public M (int i){
		super("M");
		serialNumber = i;
		mTime = 0;
	}

	@Override
	protected void initialize() {
		System.out.println(getName() + getSerialNumber() + " started");
		subscribeBroadcast(TimeEndedBroadcast.class, b -> {
			diary.printToFile("diary.json");
			getSimplePublisher().sendEvent(new MTerminatedEvent(getName()+getSerialNumber()));
			terminate();
			System.out.println(getName() + getSerialNumber() + " terminated");

		});
		subscribeBroadcast(TickBroadcast.class, b -> {
			mTime = b.getTime();
		});
		subscribeEvent(MissionReceivedEvent.class, ev -> {
			Report report = new Report();
			report.setTimeCreated(mTime);
			report.setMissionName(ev.getMissionInfo().getMissionName());
			report.setTimeIssued(ev.getMissionInfo().getTimeIssued());
			System.out.println(getName() + getSerialNumber() + " is handling a missionReceivedEvent from " +ev.getSender());
			MissionInfo missionInfo = ev.getMissionInfo();
			report.setM(serialNumber);
			Future agentsAvailable = getSimplePublisher().sendEvent(new AgentsAvailableEvent(ev.getMissionInfo().getSerialAgentsNumbers(), report, getName()+getSerialNumber()));
			if (agentsAvailable.get().equals("success")) {
				Future gadgetAvailable = getSimplePublisher().sendEvent(new GadgetAvailableEvent(missionInfo.getGadget(), report, getName()+getSerialNumber()));
				if (gadgetAvailable.get().equals("success")) {
					if (mTime < missionInfo.getTimeExpired()) {
						getSimplePublisher().sendEvent(new SendAgentsEvent(ev.getMissionInfo().getSerialAgentsNumbers(), missionInfo.getDuration(), getName()+getSerialNumber()));
						complete(ev, "success");
						diary.addReport(report);
						print("success");

					} else {
						getSimplePublisher().sendEvent(new ReleaseAgentsEvent(ev.getMissionInfo().getSerialAgentsNumbers(), getName()+getSerialNumber()));
						complete(ev, "fail - mission time expired");
						print("fail - mission time expired");
					}
				} else {
					getSimplePublisher().sendEvent(new ReleaseAgentsEvent(ev.getMissionInfo().getSerialAgentsNumbers(), getName()+getSerialNumber()));
					complete(ev, "fail - gadget is not available");
					print("fail - gadget is not available");

				}
			} else {
				complete(ev, "fail - agents are not available");
				print("fail - agents are not available");
			}
			diary.incrementTotal();
		});
	}

	private int getSerialNumber() {
		return serialNumber;
	}

	private void print(String msg){
		System.out.println(getName() + getSerialNumber() + " finished handling missionReceivedEvent. result: " +msg);
	}

}
