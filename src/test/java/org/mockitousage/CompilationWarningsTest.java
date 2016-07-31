package org.mockitousage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.BDDMockito.*;


public class CompilationWarningsTest {

    @Before
    public void pay_attention_to_compilation_warnings_and_JDK_version() {
    }

    @Test
    public void no_warnings_for_most_common_api() throws Exception {
        doReturn(null).when(mock(IMethods.class)).objectReturningMethodNoArgs();
        doReturn("a", 12).when(mock(IMethods.class)).objectReturningMethodNoArgs();
        doReturn(1000).when(mock(IMethods.class)).objectReturningMethodNoArgs();
        doThrow(new NullPointerException()).when(mock(IMethods.class)).objectReturningMethodNoArgs();
        doThrow(new NullPointerException(), new IllegalArgumentException()).when(mock(IMethods.class)).objectReturningMethodNoArgs();
        doThrow(NullPointerException.class).when(mock(IMethods.class)).objectReturningMethodNoArgs();

        doAnswer(ignore()).doReturn(null).when(mock(IMethods.class)).objectReturningMethodNoArgs();
        doAnswer(ignore()).doReturn("a", 12).when(mock(IMethods.class)).objectReturningMethodNoArgs();
        doAnswer(ignore()).doReturn(1000).when(mock(IMethods.class)).objectReturningMethodNoArgs();
        doAnswer(ignore()).doThrow(new NullPointerException()).when(mock(IMethods.class)).objectReturningMethodNoArgs();
        doAnswer(ignore()).doThrow(new NullPointerException(), new IllegalArgumentException()).when(mock(IMethods.class)).objectReturningMethodNoArgs();
        doAnswer(ignore()).doThrow(NullPointerException.class).when(mock(IMethods.class)).objectReturningMethodNoArgs();

        when(mock(IMethods.class).objectReturningMethodNoArgs()).thenReturn(null);
        when(mock(IMethods.class).objectReturningMethodNoArgs()).thenReturn("a", 12L);
        when(mock(IMethods.class).objectReturningMethodNoArgs()).thenReturn(1000);
        when(mock(IMethods.class).objectReturningMethodNoArgs()).thenThrow(new NullPointerException());
        when(mock(IMethods.class).objectReturningMethodNoArgs()).thenThrow(new NullPointerException(), new IllegalArgumentException());
        when(mock(IMethods.class).objectReturningMethodNoArgs()).thenThrow(NullPointerException.class);

        when(mock(IMethods.class).objectReturningMethodNoArgs()).then(ignore()).thenReturn(null);
        when(mock(IMethods.class).objectReturningMethodNoArgs()).then(ignore()).thenReturn("a", 12L);
        when(mock(IMethods.class).objectReturningMethodNoArgs()).then(ignore()).thenReturn(1000);
        when(mock(IMethods.class).objectReturningMethodNoArgs()).then(ignore()).thenThrow(new NullPointerException());
        when(mock(IMethods.class).objectReturningMethodNoArgs()).then(ignore()).thenThrow(new NullPointerException(), new IllegalArgumentException());
        when(mock(IMethods.class).objectReturningMethodNoArgs()).then(ignore()).thenThrow(NullPointerException.class);

        willReturn(null).given(mock(IMethods.class)).objectReturningMethodNoArgs();
        willReturn("a", 12).given(mock(IMethods.class)).objectReturningMethodNoArgs();
        willReturn(1000).given(mock(IMethods.class)).objectReturningMethodNoArgs();
        willThrow(new NullPointerException()).given(mock(IMethods.class)).objectReturningMethodNoArgs();
        willThrow(new NullPointerException(), new IllegalArgumentException()).given(mock(IMethods.class)).objectReturningMethodNoArgs();
        willThrow(NullPointerException.class).given(mock(IMethods.class)).objectReturningMethodNoArgs();

        willAnswer(ignore()).willReturn(null).given(mock(IMethods.class)).objectReturningMethodNoArgs();
        willAnswer(ignore()).willReturn("a", 12).given(mock(IMethods.class)).objectReturningMethodNoArgs();
        willAnswer(ignore()).willReturn(1000).given(mock(IMethods.class)).objectReturningMethodNoArgs();
        willAnswer(ignore()).willThrow(new NullPointerException()).given(mock(IMethods.class)).objectReturningMethodNoArgs();
        willAnswer(ignore()).willThrow(new NullPointerException(), new IllegalArgumentException()).given(mock(IMethods.class)).objectReturningMethodNoArgs();
        willAnswer(ignore()).willThrow(NullPointerException.class).given(mock(IMethods.class)).objectReturningMethodNoArgs();

        given(mock(IMethods.class).objectReturningMethodNoArgs()).willReturn(null);
        given(mock(IMethods.class).objectReturningMethodNoArgs()).willReturn("a", 12L);
        given(mock(IMethods.class).objectReturningMethodNoArgs()).willReturn(1000);
        given(mock(IMethods.class).objectReturningMethodNoArgs()).willThrow(new NullPointerException());
        given(mock(IMethods.class).objectReturningMethodNoArgs()).willThrow(new NullPointerException(), new IllegalArgumentException());
        given(mock(IMethods.class).objectReturningMethodNoArgs()).willThrow(NullPointerException.class);

        given(mock(IMethods.class).objectReturningMethodNoArgs()).will(ignore()).willReturn(null);
        given(mock(IMethods.class).objectReturningMethodNoArgs()).will(ignore()).willReturn("a", 12L);
        given(mock(IMethods.class).objectReturningMethodNoArgs()).will(ignore()).willReturn(1000);
        given(mock(IMethods.class).objectReturningMethodNoArgs()).will(ignore()).willThrow(new NullPointerException());
        given(mock(IMethods.class).objectReturningMethodNoArgs()).will(ignore()).willThrow(new NullPointerException(), new IllegalArgumentException());
        given(mock(IMethods.class).objectReturningMethodNoArgs()).will(ignore()).willThrow(NullPointerException.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void heap_pollution_JDK7plus_warning_avoided_BUT_now_unchecked_generic_array_creation_warnings_ON_JDK5plus_environment() throws Exception {
        doThrow(NullPointerException.class, IllegalArgumentException.class).when(mock(IMethods.class)).objectReturningMethodNoArgs();
        when(mock(IMethods.class).objectReturningMethodNoArgs()).thenThrow(NullPointerException.class, IllegalArgumentException.class);
        doAnswer(ignore()).doThrow(NullPointerException.class, IllegalArgumentException.class).when(mock(IMethods.class)).objectReturningMethodNoArgs();

        willThrow(NullPointerException.class, IllegalArgumentException.class).given(mock(IMethods.class)).objectReturningMethodNoArgs();
        given(mock(IMethods.class).objectReturningMethodNoArgs()).willThrow(NullPointerException.class, IllegalArgumentException.class);
        willAnswer(ignore()).willThrow(NullPointerException.class, IllegalArgumentException.class).given(mock(IMethods.class)).objectReturningMethodNoArgs();
    }

    @Test
    public void unchecked_confusing_null_argument_warnings() throws Exception {
        doReturn(null, (Object[]) null).when(mock(IMethods.class)).objectReturningMethodNoArgs();
        doAnswer(ignore()).doReturn(null, (Object[]) null).when(mock(IMethods.class)).objectReturningMethodNoArgs();
        when(mock(IMethods.class).objectReturningMethodNoArgs()).thenReturn(null, (Object[]) null);
        when(mock(IMethods.class).objectReturningMethodNoArgs()).then(ignore()).thenReturn(null, (Object[]) null);
        willReturn(null, (Object[]) null).given(mock(IMethods.class)).objectReturningMethodNoArgs();
        given(mock(IMethods.class).objectReturningMethodNoArgs()).willReturn(null, (Object[]) null);
        willAnswer(ignore()).willReturn(null, (Object[]) null).given(mock(IMethods.class)).objectReturningMethodNoArgs();
        given(mock(IMethods.class).objectReturningMethodNoArgs()).will(ignore()).willReturn(null, (Object[]) null);
    }

    private static Answer<?> ignore() {
        return new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
    }
}
