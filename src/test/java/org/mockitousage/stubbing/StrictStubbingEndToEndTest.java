package org.mockitousage.stubbing;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.StateMaster;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.UnfinishedMockingException;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;
import org.mockitoutil.JUnitResultAssert;

import static org.mockito.BDDMockito.given;

public class StrictStubbingEndToEndTest {

    JUnitCore junit = new JUnitCore();

    @After public void after() {
        new StateMaster().clearMockitoListeners();
    }

    @Test public void finish_mocking_exception_does_not_hide_the_exception_from_test() {
        Result result = junit.run(ArgumentMismatch.class);
        JUnitResultAssert.assertThat(result)
                //both exceptions are reported to JUnit:
                .fails("stubbing_argument_mismatch", IllegalStateException.class)
                .fails("stubbing_argument_mismatch", UnnecessaryStubbingException.class);
    }

    @Test public void does_not_report_unused_stubbing_if_mismatch_reported() {
        Result result = junit.run(ReportMismatchButNotUnusedStubbing.class);
        JUnitResultAssert.assertThat(result).fails(1, PotentialStubbingProblem.class);
    }

    @Test public void strict_stubbing_does_not_leak_to_other_tests() {
        Result result = junit.run(LenientStrictness1.class, StrictStubsPassing.class, LenientStrictness2.class);
        //all tests pass, lenient test cases contain incorrect stubbing
        JUnitResultAssert.assertThat(result).succeeds(5);
    }

    @Test public void detects_unfinished_mocking() {
        Result result = junit.run(UnfinishedMocking.class);
        JUnitResultAssert.assertThat(result)
            .fails(UnfinishedMockingException.class, "\n" +
                "Unfinished mocking detected.\n" +
                "Previous 'Mockito.startMocking()' was not concluded with 'finishMocking()'. Example:\n" +
                "  MockitoMocking mocking = Mockito.startMocking(this, Strictness.STRICT_STUBS);\n" +
                "  //...\n" +
                "  mocking.finishMocking();\n" +
                "For more information, see javadoc for UnfinishedMockingException class");
    }

    public static class ArgumentMismatch {
        @Mock IMethods mock;
        MockitoSession mockito = Mockito.mockitoSession().initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();

        @After public void after() {
            mockito.finishMocking();
        }

        @Test public void stubbing_argument_mismatch() {
            given(mock.simpleMethod("1")).willReturn("one");
            throw new IllegalStateException();
        }
    }

    public static class ReportMismatchButNotUnusedStubbing {
        @Mock IMethods mock;
        MockitoSession mockito = Mockito.mockitoSession().initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();

        @After public void after() {
            mockito.finishMocking();
        }

        @Test public void mismatch() {
            given(mock.simpleMethod(1)).willReturn("");
            mock.simpleMethod(2);
        }
    }

    public static class StrictStubsPassing {
        @Mock IMethods mock;
        MockitoSession mockito = Mockito.mockitoSession().initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();

        @After public void after() {
            mockito.finishMocking();
        }

        @Test public void used() {
            System.out.println("working");
            given(mock.simpleMethod(1)).willReturn("");
            mock.simpleMethod(1);
        }
    }

    public static class LenientStrictness1 {
        @Mock IMethods mock = Mockito.mock(IMethods.class);

        @Test public void unused() {
            given(mock.simpleMethod(1)).willReturn("");
        }

        @Test public void mismatch() {
            given(mock.simpleMethod(2)).willReturn("");
            mock.simpleMethod(3);
        }
    }

    public static class LenientStrictness2 {
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

    public static class UnfinishedMocking {
        @Mock IMethods mock;
        MockitoSession mockito = Mockito.mockitoSession().initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();

        @Test public void unused() {
            given(mock.simpleMethod("1")).willReturn("one");
        }

        @Test public void unused2() {
            given(mock.simpleMethod("1")).willReturn("one");
        }
    }
}