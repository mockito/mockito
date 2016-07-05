package org.mockitousage.bugs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalMatchers.leq;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;

public class CompareMatcherTest {
    private static final Object NOT_A_COMPARABLE = new Object();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public IMethods mock;

    /**
     * Should not throw an {@link NullPointerException}
     * 
     * @see Bug-ID https://github.com/mockito/mockito/issues/457
     */
    @Test
    public void compareNullArgument() {
        final IMethods mock = mock(IMethods.class);

        when(mock.forInteger(leq(5))).thenReturn("");

        assertThat(mock.forInteger(null)).isNull();// a default value must be returned
    }

    /**
     * Should not throw an {@link ClassCastException}
     */
    @Test
    public void compareToNonCompareable() {
        when(mock.forObject(leq(5))).thenReturn("");

        assertThat(mock.forObject(NOT_A_COMPARABLE)).isNull();// a default value must be returned
    }

    /**
     * Should not throw an {@link ClassCastException}
     */
    @Test
    public void compareToNull() {
        when(mock.forInteger(leq((Integer) null))).thenReturn("");

        assertThat(mock.forInteger(null)).isNull();// a default value must be returned
    }

    /**
     * Should not throw an {@link ClassCastException}
     */
    @Test
    public void compareToStringVsInt() {
        when(mock.forObject(startsWith("Hello"))).thenReturn("");

        assertThat(mock.forObject(123)).isNull();// a default value must be returned
    }
    
    @Test
    public void compareToIntVsString() throws Exception {
        when(mock.forObject(leq(5))).thenReturn("");
        
        mock.forObject("abc");
    }

    @Test
    public void matchesOverloadsMustBeIgnored() {
        class TestMatcher implements ArgumentMatcher<Integer> {
            @Override
            public boolean matches(Integer arg) {
                return false;
            }

            @SuppressWarnings("unused")
            public boolean matches(Date arg) {
                throw new UnsupportedOperationException();
            }

            @SuppressWarnings("unused")
            public boolean matches(Integer arg, Void v) {
                throw new UnsupportedOperationException();
            }

        }

        when(mock.forObject(argThat(new TestMatcher()))).thenReturn("x");

        assertThat(mock.forObject(123)).isNull();
    }

    @Test
    public void matchesWithSubTypeExtendingGenericClass() {
        abstract class GenericMatcher<T> implements ArgumentMatcher<T> {}
        class TestMatcher extends GenericMatcher<Integer> {
            @Override
            public boolean matches(Integer argument) {
                return false;
            }
        }
        when(mock.forObject(argThat(new TestMatcher()))).thenReturn("x");

        assertThat(mock.forObject(123)).isNull();
    }

    @Test
    public void matchesWithSubTypeGenericMethod() {
        class GenericMatcher<T> implements ArgumentMatcher<T> {
            @Override
            public boolean matches(T argument) {
                return false;
            }
        }
        when(mock.forObject(argThat(new GenericMatcher<Integer>()))).thenReturn("x");

        assertThat(mock.forObject(123)).isNull();
    }

}