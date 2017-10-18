/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging.core.appender;

import java.text.DateFormat;

import com.esv.utile.logging.Logger.Level;
import com.esv.utile.logging.core.layout.LogLayout;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 13/10/2017
 */
public abstract class AbstractLogAppender implements LogAppender {
    
    private final String appenderName;
    private Level level;
    private final DateFormat dateFormat;
    private final LogLayout logLayout;
    
    /**
     * @param appenderName
     * @param level
     * @param dateFormat
     * @param logLayout
     */
    public AbstractLogAppender(final String appenderName, final Level level, final DateFormat dateFormat, final LogLayout logLayout) {
        super();
        this.appenderName = appenderName;
        this.level = level;
        this.dateFormat = dateFormat;
        this.logLayout = logLayout;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.core.LogAppender#setLevel(com.esv.utile.logging.Logger.Level)
     */
    @Override
    public void setLevel(final Level level) {
        this.level = level;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.LogAppender#getName()
     */
    @Override
    public String getAppenderName() {
        return this.appenderName;
    }
    
    /* (non-Javadoc)
     * @see com.esv.utile.logging.LogAppender#getLevel()
     */
    @Override
    public Level getLevel() {
        return this.level;
    }

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.core.LogAppender#getLogLayout()
     */
    @Override
    public LogLayout getLogLayout() {
        return logLayout;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appenderName == null) ? 0 : appenderName.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractLogAppender other = (AbstractLogAppender) obj;
        if (appenderName == null) {
            if (other.appenderName != null)
                return false;
        } else if (!appenderName.equals(other.appenderName))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AbstractLogAppender [appenderName=" + appenderName + ", level=" + level + ", dateFormat=" + dateFormat + ", logLayout="
                + logLayout + "]";
    }
}