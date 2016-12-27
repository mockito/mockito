/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitoutil;

import org.junit.After;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.mockito.StateMaster;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.configuration.ConfigurationAccess;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.SerializableMethod;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.invocation.Invocation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.mockito.Mockito.mock;

/**
 * the easiest way to make sure that tests clean up invalid state is to require
 * valid state for all tests.
 */
public class TestBase {

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

    protected static Invocation invocationOf(Class<?> type, String methodName, Object ... args) throws NoSuchMethodException {
        Class<?>[] types = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }
        return new InvocationImpl(mock(type), new SerializableMethod(type.getMethod(methodName,
                types)), args, 1, null, new LocationImpl());
    }

    protected static Invocation invocationOf(Class<?> type, String methodName, RealMethod realMethod) throws NoSuchMethodException {
        return new InvocationImpl(new Object(), new SerializableMethod(type.getMethod(methodName,
                new Class<?>[0])), new Object[0], 1, realMethod, new LocationImpl());
    }

    protected static Invocation invocationAt(String location) {
        return new InvocationBuilder().location(location).toInvocation();
    }

    protected static InvocationMatcher invocationMatcherAt(String location) {
        return new InvocationBuilder().location(location).toInvocationMatcher();
    }

    protected String getStackTrace(Throwable e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(out));
        try {
            out.close();
        } catch (IOException ex) {}
        return out.toString();
    }

    /**
     * Filters out unwanted line numbers from provided stack trace String.
     * This is useful for writing assertions for exception messages that contain line numbers.
     *
     * For example it turns:
     * blah blah (UnusedStubsExceptionMessageTest.java:27)
     * into:
     * blah blah (UnusedStubsExceptionMessageTest.java:0)
     */
    public static String filterLineNo(String stackTrace) {
        return stackTrace.replaceAll("(\\((\\w+\\.java):(\\d)+\\))", "($2:0)");
    }
}
