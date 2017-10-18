package com.esv.core.utils;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.esv.utile.logging.Logger;

/**
 * 
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 14/10/2017
 */
public class LoggerTest {
    
    private static final Logger LOGGER = Logger.getLogger(LoggerTest.class);
    
    /**
     * @throws InterruptedException 
     * 
     */
    @Test
    public void testLog() throws InterruptedException {
        final AtomicInteger c = new AtomicInteger();
        for (int k = 0; k < 1000; k++) {
            final int t = k;
            final String threadName = "Thread-" + t;
            Thread thread = new Thread(() -> {
                for (int i = 0; i < 100; i++) {
                    final int counter = c.getAndIncrement();
                    final String message = "Teste thread: " + t + " logging " + counter;
                    LOGGER.info(() -> message);
                    System.out.println(message);
                }
            }, threadName);
            thread.start();
        }
    }
    
    @Test
    public void testLogException() throws InterruptedException {
        LOGGER.error("testLogException", new IllegalArgumentException(new UnsupportedOperationException("testLogException")));
    }
}