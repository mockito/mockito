/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.testbundle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Regression test for <a href="https://github.com/mockito/mockito/issues/2694">#2694</a>:
 * mocking a class whose superclass is defined by another bundle (class loader) and
 * references a type that the mocking bundle cannot see must not fail with
 * {@link NoClassDefFoundError}.
 *
 * <p>Note: this test deliberately never touches {@code ForeignType} directly (not even
 * via a method call), so the test bundle has no import for its package. The reflective
 * invocation below only uses {@code java.lang} types.
 */
public class MockSubclassWithForeignSuperclassTest {

    @Test
    public void mock_subclass_whose_superclass_references_invisible_type() throws Exception {
        SubclassOfForeignSuperclass mock = mock(SubclassOfForeignSuperclass.class);

        when(mock.name()).thenReturn("mocked");
        assertEquals("mocked", mock.name());

        // The generated mock redeclares the inherited setForeignType method; resolving it
        // through the mock's own class loader must work as well.
        Class<?> foreignType =
                Class.forName(
                        "org.mockito.osgitest.depbundle.ForeignType",
                        false,
                        SubclassOfForeignSuperclass.class
                                .getSuperclass()
                                .getClassLoader());
        Object result =
                mock.getClass()
                        .getMethod("setForeignType", foreignType)
                        .invoke(mock, new Object[] {null});
        assertNull(result);
    }
}
