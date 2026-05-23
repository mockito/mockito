/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.bugs;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class GenericArrayMockingTest {

    public abstract static class DaService<T, S> {
        public S doSmth(T o, Class<S> clazz) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public abstract static class HttpDaService<T, S> extends DaService<T, S[]> {}

    public static class HttpDaServiceImpl extends HttpDaService<Object, Object> {}

    @Mock HttpDaServiceImpl daService;

    public GenericArrayMockingTest() {
        MockitoAnnotations.openMocks(this); // Initializes @Mock
    }

    @Test
    public void when_doSmth_thenReturnObjectsArray() {
        when(daService.doSmth(any(), any())).thenReturn(new Object[4]);
        Object[] result = daService.doSmth(new Object(), Object[].class);
        assertEquals(4, result.length);
    }
}
