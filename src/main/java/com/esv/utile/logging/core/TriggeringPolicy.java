/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging.core;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 14/10/2017
 */
@FunctionalInterface
public interface TriggeringPolicy {
    
    /**
     * 
     * @param logEvent
     * @return
     */
    boolean isTriggeringEvent(final LogEvent logEvent);
}
