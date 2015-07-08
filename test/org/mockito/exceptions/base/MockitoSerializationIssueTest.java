/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.exceptions.base;

import org.junit.Test;
import org.mockito.internal.configuration.ConfigurationAccess;
import org.mockitoutil.TestBase;

import java.util.Arrays;

public class MockitoSerializationIssueTest extends TestBase {

    @Test
    public void shouldFilterOutTestClassFromStacktraceWhenCleanFlagIsTrue() {
        // given
        ConfigurationAccess.getConfig().overrideCleansStackTrace(true);

        // when
        MockitoSerializationIssue issue = new MockitoSerializationIssue("msg", new Exception("cause"));

        // then
        assertContains("MockitoSerializationIssueTest", Arrays.toString(issue.getUnfilteredStackTrace()));
        assertNotContains("MockitoSerializationIssueTest", Arrays.toString(issue.getStackTrace()));
    }

    @Test
    public void shouldKeepExecutingClassInStacktraceWhenCleanFlagIsFalse() {
        // given
        ConfigurationAccess.getConfig().overrideCleansStackTrace(false);

        // when
        MockitoSerializationIssue issue = new MockitoSerializationIssue("msg", new Exception("cause"));

        // then
        assertContains("MockitoSerializationIssueTest", Arrays.toString(issue.getUnfilteredStackTrace()));
        assertContains("MockitoSerializationIssueTest", Arrays.toString(issue.getStackTrace()));
    }
}
