/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.core.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.esv.utile.utils.IOUtils;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 05/10/2017
 */
public class IOUtilsTest {

    @Test
    public void testGetByteArray() throws IOException {
        final String str = "testGetByteArray";
        Assert.assertTrue(IOUtils.toByteArray(newInputStream(str)) instanceof byte[]);
        Assert.assertEquals(str, new String(IOUtils.toByteArray(newInputStream(str))));
    }


    @Test
    public void testGetBufferedReader() throws IOException {
        final String str = "testGetBufferedReader";
        Assert.assertTrue(IOUtils.toBufferedReader(newInputStream(str)) instanceof BufferedReader);
        Assert.assertEquals(str, IOUtils.toBufferedReader(newInputStream(str)).lines().collect(Collectors.joining()));
    }

    @Test
    public void testClose() throws IOException {
        final AtomicBoolean closed = new AtomicBoolean(false);
        IOUtils.close(() -> closed.set(true));
        Assert.assertTrue(closed.get());
    }
    
    @Test
    public void testCloseQuielty() throws IOException {
        final AtomicBoolean closed = new AtomicBoolean(false);
        IOUtils.closeQuietly(() -> {throw new RuntimeException();});
        Assert.assertFalse(closed.get());
    }
    
    @Test(expected=RuntimeException.class)
    public void testCloseThrowException() throws IOException {
        IOUtils.close(() -> {throw new RuntimeException();});
    }

    /**
     * @return
     */
    private InputStream newInputStream(final String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

}