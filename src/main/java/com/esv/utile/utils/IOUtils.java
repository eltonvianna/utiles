/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.utile.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPOutputStream;

import com.esv.utile.logging.Logger;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 14/10/2017
 */
public final class IOUtils {

    private static final Logger LOGGER = Logger.getLogger(IOUtils.class);
    
    /**
     * Suppressing default constructor for non instantiability
     */
    private IOUtils() {
        throw new AssertionError("Suppress default constructor for non instantiability");
    }

    /**
     * @param closeable a {@link Closeable} resource
     * @throws IOException if an I/O error occurs
     */
    public static void close(final Closeable closeable) throws IOException {
        if (null != closeable) {
            closeable.close();
        }
    }

    /**
     * @param closeable a {@link Closeable} resource
     * return true if closes the resource successfully
     */
    public static boolean closeQuietly(final Closeable closeable) {
        try {
            IOUtils.close(closeable);
        } catch (Exception e) {
            LOGGER.warn(() -> "Failure to close resource: " + closeable.getClass().getCanonicalName()).debug("Stack trace:", e);
            return false;
        }
        return true;
    }
    
    /**
     * @param closeable a {@link Flushable} and {@link Closeable} resource
     * return true if flush and closes the resource successfully
     */
    public static <F extends Flushable & Closeable> boolean flushAndlCloseQuietly(final F closeable) {
        try {
            IOUtils.flushAndlClose(closeable);
        } catch (Exception e) {
            LOGGER.warn(() -> "Failure to flush and close resource: " + closeable.getClass().getCanonicalName()).debug("Stack trace:", e);
            return false;
        }
        return true;
    }
    
    /**
     * @param closeable a {@link Flushable} and {@link Closeable} resource
     * @throws IOException if an I/O error occurs
     */
    private static <F extends Flushable & Closeable> void flushAndlClose(final F closeable) throws IOException {
       if (null != closeable) {
           closeable.flush();
           closeable.close();
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

    /**
     * @param inputStream
     * @return
     * @throws IOException 
     */
    public static OutputStream newFileOutputStream(final String fileName) throws IOException {
        return IOUtils.newFileOutputStream(fileName, false);
    }
    
    /**
     * @param inputStream
     * @return
     * @throws IOException 
     */
    public static OutputStream newFileOutputStream(final String fileName, final boolean gzipped) throws IOException {
        final OutputStream os = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
        return true == gzipped ? new GZIPOutputStream(os) : os;
    }
}