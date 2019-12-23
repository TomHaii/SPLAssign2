package bgu.spl.mics;

import bgu.spl.mics.application.messages.TimeEndedBroadcast;

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
	private volatile ConcurrentHashMap<Event, Future> futureMap;  //this map contains all the unresolved events and their matching futures
	private volatile ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> subscriberList; //this map contains all the subscribers and their messages queue
	private volatile ConcurrentHashMap<Subscriber, ConcurrentLinkedQueue<Class<? extends Message>>> topicsList; //this map contains all the subscribers and the topics they are subscribed to
	private volatile ConcurrentHashMap<Class<? extends Broadcast>, ConcurrentLinkedQueue<Subscriber>> broadcastMap; //this maps contains all types of events adn the subscribers that are subscribed to it
	private volatile ConcurrentHashMap<Class<? extends Event<?>>, ConcurrentLinkedQueue<Subscriber>> eventMap; //this maps contains all types of broadcasts adn the subscribers that are subscribed to it
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
		eventMap.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		eventMap.get(type).add(m);
		topicsList.get(m).add(type);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
			broadcastMap.putIfAbsent(type, new ConcurrentLinkedQueue<>());
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
	//copying this broadcast subscribers queue and emptying it
	public void sendBroadcast(Broadcast b) {
		if (broadcastMap.containsKey(b.getClass())) {
			ConcurrentLinkedQueue<Subscriber> tmpQ = new ConcurrentLinkedQueue<>(broadcastMap.get(b.getClass()));
			while (!tmpQ.isEmpty()) {
				Subscriber tmpSub = tmpQ.poll();
				subscriberList.get(tmpSub).add(b);
			}
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		if (eventMap.containsKey(e.getClass())) {
			synchronized (eventMap.get(e.getClass())) {  //synchronizing the queue so that the round robin would work correctly
				Subscriber subToSendEvent = eventMap.get(e.getClass()).poll(); //retrieving the subscriber at the head of the queue
				Future<T> future = new Future<>();
				futureMap.put(e, future);
				if (subToSendEvent != null) {
					subscriberList.get(subToSendEvent).add(e);
					eventMap.get(e.getClass()).add(subToSendEvent); //adding the subscriber we pulled at the tail of the queue
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
		for (Class<? extends Message> type : topicsList.get(m)) {
				if (eventMap.containsKey(type)){
					eventMap.get(type).remove(m);
			}
				if (broadcastMap.containsKey(type))
					broadcastMap.get(type).remove(m);
			}
		topicsList.remove(m);
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		return subscriberList.get(m).take();
	}

	

}
