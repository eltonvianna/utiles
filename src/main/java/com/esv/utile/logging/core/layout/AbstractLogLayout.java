/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.logging.core.layout;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 15/10/2017
 */
public abstract class AbstractLogLayout implements LogLayout {

    /**
     * @param event
     * @param sb
     */
    protected StringBuilder appendStackTrace(final Throwable throwable, final StringBuilder sb) {
        if (null != throwable) {
            try (final Writer result = new StringWriter()) {
                throwable.printStackTrace(new PrintWriter(result));
                sb.append(result.toString());
            } catch (IOException e) {
                // ignored
            }
        }
        return sb;
    }

    /**
     * @return
     */
    protected String getThreadName() {
        return Thread.currentThread().getName();
    }
}