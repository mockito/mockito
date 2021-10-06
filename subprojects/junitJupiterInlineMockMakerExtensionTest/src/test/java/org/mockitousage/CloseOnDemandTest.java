/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests to assert that ScopedMock mocks are properly closed on scope exit.
 */
@ExtendWith(MockitoExtension.class)
class CloseOnDemandTest {

    @Test
    void create_mocked_static_1(@Mock MockedStatic<String> mockedStatic) {
        assertNotNull(mockedStatic);
    }

    @Test
    void create_mocked_static_2(@Mock MockedStatic<String> mockedStatic) {
        assertNotNull(mockedStatic);
    }

    @Test
    void create_mocked_construction_1(@Mock MockedConstruction<String> mockConstruction) {
        assertNotNull(mockConstruction);
    }

    @Test
    void create_mocked_construction_2(@Mock MockedConstruction<String> mockConstruction) {
        assertNotNull(mockConstruction);
    }
}
