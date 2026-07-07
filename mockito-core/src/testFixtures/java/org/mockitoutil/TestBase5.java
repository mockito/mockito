/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.mockito.StateMaster;
import org.mockito.internal.configuration.ConfigurationAccess;

/**
 * JUnit 5 (Jupiter) counterpart of {@link TestBase}.
 */
public class TestBase5 {

    /**
     * Condition to be used with AssertJ
     */
    public static Condition<Throwable> hasMessageContaining(final String substring) {
        return new Condition<Throwable>() {
            @Override
            public boolean matches(Throwable e) {
                return e.getMessage().contains(substring);
            }
        };
    }

    @AfterEach
    public void cleanUpConfigInAnyCase() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(false);
        ConfigurationAccess.getConfig().overrideDefaultAnswer(null);
        StateMaster state = new StateMaster();
        // catch any invalid state left over after test case run
        // this way we can catch early if some Mockito operations leave weird state afterwards
        state.validate();
        // reset the state, especially, reset any ongoing stubbing for correct error messages of
        // tests that assert unhappy paths
        state.reset();
    }

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    public static void makeStackTracesClean() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(true);
    }

    public void resetState() {
        new StateMaster().reset();
    }

    protected String getStackTrace(Throwable e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(out));
        try {
            out.close();
        } catch (IOException ignored) {
        }
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
