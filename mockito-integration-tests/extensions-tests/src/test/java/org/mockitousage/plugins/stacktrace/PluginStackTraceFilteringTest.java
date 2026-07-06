/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.StateMaster;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.internal.configuration.ConfigurationAccess;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockitousage.IMethods;

@ExtendWith(MockitoExtension.class)
public class PluginStackTraceFilteringTest {

    @Mock private IMethods mock;

    @BeforeEach
    public void setup() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(true);
    }

    @AfterEach
    public void resetState() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(false);
        new StateMaster().reset();
    }

    @Test
    public void pluginFiltersOutStackTraceElement() {
        try {
            MyStackTraceCleanerProvider.ENABLED = true;
            verifyMock_x();
            fail();
        } catch (WantedButNotInvoked e) {
            String trace = getStackTrace(e);
            assertThat(trace).contains("verifyMock_x").doesNotContain("verify_excludeMe_x");
        }
    }

    @Test
    public void pluginDoesNotFilterOutStackTraceElement() {
        try {
            MyStackTraceCleanerProvider.ENABLED = false;
            verifyMock_x();
            fail();
        } catch (WantedButNotInvoked e) {
            String trace = getStackTrace(e);
            assertThat(trace).contains("verifyMock_x").contains("verify_excludeMe_x");
        }
    }

    private String getStackTrace(Throwable e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(out));
        return out.toString();
    }

    private void verify_excludeMe_x() {
        verify(mock).simpleMethod();
    }

    private void verifyMock_x() {
        verify_excludeMe_x();
    }
}
