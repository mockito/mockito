/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Observer;
import java.util.Set;

import org.junit.After;
import org.junit.Test;
import org.mockito.internal.configuration.injection.MockInjection;

@SuppressWarnings("unchecked")
public class MockInjectionTest {

    private AnObjectWithConstructor withConstructor;
    private AnObjectWithoutConstructor withoutConstructor;

    @After
    public void reset() throws Exception {
        withConstructor = null;
        withoutConstructor = null;
    }

    @Test
    public void should_not_allow_null_on_field() {
        assertThatThrownBy(
                        () -> {
                            MockInjection.onField((Field) null, this);
                        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("item in fields should not be null");
    }

    @Test
    public void should_not_allow_null_on_fields() {
        assertThatThrownBy(
                        () -> {
                            MockInjection.onFields((Set<Field>) null, this);
                        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("fields should not be null");
    }

    @Test
    public void should_not_allow_null_on_instance_owning_the_field() {
        assertThatThrownBy(
                        () -> {
                            MockInjection.onField(field("withConstructor"), null);
                        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("fieldOwner should not be null");
    }

    @Test
    public void should_not_allow_null_on_mocks() {
        assertThatThrownBy(
                        () -> {
                            MockInjection.onField(field("withConstructor"), this).withMocks(null);
                        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("mocks should not be null");
    }

    @Test
    public void can_try_constructor_injection() throws Exception {
        MockInjection.onField(field("withConstructor"), this)
                .withMocks(oneSetMock())
                .tryConstructorInjection()
                .apply();

        assertThat(withConstructor.initializedWithConstructor).isTrue();
    }

    @Test
    public void should_not_fail_if_constructor_injection_is_not_possible() throws Exception {
        MockInjection.onField(field("withoutConstructor"), this)
                .withMocks(otherKindOfMocks())
                .tryConstructorInjection()
                .apply();

        assertThat(withoutConstructor).isNull();
    }

    @Test
    public void can_try_property_or_setter_injection() throws Exception {
        MockInjection.onField(field("withoutConstructor"), this)
                .withMocks(oneSetMock())
                .tryPropertyOrFieldInjection()
                .apply();

        assertThat(withoutConstructor.theSet).isNotNull();
    }

    @Test
    public void should_not_fail_if_property_or_field_injection_is_not_possible() throws Exception {
        MockInjection.onField(field("withoutConstructor"), this)
                .withMocks(otherKindOfMocks())
                .tryPropertyOrFieldInjection()
                .apply();

        assertThat(withoutConstructor.theSet).isNull();
    }

    private Set oneSetMock() {
        return Collections.singleton(mock(Set.class));
    }

    private Set otherKindOfMocks() {
        return Collections.singleton(mock(Observer.class));
    }

    private Field field(String field) throws NoSuchFieldException {
        return getClass().getDeclaredField(field);
    }

    public static class AnObjectWithConstructor {
        public boolean initializedWithConstructor = false;

        public AnObjectWithConstructor(Set<String> strings) {
            initializedWithConstructor = true;
        }
    }

    public static class AnObjectWithoutConstructor {
        private Set<?> theSet;
    }
}
