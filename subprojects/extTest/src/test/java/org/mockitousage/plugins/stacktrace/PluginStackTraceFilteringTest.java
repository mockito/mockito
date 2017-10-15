/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.plugins.stacktrace;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class PluginStackTraceFilteringTest extends TestBase {

    @Mock private IMethods mock;

    @After
    public void resetState() {
        super.resetState();
    }

    @Before
    public void setup() {
        super.makeStackTracesClean();
    }

    @Test
    public void pluginFiltersOutStackTraceElement() {
        try {
            MyStackTraceCleanerProvider.ENABLED = true;
            verifyMock_x();
            fail();
        } catch (WantedButNotInvoked e) {
            String trace = getStackTrace(e);
            assertThat(trace)
                    .contains("verifyMock_x")
                    .doesNotContain("verify_excludeMe_x");
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
            assertThat(trace)
                    .contains("verifyMock_x")
                    .contains("verify_excludeMe_x");
        }
    }

    private void verify_excludeMe_x() {
        verify(mock).simpleMethod();
    }

    private void verifyMock_x() {
        verify_excludeMe_x();
    }
}
