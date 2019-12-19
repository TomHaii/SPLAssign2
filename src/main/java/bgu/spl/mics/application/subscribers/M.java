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
	}

	public M() {
		super("M");
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, ev->{
			mTime++;
		});
		subscribeEvent(MissionReceivedEvent.class, ev->{
			MissionInfo missionInfo = ev.getMissionInfo();
			Future agentsAvailable = getSimplePublisher().sendEvent(new AgentsAvailableEvent(ev.getMissionInfo().getSerialAgentsNumbers()));
			if(agentsAvailable.get().equals("success")){
				Future gadgetAvailable = getSimplePublisher().sendEvent(new GadgetAvailableEvent(missionInfo.getGadget()));
				if(gadgetAvailable.get().equals("success")){
					if(mTime < missionInfo.getTimeExpired()){
						getSimplePublisher().sendEvent(new SendAgentsEvent(ev.getMissionInfo().getSerialAgentsNumbers(), missionInfo.getDuration()));
					}
				}
			}
		});
		
	}

	protected void setSerialNumber(int i) {
		serialNumber = i;
	}
}
