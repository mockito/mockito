/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
        Field field = field("whatever");
        boolean result = underTest.process(field, this, newMapOf(field, observer));

        assertTrue(result);
        assertNotNull(whatever);
    }

    private Map<Field, Object> newMapOf(Field field, Object item) {
        Map<Field, Object> mocks = new HashMap<>();
        mocks.put(field, item);
        return mocks;
    }

    private Field field(String fieldName) throws NoSuchFieldException {
        return this.getClass().getDeclaredField(fieldName);
    }

    private static class ArgConstructor {
        ArgConstructor(Observer observer) {}
    }
}
