/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

import org.junit.Test;

public class ImplementationOfGenericAbstractMethodNotInvokedOnSpyTest {
    public abstract class GenericAbstract<T> {
        protected abstract String method_to_implement(T value);

        public String public_method(T value) {
            return method_to_implement(value);
        }
    }

    public class ImplementsGenericMethodOfAbstract<T extends Number> extends GenericAbstract<T> {
        @Override
        protected String method_to_implement(T value) {
            return "concrete value";
        }
    }

    @Test
    public void should_invoke_method_to_implement() {
        GenericAbstract<Number> spy = spy(new ImplementsGenericMethodOfAbstract<Number>());

        assertThat(spy.public_method(73L)).isEqualTo("concrete value");
    }
}
