/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging.core.appender;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.esv.utile.logging.Logger.Level;
import com.esv.utile.logging.core.Configuration;
import com.esv.utile.logging.core.LogEvent;
import com.esv.utile.logging.core.TriggeringPolicy;
import com.esv.utile.logging.core.layout.LogLayout;
import com.esv.utile.utils.IOUtils;

/**
 * 
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 14/10/2017
 */
public abstract class AbstractRollingLogAppender extends AbstractLogAppender {
    
    private final Lock readLock = new ReentrantReadWriteLock().readLock();
    private final DateFormat timestampFormater;
    private final String fileName;
    private final Path path;

    private static FileOutputStream fos;
    private volatile static long nextRolloverTime;
    private volatile static boolean logFileEmpty;
    
    /**
     * @param appenderName
     * @param level
     * @param dateFormat
     * @param logLayout
     */
    public AbstractRollingLogAppender(final String appenderName, final Level level, final DateFormat dateFormat, final LogLayout logLayout) {
        super(appenderName, level, dateFormat, logLayout);
        this.fileName = Configuration.getFileName(appenderName);
        try {
            this.timestampFormater = new SimpleDateFormat(Configuration.getTimestampPattern(appenderName));
            this.path = Paths.get(this.fileName);
            if (touchFile()) {
                setNextRolloverTime();
                openOutputStream();
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 
     * @return
     */
    protected abstract TriggeringPolicy triggeringPolicy();

    /**
     * 
     * @param logEvent
     * @return
     */
    protected abstract boolean rollover(final LogEvent logEvent);

    /**
     * @return the fileName
     */
    protected String getFileName() {
        return fileName;
    }

    /**
     * @return the rolloverPeriod
     */
    protected long getRolloverPeriod() {
        return Configuration.getRolloverPeriod(getAppenderName());
    }

    /**
     * 
     * @param logEvent
     */
    protected synchronized void checkRollover(final LogEvent logEvent) {
        if (triggeringPolicy().isTriggeringEvent(logEvent)) {
            setNextRolloverTime();
            closeOutputStream();
            rollover(logEvent);
            openOutputStream();
        }
    }

    /**
     * 
     */
    protected void setNextRolloverTime() {
        nextRolloverTime = System.currentTimeMillis() + (getRolloverPeriod() * 1000);
    }

    /**
     * @param now
     * @return
     */
    protected boolean isNextRolloverExpired() {
        return System.currentTimeMillis() > nextRolloverTime;
    }

    /**
     * @return
     */
    protected boolean isLogFileEmpty() {
        return logFileEmpty || (logFileEmpty = checkLogFileSize() == 0);
    }
    
    /**
     * @return
     */
    protected boolean isLogFileNotEmpty() {
        return ! isLogFileEmpty();
    }
    
    /**
     * 
     * @return
     */
    protected String getNewFileName(final boolean gzipped) {
        final int idx = this.fileName.lastIndexOf(".");
        return (idx == -1 ? this.fileName : this.fileName.substring(0, idx)).concat(getTimestampSufix()).concat(true == gzipped ? ".log.gz" : ".log");
    }

    /**
     * 
     * @return
     */
    protected String getTimestampSufix() {
        return ".".concat(this.timestampFormater.format(new Date()));
    }

    /**
     * @param os
     */
    protected synchronized boolean moveFile(final OutputStream os) {
        if (null != os) {
            try {
                Files.copy(this.path, os);
                os.flush();
                deleteFile();
                return touchFile();
            } catch (Exception e) {
                // ignored
            }
        }
        return false;
    }

    /**
     * 
     */
    protected synchronized void deleteFile() {
        try {
            Files.delete(this.path);
        } catch (Exception e) {
            // ignored
        }
    }
    
    /**
     * 
     * @return
     */
    protected synchronized boolean touchFile() {
        try {
            if (!Files.exists(this.path)) {
                Files.createFile(this.path);
                return logFileEmpty = true;
            }
        } catch (Exception e) {
            // ignored
        }
        return false;
    }

    /**
     * 
     */
    protected synchronized void openOutputStream() {
        try {
            fos = new FileOutputStream(this.path.toFile(), true);
        } catch (Exception e) {
            // ignored
        }
    }
    
    /**
     * @throws IOException
     */
    protected synchronized void closeOutputStream() {
        IOUtils.flushAndlCloseQuietly(fos);
    }
    
    /**
     * @return
     */
    protected synchronized boolean isOutputStreamOpen() {
        return null != fos && fos.getChannel().isOpen();
    }
    
    /**
     * @return
     */
    protected synchronized boolean isOutputStreamClosed() {
        return ! isOutputStreamOpen();
    }
    
    /**
     * 
     * @return
     */
    protected synchronized long checkLogFileSize() {
        try {
            return Files.size(this.path);
        } catch (Exception e) {
            // ignored
        }
        return 0;
    }

    /**
     * 
     * @param logEvent
     */
    protected void write(final LogEvent logEvent) {
        if (null != logEvent) {
            this.readLock.lock();
            try {
                if (isOutputStreamClosed()) {
                    openOutputStream();
                }
                fos.write(getLogLayout().toByteArray(logEvent));
            } catch (IOException e) {
                // ignored
            } finally {
                this.readLock.unlock();
            }
        }
    }
}