/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging.core.layout;

import com.esv.utile.logging.core.LogEvent;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 14/10/2017
 */
final public class SimpleLogLayout extends AbstractLogLayout {

    /*
     * (non-Javadoc)
     * @see com.esv.utile.logging.core.LogLayout#toByteArray(com.esv.utile.logging.core.LogEvent)
     */
    @Override
    public byte[] toByteArray(final LogEvent event) {
        final StringBuilder sb = new StringBuilder("[").append(event.getLevel()).append("] ").append(event.getFormatedDate()).append(" [")
                .append(getThreadName()).append("] ").append(event.getAppenderName()).append(" - ").append(event.getMessage()).append("\n");
       return appendStackTrace(event.getThrowable(), sb).toString().getBytes();
    }
}