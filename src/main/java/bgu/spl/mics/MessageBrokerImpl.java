package bgu.spl.mics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private static MessageBroker instance = null;
	private ConcurrentHashMap<Event<?>, Future<?>> futureMap;
	private ConcurrentHashMap<Subscriber, LinkedBlockingDeque<Message>> subscriberList;
	private ConcurrentHashMap<Subscriber, LinkedBlockingDeque<Class<? extends Message>>> topicsList;
	private ConcurrentHashMap<Class<? extends Broadcast>, LinkedBlockingDeque<Subscriber>> broadcastMap;
	private ConcurrentHashMap<Class<? extends Event<?>>, LinkedBlockingDeque<Subscriber>> eventMap;


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
		topicsList = new ConcurrentHashMap<>();
		subscriberList = new ConcurrentHashMap<>();
		eventMap = new ConcurrentHashMap<>();
		futureMap = new ConcurrentHashMap<>();
		broadcastMap = new ConcurrentHashMap<>();
	}
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		if(!eventMap.containsKey(type)){
			eventMap.put(type, new LinkedBlockingDeque<>());
		}
		eventMap.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		if(!broadcastMap.containsKey(type)){
			broadcastMap.put(type, new LinkedBlockingDeque<>());
		}
		broadcastMap.get(type).add(m);
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
		subscriberList.put(m, new LinkedBlockingDeque<>());
		topicsList.put(m, new LinkedBlockingDeque<>());
		// TODO Auto-generated method stub

	}

	@Override
	public void unregister(Subscriber m) {
		subscriberList.remove(m);
		for(Class<? extends Message> type: topicsList.get(m)){
			eventMap.get(type).remove(m);
			broadcastMap.get(type).remove(m);
		}
		topicsList.remove(m);
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
