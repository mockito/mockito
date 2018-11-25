/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
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
import org.mockito.exceptions.misusing.UnfinishedMockingSessionException;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;
import org.mockitousage.strictness.ProductionCode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.BDDMockito.given;
import static org.mockitoutil.ConcurrentTesting.concurrently;
import static org.mockitoutil.JUnitResultAssert.assertThat;

public class StrictStubbingEndToEndTest {

    JUnitCore junit = new JUnitCore();

    @After public void after() {
        new StateMaster().clearMockitoListeners();
    }

    @Test public void finish_mocking_exception_does_not_hide_the_exception_from_test() {
        Result result = junit.run(UnnecessaryStubbing.class);
        assertThat(result)
                //both exceptions are reported to JUnit:
                .fails("unnecessary_stubbing", IllegalStateException.class)
                .fails("unnecessary_stubbing", UnnecessaryStubbingException.class);
    }

    @Test public void does_not_report_unused_stubbing_if_mismatch_reported() {
        Result result = junit.run(ReportMismatchButNotUnusedStubbing.class);
        assertThat(result).fails(1, PotentialStubbingProblem.class);
    }

    @Test public void strict_stubbing_does_not_leak_to_other_tests() {
        Result result = junit.run(LenientStrictness1.class, StrictStubsPassing.class, LenientStrictness2.class);
        //all tests pass, lenient test cases contain incorrect stubbing
        assertThat(result).succeeds(5);
    }

    @Test public void detects_unfinished_session() {
        Result result = junit.run(UnfinishedMocking.class);
        assertThat(result)
            .fails(UnfinishedMockingSessionException.class, "\n" +
                "Unfinished mocking session detected.\n" +
                "Previous MockitoSession was not concluded with 'finishMocking()'.\n" +
                "For examples of correct usage see javadoc for MockitoSession class.");
    }

    @Test public void concurrent_sessions_in_different_threads() throws Exception {
        final Map<Class, Result> results = new ConcurrentHashMap<Class, Result>();
        concurrently(new Runnable() {
                         public void run() {
                             results.put(StrictStubsPassing.class, junit.run(StrictStubsPassing.class));
                         }
                     }, new Runnable() {
                         public void run() {
                             results.put(ReportMismatchButNotUnusedStubbing.class, junit.run(ReportMismatchButNotUnusedStubbing.class));
                         }
                     }
        );

        assertThat(results.get(StrictStubsPassing.class)).succeeds(1);
        assertThat(results.get(ReportMismatchButNotUnusedStubbing.class)).fails(1);
    }

    public static class UnnecessaryStubbing {
        @Mock IMethods mock;
        MockitoSession mockito = Mockito.mockitoSession().initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();

        @After public void after() {
            mockito.finishMocking();
        }

        @Test public void unnecessary_stubbing() {
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
            ProductionCode.simpleMethod(mock, 2);
        }
    }

    public static class StrictStubsPassing {
        @Mock IMethods mock;
        MockitoSession mockito = Mockito.mockitoSession().initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();

        @After public void after() {
            mockito.finishMocking();
        }

        @Test public void used() {
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
