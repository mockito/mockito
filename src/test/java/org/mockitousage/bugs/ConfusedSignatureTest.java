package org.mockito;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfusedSignatureTest {

    @Test
    public void should_mock_method_which_has_generic_return_type_in_superclass_and_concrete_one_in_interface() {
        Sub mock = Mockito.mock(Sub.class);
        // The following line resulted in
        // org.mockito.exceptions.misusing.MissingMethodInvocationException:
        // when() requires an argument which has to be 'a method call on a mock'.
        // Presumably confused by the interface/superclass signatures.
        Mockito.when(mock.getFoo()).thenReturn("Hello");

        assertThat(mock.getFoo()).isEqualTo("Hello");
    }

    public class Super<T> {
        private T value;

        public Super(T value) {
            this.value = value;
        }

        public T getFoo() { return value; }
    }

    public class Sub
            extends Super<String>
            implements iInterface {

        public Sub(String s) {
            super(s);
        }
    }

    public interface iInterface {
        String getFoo();
    }
}
