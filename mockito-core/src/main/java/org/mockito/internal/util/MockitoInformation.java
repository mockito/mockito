/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static org.mockito.internal.util.StringUtil.join;

public abstract class MockitoInformation {
    public static String describe(String currentMockMaker) {
        String mockMakerName = MockUtil.getMockMaker(currentMockMaker).getName();
        return join("mock-maker         : " + mockMakerName);
    }
}
