/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

public class MockedAnnotationInterfaceWithDefaultsTest extends TestBase {

    public @interface AnnotationInterface {
        String strAttWithDefault() default "default";
        String strAttWithoutDefault();
        String[] strArrAtt() default { "default1", "default2" };
        int intAtt() default 1;
        long longAtt() default 2L;
    }

    @Mock
    AnnotationInterface mockedAnnotationInterface;

    @Test
    public void shouldMockDefaultValuesFromAnnoationInterface() throws Exception {

        assertEquals("default", mockedAnnotationInterface.strAttWithDefault());
        assertNull(mockedAnnotationInterface.strAttWithoutDefault());
        assertArrayEquals(new String[] { "default1", "default2" }, mockedAnnotationInterface.strArrAtt());
        assertArrayEquals(new String[] { "default1", "default2" }, mockedAnnotationInterface.strArrAtt());
        assertEquals(1, mockedAnnotationInterface.intAtt());
        assertEquals(2L, mockedAnnotationInterface.longAtt());
    }
}
