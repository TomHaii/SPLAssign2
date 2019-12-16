package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import com.google.gson.JsonArray;

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
	private LinkedList<MissionInfo> missions;

	public Intelligence(int _serialNumber, JsonArray _missions) {
		super("Intelligence");
		serialNumber = _serialNumber+1;
		missions =  new LinkedList<>();
		for (int i = 0; i < _missions.size(); i++) {
			JsonArray mission = _missions.get(i).getAsJsonArray();
			MissionInfo mi = new MissionInfo();
			LinkedList<String> serialNumbers = createSerialNumbersList(mission.get(0).getAsJsonArray());
			mi.setSerialAgentsNumbers(serialNumbers);
			mi.setDuration(mission.get(1).getAsInt());
			mi.setGadget(mission.get(2).toString());
			mi.setMissionName(mission.get(3).toString());
			mi.setTimeExpired(mission.get(4).getAsInt());
			mi.setTimeExpired(mission.get(5).getAsInt());
			missions.add(mi);
		}
	}

	public Intelligence() {
		super("Intelligence");
	}

	private LinkedList<String> createSerialNumbersList(JsonArray tmp) {
		LinkedList<String> list = new LinkedList<>();
		JsonArray numbers = tmp.get(0).getAsJsonArray();
		for (int k = 0; k < numbers.size(); k++){
			list.add(numbers.get(k).toString());
		}
		return list;
	}

	@Override
	protected void initialize() {

		// TODO Implement this
	}
}