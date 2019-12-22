package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Killer extends Subscriber {
    private int mDesired;
    private int mAmount;
    public Killer (int mAmount) {
        super("Killer");
        this.mAmount = mAmount;
    }




    @Override
    protected void initialize() {
        subscribeEvent(MTerminatedEvent.class, ev->{
            mDesired++;
            if(mDesired == mAmount){
                getSimplePublisher().sendBroadcast(new KillSubsBroadcast());
            }
        });

    }


}
