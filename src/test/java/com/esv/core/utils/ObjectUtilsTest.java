/* 
 * Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.
 */
package com.esv.core.utils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.esv.utile.utils.CharSequenceUtils;
import com.esv.utile.utils.ObjectUtils;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 05/10/2017
 */
public class ObjectUtilsTest {

    private static enum TestEnum { TEST }
    
    @Test(expected=IllegalArgumentException.class)
    public void testRequireNotNull() {
        ObjectUtils.requireNotNull(null, "testRequireNotNull");
    }
    
    @Test
    public void testGetTypes() {
        final Class<?>[] expecteds = {String.class};
        Assert.assertArrayEquals(expecteds, ObjectUtils.getTypes("testGetTypes"));
    }
    
    @Test
    public void testFindConstructor() {
        try {
            Assert.assertTrue(ObjectUtils.findConstructor(String.class) instanceof Constructor);
            Assert.assertTrue(ObjectUtils.findConstructor(String.class, "testFindConstructor") instanceof Constructor);
        } catch (Exception e) {
            Assert.assertNotNull(e.getMessage(), e);
        }
    }
    
    @Test
    public void testNewInstances() {
        final String[] classNames = {"java.lang.String"};
        Assert.assertTrue(ObjectUtils.newInstances(classNames) instanceof List);
        Assert.assertTrue(ObjectUtils.newInstances(classNames).size() == 1);
        Assert.assertTrue(ObjectUtils.newInstances(classNames).get(0) instanceof String);
        Assert.assertEquals(CharSequenceUtils.empty, ObjectUtils.newInstances(classNames).get(0));
    }
    
    @Test
    public void testNewInstanceWithArguments() {
        Assert.assertEquals("testNewInstanceWithArguments", ObjectUtils.newInstance("java.lang.String", "testNewInstanceWithArguments"));
    }
    
    @Test
    public void testIsMap() {
        Assert.assertTrue(ObjectUtils.isMap(new HashMap<>()));
    }
    
    @Test
    public void testIsCollection() {
        Assert.assertTrue(ObjectUtils.isCollection(new ArrayList<>()));
        Assert.assertTrue(ObjectUtils.isCollection(new HashSet<>()));
    }
    
    @Test
    public void testIsArray() {
        Assert.assertTrue(ObjectUtils.isArray(new Object[]{}));
        Assert.assertTrue(ObjectUtils.isArray(new Object[0]));
    }
    
    @Test
    public void testIsEnum() {
        Assert.assertTrue(ObjectUtils.isEnum(TestEnum.TEST));
    }
    
    @Test
    public void testIsCharSequence() {
        Assert.assertTrue(ObjectUtils.isCharSequence("test"));
        Assert.assertTrue(ObjectUtils.isCharSequence('c'));
    }
    
    @Test
    public void testIsNumber() {
        Assert.assertTrue(ObjectUtils.isNumber(0));
        Assert.assertTrue(ObjectUtils.isNumber(0.1));
        Assert.assertTrue(ObjectUtils.isNumber(0.1d));
        Assert.assertTrue(ObjectUtils.isNumber(0.1f));
        Assert.assertTrue(ObjectUtils.isNumber(112l));
    }
    
    @Test
    public void testIsBoolean() {
        Assert.assertTrue(ObjectUtils.isBoolean(Boolean.TRUE));
        Assert.assertTrue(ObjectUtils.isBoolean(true));
    }
    
    @Test
    public void testIsScalar() {
        Assert.assertTrue(ObjectUtils.isScalar("Test"));
        Assert.assertTrue(ObjectUtils.isScalar('c'));
        Assert.assertTrue(ObjectUtils.isScalar(1));
        Assert.assertTrue(ObjectUtils.isScalar(999.999d));
        Assert.assertTrue(ObjectUtils.isScalar(10000000L));
        Assert.assertTrue(ObjectUtils.isScalar(Boolean.FALSE));
        Assert.assertTrue(ObjectUtils.isScalar(true));
    }
}