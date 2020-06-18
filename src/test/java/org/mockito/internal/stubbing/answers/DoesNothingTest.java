/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.stubbing.answers.DoesNothing.doesNothing;
import static org.mockitoutil.TestBase.getLastInvocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.Invocation;
import org.mockitousage.IMethods;

public class DoesNothingTest {

    private IMethods mock;
    private Invocation invocation_Void;
    private Invocation invocation_void;
    private Invocation invocation_String;

    @Before
    public void init() {
        mock = mock(IMethods.class);

        mock.voidMethod();
        invocation_Void = getLastInvocation();

        mock.voidReturningMethod();
        invocation_void = getLastInvocation();

        mock.simpleMethod();
        invocation_String = getLastInvocation();
    }

    @Test
    public void answer_returnsNull() throws Throwable {
        assertThat(doesNothing().answer(invocation_Void)).isNull();
        assertThat(doesNothing().answer(invocation_void)).isNull();
        assertThat(doesNothing().answer(invocation_String)).isNull();
    }

    @Test(expected = MockitoException.class)
    public void validateFor_nonVoidReturnType_shouldFail() {
        doesNothing().validateFor(invocation_String);
    }

    @Test
    public void validateFor_voidReturnType_shouldPass() {
        doesNothing().validateFor(invocation_void);
    }

    @Test
    public void validateFor_voidObjectReturnType() throws Throwable {
        doesNothing().validateFor(invocation_Void);
    }

    @Test
    public void answer_returns_null_for_generic_parameter() {
        SubclassWithGenericParameter mock = mock(SubclassWithGenericParameter.class);
        doNothing().when(mock).methodReturningT();
    }

    static class SuperClassWithGenericParameter<T> {
        T methodReturningT() {
            return null;
        }
    }

    static class SubclassWithGenericParameter extends SuperClassWithGenericParameter<Void> {}
}
