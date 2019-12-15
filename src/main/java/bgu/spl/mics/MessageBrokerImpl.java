package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.LinkedList;
import java.util.List;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private List<Subscriber> subscriberList;
	private static MessageBroker instance = null;
	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		if(instance == null){
			instance = new MessageBrokerImpl();
		}
		return instance;
	}
	private MessageBrokerImpl(){
		subscriberList = new LinkedList<Subscriber>();
	}
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void register(Subscriber m) {
		subscriberList.add(m);
		// TODO Auto-generated method stub

	}

	@Override
	public void unregister(Subscriber m) {
		// TODO Auto-generated method stub
		subscriberList.remove(m);
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
