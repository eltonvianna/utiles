/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging.core.logger;

import static java.lang.System.currentTimeMillis;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
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

    private static final ExecutorService consumer;
    private static final BlockingQueue<LogEvent> queue;

    /*
     * Adding the LogEvent shutdown hook as way to flush /* all enqueued log
     * events when the shutdown sequence is initiated
     */
    static {
        consumer = Executors.newFixedThreadPool(Configuration.getThreadPoolSize());
        queue = new LinkedBlockingQueue<>();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.warn(() -> "JVM shutdown sequence has been initiated! :O");
            LOGGER.warn(() -> "Stopping gracefully the logging thread pool. Current enqueued LogEvent's: " + queue.size());
            consumer.shutdown();
            final long startTime = currentTimeMillis();
            while (! queue.isEmpty()) {}
            if (consumer.isTerminated() && ! queue.isEmpty()) {
                LOGGER.warn(() -> "Forcing to halts " + consumer.shutdownNow().size() + " logging threads");
            }
            LOGGER.info(() -> queue.isEmpty() ? "All enqueued LogEvent's has been flushed! :)"
                    : queue.size() + " enqueued LogEvent's hasn't been flushed... :(");
            LOGGER.info(() -> "LogEvent's consumer has been stopped in: " + (currentTimeMillis() - startTime) + " ms");
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
     * <p>
     * Inserts the {@link LogEvent} into logging queue
     * </p>
     * 
     * @param logEvent
     * @throws IllegalStateException if no space is currently available
     */
    @Override
    protected void log(LogEvent logEvent) {
        AsyncLogger.queue.add(logEvent);
    }

    /**
     * <p>
     * Starts the {@link LogEvent} consumer
     * <p>
     */
    private void startConsumer() {
        AsyncLogger.consumer.execute(logEventConsumer());
    }

    /**
     * <p>
     * Creates a {@link LogEvent} consumer with guaranteed to execute
     * sequentially, and no more than one task will be active at any given time
     * </p>
     * 
     * @return a {@link LogEvent} consumer
     */
    private Runnable logEventConsumer() {
        return () -> {
            LOGGER.trace(() -> "LogEvent's consumer has been started successfully");
            halt:
            for(;;) {
                try {
                    LogEvent logEvent = null;
                    while (null != (logEvent = queue.take())) {
                        AsyncLogger.super.log(logEvent);
                    }
                    TimeUnit.MILLISECONDS.sleep(Configuration.getLogEventTimeWait());
                    if (consumer.isShutdown() && queue.isEmpty()) {
                        LOGGER.trace(() -> "Stopping gracefully LogEvent's consumer after shutdown sequence initiated");
                        break halt;
                    }
                } catch (InterruptedException e) {
                    LOGGER.warn(() -> "Forcing to halts LogEvent's consumer after shutdown sequence initiated").trace("Stack trace: ", e);
                    break halt;
                }
            }
        };
    }
}