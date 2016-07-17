/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Observer;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInjectionTest {

    @Mock private Observer observer;
    private ArgConstructor whatever;

    private ConstructorInjection underTest;

    @Before
    public void initialize_dependencies() {
        underTest = new ConstructorInjection();
    }

    @Test
    public void should_do_the_trick_of_instantiating() throws Exception {
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
