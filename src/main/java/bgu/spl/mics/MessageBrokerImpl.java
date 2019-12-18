package bgu.spl.mics;

import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private static class MessageBrokerSingletonHolder {
		private static MessageBroker instance = new MessageBrokerImpl();
	}
	private ConcurrentHashMap<Event<?>, Future<?>> futureMap;
	private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> subscriberList;
	private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Class<? extends Message>>> topicsList;
	private ConcurrentHashMap<Class<? extends Broadcast>, LinkedBlockingQueue<Subscriber>> broadcastMap;
	private ConcurrentHashMap<Class<? extends Event<?>>, LinkedBlockingQueue<Subscriber>> eventMap;
	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return MessageBrokerSingletonHolder.instance;
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
			eventMap.put(type, new LinkedBlockingQueue<>());
		}
		eventMap.get(type).add(m);
		topicsList.get(m).add(type);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		if(!broadcastMap.containsKey(type)){
			broadcastMap.put(type, new LinkedBlockingQueue<>());
		}
		broadcastMap.get(type).add(m);
		topicsList.get(m).add(type);

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		for(Event<?> event: futureMap.keySet()){
			if(event.equals(e))
				futureMap.get(e).resolve(result);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		LinkedBlockingQueue<Subscriber> tmpQ;
		if(broadcastMap.containsKey(b.getClass())) {
			tmpQ = broadcastMap.get(b.getClass());
			while (!tmpQ.isEmpty()) {
				Subscriber tmpSub = tmpQ.poll();
				subscriberList.get(tmpSub).add(b);
			}
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		if(eventMap.containsKey(e.getClass()) && !eventMap.get(e.getClass()).isEmpty()){
			synchronized (eventMap.get(e.getClass())){
				Subscriber subToSendEvent = eventMap.get(e.getClass()).poll();
				subscriberList.get(subToSendEvent).add(e);
				Future<T> future = new Future<>();
				futureMap.put(e,future);
				eventMap.get(e.getClass()).add(subToSendEvent);
				return future;
			}
		}
		return null;
	}

	@Override
	public void register(Subscriber m) {
		subscriberList.put(m, new LinkedBlockingQueue<>());
		topicsList.put(m, new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(Subscriber m) {
		subscriberList.remove(m);
		for(Class<? extends Message> type: topicsList.get(m)){
			if(eventMap.containsKey(type))
				eventMap.get(type).remove(m);
			else
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
