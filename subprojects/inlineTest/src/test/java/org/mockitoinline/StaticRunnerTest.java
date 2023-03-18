/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public final class StaticRunnerTest {

    @Mock
    private MockedStatic<UUID> mock;

    @Test
    public void runs() {
        mock.when(UUID::randomUUID).thenReturn(new UUID(123, 456));
        assertEquals(UUID.randomUUID(), new UUID(123, 456));
    }
}
