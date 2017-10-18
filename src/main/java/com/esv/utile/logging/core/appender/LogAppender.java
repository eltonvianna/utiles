/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging.core.appender;

import com.esv.utile.logging.Logger.Level;
import com.esv.utile.logging.core.LogEvent;
import com.esv.utile.logging.core.layout.LogLayout;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 13/10/2017
 */
public interface LogAppender {
    
    String GLOBAL = "global";
    
    /**
     * 
     * @param name
     * @return
     */
    static boolean isGlobal(final String name) {
        return LogAppender.GLOBAL.equalsIgnoreCase(name);
    }
    
    /**
     * @param logEvent
     */
    void append(final LogEvent logEvent);

    /**
     * 
     * @return
     */
    String getAppenderName();
    
    /**
     * 
     * @return
     */
    LogLayout getLogLayout();

    /**
     * 
     * @return
     */
    Level getLevel();

    /**
     * 
     * @param level
     */
    void setLevel(final Level level);
}