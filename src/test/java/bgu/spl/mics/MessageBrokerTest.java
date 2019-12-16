package bgu.spl.mics;

import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.subscribers.ExampleBroadcastSubscriber;
import bgu.spl.mics.example.subscribers.ExampleEventHandlerSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    MessageBroker broker;
    ExampleEvent exampleEvent;
    ExampleBroadcast exampleBroadcast;
    ExampleEventHandlerSubscriber s1;
    ExampleEventHandlerSubscriber s2;
    ExampleEventHandlerSubscriber s3;
    @BeforeEach
    public void setUp(){
      //  broker = new MessageBrokerImpl();
        String[] args = new String[2];
        exampleBroadcast = new ExampleBroadcast("006");
        exampleEvent = new ExampleEvent("006");
        s1 = new ExampleEventHandlerSubscriber("006", args);
        s2 = new ExampleEventHandlerSubscriber("007", args);
        s3 = new ExampleEventHandlerSubscriber("008", args);
        broker.register(s1);
        broker.register(s2);
        broker.register(s3);

    }

    @Test
    public void test(){
        assertNotNull(broker);
        broker.subscribeEvent(exampleEvent.getClass(), s3);
        assertNull(broker.sendEvent(exampleEvent));
        broker.subscribeEvent(exampleEvent.getClass(), s1);
        assertNotNull(broker.sendEvent(exampleEvent));
        broker.unregister(s1);
        assertNull(broker.sendEvent(exampleEvent));
        broker.subscribeEvent(exampleEvent.getClass(), s1);
        Future o1 = broker.sendEvent(exampleEvent);
        assertFalse(o1.isDone());
        broker.complete(exampleEvent, "YES");
        assertTrue(o1.isDone());
        assertEquals(o1.get(1, TimeUnit.SECONDS), "YES");
    }
}
