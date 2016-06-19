package org.mockitousage.junitrule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.VerificationCollector;
import org.mockitousage.IMethods;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class VerificationCollectorImplTest {

    @Test
    public void should_not_throw_any_exceptions_when_verifications_are_succesful() {
        VerificationCollector collector = MockitoJUnit.collector().assertLazily();

        IMethods methods = mock(IMethods.class);
        methods.simpleMethod();

        verify(methods).simpleMethod();
        collector.collectAndReport();
    }

    @Test(expected = MockitoAssertionError.class)
    public void should_collect_verification_failures() {
        VerificationCollector collector = MockitoJUnit.collector().assertLazily();

        IMethods methods = mock(IMethods.class);

        verify(methods).simpleMethod();
        collector.collectAndReport();
    }

    @Test
    public void should_collect_multiple_verification_failures() {
        VerificationCollector collector = MockitoJUnit.collector().assertLazily();

        IMethods methods = mock(IMethods.class);

        verify(methods).simpleMethod();
        verify(methods).byteReturningMethod();
        try {
            collector.collectAndReport();
            fail();
        } catch (MockitoAssertionError error) {
            assertThat(error).hasMessageContaining("1. Wanted but not invoked:");
            assertThat(error).hasMessageContaining("2. Wanted but not invoked:");
        }
    }

    @Test
    public void should_only_collect_failures_ignore_succesful_verifications() {
        VerificationCollector collector = MockitoJUnit.collector().assertLazily();

        IMethods methods = mock(IMethods.class);

        verify(methods, never()).simpleMethod();
        verify(methods).byteReturningMethod();

        this.assertAtLeastOneFailure(collector);
    }

    @Test
    public void should_continue_collecting_after_failing_verification() {
        VerificationCollector collector = MockitoJUnit.collector().assertLazily();

        IMethods methods = mock(IMethods.class);
        methods.simpleMethod();

        verify(methods).byteReturningMethod();
        verify(methods).simpleMethod();

        this.assertAtLeastOneFailure(collector);
    }

    private void assertAtLeastOneFailure(VerificationCollector collector) {
        try {
            collector.collectAndReport();
            fail();
        } catch (MockitoAssertionError error) {
            assertThat(error).hasMessageContaining("1. Wanted but not invoked:");
            assertThat(error.getMessage()).doesNotContain("2.");
        }
    }

    @Test
    public void should_invoke_collector_rule_after_test() {
        JUnitCore runner = new JUnitCore();
        Result result = runner.run(VerificationCollectorRuleInner.class);

        assertThat(result.getFailureCount()).isEqualTo(1);
        assertThat(result.getFailures().get(0).getMessage()).contains("1. Wanted but not invoked:");
    }

    // This class is picked up when running a test suite using an IDE. It fails on purpose.
    public static class VerificationCollectorRuleInner {

        @Rule
        public VerificationCollector collector = MockitoJUnit.collector();

        @Test
        public void should_fail() {
            IMethods methods = mock(IMethods.class);

            verify(methods).simpleMethod();
        }

        @Test
        public void should_not_fail() {
            IMethods methods = mock(IMethods.class);
            methods.simpleMethod();

            verify(methods).simpleMethod();
        }
    }
}
