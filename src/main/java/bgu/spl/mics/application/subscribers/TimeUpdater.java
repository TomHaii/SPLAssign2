package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.KillSubsBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TimeEndedBroadcast;
import bgu.spl.mics.application.messages.TimeUpdateEvent;

import java.util.Timer;
import java.util.TimerTask;

public class TimeUpdater extends Subscriber {
    private int time;
    Timer timer = new Timer();

    public TimeUpdater(){
        super("TimeUpdater");
        time = 0;

    }

    @Override
    protected void initialize() {
        subscribeBroadcast(KillSubsBroadcast.class, b -> {
            timer.cancel();
            terminate();
            System.out.println(getName() + " terminated");

        });
        subscribeBroadcast(TickBroadcast.class, b -> {
            time = b.getTime();
        });
        subscribeBroadcast(TimeEndedBroadcast.class, b -> {
            timer.schedule(new TimerTask(){
                public void run(){
                    time++; }
            },0, 100);
        });
        subscribeEvent(TimeUpdateEvent.class, ev->{complete(ev, time);});
    }
}
