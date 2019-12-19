package bgu.spl.mics;

import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private static class MessageBrokerSingletonHolder {
		private volatile static MessageBroker instance = new MessageBrokerImpl();
	}
	private volatile ConcurrentHashMap<Event, Future> futureMap;
	private volatile ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> subscriberList;
	private volatile ConcurrentHashMap<Subscriber, ConcurrentLinkedQueue<Class<? extends Message>>> topicsList;
	private volatile ConcurrentHashMap<Class<? extends Broadcast>, ConcurrentLinkedQueue<Subscriber>> broadcastMap;
	private volatile ConcurrentHashMap<Class<? extends Event<?>>, ConcurrentLinkedQueue<Subscriber>> eventMap;
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
			while (!tmpQ.isEmpty()) {
				Subscriber tmpSub = tmpQ.poll();
				subscriberList.get(tmpSub).add(b);
			}
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		if (eventMap.containsKey(e.getClass()) && !eventMap.get(e.getClass()).isEmpty()) {
			synchronized (eventMap.get(e.getClass())) {
				Subscriber subToSendEvent = eventMap.get(e.getClass()).poll();
				Future<T> future = new Future<>();
				futureMap.putIfAbsent(e, future);
				if (subToSendEvent != null) {
					subscriberList.get(subToSendEvent).add(e);
					eventMap.get(e.getClass()).add(subToSendEvent);
					return future;
				}
				else{
					System.out.println("None of the subscribers is capable of handling this event");
					complete(e, null);
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
		/* synchronized (subscriberList.get(m)) {
			while (!subscriberList.get(m).isEmpty()) {
				try {
					subscriberList.get(m).wait();
				} catch (Exception ignored) {}
				subscriberList.remove(m);
			}
		} */
		for(Class<? extends Message> type: topicsList.get(m)){
			if(eventMap.containsKey(type))
				eventMap.get(type).remove(m);
			if(broadcastMap.containsKey(type))
				broadcastMap.get(type).remove(m);
		}
		topicsList.remove(m);
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
			return subscriberList.get(m).poll();
	}

	

}
