/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.validateMockitoUsage;

import org.junit.After;
import org.junit.Test;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;

import java.util.Map;

public class ArgumentCaptorTest {

    /**
     * Clean up the internal Mockito-Stubbing state
     */
    @After
    public void tearDown() {
        try {
            validateMockitoUsage();
        } catch (InvalidUseOfMatchersException ignore) {
        }
    }

    @Test
    public void tell_handy_return_values_to_return_value_for() throws Exception {

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        assertThat(captor.capture()).isNull();
    }

    @Test
    public void getCaptorType_returns_captor_type() throws Exception {
        class Foo {}

        class Bar {}

        ArgumentCaptor<Foo> fooCaptor = ArgumentCaptor.forClass(Foo.class);
        ArgumentCaptor<Bar> barCaptor = ArgumentCaptor.forClass(Bar.class);

        assertThat(fooCaptor.getCaptorType()).isEqualTo(Foo.class);
        assertThat(barCaptor.getCaptorType()).isEqualTo(Bar.class);
    }

    @Test
    public void captor_calls_forClass_with_the_inferred_argument() throws Exception {
        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.captor();
        assertThat(captor.getCaptorType()).isEqualTo(Map.class);
    }

    @Test
    public void captor_called_with_explicit_varargs_is_invalid() throws Exception {
        // Test passing a single argument.
        assertThatThrownBy(() -> ArgumentCaptor.captor(1234L))
                .isInstanceOf(IllegalArgumentException.class);
        // Test passing multiple arguments.
        assertThatThrownBy(() -> ArgumentCaptor.captor("this shouldn't", "be here"))
                .isInstanceOf(IllegalArgumentException.class);
        // Test passing a totally null varargs array.
        assertThatThrownBy(() -> ArgumentCaptor.<String>captor((String[]) null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
