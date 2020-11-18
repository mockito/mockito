/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.strictness;

import org.assertj.core.api.ThrowableAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.leniently;
import static org.mockito.Mockito.*;

public class StrictnessPerStubbingTest {

    MockitoSession mockito;
    @Mock IMethods mock;

    @Before
    public void before() {
        mockito =
                Mockito.mockitoSession()
                        .initMocks(this)
                        .strictness(Strictness.STRICT_STUBS)
                        .startMocking();
    }

    @Test
    public void potential_stubbing_problem() {
        // when
        when(mock.simpleMethod("1")).thenReturn("1");
        lenient().when(mock.differentMethod("2")).thenReturn("2");

        // then on lenient stubbing, we can call it with different argument:
        ProductionCode.differentMethod(mock, "200");

        // but on strict stubbing, we cannot:
        assertThatThrownBy(() -> ProductionCode.simpleMethod(mock, "100"))
                .isInstanceOf(PotentialStubbingProblem.class);
    }

    @Test
    public void doReturn_syntax() {
        // when
        lenient().doReturn("2").doReturn("3").when(mock).simpleMethod(1);

        // then on lenient stubbing, we can call it with different argument:
        ProductionCode.simpleMethod(mock, 200);

        // and stubbing works, too:
        assertEquals("2", mock.simpleMethod(1));
        assertEquals("3", mock.simpleMethod(1));
    }

    @Test
    public void doReturn_varargs_syntax() {
        // when
        lenient().doReturn("2", "3").when(mock).simpleMethod(1);

        // then on lenient stubbing, we can call it with different argument with no exception:
        ProductionCode.simpleMethod(mock, 200);

        // and stubbing works, too:
        assertEquals("2", mock.simpleMethod(1));
        assertEquals("3", mock.simpleMethod(1));
    }

