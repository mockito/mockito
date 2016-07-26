package org.mockito;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfusedSignatureTest {

    @Test
    public void should_work() {
        Sub mock = Mockito.mock(Sub.class);
        // The following line results in
        // org.mockito.exceptions.misusing.MissingMethodInvocationException:
        // when() requires an argument which has to be 'a method call on a mock'.
        // Presumably confused by the interface/superclass signatures.
        Mockito.when(mock.getFoo()).thenReturn("Hello");

        assertThat(mock.getFoo()).isEqualTo("Hello");
    }

    private class Super<T> {
        private T value;

        Super(T value) {
            this.value = value;
        }

        public T getFoo() { return value; }
    }

    private class Sub
            extends Super<String>
            implements iInterface {

        Sub(String s) {
            super(s);
        }
    }

    interface iInterface {
        String getFoo();
    }
}
