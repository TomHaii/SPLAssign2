package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    MessageBroker broker;
    @BeforeEach
    public void setUp(){
        broker = new MessageBrokerImpl();
    }

    @Test
    public void test(){
        assertNotNull(broker);
        Subscriber q = new Q();
        Subscriber money = new Moneypenny();
        broker.register(q);
        broker.register(money);
        List<Subscriber> tmp = new ArrayList<>();
        tmp.add(q);
        tmp.add(money);
    }
}
