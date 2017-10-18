/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging.core;

import com.esv.utile.logging.Logger.Level;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 13/10/2017
 */
public final class LogEvent {

    private final String formatedDate;
    private final Level level;
    private final String appenderName;
    private final String message;
    private final transient Throwable throwable;
    
    /**
     * @param formatedDate
     * @param level
     * @param appenderName
     * @param message
     * @param throwable
     */
    public LogEvent(final String formatedDate, final Level level, final String appenderName, final String message, final Throwable throwable) {
        super();
        this.formatedDate = formatedDate;
        this.level = level;
        this.appenderName = appenderName;
        this.message = message;
        this.throwable = throwable;
    }

    /**
     * @return the formatedDate
     */
    public String getFormatedDate() {
        return formatedDate;
    }

    /**
     * @return the level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * @return the appenderName
     */
    public String getAppenderName() {
        return appenderName;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the throwable
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appenderName == null) ? 0 : appenderName.hashCode());
        result = prime * result + ((formatedDate == null) ? 0 : formatedDate.hashCode());
        result = prime * result + ((level == null) ? 0 : level.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
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
        LogEvent other = (LogEvent) obj;
        if (appenderName == null) {
            if (other.appenderName != null)
                return false;
        } else if (!appenderName.equals(other.appenderName))
            return false;
        if (formatedDate == null) {
            if (other.formatedDate != null)
                return false;
        } else if (!formatedDate.equals(other.formatedDate))
            return false;
        if (level != other.level)
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LogEvent [formatedDate=" + formatedDate + ", level=" + level + ", appenderName=" + appenderName
                + ", message=" + message + ", throwable=" + (null != throwable) + "]";
    }
}