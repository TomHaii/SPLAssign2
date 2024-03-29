package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

import java.sql.Time;
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
	private int TIME;

	public M (int i, int time){
		super("M");
		serialNumber = i;
		mTime = 0;
		TIME = time;
	}

	@Override
	protected void initialize() {
		System.out.println(getName() + getSerialNumber() + " started");
		subscribeBroadcast(TimeEndedBroadcast.class, b -> {
			terminate();
            System.out.println(getName() + getSerialNumber() + " terminated");

        });
		subscribeBroadcast(TickBroadcast.class, b -> {
			mTime = b.getTime();
		});
		subscribeEvent(MissionReceivedEvent.class, ev -> {
			diary.incrementTotal();
			Report report = new Report();
			System.out.println(getName() + getSerialNumber() + " is handling a missionReceivedEvent from " + ev.getSender() + "  |  mission name: " + ev.getMissionInfo().getMissionName());
			MissionInfo missionInfo = ev.getMissionInfo();
			Future agentsAvailable = getSimplePublisher().sendEvent(new AgentsAvailableEvent(ev.getMissionInfo().getSerialAgentsNumbers(), report, getName() + getSerialNumber()));
			if (agentsAvailable != null) {
				if (agentsAvailable.get(100*TIME,TimeUnit.MILLISECONDS)!= null && agentsAvailable.get(100*(TIME),TimeUnit.MILLISECONDS).equals("success")) {
					Future gadgetAvailable = getSimplePublisher().sendEvent(new GadgetAvailableEvent(missionInfo.getGadget(), report, getName() + getSerialNumber()));
					if (gadgetAvailable != null) {
						if (gadgetAvailable.get(100*TIME,TimeUnit.MILLISECONDS)!= null && gadgetAvailable.get(100*(TIME),TimeUnit.MILLISECONDS).equals("success")) {
							if (mTime < TIME && mTime < missionInfo.getTimeExpired()) {
								report.setTimeIssued(ev.getMissionInfo().getTimeIssued());

								report.setTimeCreated(mTime);
								report.setMissionName(ev.getMissionInfo().getMissionName());
								report.setM(serialNumber);
								diary.addReport(report);
								Future sendAgents = getSimplePublisher().sendEvent(new SendAgentsEvent(ev.getMissionInfo().getSerialAgentsNumbers(), missionInfo.getDuration(), getName() + getSerialNumber()));
								if(sendAgents != null) {
									complete(ev, "success");
									print("success", ev.getSender());
								}
								else{
									complete(ev, "fail - time ended");
									print("fail - time ended", ev.getSender());
								}
							} else {
								getSimplePublisher().sendEvent(new ReleaseAgentsEvent(ev.getMissionInfo().getSerialAgentsNumbers(), getName() + getSerialNumber()));
								complete(ev, "fail - mission time expired");
								print("fail - mission time expired", ev.getSender());
							}
						} else {
							getSimplePublisher().sendEvent(new ReleaseAgentsEvent(ev.getMissionInfo().getSerialAgentsNumbers(), getName() + getSerialNumber()));
							complete(ev, "fail - gadget is not available");
							print("fail - gadget is not available", ev.getSender());

						}
					}
					else{
						complete(ev, "fail - time ended");
						print("fail - time ended", ev.getSender());
					}
				} else {
					complete(ev, "fail - agents are not available");
					print("fail - agents are not available", ev.getSender());
				}
			}
			else{
				complete(ev, "fail - time ended");
				print("fail - time ended", ev.getSender());
			}
		});
	}

	private int getSerialNumber() {
		return serialNumber;
	}

	private void print(String msg, String sender){
		System.out.println(getName() + getSerialNumber() + " finished handling missionReceivedEvent for "+sender+". result: " +msg);
	}

}