    @Test
    public void doThrow_syntax() {
        // when
        lenient()
                .doThrow(IllegalArgumentException.class)
                .doThrow(IllegalStateException.class)
                .when(mock)
                .simpleMethod(1);

        // then on lenient stubbing, we can call it with different argument with no exception:
        ProductionCode.simpleMethod(mock, 200);

        // and stubbing works, too:
        assertThatThrownBy(() -> mock.simpleMethod(1))
                .isInstanceOf(IllegalArgumentException.class);

        // testing consecutive call:
        assertThatThrownBy(() -> mock.simpleMethod(1))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void doThrow_vararg_syntax() {
        // when
        lenient()
                .doThrow(IllegalArgumentException.class, IllegalStateException.class)
                .when(mock)
                .simpleMethod(1);

        // then on lenient stubbing, we can call it with different argument with no exception:
        ProductionCode.simpleMethod(mock, 200);

        // and stubbing works, too:
        assertThatThrownBy(() -> mock.simpleMethod(1))
                .isInstanceOf(IllegalArgumentException.class);

        // testing consecutive call:
        assertThatThrownBy(() -> mock.simpleMethod(1))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void doThrow_instance_vararg_syntax() {
        // when
        lenient()
                .doThrow(new IllegalArgumentException(), new IllegalStateException())
                .when(mock)
                .simpleMethod(1);

        // then on lenient stubbing, we can call it with different argument with no exception:
        ProductionCode.simpleMethod(mock, 200);

        // and stubbing works, too:
        assertThatThrownBy(() -> mock.simpleMethod(1))
                .isInstanceOf(IllegalArgumentException.class);

        // testing consecutive call:
        assertThatThrownBy(() -> mock.simpleMethod(1))
                .isInstanceOf(IllegalStateException.class);
    }

    static class Counter {
        int increment(int x) {
            return x + 1;
        }

        void scream(String message) {
            throw new RuntimeException(message);
        }
    }

    @Test
    public void doCallRealMethod_syntax() {
        // when
        Counter mock = mock(Counter.class);
        //TODO: removing lenient does not make the test fail
        lenient().doCallRealMethod().when(mock).increment(1);

        // then no exception and default return value if we call it with different arg:
        assertEquals(0, mock.increment(0));

        // and real method is called when using correct arg:
        assertEquals(2, mock.increment(1));
    }

    @Test
    public void doNothing_syntax() {
        // when
        final Counter spy = spy(Counter.class);
        //TODO: removing lenient does not make the test fail
        lenient().doNothing().when(spy).scream("1");

        // then no stubbing exception and real method is called if we call stubbed method with
        // different arg:
        assertThatThrownBy(() -> spy.scream("2"))
                .hasMessage("2");

        // and we do nothing when stubbing called with correct arg:
        spy.scream("1");
    }

    @Test
    public void doAnswer_syntax() {
        // when
        lenient().doAnswer(AdditionalAnswers.returnsFirstArg()).when(mock).simpleMethod("1");

        // then on lenient stubbing, we can call it with different argument:
        ProductionCode.simpleMethod(mock, "200");

        // and stubbing works, too:
        assertEquals("1", mock.simpleMethod("1"));
    }

    @Test
    public void unnecessary_stubbing() {
        // when
        when(mock.simpleMethod("1")).thenReturn("1");
        lenient().when(mock.differentMethod("2")).thenReturn("2");

        // then unnecessary stubbing flags method only on the strict stubbing:
        assertThatThrownBy(() -> mockito.finishMocking())
                .isInstanceOf(UnnecessaryStubbingException.class)
                .hasMessageContaining("1. -> ")
                // good enough to prove that we're flagging just one unnecessary stubbing:
                // TODO 792: this assertion is duplicated with StrictnessPerMockTest
                .isNot(TestBase.hasMessageContaining("2. ->"));
    }

    @Test
    public void unnecessary_stubbing_with_doReturn() {
        // when
        lenient().doReturn("2").when(mock).differentMethod("2");

        // then no exception is thrown:
        mockito.finishMocking();
    }

    @Test
    public void verify_no_more_invocations() {
        // when
        when(mock.simpleMethod("1")).thenReturn("1");
        lenient().when(mock.differentMethod("2")).thenReturn("2");

        // and:
        mock.simpleMethod("1");
        ProductionCode.differentMethod(mock, "200"); // <- different arg

        // then 'verifyNoMoreInteractions' flags the lenient stubbing (called with different arg)
        // and reports it with [?] in the exception message
        assertThatThrownBy(() -> verifyNoMoreInteractions(mock))
                .isInstanceOf(NoInteractionsWanted.class)
                .hasMessageContaining("1. ->")
                .hasMessageContaining("2. [?]->");
        // TODO 792: assertion duplicated with StrictnessPerMockTest
        // and we should use assertions based on content of the exception rather than the string
    }

    @Test
    public void bdd_potential_stubbing_problem() {
        // when
        given(mock.simpleMethod("1")).willReturn("1");
        leniently().given(mock.differentMethod("2")).willReturn("2");

        // then on lenient stubbing, we can call it with different argument:
        ProductionCode.differentMethod(mock, "200");

        // but on strict stubbing, we cannot:
        assertThatThrownBy(() -> ProductionCode.simpleMethod(mock, "100"))
                .isInstanceOf(PotentialStubbingProblem.class);
    }

    @Test
    public void bdd_willReturn_syntax() {
        // when
        leniently().willReturn("2").willReturn("3").given(mock).simpleMethod(1);

        // then on lenient stubbing, we can call it with different argument:
        ProductionCode.simpleMethod(mock, 200);

        // and stubbing works, too:
        assertEquals("2", mock.simpleMethod(1));
        assertEquals("3", mock.simpleMethod(1));
    }

    @Test
    public void bdd_willReturn_varargs_syntax() {
        // when
        leniently().willReturn("2", "3").given(mock).simpleMethod(1);

        // then on lenient stubbing, we can call it with different argument with no exception:
        ProductionCode.simpleMethod(mock, 200);

        // and stubbing works, too:
        assertEquals("2", mock.simpleMethod(1));
        assertEquals("3", mock.simpleMethod(1));
    }

    @Test
    public void bdd_willThrow_syntax() {
        // when
        leniently()
                .willThrow(IllegalArgumentException.class)
                .willThrow(IllegalStateException.class)
                .given(mock)
                .simpleMethod(1);

        // then on lenient stubbing, we can call it with different argument with no exception:
        ProductionCode.simpleMethod(mock, 200);

        // and stubbing works, too:
        assertThatThrownBy(() -> mock.simpleMethod(1))
                .isInstanceOf(IllegalArgumentException.class);

        // testing consecutive call:
        assertThatThrownBy(() -> mock.simpleMethod(1))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void bdd_willThrow_vararg_syntax() {
        // when
        leniently()
                .willThrow(IllegalArgumentException.class, IllegalStateException.class)
                .given(mock)
                .simpleMethod(1);

        // then on lenient stubbing, we can call it with different argument with no exception:
        ProductionCode.simpleMethod(mock, 200);

        // and stubbing works, too:
        assertThatThrownBy(() -> mock.simpleMethod(1))
                .isInstanceOf(IllegalArgumentException.class);

        // testing consecutive call:
        assertThatThrownBy(() -> mock.simpleMethod(1))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void bdd_willThrow_instance_vararg_syntax() {
        // when
        leniently()
                .willThrow(new IllegalArgumentException(), new IllegalStateException())
                .given(mock)
                .simpleMethod(1);

        // then on lenient stubbing, we can call it with different argument with no exception:
        ProductionCode.simpleMethod(mock, 200);

        // and stubbing works, too:
        assertThatThrownBy(() -> mock.simpleMethod(1))
                .isInstanceOf(IllegalArgumentException.class);

        // testing consecutive call:
        assertThatThrownBy(
                        new ThrowableAssert.ThrowingCallable() {
                            public void call() throws Throwable {
                                mock.simpleMethod(1);
                            }
                        })
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void bdd_willCallRealMethod_syntax() {
        // when
        Counter mock = mock(Counter.class);
        //TODO: removing 'leniently()' from here does not make this test fail.
        leniently().willCallRealMethod().given(mock).increment(1);

        // then no exception and default return value if we call it with different arg:
        assertEquals(0, mock.increment(0));

        // and real method is called when using correct arg:
        assertEquals(2, mock.increment(1));
    }

    @Test
    public void bdd_willDoNothing_syntax() {
        // when
        final Counter spy = spy(Counter.class);
        //TODO: removing 'leniently()' from here does not make this test fail.
        leniently().willDoNothing().given(spy).scream("1");

        // then no stubbing exception and real method is called if we call stubbed method with
        // different arg:
        assertThatThrownBy(() -> spy.scream("2"))
                .hasMessage("2");

        // and we do nothing when stubbing called with correct arg:
        spy.scream("1");
    }

    @Test
    public void bdd_willAnswer_syntax() {
        // when
        leniently().willAnswer(AdditionalAnswers.returnsFirstArg()).given(mock).simpleMethod("1");

        // then on lenient stubbing, we can call it with different argument:
        ProductionCode.simpleMethod(mock, "200");

        // and stubbing works, too:
        assertEquals("1", mock.simpleMethod("1"));
    }

    @Test
    public void bdd_unnecessary_stubbing() {
        // when
        when(mock.simpleMethod("1")).thenReturn("1");
        leniently().given(mock.differentMethod("2")).willReturn("2");

        // then unnecessary stubbing flags method only on the strict stubbing:
        assertThatThrownBy(() -> mockito.finishMocking())
                .isInstanceOf(UnnecessaryStubbingException.class)
                .hasMessageContaining("1. -> ")
                // good enough to prove that we're flagging just one unnecessary stubbing:
                // TODO 792: this assertion is duplicated with StrictnessPerMockTest
                .isNot(TestBase.hasMessageContaining("2. ->"));
    }

    @Test
    public void bdd_unnecessary_stubbing_with_doReturn() {
        // when
        leniently().willReturn("2").given(mock).differentMethod("2");

        // then no exception is thrown:
        mockito.finishMocking();
    }

    @Test
    public void bdd_verify_no_more_invocations() {
        // when
        when(mock.simpleMethod("1")).thenReturn("1");
        leniently().given(mock.differentMethod("2")).willReturn("2");

        // and:
        ProductionCode.simpleMethod(mock, "1");
        ProductionCode.differentMethod(mock, "200"); // <- different arg

        // then 'verifyNoMoreInteractions' flags the lenient stubbing (called with different arg)
        // and reports it with [?] in the exception message
        assertThatThrownBy(
                new ThrowableAssert.ThrowingCallable() {
                    @Override
                    public void call() throws Throwable {
                        verifyNoMoreInteractions(mock);
                    }
                })
                .isInstanceOf(NoInteractionsWanted.class)
                .hasMessageContaining("1. ->")
                .hasMessageContaining("2. [?]->");
        // TODO 792: assertion duplicated with StrictnessPerMockTest
        // and we should use assertions based on content of the exception rather than the string
    }

    @After
    public void after() {
        mockito.finishMocking();
    }
}
