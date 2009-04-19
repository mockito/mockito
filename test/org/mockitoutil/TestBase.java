/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import junit.framework.Assert;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.mockito.StateMaster;
import org.mockito.internal.configuration.ConfigurationAccess;

/**
 * the easiest way to make sure that tests clean up invalid state is to require
 * valid state for all tests.
 */
public class TestBase extends Assert {

    @After
    public void cleanUpConfigInAnyCase() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(false);
        ConfigurationAccess.getConfig().overrideReturnValues(null);
    }

    @Before
    public void init() {
        new StateMaster().validate();
        MockitoAnnotations.initMocks(this);
    }
    
    public void makeStackTracesClean() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(true);
    }
    
    public void resetState() {
        new StateMaster().reset();
    }
    
    //I'm really tired of matchers, enter the assertor!
    protected static <T> void assertThat(T o, Assertor<T> a) {
        a.assertValue(o);
    }
    
    protected static <T> void assertThat(T actual, Matcher<T> m) {
        org.junit.Assert.assertThat(actual, m);
    }
    
    protected static <T> void assertThat(String message, T actual, Matcher<T> m) {
        org.junit.Assert.assertThat(message, actual, m);
    }
    
    public static <T> Assertor<String> contains(final String substring) {
        return new Assertor<String>() {
            public void assertValue(String value) {
                assertTrue("This substring: \n" + substring + 
                        "\nshould be inside of:\n" + value
                        , value.contains(substring));
            }
        };
    }
    
    public static <T> Assertor<String> endsWith(final String substring) {
        return new Assertor<String>() {
            public void assertValue(String value) {
                assertTrue("This substring: \n" + substring + 
                        "\nshould be at the end of:\n" + value
                        , value.endsWith(substring));
            }
        };
    }

    public static <T> Assertor<Throwable> messageContains(final String text) {
        return new Assertor<Throwable>() {
            public void assertValue(Throwable value) {
                assertTrue("This substring: \n" + text + 
                        "\nshould occur in this exception message:" + value.getMessage()
                        , ((Throwable) value).getMessage().contains(text));
            }
        };
    }

    public static void assertContains(String sub, String string) {
        assertTrue("\n" +
                "This substing:" +
                sub +
                "\n" +
                "should be inside of:" +
                string +
                "\n"
                , string.contains(sub));
    }
    
    public static void assertNotContains(String sub, String string) {
        assertFalse("\n" +
                "This substing:" +
                sub +
                "\n" +
                "should NOT be inside of:" +
                string +
                "\n"
                , string.contains(sub));
    }
}