package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Killer extends Subscriber {
    private AtomicInteger mTerminated = new AtomicInteger(0);
    private int mAmount;
    public Killer (int mAmount) {
        super("Killer");
        this.mAmount = mAmount;
    }




    @Override
    protected void initialize() {
        subscribeEvent(MTerminatedEvent.class, ev->{
            mTerminated.compareAndSet(mTerminated.get(), mTerminated.get()+1);
            if(mTerminated.get() >= mAmount){
                System.out.println("KILLING STARTED MUAHAHAHA");
                getSimplePublisher().sendBroadcast(new KillSubsBroadcast());
                complete(ev, "success");
                terminate();
                System.out.println("Killer Terminated");
            }
            complete(ev, "waiting");
        });

    }


}
