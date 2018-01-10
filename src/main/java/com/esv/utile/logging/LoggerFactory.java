/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.esv.utile.logging.core.Configuration;
import com.esv.utile.logging.core.appender.LogAppender;
import com.esv.utile.utils.CharSequenceUtils;
import com.esv.utile.utils.ObjectUtils;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 13/10/2017
 */
final class LoggerFactory {
    
    /**
     * Log appenders singleton map
     */
    private static final Map<String, Logger> loggers = new ConcurrentHashMap<>();
    
    /**
     * <p>Returns the singleton instance of global logger</p>
     * 
     */
    public static Logger getLogger() {
        return LoggerFactory.getLogger(LogAppender.GLOBAL);
    }
    
    /**
     * @param clazz
     * @return
     */
    public static Logger getLogger(final Class<?> clazz) {
        ObjectUtils.requireNotNull(clazz, "clazz parameter is null");
        return LoggerFactory.getLogger(clazz.getSimpleName());
    }
    
    /**
     * @param name
     * @return
     */
    public synchronized static Logger getLogger(final String name) {
        CharSequenceUtils.requireNotBlank(name, "name parameter is null");
        Logger logger = LoggerFactory.loggers.get(name);
        if (null == logger) {
            final String loggerClass = Configuration.getLogger(name);
            logger = ObjectUtils.newInstance(loggerClass, name);
            LoggerFactory.loggers.put(name, logger);
        }
        return logger;
    }
}