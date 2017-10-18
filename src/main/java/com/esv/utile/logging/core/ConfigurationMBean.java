/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging.core;

import java.util.Map;

import com.esv.utile.logging.Logger.Level;

/**
 * 
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 16/10/2017
 */
public interface ConfigurationMBean {
    
    /**
     * 
     * @param appenderName
     * @return
     */
    boolean isRolloverGzipOn(final String appenderName);
    
    /**
     * 
     * @param appenderName
     * @param gzipEnabled
     * @return
     */
    void setRolloverGzipOn(final String appenderName, final boolean gzipOn);
    
    /**
     * 
     * @param appenderName
     * @return
     */
    Level getLogLevel(final String appenderName);
    
    /**
     * 
     * @param appenderName
     * @param level
     * @return
     */
    void setLogLevel(final String appenderName, final Level level);
    
    /**
     * 
     * @return
     */
    long getLogEventConsumerTimeWait();
    
    /**
     * 
     * @param value
     */
    void setLogEventConsumerTimeWait(final long value);
    
    /**
     * 
     * @param value
     * @return
     */
    int getRolloverTimePeriod(final String appenderName);
    
    /**
     * 
     * @param value
     */
    void setRolloverTimePeriod(final String appenderName, final int value);
    
    /**
     * 
     */
    Map<String, String> viewAllProperties();
}