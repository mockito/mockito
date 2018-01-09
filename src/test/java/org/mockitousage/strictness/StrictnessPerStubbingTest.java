package org.mockitousage.strictness;

import org.assertj.core.api.ThrowableAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class StrictnessPerStubbingTest {

    MockitoSession mockito;
    @Mock IMethods mock;

    @Before
    public void before() {
        mockito = Mockito.mockitoSession().initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();
    }

    @Test
    public void potential_stubbing_problem() {
        //when
        when(mock.simpleMethod("1")).thenReturn("1");
        //TODO 792: consider renaming lenient() -> optional()
        lenient().when(mock.differentMethod("2")).thenReturn("2");

        //then on lenient stubbing, we can call it with different argument:
        mock.differentMethod("200");

        //but on strict stubbing, we cannot:
        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                mock.simpleMethod("100");
            }
        }).isInstanceOf(PotentialStubbingProblem.class);
    }

    @Test
    public void doReturn_syntax() {
        //when
        lenient().doReturn("2").doReturn("3")
            .when(mock).simpleMethod(1);

        //then on lenient stubbing, we can call it with different argument:
        mock.differentMethod("200");

        //and stubbing works, too:
        assertEquals("2", mock.simpleMethod(1));
        assertEquals("3", mock.simpleMethod(1));
    }

    @Test
    public void doReturn_varargs_syntax() {
        //when
        lenient().doReturn("2", "3")
            .when(mock).simpleMethod(1);

        //then on lenient stubbing, we can call it with different argument with no exception:
        mock.differentMethod("200");

        //and stubbing works, too:
        assertEquals("2", mock.simpleMethod(1));
        assertEquals("3", mock.simpleMethod(1));
    }

    static class Counter {
        int increment(int x) {
            return x + 1;
        }
    }

    @Test
    public void doCallRealMethod_syntax() {
        //when
        Counter mock = mock(Counter.class);
        lenient().doCallRealMethod().when(mock).increment(1);

        //then no exception and default return value if we call it with different arg:
        assertEquals(0, mock.increment(0));

        //and real method is called when using correct arg:
        assertEquals(2, mock.increment(1));
    }

    @Test
    public void unnecessary_stubbing() {
        //when
        when(mock.simpleMethod("1")).thenReturn("1");
        lenient().when(mock.differentMethod("2")).thenReturn("2");

        //then unnecessary stubbing flags method only on the strict stubbing:
        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                mockito.finishMocking();
            }
        }).isInstanceOf(UnnecessaryStubbingException.class)
            .hasMessageContaining("1. -> ")
            //good enough to prove that we're flagging just one unnecessary stubbing:
            //TODO 792: this assertion is duplicated with StrictnessPerMockTest
            .isNot(TestBase.hasMessageContaining("2. ->"));
    }

    @Test
    public void verify_no_more_invocations() {
        //when
        when(mock.simpleMethod("1")).thenReturn("1");
        lenient().when(mock.differentMethod("2")).thenReturn("2");

        //and:
        mock.simpleMethod("1");
        mock.differentMethod("200"); // <- different arg

        //then 'verifyNoMoreInteractions' flags the lenient stubbing (called with different arg)
        //and reports it with [?] in the exception message
        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                verifyNoMoreInteractions(mock);
            }
        }).isInstanceOf(NoInteractionsWanted.class)
            .hasMessageContaining("1. ->")
            .hasMessageContaining("2. [?]->");
            //TODO 792: assertion duplicated with StrictnessPerMockTest
            // and we should use assertions based on content of the exception rather than the string
    }

    @After
    public void after() {
        mockito.finishMocking();
    }
}
