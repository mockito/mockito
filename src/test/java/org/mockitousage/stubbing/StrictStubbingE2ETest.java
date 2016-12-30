package org.mockitousage.stubbing;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoMocking;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;
import org.mockitoutil.JUnitResultAssert;

import static org.mockito.BDDMockito.given;

public class StrictStubbingE2ETest {

    JUnitCore junit = new JUnitCore();

    @Test public void finish_mocking_exception_does_not_hide_the_exception_from_test() {
        Result result = junit.run(ArgumentMismatch.class);
        JUnitResultAssert.assertThat(result)
                .fails("stubbing_argument_mismatch", IllegalStateException.class)
                .fails("stubbing_argument_mismatch", UnnecessaryStubbingException.class);
    }

    @Test public void does_not_report_unused_stubbing_if_mismatch_reported() {
        Result result = junit.run(ReportMismatchButNotUnusedStubbing.class);
        JUnitResultAssert.assertThat(result).fails(1, PotentialStubbingProblem.class);
    }

    @Test public void strict_stubbing_does_not_leak_to_other_tests() {
        Result result = junit.run(DefaultStrictness1.class, StrictStubsPassing.class, DefaultStrictness2.class);
        JUnitResultAssert.assertThat(result).succeeds(5);
    }

    public static class ArgumentMismatch {
        @Mock IMethods mock;
        MockitoMocking mocking = Mockito.startMocking(this, Strictness.STRICT_STUBS);

        @After public void after() {
            mocking.finishMocking();
        }

        @Test public void stubbing_argument_mismatch() {
            given(mock.simpleMethod("1")).willReturn("one");
            throw new IllegalStateException();
        }
    }

    public static class ReportMismatchButNotUnusedStubbing {
        @Mock IMethods mock;
        MockitoMocking mocking = Mockito.startMocking(this, Strictness.STRICT_STUBS);

        @After public void after() {
            mocking.finishMocking();
        }

        @Test public void mismatch() {
            given(mock.simpleMethod(1)).willReturn("");
            mock.simpleMethod(2);
        }
    }

    public static class StrictStubsPassing {
        @Mock IMethods mock;
        MockitoMocking mocking = Mockito.startMocking(this, Strictness.STRICT_STUBS);

        @After public void after() {
            mocking.finishMocking();
        }

        @Test public void used() {
            System.out.println("working");
            given(mock.simpleMethod(1)).willReturn("");
            mock.simpleMethod(1);
        }
    }

    public static class DefaultStrictness1 {
        @Mock IMethods mock = Mockito.mock(IMethods.class);

        @Test public void unused() {
            given(mock.simpleMethod(1)).willReturn("");
        }

        @Test public void mismatch() {
            given(mock.simpleMethod(2)).willReturn("");
            mock.simpleMethod(3);
        }
    }

    public static class DefaultStrictness2 {
        @Mock IMethods mock = Mockito.mock(IMethods.class);

        @Test public void unused() {
            given(mock.simpleMethod(1)).willReturn("");
        }

        @Test public void mismatch() {
            System.out.println("SomeDemo");
            given(mock.simpleMethod(2)).willReturn("");
            mock.simpleMethod(3);
        }
    }
}