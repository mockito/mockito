package org.mockitousage.bugs;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

/**
 * @see <a href="https://github.com/mockito/mockito/issues/1279">Issue #1279</a>
 */
public class MockitoStubbedCallInAnswerTest {

    @Test
    public void directInvocationInAnswer() throws Exception {
        final Foo foo = mock(Foo.class);
        final Bar bar = mock(Bar.class);

        when(foo.doInt()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return bar.doInt();
            }
        });
        assertEquals(0, foo.doInt());
        assertEquals(0, bar.doInt());

        when(foo.doInt()).thenReturn(1);
        assertEquals(1, foo.doInt());
        assertEquals(0, bar.doInt());
    }

    @Test
    public void useOtherStubbedMethodReturnTypeInLogic() throws Exception {
        final Foo foo = mock(Foo.class);
        final Bar bar = mock(Bar.class);

        when(foo.doString()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return String.valueOf(bar.doInt());
            }
        });
        assertEquals("0", foo.doString());

        when(foo.doString()).thenReturn("");
        assertEquals("", foo.doString());
    }

    @Test
    public void doesNotProduceStackOverflowWhenStubbingTwice() throws Exception {
        final Foo foo = mock(Foo.class);
        final Bar bar = mock(Bar.class);

        when(foo.doInt()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return bar.doInt();
            }
        });
        assertEquals(0, foo.doInt());

        when(foo.doInt()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return bar.doInt() + 1;
            }
        });
        assertEquals(1, foo.doInt());
    }

    interface Foo {
        String doString();
        int doInt();
    }

    interface Bar {
        int doInt();
    }
}
