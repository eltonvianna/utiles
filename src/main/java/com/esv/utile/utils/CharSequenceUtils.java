/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.utils;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 28/09/2017
 */
public final class CharSequenceUtils {
    
    public static final CharSequence empty = "";

    /**
     * Suppressing default constructor for non instantiability
     */
    private CharSequenceUtils() {
        throw new AssertionError("Suppress default constructor for non instantiability");
    }
    
    /**
     * 
     * @param cs
     * @param message
     */
    public static void requireNotBlank(final CharSequence cs, final String message) {
        if (CharSequenceUtils.isBlank(cs)) {
           throw new IllegalArgumentException(message);
        }
    }

    /**
     * @param cs
     * @return
     */
    public static boolean isBlank(final CharSequence cs) {
        int i;
        if (cs == null || (i = cs.length()) == 0) {
            return true;
        }
        while (--i >= 0) {
            if (false == Character.isWhitespace(cs.charAt(i)))
                return false;
        }
        return true;
    }
}