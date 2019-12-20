package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TimeEndedBroadcast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */


public class TimeService extends Publisher {

	private int timeTicks;
	private int currTime = 0;
	private Timer timer;


	public TimeService(int timeTicks) {
		super("TimeService");
		this.timeTicks = timeTicks;
		timer = new Timer();
		initialize();

	}

	@Override
	protected void initialize() {
		System.out.println("TimeService started");
		
	}


	@Override
	public void run() {
		timer.schedule(new TimerTask(){
			public void run(){
				if(currTime<timeTicks) {
					currTime++;
					MessageBrokerImpl.getInstance().sendBroadcast(new TickBroadcast(currTime));
				}
				else{
					timer.cancel();
					MessageBrokerImpl.getInstance().sendBroadcast(new TimeEndedBroadcast());
				}
			}
		},0, 100);
	}

}
