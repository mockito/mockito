/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.basicapi;


import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockAccessTest {

    @Test
    public void shouldAllowStubbedMockReferenceAccess() throws Exception {
        Set<?> expectedMock = mock(Set.class);

        Set<?> returnedMock = when(expectedMock.isEmpty()).thenReturn(false).getMock();

        assertEquals(expectedMock, returnedMock);
    }

    @Test
    public void stubbedMockShouldWorkAsUsual() throws Exception {
        Set<?> returnedMock = when(mock(Set.class).isEmpty()).thenReturn(false, true).getMock();

        assertEquals(false, returnedMock.isEmpty());
        assertEquals(true, returnedMock.isEmpty());
    }
}
