package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private int time;
    private int totalTime;
    public TickBroadcast(int time, int totalTime){
        this.time = time;
        this.totalTime = totalTime;
    }

    public int getTime() {
            return time;
        }


    public int getTotalTime() {
        return totalTime;
    }
}
