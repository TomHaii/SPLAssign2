package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	private int serialNumber;
	private int mTime;


	public M (int i){
		super("M");
		serialNumber = i;
		mTime = 0;
		initialize();
	}

	@Override
	protected void initialize() {
		System.out.println(getName() + " " + getSerialNumber() + " started");
		subscribeBroadcast(TimeEndedBroadcast.class, b->{terminate();});
		subscribeBroadcast(TickBroadcast.class, b->{
			mTime = b.getTime();
		});
		subscribeEvent(MissionReceivedEvent.class, ev->{
			System.out.println(getName() + " " + getSerialNumber() + " is handling a missionReceivedEvent");
			MissionInfo missionInfo = ev.getMissionInfo();
			Future agentsAvailable = getSimplePublisher().sendEvent(new AgentsAvailableEvent(ev.getMissionInfo().getSerialAgentsNumbers()));
			if(agentsAvailable.get().equals("success")){
				Future gadgetAvailable = getSimplePublisher().sendEvent(new GadgetAvailableEvent(missionInfo.getGadget()));
				if(gadgetAvailable.get().equals("success")){
					if(mTime < missionInfo.getTimeExpired()){
						getSimplePublisher().sendEvent(new SendAgentsEvent(ev.getMissionInfo().getSerialAgentsNumbers(), missionInfo.getDuration()));
						complete(ev, "success");
						print("success");

					}
					else{
						getSimplePublisher().sendEvent(new ReleaseAgentsEvent(ev.getMissionInfo().getSerialAgentsNumbers()));
						complete(ev, "fail - mission time expired");
						print("fail - mission time expired");
					}
				}
				else{
					getSimplePublisher().sendEvent(new ReleaseAgentsEvent(ev.getMissionInfo().getSerialAgentsNumbers()));
					complete(ev, "fail - gadget not available");
					print("fail - gadget not available");

				}
			}
			else{
				complete(ev, "fail - agents are not available");
				print("fail - agents are not available");
			}
		});
		
	}

	private int getSerialNumber() {
		return serialNumber;
	}

	private void print(String msg){
		System.out.println(getName() + " " + getSerialNumber() + " finished handling missionReceivedEvent. result: " +msg);
	}

}
