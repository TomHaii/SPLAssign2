package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private int remainingTime;
    private int time;

    public TickBroadcast(int time, int remainingTime){
        this.remainingTime = remainingTime;
        this.time = time;
    }

    public int getTime() {
            return time;
        }

    public int getRemainingTime() {
        return remainingTime;
    }
}
