/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.graalvm;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;

public final class GraalVMSubclassMockMakerTest {

    @Test
    public void test_subclass_mock_maker_with_simple_object() {
        DummyObject dummyMock = mock(DummyObject.class, withSettings());

        when(dummyMock.getValue()).thenReturn("mocked");

        assertEquals("mocked", dummyMock.getValue());
    }
}
