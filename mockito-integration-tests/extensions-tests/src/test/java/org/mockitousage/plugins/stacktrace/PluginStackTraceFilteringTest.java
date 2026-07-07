/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase5;

public class PluginStackTraceFilteringTest extends TestBase5 {

    @Mock private IMethods mock;

    @AfterEach
    public void resetState() {
        super.resetState();
    }

    @BeforeEach
    public void setup() {
        makeStackTracesClean();
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

    private void verify_excludeMe_x() {
        verify(mock).simpleMethod();
    }

    private void verifyMock_x() {
        verify_excludeMe_x();
    }
}
