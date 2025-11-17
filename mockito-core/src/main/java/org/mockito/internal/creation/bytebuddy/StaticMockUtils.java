/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import org.mockito.StaticMockSettings;
import org.mockito.internal.creation.bytebuddy.access.MockMethodInterceptor;
import org.mockito.mock.MockCreationSettings;

class StaticMockUtils {

    private StaticMockUtils() {}

    public static boolean isStubbingInstanceMethods(MockMethodInterceptor interceptor) {
        MockCreationSettings<?> settings = interceptor.getMockHandler().getMockSettings();
        StaticMockSettings staticMockSettings = settings.getStaticMockSettings();
        if (staticMockSettings == null) {
            return false;
        }
        return staticMockSettings.stubInstanceMethods;
    }
}
