package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TimeEndedBroadcast implements Broadcast {

    private final String sender  = "TIME_SERVICE";

    public String getSender() {
        return sender;
    }
}
