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

    }
}