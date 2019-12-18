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
	private ConcurrentHashMap<Message, Future> futureMap;
	private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> subscriberList;
	private ConcurrentHashMap<Subscriber, ConcurrentLinkedQueue<Class<? extends Message>>> topicsList;
	private ConcurrentHashMap<Class<? extends Broadcast>, ConcurrentLinkedQueue<Subscriber>> broadcastMap;
	private ConcurrentHashMap<Class<? extends Event<?>>, ConcurrentLinkedQueue<Subscriber>> eventMap;
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
			eventMap.put(type, new ConcurrentLinkedQueue<>());
		}
		eventMap.get(type).add(m);
		topicsList.get(m).add(type);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		if(!broadcastMap.containsKey(type)){
			broadcastMap.put(type, new ConcurrentLinkedQueue<>());
		}
		broadcastMap.get(type).add(m);
		topicsList.get(m).add(type);

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		Future<T> future = futureMap.get(e);
		future.resolve(result);
		futureMap.remove(e);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if(broadcastMap.containsKey(b.getClass())) {
			ConcurrentLinkedQueue<Subscriber> tmpQ = new ConcurrentLinkedQueue<>(broadcastMap.get(b.getClass()));
			//Future future = new Future<>();
			//futureMap.putIfAbsent(b, future);
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
				if(subToSendEvent != null) {
					subscriberList.get(subToSendEvent).add(e);
					Future<T> future = new Future<>();
					futureMap.putIfAbsent(e, future);
					eventMap.get(e.getClass()).add(subToSendEvent);
					return future;
				}
			}
		}
		return null;
	}

	@Override
	public void register(Subscriber m) {
		subscriberList.putIfAbsent(m, new LinkedBlockingQueue<>());
		topicsList.putIfAbsent(m, new ConcurrentLinkedQueue<>());
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
			return subscriberList.get(m).poll();
	}

	

}
