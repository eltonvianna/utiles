/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging.core.logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.esv.utile.logging.core.Configuration;
import com.esv.utile.logging.core.LogEvent;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 13/10/2017
 * @see AbstractLogger
 */
public final class AsyncLogger extends AbstractLogger {
    
    private static final com.esv.utile.logging.Logger LOGGER = new Logger(AsyncLogger.class.getSimpleName());
    private static final Queue<LogEvent> queue = new ConcurrentLinkedQueue<>();
    private static final ExecutorService consumer = Executors.newSingleThreadExecutor(AsyncLogger.logEventThreadFactory());

    /* Adding the LogEvent shutdown hook as way to flush 
    /* all enqueued log events when the shutdown sequence is initiated */
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            final long startTime = System.currentTimeMillis();
            LOGGER.warn(() -> "JVM shutdown sequence has been initiated! :O");
            final int enqueuedLogEvents = queue.size();
            LOGGER.warn(() -> "LogEvent's enqueued: " + enqueuedLogEvents);
            while (queue.size() > 0) {
                try {
                    final long timeWait = Configuration.getLogEventTimeWait();
                    TimeUnit.MILLISECONDS.sleep(timeWait);
                } catch (InterruptedException e) {
                    LOGGER.warn(() -> "LogEvent's shutdown hook has been interrupted: " + e.getMessage()).trace(() -> "Stack trace: ", e);
                }
            }
            LOGGER.info(() -> "All "+ enqueuedLogEvents + " LogEvent's has been flushed! :)");
            consumer.shutdownNow();
            final long endTime = System.currentTimeMillis();
            LOGGER.info(() -> "LogEvent's consumer has been stopped in: " + (endTime - startTime) + " ms");
        }, "logEvent-shutdownHook"));
    }
    
    /**
     * 
     * @param appenderName
     */
    public AsyncLogger(final String appenderName) {
        super(appenderName);
        startConsumer();
    }
    
    /**
     * <p>Inserts the {@link LogEvent} into this queue if it is possible to do so
     * immediately without violating capacity restrictions</p>
     * @param logEvent
     * @throws IllegalStateException if no space is currently available
     */
    @Override
    protected void log(LogEvent logEvent) {
        AsyncLogger.queue.add(logEvent);
    }

    /**
     * <p>Starts the {@link LogEvent} consumer<p> 
     */
    private void startConsumer() {
        AsyncLogger.consumer.execute(logEventConsumer());
    }
    
    /**
     * <p>Creates a {@link LogEvent} consumer with guaranteed to execute
     * sequentially, and no more than one task will be active at any given time</p>
     * 
     * @return a {@link LogEvent} consumer
     */
    private Runnable logEventConsumer() {
        return () -> {
            LOGGER.info(() -> "LogEvent's consumer has been started successfully");
            for(;;) {
                LogEvent logEvent = null;
                try {
                    while (null != (logEvent = AsyncLogger.queue.poll())) {
                        AsyncLogger.super.log(logEvent);
                    }
                    final long timeWait = Configuration.getLogEventTimeWait();
                    LOGGER.trace(() -> "LogEvent queue empty, sleeping " + timeWait + " ms");
                    TimeUnit.MILLISECONDS.sleep(timeWait);
                } catch (InterruptedException e) {
                    LOGGER.warn(() -> "LogEvent's consumer has been interrupted: " + e.getMessage()).trace(() -> "Stack trace: ", e);
                }
            }
        };
    }
    
    /**
     * <p>Creates a {@link ThreadFactory} for {@link LogEvent} consumer</p>
     * 
     * @return a {@link ThreadFactory} for {@link LogEvent} consumer
     */
    private static ThreadFactory logEventThreadFactory() {
        return (runnable) -> {
            return new Thread(runnable, "logEvent-consumer");
        };
    }
}