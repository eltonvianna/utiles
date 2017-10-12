/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 18/09/2017
 */
public final class IOUtils {

    private static final Logger LOGGER = Logger.getGlobal();

    /**
     * Suppressing default constructor for non instantiability
     */
    private IOUtils() {
        throw new AssertionError("Suppress default constructor for non instantiability");
    }

    /**
     * @param closeable
     *            a {@link Closeable} resource
     * @throws IOException
     *             if an I/O error occurs
     */
    public static void close(final Closeable closeable) throws IOException {
        if (null != closeable) {
            closeable.close();
        }
    }

    /**
     * @param closeable
     *            a {@link Closeable} resource
     */
    public static void closeQuietly(final Closeable closeable) {
        try {
            IOUtils.close(closeable);
        } catch (Exception e) {
            LOGGER.warning(() -> "Failure to close resource: " + e.getMessage());
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "Stack trace:", e);
            }
        }
    }

    /**
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(final InputStream inputStream) throws IOException {
        if (null == inputStream) {
            return null;
        }
        try (final ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            final byte[] data = new byte[1024];
            int length;
            while ((length = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, length);
            }
            buffer.flush();
            return buffer.toByteArray();
        }
    }

    /**
     * @param inputStream
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static BufferedReader toBufferedReader(final InputStream inputStream)
            throws UnsupportedEncodingException, IOException {
        if (null == inputStream) {
            return null;
        }
        return new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
    }
}