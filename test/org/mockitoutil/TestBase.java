/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import junit.framework.Assert;

import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.junit.After;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.mockito.StateMaster;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.configuration.ConfigurationAccess;
import org.mockito.internal.invocation.*;
import org.mockito.internal.invocation.realmethod.RealMethod;

import java.io.*;

/**
 * the easiest way to make sure that tests clean up invalid state is to require
 * valid state for all tests.
 */
public class TestBase extends Assert {

    @After
    public void cleanUpConfigInAnyCase() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(false);
        ConfigurationAccess.getConfig().overrideDefaultAnswer(null);
        new StateMaster().validate();
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

    protected static Invocation invocationOf(Class<?> type, String methodName, Object ... args) throws NoSuchMethodException {
        return new Invocation(new Object(), new SerializableMockitoMethod(type.getMethod(methodName,
                new Class[0])), args, 1, null);
    }

    protected static Invocation invocationOf(Class<?> type, String methodName, RealMethod realMethod) throws NoSuchMethodException {
        return new Invocation(new Object(), new SerializableMockitoMethod(type.getMethod(methodName,
                new Class[0])), new Object[0], 1, realMethod);
    }

    protected static String describe(SelfDescribing m) {
        return StringDescription.toString(m);
    }

    @SuppressWarnings("unchecked")
    protected <T> T serializeAndBack(T obj) throws Exception {
        ByteArrayOutputStream os = this.serializeMock(obj);
        return (T) this.deserializeMock(os, Object.class);
    }

    protected <T> T deserializeMock(ByteArrayOutputStream serialized, Class<T> type) throws IOException,
            ClassNotFoundException {
        InputStream unserialize = new ByteArrayInputStream(serialized.toByteArray());
        Object readObject = new ObjectInputStream(unserialize).readObject();
        assertNotNull(readObject);
        return type.cast(readObject);
    }

    protected ByteArrayOutputStream serializeMock(Object mock) throws IOException {
        ByteArrayOutputStream serialized = new ByteArrayOutputStream();
        new ObjectOutputStream(serialized).writeObject(mock);
        return serialized;
    }
}