package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    Future<String> future;
    Future<String> future2;

    @BeforeEach
    public void setUp(){
        future = new Future<String>();
        future2 = new Future<String>();
    }


    @Test
    public void test(){
        //hi
        assertNotNull(future);
        assertFalse(future.isDone());
        future.resolve("Hi");
        assertEquals(future.get(), "Hi");
        assertTrue(future.isDone());
        future2.resolve("Bye");

        assertEquals(future2.get(10, TimeUnit.SECONDS), "Bye");
    }
}
