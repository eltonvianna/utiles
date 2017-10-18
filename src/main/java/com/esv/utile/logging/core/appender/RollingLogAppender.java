/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging.core.appender;

import java.io.OutputStream;
import java.text.DateFormat;
import java.util.concurrent.Semaphore;

import com.esv.utile.logging.Logger.Level;
import com.esv.utile.logging.core.Configuration;
import com.esv.utile.logging.core.LogEvent;
import com.esv.utile.logging.core.TriggeringPolicy;
import com.esv.utile.logging.core.layout.LogLayout;
import com.esv.utile.utils.IOUtils;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 13/10/2017
 */
public final class RollingLogAppender extends AbstractRollingLogAppender {

    private static final Semaphore semaphore = new Semaphore(1);

    /**
     * @param appenderName
     * @param level
     * @param dateFormat
     * @param logLayout
     */
    public RollingLogAppender(final String appenderName, final Level level, final DateFormat dateFormat, final LogLayout logLayout) {
        super(appenderName, level, dateFormat, logLayout);
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.LogAppender#append(com.esv.utile.logging.Logger.Level, java.lang.CharSequence, java.lang.Throwable)
     */
    @Override
    public void append(final LogEvent logEvent) {
        checkRollover(logEvent);
        write(logEvent);
    }
    
    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.core.appender.AbstractRollingLogAppender#rollover(com.esv.utile.logging.core.LogEvent)
     */
    @Override
    protected boolean rollover(final LogEvent logEvent) {
        boolean success = false;
        try {
            RollingLogAppender.semaphore.acquire();
            final boolean gzipEnabled = Configuration.isRolloverGzipEnabled(logEvent.getAppenderName());
            final String renameTo = getNewFileName(gzipEnabled);
            try (final OutputStream os = IOUtils.newFileOutputStream(renameTo, gzipEnabled)) {
                success = moveFile(os);
            }
            return success;
        } catch (Exception e) {
            return false;
        } finally {
            if (true == success) {
                RollingLogAppender.semaphore.release();
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.core.appender.AbstractRollingLogAppender#triggeringPolicy()
     */
    @Override
    protected TriggeringPolicy triggeringPolicy() {
        return (e) -> isLogFileNotEmpty() && isNextRolloverExpired();
    }
}