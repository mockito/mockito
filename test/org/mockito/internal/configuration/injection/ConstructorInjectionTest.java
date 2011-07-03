/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.FieldInitializer.ConstructorArgumentResolver;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Observer;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInjectionTest {

    @Mock private Observer observer;
    private ArgConstructor whatever;

    @Mock private ConstructorArgumentResolver resolver;
    private ConstructorInjection underTest;

    @Before
    public void initialize_dependencies() {
        underTest = new ConstructorInjection(resolver);
    }

    @Test
    public void should_do_the_trick_of_instantiating() throws Exception {
        given(resolver.resolveTypeInstances(Matchers.<Class<?>[]>anyVararg())).willReturn(new Object[] { observer });

        boolean result = underTest.process(field("whatever"), this, newSetOf(observer));

        assertTrue(result);
        assertNotNull(whatever);
    }

    private Set<Object> newSetOf(Object item) {
        HashSet<Object> mocks = new HashSet<Object>();
        mocks.add(item);
        return mocks;
    }

    private Field field(String fieldName) throws NoSuchFieldException {
        return this.getClass().getDeclaredField(fieldName);
    }

    private static class ArgConstructor {
        ArgConstructor(Observer observer) {}
    }
}
