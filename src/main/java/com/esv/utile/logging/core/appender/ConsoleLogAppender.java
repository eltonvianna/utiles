/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging.core.appender;

import java.io.IOException;
import java.text.DateFormat;

import com.esv.utile.logging.Logger.Level;
import com.esv.utile.logging.core.LogEvent;
import com.esv.utile.logging.core.layout.LogLayout;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 14/10/2017
 */
public class ConsoleLogAppender extends AbstractLogAppender {

    /**
     * @param appenderName
     * @param level
     * @param dateFormat
     * @param logLayout
     */
    public ConsoleLogAppender(final String appenderName, final Level level, final DateFormat dateFormat, final LogLayout logLayout) {
        super(appenderName, level, dateFormat, logLayout);
    }

    /* (non-Javadoc)
     * @see com.esv.utile.logging.core.LogAppender#append(com.esv.utile.logging.core.LogEvent)
     */
    @Override
    public void append(LogEvent logEvent) {
        try {
            System.out.write(getLogLayout().toByteArray(logEvent));
        } catch (IOException e) {
            // ignored
        };
    }
}