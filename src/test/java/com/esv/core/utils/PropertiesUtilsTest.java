/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.core.utils;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.esv.utile.utils.PropertiesUtils;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 05/10/2017
 */
public class PropertiesUtilsTest {
    
    @BeforeClass
    public static void setup() throws IOException {
        PropertiesUtils.loadProperties();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetRequiredProperty() {
        PropertiesUtils.getRequiredProperty("testGetRequiredProperty");
    }
    
    @Test
    public void testGetStringProperty() {
        Assert.assertEquals("ok", PropertiesUtils.getStringProperty("test.valid.property.string", null));
        Assert.assertEquals("ok", PropertiesUtils.getStringProperty("test.null.property.string", "ok"));
        Assert.assertEquals("ok", PropertiesUtils.getStringProperty("test.invalid.property.string", "ok"));
    }
    
    @Test
    public void testGetBooleanProperty() {
        Assert.assertEquals(true, PropertiesUtils.getBooleanProperty("test.valid.property.boolean", false));
        Assert.assertEquals(true, PropertiesUtils.getBooleanProperty("test.null.property.boolean", true));
        Assert.assertEquals(false, PropertiesUtils.getBooleanProperty("test.invalid.property.boolean", true));
    }
    
    @Test
    public void testGetIntProperty() {
        Assert.assertEquals(Integer.valueOf(1), PropertiesUtils.getIntProperty("test.valid.property.int", 0));
        Assert.assertEquals(Integer.valueOf(1), PropertiesUtils.getIntProperty("test.null.property.int", 1));
        Assert.assertEquals(Integer.valueOf(1), PropertiesUtils.getIntProperty("test.invalid.property.int", 1));
    }
    
    @Test
    public void testGetLongProperty() {
        Assert.assertEquals(Long.valueOf(1), PropertiesUtils.getLongProperty("test.valid.property.long", 0));
        Assert.assertEquals(Long.valueOf(1), PropertiesUtils.getLongProperty("test.null.property.long", 1));
        Assert.assertEquals(Long.valueOf(1), PropertiesUtils.getLongProperty("test.invalid.property.long", 1));
    }
    
    @Test
    public void testGetFloatProperty() {
        Assert.assertEquals(Float.valueOf(1), PropertiesUtils.getFloatProperty("test.valid.property.float", 0));
        Assert.assertEquals(Float.valueOf(1), PropertiesUtils.getFloatProperty("test.null.property.float", 1));
        Assert.assertEquals(Float.valueOf(1), PropertiesUtils.getFloatProperty("test.invalid.property.float", 1));
    }
    
    @Test
    public void testGetDoubleProperty() {
        Assert.assertEquals(Double.valueOf(1), PropertiesUtils.getDoubleProperty("test.valid.property.double", 0));
        Assert.assertEquals(Double.valueOf(1), PropertiesUtils.getDoubleProperty("test.null.property.double", 1));
        Assert.assertEquals(Double.valueOf(1), PropertiesUtils.getDoubleProperty("test.invalid.property.double", 1));
    }
}