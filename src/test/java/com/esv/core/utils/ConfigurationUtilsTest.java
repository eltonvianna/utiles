/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.core.utils;

import org.junit.Assert;
import org.junit.Test;

import com.esv.utile.utils.ConfigurationUtils;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 05/10/2017
 */
public class ConfigurationUtilsTest {

    @Test(expected=IllegalArgumentException.class)
    public void testGetRequiredProperty() {
        ConfigurationUtils.getRequiredProperty("testGetRequiredProperty");
    }
    
    @Test
    public void testGetStringProperty() {
        Assert.assertEquals("ok", ConfigurationUtils.getStringProperty("test.valid.property.string", null));
        Assert.assertEquals("ok", ConfigurationUtils.getStringProperty("test.null.property.string", "ok"));
        Assert.assertEquals("ok", ConfigurationUtils.getStringProperty("test.invalid.property.string", "ok"));
    }
    
    @Test
    public void testGetBooleanProperty() {
        Assert.assertEquals(true, ConfigurationUtils.getBooleanProperty("test.valid.property.boolean", false));
        Assert.assertEquals(true, ConfigurationUtils.getBooleanProperty("test.null.property.boolean", true));
        Assert.assertEquals(false, ConfigurationUtils.getBooleanProperty("test.invalid.property.boolean", true));
    }
    
    @Test
    public void testGetIntProperty() {
        Assert.assertEquals(Integer.valueOf(1), ConfigurationUtils.getIntProperty("test.valid.property.int", 0));
        Assert.assertEquals(Integer.valueOf(1), ConfigurationUtils.getIntProperty("test.null.property.int", 1));
        Assert.assertEquals(Integer.valueOf(1), ConfigurationUtils.getIntProperty("test.invalid.property.int", 1));
    }
    
    @Test
    public void testGetLongProperty() {
        Assert.assertEquals(Long.valueOf(1), ConfigurationUtils.getLongProperty("test.valid.property.long", 0));
        Assert.assertEquals(Long.valueOf(1), ConfigurationUtils.getLongProperty("test.null.property.long", 1));
        Assert.assertEquals(Long.valueOf(1), ConfigurationUtils.getLongProperty("test.invalid.property.long", 1));
    }
    
    @Test
    public void testGetFloatProperty() {
        Assert.assertEquals(Float.valueOf(1), ConfigurationUtils.getFloatProperty("test.valid.property.float", 0));
        Assert.assertEquals(Float.valueOf(1), ConfigurationUtils.getFloatProperty("test.null.property.float", 1));
        Assert.assertEquals(Float.valueOf(1), ConfigurationUtils.getFloatProperty("test.invalid.property.float", 1));
    }
    
    @Test
    public void testGetDoubleProperty() {
        Assert.assertEquals(Double.valueOf(1), ConfigurationUtils.getDoubleProperty("test.valid.property.double", 0));
        Assert.assertEquals(Double.valueOf(1), ConfigurationUtils.getDoubleProperty("test.null.property.double", 1));
        Assert.assertEquals(Double.valueOf(1), ConfigurationUtils.getDoubleProperty("test.invalid.property.double", 1));
    }
}