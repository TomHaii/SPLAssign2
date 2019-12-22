package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEvent implements Event<String> {
    private MissionInfo missionInfo;
    private String sender;

    public MissionReceivedEvent(MissionInfo missionInfo, String sender){
        this.missionInfo = missionInfo;
        this.sender = sender;
    }

    public MissionInfo getMissionInfo(){
        return missionInfo;
    }


    public String getSender() {
        return sender;
    }
}
