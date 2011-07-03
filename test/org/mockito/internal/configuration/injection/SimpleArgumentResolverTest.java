/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.configuration.injection;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimpleArgumentResolverTest {

    @Test
    public void should_return_object_matching_given_types() throws Exception {
        ConstructorInjection.SimpleArgumentResolver resolver =
                new ConstructorInjection.SimpleArgumentResolver(newSetOf(new HashSet(), new ByteArrayOutputStream(), new HashMap()));

        Object[] resolvedInstance = resolver.resolveTypeInstances(Set.class, Map.class, OutputStream.class);

        assertEquals(3, resolvedInstance.length);
        assertTrue(resolvedInstance[0] instanceof Set);
        assertTrue(resolvedInstance[1] instanceof Map);
        assertTrue(resolvedInstance[2] instanceof OutputStream);
    }

    @Test
    public void should_return_null_when_match_is_not_possible_on_given_types() throws Exception {
        ConstructorInjection.SimpleArgumentResolver resolver =
                new ConstructorInjection.SimpleArgumentResolver(newSetOf(new HashSet(), new ByteArrayOutputStream()));

        Object[] resolvedInstance = resolver.resolveTypeInstances(Set.class, Map.class, OutputStream.class);

        assertEquals(3, resolvedInstance.length);
        assertTrue(resolvedInstance[0] instanceof Set);
        assertNull(resolvedInstance[1]);
        assertTrue(resolvedInstance[2] instanceof OutputStream);
    }

    @Test
    public void should_return_null_when_types_are_primitives() throws Exception {
        ConstructorInjection.SimpleArgumentResolver resolver =
                new ConstructorInjection.SimpleArgumentResolver(newSetOf(new HashMap(), new TreeSet()));

        Object[] resolvedInstance = resolver.resolveTypeInstances(Set.class, Map.class, Boolean.class);

        assertEquals(3, resolvedInstance.length);
        assertTrue(resolvedInstance[0] instanceof Set);
        assertTrue(resolvedInstance[1] instanceof Map);
        assertNull(resolvedInstance[2]);
    }

    private Set<Object> newSetOf(Object... objects) {
        return new HashSet<Object>(Arrays.asList(objects));
    }


}
