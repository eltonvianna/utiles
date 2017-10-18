/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging;

import java.util.function.Supplier;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 13/10/2017
 * @see Level
 */
public interface Logger {
    
    /**
     * 
     * @return
     */
    static Logger getLogger() {
        return LoggerFactory.getLogger();
    }
    
    /**
     * 
     * @param clazz
     * @return
     */
    static Logger getLogger(final Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
    
    /**
     * 
     * @param appenderName
     * @return
     */
    static Logger getLogger(final String appenderName) {
        return LoggerFactory.getLogger(appenderName);
    }
    
    /**
     * Log a message with {@link Level#DEBUG DEBUG} level.
     *
     * @param message         a message string to be logged
     * @param throwable       a Throwable or null.
     */
    Logger debug(final Supplier<String> message, final Throwable throwable);

    /**
     * Log a message with {@link Level#DEBUG DEBUG} level.
     *
     * @param message         a message string to be logged
     * @param throwable       a Throwable or null.
     */
    Logger debug(final String message, final Throwable throwable);

    /**
     * Log a message with {@link Level#DEBUG DEBUG} level.
     *
     * @param message         a message string to be logged
     */
    Logger debug(final Supplier<String> message);
    
    /**
     * Log a message with {@link Level#DEBUG DEBUG} level.
     *
     * @param message         a message string to be logged
     */
    Logger debug(final String message);

    /**
     * Log a message with {@link Level#ERROR ERROR} level.
     *
     * @param message         a message string to be logged
     * @param throwable       a Throwable or null.
     */
    Logger error(final Supplier<String> message, final Throwable throwable);

    /**
     * Log a message with {@link Level#ERROR ERROR} level.
     *
     * @param message         a message string to be logged
     * @param throwable       a Throwable or null.
     */
    Logger error(final String message, final Throwable throwable);
    
    /**
     * Log a message with {@link Level#ERROR ERROR} level.
     *
     * @param message         a message string to be logged
     */
    Logger error(final Supplier<String> message);

    /**
     * Log a message with {@link Level#ERROR ERROR} level.
     *
     * @param message         a message string to be logged
     */
    Logger error(final String message);
    
    /**
     * Log a message with {@link Level#FATAL FATAL} level.
     *
     * @param message         a message string to be logged
     * @param throwable       a Throwable or null.
     */
    Logger fatal(final Supplier<String> message, final Throwable throwable);

    /**
     * Log a message with {@link Level#FATAL FATAL} level.
     *
     * @param message         a message string to be logged
     * @param throwable       a Throwable or null.
     */
    Logger fatal(final String message, final Throwable throwable);

    /**
     * Log a message with {@link Level#FATAL FATAL} level.
     *
     * @param message         a message string to be logged
     */
    Logger fatal(final Supplier<String> message);

    /**
     * Log a message with {@link Level#FATAL FATAL} level.
     *
     * @param message         a message string to be logged
     */
    Logger fatal(final String message);

    /**
     * Log a message with {@link Level#INFO INFO} level.
     *
     * @param message         a message string to be logged
     * @param throwable       a Throwable or null.
     */
    Logger info(final Supplier<String> message, final Throwable throwable);

    /**
     * Log a message with {@link Level#INFO INFO} level.
     *
     * @param message         a message string to be logged
     * @param throwable       a Throwable or null.
     */
    Logger info(final String message, final Throwable throwable);

    /**
     * Log a message with {@link Level#INFO INFO} level.
     *
     * @param message         a message string to be logged
     */
    Logger info(final Supplier<String> message);
    
    /**
     * Log a message with {@link Level#INFO INFO} level.
     *
     * @param message         a message string to be logged
     */
    Logger info(final String message);
    
    /**
     * Log a message with {@link Level#TRACE TRACE} level.
     *
     * @param message         a message string to be logged
     * @param throwable       a Throwable or null.
     */
    Logger trace(final Supplier<String> message, final Throwable throwable);

    /**
     * Log a message with {@link Level#TRACE TRACE} level.
     *
     * @param message         a message string to be logged
     * @param throwable       a Throwable or null.
     */
    Logger trace(final String message, final Throwable throwable);

    /**
     * Log a message with {@link Level#TRACE TRACE} level.
     *
     * @param message         a message string to be logged
     */
    Logger trace(final Supplier<String> message);

    /**
     * Log a message with {@link Level#TRACE TRACE} level.
     *
     * @param message         a message string to be logged
     */
    Logger trace(final String message);

    /**
     * Log a message with {@link Level#TRACE TRACE} level.
     *
     * @param message         a message string to be logged
     * @param throwable       a Throwable or null.
     */
    Logger warn(final Supplier<String> message, final Throwable throwable);
    
    /**
     * Log a message with {@link Level#TRACE TRACE} level.
     *
     * @param message         a message string to be logged
     * @param throwable       a Throwable or null.
     */
    Logger warn(final String message, final Throwable throwable);

    /**
     * Log a message with {@link Level#TRACE TRACE} level.
     *
     * @param message         a message string to be logged
     */
    Logger warn(final Supplier<String> message);

    /**
     * Log a message with {@link Level#TRACE TRACE} level.
     *
     * @param message         a message string to be logged
     */
    Logger warn(final String message);

    /**
     * Checks whether this Logger is enabled for the {@link Level#DEBUG DEBUG}
     * Level.
     *
     * @return boolean - {@code true} if this Logger is enabled for level DEBUG,
     *         {@code false} otherwise.
     */
    boolean isDebugEnabled();

    /**
     * Checks whether this Logger is enabled for the the given Level.
     * <p>
     * Note that passing in {@link Level#OFF OFF} always returns {@code true}.
     * </p>
     * 
     * @param level
     *            the level to check
     * @return boolean - {@code true} if this Logger is enabled for level,
     *         {@code false} otherwise.
     */
    boolean isEnabled(Level level);

    /**
     * Checks whether this Logger is enabled for the {@link Level#ERROR ERROR}
     * Level.
     *
     * @return boolean - {@code true} if this Logger is enabled for level
     *         {@link Level#ERROR ERROR}, {@code false} otherwise.
     */
    boolean isErrorEnabled();

    /**
     * Checks whether this Logger is enabled for the {@link Level#FATAL FATAL}
     * Level.
     *
     * @return boolean - {@code true} if this Logger is enabled for level
     *         {@link Level#FATAL FATAL}, {@code false} otherwise.
     */
    boolean isFatalEnabled();

    /**
     * Checks whether this Logger is enabled for the {@link Level#INFO INFO}
     * Level.
     *
     * @return boolean - {@code true} if this Logger is enabled for level INFO,
     *         {@code false} otherwise.
     */
    boolean isInfoEnabled();

    /**
     * Checks whether this Logger is enabled for the {@link Level#TRACE TRACE}
     * level.
     *
     * @return boolean - {@code true} if this Logger is enabled for level TRACE,
     *         {@code false} otherwise.
     */
    boolean isTraceEnabled();

    /**
     * Checks whether this Logger is enabled for the {@link Level#WARN WARN}
     * Level.
     *
     * @return boolean - {@code true} if this Logger is enabled for level
     *         {@link Level#WARN WARN}, {@code false} otherwise.
     */
    boolean isWarnEnabled();
  
    /**
     * 
     * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
     * @version 1.0
     * @since 13/10/2017
     */
    public static enum Level {
        OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL;
    }
}