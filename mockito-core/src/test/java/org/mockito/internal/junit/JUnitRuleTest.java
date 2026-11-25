/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.DisabledMockException;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.junit.MockitoJUnit;
import org.mockito.plugins.InlineMockMaker;
import org.mockitousage.IMethods;
import org.mockitoutil.SafeJUnitRule;

public class JUnitRuleTest {

    @Rule public SafeJUnitRule rule = new SafeJUnitRule(MockitoJUnit.rule());
    @Mock IMethods mock;

    @Test
    public void injects_into_test_case() throws Throwable {
        assertTrue(mockingDetails(mock).isMock());
    }

    @Test
    public void rethrows_exception() throws Throwable {
        rule.expectFailure(RuntimeException.class, "foo");
        throw new RuntimeException("foo");
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void detects_invalid_mockito_usage_on_success() throws Throwable {
        rule.expectFailure(UnfinishedStubbingException.class);
        when(mock.simpleMethod());
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void does_not_check_invalid_mockito_usage_on_failure() throws Throwable {
        // This intended behavior is questionable
        // However, it was like that since the beginning of JUnit rule support
        // Users never questioned this behavior. Hence, let's stick to it unless we have more data
        rule.expectFailure(RuntimeException.class, "foo");

        Mockito.when(mock.simpleMethod()); // <--- unfinished stubbing
        throw new RuntimeException("foo"); // <--- some failure
    }

    // Reproduction for https://github.com/mockito/mockito/issues/1902
    // The user's @After runs inside the statement that the rule wraps, i.e. before the rule
    // finishes the MockitoSession. When @After calls Mockito.framework().clearInlineMocks(),
    // finishing the session inspects the cleared mocks and fails the test. Without the guard
    // in JUnitSessionStore, this used to surface as a bare DisabledMockException ("Mock
    // accessed after inline mocks were cleared"), without any indication of what to change.
    @Test
    public void clear_inline_mocks_in_after_gives_descriptive_error() throws Throwable {
        // Only inline mocks are disabled by Mockito.framework().clearInlineMocks(). With the
        // subclass mock maker the mocks are not disabled, so the rule does not throw and the
        // scenario below cannot be reproduced.
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));

        rule.expectFailure(
                new SafeJUnitRule.FailureAssert() {
                    public void doAssert(Throwable t) {
                        assertThat(t)
                                .isInstanceOf(MockitoException.class)
                                .hasMessageContaining("clearInlineMocks")
                                .hasMessageContaining("MockitoJUnit.rule()");
                        assertThat(t.getCause()).isInstanceOf(DisabledMockException.class);
                    }
                });

        mock.simpleMethod();
        Mockito.verify(mock).simpleMethod();
        // Simulates the user's @After, which runs before the rule finishes the session
        Mockito.framework().clearInlineMocks();
    }
}
