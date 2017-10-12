/* 
 * © 2017 Springer Nature 
 */
package com.esv.core.utils;

import org.junit.Assert;
import org.junit.Test;

import com.esv.utile.utils.CharSequenceUtils;

/**
 * @author Elton S. Vianna <elton.vianna@yahoo.co.uk>
 * @version 1.0
 * @since 05/10/2017
 */
public class CharSequenceUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testRequireNotBlankWithNull() {
        CharSequenceUtils.requireNotBlank(null, "testRequireNotBlank");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequireNotBlankWithEmptyString() {
        CharSequenceUtils.requireNotBlank("", "testRequireNotBlank");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequireNotBlankWithEspaceString() {
        CharSequenceUtils.requireNotBlank("  ", "testRequireNotBlank");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequireNotBlankWithTab() {
        CharSequenceUtils.requireNotBlank("\t", "testRequireNotBlank");
    }

    public void testIsBlankWithNull() {
        Assert.assertTrue(CharSequenceUtils.isBlank(null));
    }

    public void testIsBlankWithEmptyString() {
        Assert.assertTrue(CharSequenceUtils.isBlank(""));
    }

    public void testIsBlankWithEspaceString() {
        Assert.assertTrue(CharSequenceUtils.isBlank("  "));
    }

    public void testIsNotBlankWithTab() {
        Assert.assertTrue(CharSequenceUtils.isBlank("\t"));
    }
}