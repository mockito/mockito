/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoLambda.*;

public class MockitoLambdaSpyTest {

    @Test
    public void should_invoke_real_method_on_spy() {
        SpiedClass object = new SpiedClass();
        SpiedClass spy = spy(object);

        assertThat(spy.foo()).isEqualTo(5);
    }

    @Test
    public void should_invoke_stub_on_spy() {
        SpiedClass object = new SpiedClass();
        SpiedClass spy = spy(object);

        when(spy::foo).invokedWithNoArgs().thenReturn(3);

        assertThat(spy.foo()).isEqualTo(3);
    }

    /**
     * Shows how #500 can be resolved.
     */
    @Test
    public void should_not_throw_too_many_actual_invocations_on_spy() {
        VerifyExample spy = spy(new VerifyExample());
        when(spy::getSize).invokedWith(any(Collection.class)).thenReturn(11);

        assertThat(spy.getSizeSquared(Collections.emptyList())).isEqualTo(121);

        verify(spy::getSizeSquared).invokedWith(Collections.emptyList());

        verify(spy::getSize, times(2)).invokedWith(any(Collection.class));
    }

    private class VerifyExample {
        public int getSizeSquared(final Collection c) {
            return getSize(c) * getSize(c);
        }

        public int getSize(final Collection c) {
            return getSize2(c);
        }

        public int getSize2(final Collection c) {
            return c.size();
        }
    }

    class SpiedClass {
        int foo() {
            return 5;
        }
    }
}
