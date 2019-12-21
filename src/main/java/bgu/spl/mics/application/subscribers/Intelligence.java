package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TimeEndedBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {

	private int serialNumber;
	private HashMap<Integer, LinkedList<MissionInfo>> missionMap;

	public Intelligence(int _serialNumber, JsonArray _missions) {
		super("Intelligence");
		serialNumber = _serialNumber+1;
		missionMap = new HashMap<>();
		for (int i = 0; i < _missions.size(); i++) {
			JsonObject mission = _missions.get(i).getAsJsonObject();
			MissionInfo mi = new MissionInfo();
			LinkedList<String> serialNumbers = createSerialNumbersList(mission.get("serialAgentsNumbers").getAsJsonArray());
			mi.setSerialAgentsNumbers(serialNumbers);
			mi.setDuration(mission.get("duration").getAsInt());
			mi.setGadget(mission.get("gadget").getAsString());
			mi.setMissionName(mission.get("name").getAsString());
			mi.setTimeExpired(mission.get("timeExpired").getAsInt());
			mi.setTimeIssued(mission.get("timeIssued").getAsInt());
			if(!missionMap.containsKey(mi.getTimeIssued())){
				LinkedList<MissionInfo> missionList = new LinkedList<>();
				missionMap.put(mi.getTimeIssued(), missionList);
			}
			missionMap.get(mi.getTimeIssued()).add(mi);
		}
	}


	private LinkedList<String> createSerialNumbersList(JsonArray tmp) {
		LinkedList<String> list = new LinkedList<>();
		JsonArray numbers = tmp.getAsJsonArray();
		for (int k = 0; k < numbers.size(); k++){
			list.add(numbers.get(k).getAsString());
		}
		return list;
	}

	@Override
	protected void initialize() {
		System.out.println(getName() + getSerialNumber() + " started");
		subscribeBroadcast(TickBroadcast.class, b -> {
			if (missionMap.containsKey(b.getTime())) {
				for (int i = 0; i < missionMap.get(b.getTime()).size(); i++) {
					System.out.println(getName() + getSerialNumber() + " sent a missionReceivedEvent");
					getSimplePublisher().sendEvent(new MissionReceivedEvent(missionMap.get(b.getTime()).get(i)));
				}
			}
		});
		subscribeBroadcast(TimeEndedBroadcast.class, b -> {
			terminate();
			System.out.println(getName() + getSerialNumber() + " terminated");
		});

	}

	public int getSerialNumber(){
		return serialNumber;
	}
}