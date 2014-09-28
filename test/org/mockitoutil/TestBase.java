/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitoutil;

import junit.framework.Assert;
import org.fest.assertions.Assertions;
import org.fest.assertions.Condition;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.junit.After;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.mockito.StateMaster;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.configuration.ConfigurationAccess;
import org.mockito.internal.invocation.InvocationImpl;
import org.mockito.internal.invocation.SerializableMethod;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import static org.mockito.Mockito.mock;

/**
 * the easiest way to make sure that tests clean up invalid state is to require
 * valid state for all tests.
 */
@SuppressWarnings("unchecked")
public class TestBase extends Assert {

    @After
    public void cleanUpConfigInAnyCase() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(false);
        ConfigurationAccess.getConfig().overrideDefaultAnswer(null);
        StateMaster state = new StateMaster();
        //catch any invalid state left over after test case run
        //this way we can catch early if some Mockito operations leave weird state afterwards
        state.validate();
        //reset the state, especially, reset any ongoing stubbing for correct error messages of tests that assert unhappy paths
        state.reset();
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    public static void makeStackTracesClean() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(true);
    }
    
    public void resetState() {
        new StateMaster().reset();
    }
    
    protected Invocation getLastInvocation() {
        return new MockitoCore().getLastInvocation();
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
    
    public static <T> Assertor<String> endsWith(final String substring) {
        return new Assertor<String>() {
            public void assertValue(String value) {
                assertTrue("This substring: \n" + substring + 
                        "\nshould be at the end of:\n" + value
                        , value.endsWith(substring));
            }
        };
    }
    
    public static void assertNotEquals(Object expected, Object got) {
        assertFalse(expected.equals(got));
    }

    public static void assertContains(String sub, String string) {
        assertTrue("\n" +
                "This substring:[" +
                sub +
                "]\n" +
                "should be inside of:[" +
                string +
                "]\n"
                , string.contains(sub));
    }

    public static void assertContainsIgnoringCase(String sub, String string) {
        assertTrue("\n" +
                "This substring:" +
                sub +
                "\n" +
                "should be inside of:" +
                string +
                "\n"
                , containsIgnoringCase(string, sub));
    }

    private static boolean containsIgnoringCase(String string, String sub) {
        int subLength = sub.length();
        if (string.length() < subLength) {
            return false;
        }
        int i = 0;
        while(i+subLength <= string.length()) {
            boolean temp = string.substring(i, i+subLength).equalsIgnoreCase(sub);
            if (temp) {
                return true;
            }
            i++;
        }
        return false;
    }

    public static void assertNotContains(String sub, String string) {
        assertFalse("\n" +
                "This substring:" +
                sub +
                "\n" +
                "should NOT be inside of:" +
                string +
                "\n"
                , string.contains(sub));
    }
    
    protected static Invocation invocationOf(Class<?> type, String methodName, Object ... args) throws NoSuchMethodException {
        Class[] types = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }
        return new InvocationImpl(mock(type), new SerializableMethod(type.getMethod(methodName,
                types)), args, 1, null);
    }

    protected static Invocation invocationOf(Class<?> type, String methodName, RealMethod realMethod) throws NoSuchMethodException {
        return new InvocationImpl(new Object(), new SerializableMethod(type.getMethod(methodName,
                new Class[0])), new Object[0], 1, realMethod);
    }

    protected static String describe(SelfDescribing m) {
        return StringDescription.toString(m);
    }

    protected boolean isMock(Object o) {
        return new MockUtil().isMock(o);
    }

    protected void assertContainsType(final Collection<?> list, final Class<?> clazz) {
        Assertions.assertThat(list).satisfies(new Condition<Collection<?>>() {
            @Override
            public boolean matches(Collection<?> objects) {
                for (Object object : objects) {
                    if (clazz.isAssignableFrom(object.getClass())) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    protected String getStackTrace(Throwable e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(out));
        try {
            out.close();
        } catch (IOException ex) {}
        return out.toString();
    }
}
