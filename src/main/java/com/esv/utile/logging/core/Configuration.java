/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging.core;

import java.lang.management.ManagementFactory;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.esv.utile.logging.Logger.Level;
import com.esv.utile.logging.core.appender.LogAppender;
import com.esv.utile.logging.core.appender.RollingLogAppender;
import com.esv.utile.logging.core.layout.StaticLogLayout;
import com.esv.utile.logging.core.logger.AsyncLogger;
import com.esv.utile.utils.PropertiesUtils;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 14/10/2017
 */
public class Configuration implements ConfigurationMBean {

    public static final String TIMESTAMP_PATTEN = "yyyyMMddHHmmss";
    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String LOGFILE_NAME = "logging.log";
    
    public static final int DEFAULT_ROLLOVER_PERIOD = 900;
    public static final int MIN_ROLLOVER_PERIOD = 5;
    public static final int MIN_LOG_EVENT_TIME_WAIT = 100;
    
    public static final String LOGGING_LOGGER = "logging.logger";
    public static final String LOGGING_FILE_NAME = "logging.fileName";
    public static final String LOGGING_ROLLOVER_PERIOD = "logging.rolloverPeriod";
    public static final String LOGGING_ROLLOVER_GZIP_ENABLED = "logging.rolloverGzipEnabled";
    public static final String LOGGING_TIMESTAMP_PATTERN = "logging.timestampPattern";
    public static final String LOGGING_LEVEL = "logging.level";
    public static final String LOGGING_DATE_PATTERN = "logging.datePattern";
    public static final String LOGGING_APPENDERS = "logging.appenders";
    public static final String LOGGING_ASYNC_LOG_EVENT_TIME_WAIT = "logging.async.logEventTimeWait";
    public static final String LOGGING_LOG_LAYOUT = "logging.logLayout";
    
