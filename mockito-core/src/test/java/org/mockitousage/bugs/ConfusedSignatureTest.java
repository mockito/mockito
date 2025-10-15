/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ConfusedSignatureTest {

    @Test
    public void
            should_mock_method_which_has_generic_return_type_in_superclass_and_concrete_one_in_interface() {
        Sub mock = mock(Sub.class);
        // The following line resulted in
        // org.mockito.exceptions.misusing.MissingMethodInvocationException:
        // when() requires an argument which has to be 'a method call on a mock'.
        // Presumably confused by the interface/superclass signatures.
        when(mock.getFoo()).thenReturn("Hello");

        assertThat(mock.getFoo()).isEqualTo("Hello");
    }

    public class Super<T> {
        private T value;

        public Super(T value) {
            this.value = value;
        }

        public T getFoo() {
            return value;
        }
    }

    public class Sub extends Super<String> implements iInterface {

        public Sub(String s) {
            super(s);
        }
    }

    public interface iInterface {
        String getFoo();
    }
}
