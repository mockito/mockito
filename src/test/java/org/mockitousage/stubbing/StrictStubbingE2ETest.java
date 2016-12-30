package org.mockitousage.stubbing;

import org.junit.After;
import org.junit.Ignore;
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

    @Test public void finish_mocking_exception_does_not_hide_the_exception_from_test() {
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(ArgumentMismatch.class);
        JUnitResultAssert.assertThat(result)
                .fails("stubbing_argument_mismatch", IllegalStateException.class)
                .fails("stubbing_argument_mismatch", UnnecessaryStubbingException.class);
    }

    @Test public void does_not_report_unused_stubbing_if_mismatch_reported() {
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(ReportMismatchButNotUnusedStubbing.class);
        JUnitResultAssert.assertThat(result).fails(1, PotentialStubbingProblem.class);
    }

    @Ignore //TODO
    @Test public void prevents_multiple_listeners_in_the_same_thread() {
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(ClassFieldInitialization.class);
        JUnitResultAssert.assertThat(result)
                .fails(2, PotentialStubbingProblem.class);
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

    public static class ClassFieldInitialization {
        @Mock IMethods mock;
        MockitoMocking mocking = Mockito.startMocking(this, Strictness.STRICT_STUBS);

        @After public void after() {
            mocking.finishMocking();
        }

        @Test public void test1() {
            given(mock.simpleMethod(1)).willReturn("");
            mock.simpleMethod(2);
        }

        @Test public void test2() {
            given(mock.simpleMethod(2)).willReturn("");
            mock.simpleMethod(3);
        }
    }
}