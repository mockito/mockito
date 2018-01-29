/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Stubbing;

/**
 * Helps determining the actual strictness given that it can be configured in multiple ways (at mock, at stubbing, in rule)
 */
public class StrictnessSelector {

    /**
     * Determines the actual strictness in the following importance order:
     * 1st - strictness configured when declaring stubbing;
     * 2nd - strictness configured at mock level;
     * 3rd - strictness configured at test level (rule, mockito session)
     *
     * @param stubbing stubbing to check for strictness. Null permitted.
     * @param mockSettings settings of the mock object, may or may not have strictness configured. Must not be null.
     * @param testLevelStrictness strictness configured using the test-level configuration (rule, mockito session). Null permitted.
     *
     * @return actual strictness, can be null.
     */
    public static Strictness determineStrictness(Stubbing stubbing, MockCreationSettings mockSettings, Strictness testLevelStrictness) {
        if (stubbing != null && stubbing.getStrictness() != null) {
            return stubbing.getStrictness();
        }

        if (mockSettings.isLenient()) {
            return Strictness.LENIENT;
        }

        return testLevelStrictness;
    }
}
