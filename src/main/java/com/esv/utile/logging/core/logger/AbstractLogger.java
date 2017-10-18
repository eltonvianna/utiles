/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging.core.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import com.esv.utile.logging.Logger;
import com.esv.utile.logging.core.Configuration;
import com.esv.utile.logging.core.LogEvent;
import com.esv.utile.logging.core.appender.LogAppender;
import com.esv.utile.logging.core.layout.LogLayout;
import com.esv.utile.utils.CharSequenceUtils;
import com.esv.utile.utils.ObjectUtils;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 13/10/2017
 * @see Logger
 */
public abstract class AbstractLogger implements Logger {
    
    private final String appenderName;
    private final Level level;
    private final List<LogAppender> logAppenders;
    private final DateFormat dateFormat;
    
    /**
     * 
     * @param appenderName
     */
    protected AbstractLogger(final String appenderName) {
        this.appenderName = CharSequenceUtils.isBlank(appenderName) ? LogAppender.GLOBAL : appenderName;
        this.level = Configuration.getLevel(appenderName);
        final String[] logAppenders = Configuration.getLogAppenders(appenderName);
        final String datePattern = Configuration.getDatePattern(appenderName);
        final String layout = Configuration.getLogLayout(appenderName);
        try {
            this.dateFormat = new SimpleDateFormat(datePattern);
            final LogLayout logLayout = ObjectUtils.newInstance(layout);
            this.logAppenders = ObjectUtils.newInstances(logAppenders, appenderName, this.level, this.dateFormat, logLayout);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    /**
     * @return the appenderName
     */
    protected String getAppenderName() {
        return appenderName;
    }

    /**
     * @return the level
     */
    protected Level getLevel() {
        return level;
    }
    
    /**
     * @return the dateFormat
     */
    protected DateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * 
     * @param logEvent
     */
    protected void log(final LogEvent logEvent) {
        /* log appenders benchmark */
        /* sample size: 100.000 writes
         * foreach avg: 3145.13 ns
         * stream avg: 4579.02 ns
         * parallelStream avg: 5167.37 ns */
        for (final LogAppender logAppender : this.logAppenders) {
            logAppender.append(logEvent);
        }
    }
    
    /**
     * 
     * @return
     */
    protected List<LogAppender> getLogAppenders() {
        return this.logAppenders;
    }

    /**
     * @param level
     * @param message
     * @param throwable
     * @return
     */
    protected LogEvent toLogEvent(final Level level, final String message, final Throwable throwable) {
        return new LogEvent(this.dateFormat.format(new Date()), level, this.appenderName, message, throwable);
    }

    /**
     * 
     * @param level
     * @param message
     * @param throwable
     */
    protected void log(final Level level, final Supplier<String> message, final Throwable throwable) {
        if (isEnabled(level)) {
            log(toLogEvent(level, message.get(), throwable));
        }
    }

    /**
     * 
     * @param level
     * @param message
     * @param throwable
     */
    protected void log(final Level level, final String message, final Throwable throwable) {
        if (isEnabled(level)) {
            log(toLogEvent(level, message, throwable));
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#debug(java.util.function.Supplier, java.lang.Throwable)
     */
    @Override
    public Logger debug(final Supplier<String> message, final Throwable throwable) {
        log(Level.DEBUG, message, throwable);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#debug(java.lang.String, java.lang.Throwable)
     */
    @Override
    public Logger debug(final String message, final Throwable throwable) {
        log(Level.DEBUG, message, throwable);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#debug(java.util.function.Supplier)
     */
    @Override
    public Logger debug(final Supplier<String> message) {
        log(Level.DEBUG, message, null);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#debug(java.lang.String)
     */
    @Override
    public Logger debug(final String message) {
        log(Level.DEBUG, message, null);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#error(java.util.function.Supplier, java.lang.Throwable)
     */
    @Override
    public Logger error(final Supplier<String> message, final Throwable throwable) {
        log(Level.ERROR, message, throwable);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#error(java.lang.String, java.lang.Throwable)
     */
    @Override
    public Logger error(final String message, final Throwable throwable) {
        log(Level.ERROR, message, throwable);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#error(java.util.function.Supplier)
     */
    @Override
    public Logger error(final Supplier<String> message) {
        log(Level.ERROR, message, null);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#error(java.lang.String)
     */
    @Override
    public Logger error(final String message) {
        log(Level.ERROR, message, null);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#fatal(java.util.function.Supplier, java.lang.Throwable)
     */
    @Override
    public Logger fatal(final Supplier<String> message, final Throwable throwable) {
        log(Level.FATAL, message, throwable);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#fatal(java.lang.String, java.lang.Throwable)
     */
    @Override
    public Logger fatal(final String message, final Throwable throwable) {
        log(Level.FATAL, message, throwable);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#fatal(java.util.function.Supplier)
     */
    @Override
    public Logger fatal(final Supplier<String> message) {
        log(Level.FATAL, message, null);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#fatal(java.lang.String)
     */
    @Override
    public Logger fatal(final String message) {
        log(Level.FATAL, message, null);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#info(java.util.function.Supplier, java.lang.Throwable)
     */
    @Override
    public Logger info(final Supplier<String> message, final Throwable throwable) {
        log(Level.INFO, message, throwable);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#info(java.lang.String, java.lang.Throwable)
     */
    @Override
    public Logger info(final String message, final Throwable throwable) {
        log(Level.INFO, message, throwable);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#info(java.util.function.Supplier)
     */
    @Override
    public Logger info(final Supplier<String> message) {
        log(Level.INFO, message, null);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#info(java.lang.String)
     */
    @Override
    public Logger info(final String message) {
        log(Level.INFO, message, null);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#trace(java.util.function.Supplier, java.lang.Throwable)
     */
    @Override
    public Logger trace(final Supplier<String> message, final Throwable throwable) {
        log(Level.TRACE, message, throwable);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#trace(java.lang.String, java.lang.Throwable)
     */
    @Override
    public Logger trace(final String message, final Throwable throwable) {
        log(Level.TRACE, message, throwable);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#trace(java.util.function.Supplier)
     */
    @Override
    public Logger trace(final Supplier<String> message) {
        log(Level.TRACE, message, null);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#trace(java.lang.String)
     */
    @Override
    public Logger trace(final String message) {
        log(Level.TRACE, message, null);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#warn(java.util.function.Supplier, java.lang.Throwable)
     */
    @Override
    public Logger warn(final Supplier<String> message, final Throwable throwable) {
        log(Level.WARN, message, throwable);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#warn(java.lang.String, java.lang.Throwable)
     */
    @Override
    public Logger warn(final String message, final Throwable throwable) {
        log(Level.WARN, message, throwable);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#warn(java.util.function.Supplier)
     */
    @Override
    public Logger warn(final Supplier<String> message) {
        log(Level.WARN, message, null);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#warn(java.lang.String)
     */
    @Override
    public Logger warn(final String message) {
        log(Level.WARN, message, null);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#isDebugEnabled()
     */
    @Override
    public boolean isDebugEnabled() {
        return isEnabled(Level.DEBUG);
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#isEnabled(com.esv.utile.logging.Logger.Level)
     */
    @Override
    public boolean isEnabled(final Level level) {
        return this.level.compareTo(level) >= 0;
    }
    
    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#isErrorEnabled()
     */
    @Override
    public boolean isErrorEnabled() {
        return isEnabled(Level.ERROR);
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#isFatalEnabled()
     */
    @Override
    public boolean isFatalEnabled() {
        return isEnabled(Level.FATAL);
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#isInfoEnabled()
     */
    @Override
    public boolean isInfoEnabled() {
        return isEnabled(Level.INFO);
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#isTraceEnabled()
     */
    @Override
    public boolean isTraceEnabled() {
        return isEnabled(Level.TRACE);
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.Logger#isWarnEnabled()
     */
    @Override
    public boolean isWarnEnabled() {
        return isEnabled(Level.WARN);
    }
 }