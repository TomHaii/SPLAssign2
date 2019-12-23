package bgu.spl.mics;

import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.publishers.ExampleMessageSender;
import bgu.spl.mics.example.subscribers.ExampleBroadcastSubscriber;
import bgu.spl.mics.example.subscribers.ExampleEventHandlerSubscriber;
import com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    MessageBroker broker;
    ExampleEvent exampleEvent;
    ExampleBroadcast exampleBroadcast;
    ExampleEventHandlerSubscriber exampleEventHandlerSubscriber;
    ExampleMessageSender eventSender;
    ExampleMessageSender broadcastSender;

    ExampleBroadcastSubscriber exampleBroadcastSubscriber;
    @BeforeEach
    public void setUp(){
        broker = MessageBrokerImpl.getInstance();
        exampleBroadcast = new ExampleBroadcast("sender1");
        exampleEvent = new ExampleEvent("sender2");
        exampleEventHandlerSubscriber = new ExampleEventHandlerSubscriber("evHandler" , new String[]{"1"});
        exampleBroadcastSubscriber = new ExampleBroadcastSubscriber("broadHandler", new String[]{"1"});
        broadcastSender = new ExampleMessageSender("sender1", new String[]{"broadcast"});
        eventSender = new ExampleMessageSender("sender2", new String[]{"event"});
        broker.register(exampleEventHandlerSubscriber);
        broker.register(exampleBroadcastSubscriber);
    }

    @Test
    public void test(){
        assertNotNull(broker);
        //broker.subscribeEvent(exampleEvent.getClass(), exampleEventHandlerSubscriber);
    //    eventSender.getSimplePublisher().sendEvent(exampleEvent).resolve("Hello from evHandler");
      //  assertEquals(eventSender.getSimplePublisher().sendEvent(exampleEvent).get(), "Hello from evHandler");

//        broker.subscribeEvent(exampleEvent.getClass(), s1);
//        assertNotNull(broker.sendEvent(exampleEvent));
//        broker.unregister(s1);
//        assertNull(broker.sendEvent(exampleEvent));
//        broker.subscribeEvent(exampleEvent.getClass(), s1);
//        Future o1 = broker.sendEvent(exampleEvent);
//        assertFalse(o1.isDone());
//        broker.complete(exampleEvent, "YES");
//        assertTrue(o1.isDone());
//        assertEquals(o1.get(1, TimeUnit.SECONDS), "YES");
    }
}