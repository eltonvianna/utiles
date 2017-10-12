/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.core.utils;

import java.io.InputStream;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import com.esv.utile.utils.ResourceUtils;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 06/10/2017
 */
public class ResourceUtilsTest {
    
    @Test
    public void testGet() {
        Assert.assertTrue(ResourceUtils.get(".")  instanceof URL);
    }
    
    @Test
    public void testGetAsStream() {
        Assert.assertTrue(ResourceUtils.getAsStream(".")  instanceof InputStream);
    }
    
    @Test
    public void testNormalize() {
        Assert.assertEquals("/proc/version", ResourceUtils.normalize("/proc/../proc/./version"));
    }
}