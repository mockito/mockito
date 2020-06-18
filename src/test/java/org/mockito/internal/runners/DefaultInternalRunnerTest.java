/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.internal.junit.MockitoTestListener;
import org.mockito.internal.junit.TestFinishedEvent;
import org.mockito.internal.util.Supplier;

public class DefaultInternalRunnerTest {

    private final RunListener runListener = mock(RunListener.class);
    private final MockitoTestListener mockitoTestListener = mock(MockitoTestListener.class);
    private final Supplier<MockitoTestListener> supplier =
            new Supplier<MockitoTestListener>() {
                public MockitoTestListener get() {
                    return mockitoTestListener;
                }
            };

    @Test
    public void does_not_fail_when_tests_succeeds() throws Exception {
        new DefaultInternalRunner(SuccessTest.class, supplier).run(newNotifier(runListener));

        verifyTestFinishedSuccessfully();
    }

    @Test
    public void does_not_fail_second_test_when_first_test_fail() throws Exception {
        new DefaultInternalRunner(TestFailOnInitialization.class, supplier)
                .run(newNotifier(runListener));

        verify(runListener, times(1)).testFailure(any(Failure.class));
        verify(runListener, times(1)).testFinished(any(Description.class));
        verify(mockitoTestListener, only()).testFinished(any(TestFinishedEvent.class));

        reset(runListener, mockitoTestListener);

        new DefaultInternalRunner(SuccessTest.class, supplier).run(newNotifier(runListener));

        verifyTestFinishedSuccessfully();
    }

    @Test
    public void does_not_fail_when_rule_invokes_statement_multiple_times() throws Exception {
        new DefaultInternalRunner(TestWithRepeatingRule.class, supplier)
                .run(newNotifier(runListener));

        verifyTestFinishedSuccessfully();
    }

    private void verifyTestFinishedSuccessfully() throws Exception {
        verify(runListener, never()).testFailure(any(Failure.class));
        verify(runListener, times(1)).testFinished(any(Description.class));
        verify(mockitoTestListener, only()).testFinished(any(TestFinishedEvent.class));
    }

    private RunNotifier newNotifier(RunListener listener) {
        RunNotifier notifier = new RunNotifier();
        notifier.addListener(listener);
        return notifier;
    }

    public static class SuccessTest {

        @Test
        public void this_test_is_NOT_supposed_to_fail() {
            assertTrue(true);
        }
    }

    public static final class TestFailOnInitialization {

        @Mock private System system;

        @Test
        public void this_test_is_supposed_to_fail() {
            assertNotNull(system);
        }
    }

    public static final class TestWithRepeatingRule extends SuccessTest {

        @Rule
        public TestRule rule =
                (base, description) ->
                        new Statement() {
                            @Override
                            public void evaluate() throws Throwable {
                                base.evaluate();
                                base.evaluate();
                            }
                        };
    }
}
