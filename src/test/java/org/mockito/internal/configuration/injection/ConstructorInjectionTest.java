/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Observer;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInjectionTest {

    @Mock private Observer observer;
    public ArgConstructor whatever;
    public NoArgsConstructor noArgsConstructor;
    public ThrowingConstructor throwingConstructor;

    protected ConstructorInjection underTest;

    @Before
    public void initialize_dependencies() {
        underTest = new ConstructorInjection();
    }

    @Test
    public void should_do_the_trick_of_instantiating() throws Exception {
        boolean result = underTest.process(field("whatever"), this, newSetOf(observer));

        assertTrue(result);
        assertNotNull(whatever);
        assertThat(whatever.observer).isEqualTo(observer);
    }

    @Test
    public void should_instantiate_with_null_arg() throws Exception {
        boolean result = underTest.process(field("whatever"), this, new HashSet<>());

        assertThat(result).isTrue();
        assertThat(whatever).isNotNull();
        assertThat(whatever.observer).isNull();
    }

    @Test
    public void should_not_instantiate_no_args_constructor() throws Exception {
        boolean result = underTest.process(field("noArgsConstructor"), this, newSetOf(observer));

        assertThat(result).isFalse();
        assertThat(noArgsConstructor).isNull();
    }

    @Test
    public void should_fail_to_instantiate_throwing_constructor() throws Exception {
        try {
            underTest.process(field("throwingConstructor"), this, newSetOf(observer));
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage())
                    .contains("Cannot instantiate")
                    .contains("throwingConstructor")
                    .contains("ThrowingConstructor")
                    .contains("business logic failed");
        }
        assertThat(throwingConstructor).isNull();
    }

    private Set<Object> newSetOf(Object item) {
        HashSet<Object> mocks = new HashSet<>();
        mocks.add(item);
        return mocks;
    }

    protected Field field(String fieldName) throws NoSuchFieldException {
        return this.getClass().getField(fieldName);
    }

    private static class ArgConstructor {
        private final Observer observer;

        ArgConstructor(Observer observer) {
            this.observer = observer;
        }
    }

    private static class NoArgsConstructor {
        NoArgsConstructor() {}
    }

    private static class ThrowingConstructor {
        ThrowingConstructor(Observer observer) throws Exception {
            throw new NullPointerException("business logic failed");
        }
    }
}