    static {
        try {
            PropertiesUtils.loadProperties();
            final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
            mbs.registerMBean(new Configuration(), new ObjectName("com.esv.utile.logging.core:type=Configuration"));
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     */
    private Configuration() {
        super();
    }
    
    /**
     * 
     * @param appenderName
     * @return
     */
    public static String getLogger(final String appenderName) {
        return Configuration.getOrDefault(Configuration.LOGGING_LOGGER, AsyncLogger.class.getCanonicalName(), appenderName);
    }

    /**
     * @param appenderName
     * @return
     */
    public static String getFileName(final String appenderName) {
        return Configuration.getOrDefault(Configuration.LOGGING_FILE_NAME, Configuration.LOGFILE_NAME, appenderName);
    }

    /**
     * @param appenderName
     * @return
     */
    public static int getRolloverPeriod(final String appenderName) {
        final int value = Configuration.getOrDefault(Configuration.LOGGING_ROLLOVER_PERIOD, Configuration.DEFAULT_ROLLOVER_PERIOD, appenderName);
        return value < Configuration.MIN_ROLLOVER_PERIOD ? Configuration.MIN_ROLLOVER_PERIOD : value;
    }
    
    /**
     * @param appenderName
     * @return
     */
    public static boolean isRolloverGzipEnabled(final String appenderName) {
        return Configuration.getOrDefault(Configuration.LOGGING_ROLLOVER_GZIP_ENABLED, true, appenderName);
    }

    /**
     * @param appenderName
     * @return
     */
    public static String getTimestampPattern(final String appenderName) {
        return Configuration.getOrDefault(Configuration.LOGGING_TIMESTAMP_PATTERN, Configuration.TIMESTAMP_PATTEN, appenderName);
    }

    /**
     * @param appenderName
     * @return
     */
    public static Level getLevel(final String appenderName) {
        return Configuration.getOrDefault(Configuration.LOGGING_LEVEL, Level.INFO, appenderName);
    }

    /**
     * @param appenderName
     * @return
     */
    public static String getDatePattern(final String appenderName) {
        return Configuration.getOrDefault(Configuration.LOGGING_DATE_PATTERN, Configuration.DATE_PATTERN, appenderName);
    }
    
    /**
     * 
     * @param name
     * @return
     */
    public static String[] getLogAppenders(final String appenderName) {
        return Configuration.getOrDefault(Configuration.LOGGING_APPENDERS, RollingLogAppender.class.getCanonicalName(), appenderName).split(",");
    }
    
    /**
     * 
     * @param appenderName
     * @return
     */
    public static long getLogEventTimeWait() {
        final long value = PropertiesUtils.getLongProperty(Configuration.LOGGING_ASYNC_LOG_EVENT_TIME_WAIT, 100);
        return value < 100 ? 100 : value;
    }
    
    /**
     * @param name
     * @return
     */
    public static String getLogLayout(final String appenderName) {
        return Configuration.getOrDefault(Configuration.LOGGING_LOG_LAYOUT, StaticLogLayout.class.getCanonicalName(), appenderName);
    }
    
    /**
     * 
     * @param propName
     * @param defaultValue
     * @param appenderName
     * @return
     */
    private static <T> T getOrDefault(final String propName, final T defaultValue, final String appenderName) {
        return PropertiesUtils.getOrDefault(propName, defaultValue, getAppenderSufix(appenderName));
    }

    /**
     * @param appenderName
     * @return
     */
    private static String getAppenderSufix(final String appenderName) {
        return LogAppender.isGlobal(appenderName) ? null : appenderName;
    }
    
    // ---------------------------------------------------------------------------------------------------- //

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.core.ConfigurationMBean#isRolloverGzipOn(java.lang.String)
     */
    @Override
    public boolean isRolloverGzipOn(final String appenderName) {
        return Configuration.isRolloverGzipEnabled(appenderName);
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.core.ConfigurationMBean#setRolloverGzipOn(java.lang.String, boolean)
     */
    @Override
    public void setRolloverGzipOn(final String appenderName, final boolean gzipOn) {
        PropertiesUtils.setProperty(Configuration.LOGGING_ROLLOVER_GZIP_ENABLED, gzipOn);
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.core.ConfigurationMBean#getLogLevel(java.lang.String)
     */
    @Override
    public Level getLogLevel(final String appenderName) {
        return Configuration.getLevel(appenderName);
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.core.ConfigurationMBean#setLevel(java.lang.String, com.esv.utile.logging.Logger.Level)
     */
    @Override
    public void setLogLevel(final String appenderName, final Level level) {
        PropertiesUtils.setProperty(Configuration.LOGGING_LEVEL, level.name());
    }
    
    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.core.ConfigurationMBean#getLogEventConsumerTimeWait()
     */
    @Override
    public long getLogEventConsumerTimeWait() {
        return Configuration.getLogEventTimeWait();
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.core.ConfigurationMBean#setLogEventTimeWait(long)
     */
    @Override
    public void setLogEventConsumerTimeWait(long value) {
        PropertiesUtils.setProperty(Configuration.LOGGING_ASYNC_LOG_EVENT_TIME_WAIT, value < Configuration.MIN_LOG_EVENT_TIME_WAIT ? Configuration.MIN_LOG_EVENT_TIME_WAIT : value);
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.core.ConfigurationMBean#getRolloverTimePeriod(java.lang.String)
     */
    @Override
    public int getRolloverTimePeriod(final String appenderName) {
        return Configuration.getRolloverPeriod(appenderName);
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.core.ConfigurationMBean#setRolloverTimePeriod(java.lang.String, int)
     */
    @Override
    public void setRolloverTimePeriod(final String appenderName, final int value) {
        PropertiesUtils.setProperty(Configuration.LOGGING_ROLLOVER_PERIOD, value < Configuration.MIN_ROLLOVER_PERIOD ? Configuration.MIN_ROLLOVER_PERIOD : value);
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.core.ConfigurationMBean#viewAllProperties()
     */
    @Override
    public Map<String, String> viewAllProperties() {
        return PropertiesUtils.getApplicationPropeties();
    }
}