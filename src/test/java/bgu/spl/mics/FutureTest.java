package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    Future<String> future;
    @BeforeEach
    public void setUp(){
        future = new Future<String>();
    }


    @Test
    public void test(){
        //hi
        assertNotNull(future);
        assertFalse(future.isDone());
        future.resolve("Hi");
        assertEquals(future.get(), "Hi");
        assertTrue(future.isDone());
        future.resolve("Bye");
        assertEquals(future.get(3, TimeUnit.SECONDS), "Bye");

    }
}
