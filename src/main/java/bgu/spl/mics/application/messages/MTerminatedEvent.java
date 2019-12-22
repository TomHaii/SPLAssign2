package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class MTerminatedEvent implements Event<String> {

    private String sender;

    public MTerminatedEvent(String sender){
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }
}
