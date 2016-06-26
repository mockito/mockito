/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util.reflection;


import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldInitializer.ConstructorArgumentResolver;
import org.mockito.internal.util.reflection.FieldInitializer.ParameterizedConstructorInstantiator;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Observer;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class ParameterizedConstructorInstantiatorTest {

    private Set<?> whateverForNow;
    private OneConstructor withOneConstructor;
    private MultipleConstructor withMultipleConstructor;
    private NoArgConstructor withNoArgConstructor;
    private ThrowingConstructor withThrowingConstructor;
    private VarargConstructor withVarargConstructor;

    @After
    public void ensure_instances_to_create_are_null() {
        withMultipleConstructor = null;
        withOneConstructor = null;
        withNoArgConstructor = null;
        withThrowingConstructor = null;
        withVarargConstructor = null;
    }

    @Mock private ConstructorArgumentResolver resolver;

    @Test
    public void should_be_created_with_an_argument_resolver() throws Exception {
        new ParameterizedConstructorInstantiator(this, field("whateverForNow"), resolver);
    }

    @Test
    public void should_fail_if_no_parameterized_constructor_found___excluding_inner_and_others_kind_of_types() throws Exception {
        try {
            new ParameterizedConstructorInstantiator(this, field("withNoArgConstructor"), resolver).instantiate();
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage()).contains("no parameterized constructor").contains("withNoArgConstructor").contains("NoArgConstructor");
        }
    }

    @Test
    public void should_instantiate_type_if_resolver_provide_matching_types() throws Exception {
        Observer observer = mock(Observer.class);
        Map map = mock(Map.class);
        given(resolver.resolveTypeInstances(Matchers.<Class<?>[]>anyVararg())).willReturn(new Object[]{ observer, map });

        new ParameterizedConstructorInstantiator(this, field("withMultipleConstructor"), resolver).instantiate();

        assertNotNull(withMultipleConstructor);
        assertNotNull(withMultipleConstructor.observer);
        assertNotNull(withMultipleConstructor.map);
    }

    @Test
    public void should_fail_if_an_argument_instance_type_do_not_match_wanted_type() throws Exception {
        Observer observer = mock(Observer.class);
        Set<?> wrongArg = mock(Set.class);
        given(resolver.resolveTypeInstances(Matchers.<Class<?>[]>anyVararg())).willReturn(new Object[]{ observer, wrongArg });

        try {
            new ParameterizedConstructorInstantiator(this, field("withMultipleConstructor"), resolver).instantiate();
            fail();
        } catch (MockitoException e) {
            assertThat(e.getMessage()).contains("argResolver").contains("incorrect types");
        }
    }

    @Test
    public void should_report_failure_if_constructor_throws_exception() throws Exception {
        given(resolver.resolveTypeInstances(Matchers.<Class<?>[]>anyVararg())).willReturn(new Object[]{ null });

        try {
            new ParameterizedConstructorInstantiator(this, field("withThrowingConstructor"), resolver).instantiate();
            fail();
        } catch (MockitoException e) {
            assertThat(e.getMessage()).contains("constructor").contains("raised an exception");
        }
    }

    @Test
    public void should_instantiate_type_with_vararg_constructor() throws Exception {
        Observer[] vararg = new Observer[] {  };
        given(resolver.resolveTypeInstances(Matchers.<Class<?>[]>anyVararg())).willReturn(new Object[]{ "", vararg});

        new ParameterizedConstructorInstantiator(this, field("withVarargConstructor"), resolver).instantiate();

        assertNotNull(withVarargConstructor);
    }

    private Field field(String fieldName) throws NoSuchFieldException {
        Field field = this.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    private static class NoArgConstructor {
        NoArgConstructor() { }
    }

    private static class OneConstructor {
        public OneConstructor(Observer observer) { }
    }

    private static class ThrowingConstructor {
        public ThrowingConstructor(Observer observer) throws IOException { throw new IOException(); }
    }

    private static class MultipleConstructor extends OneConstructor {
        Observer observer;
        Map map;

        public MultipleConstructor(Observer observer) { this(observer, null); }
        public MultipleConstructor(Observer observer, Map map) {
            super(observer);
            this.observer = observer;
            this.map = map;
        }
    }

    private static class VarargConstructor {
        VarargConstructor(String whatever, Observer... observers) { }
    }
}
