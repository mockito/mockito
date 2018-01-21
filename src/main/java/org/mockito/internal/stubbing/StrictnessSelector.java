/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Stubbing;

public class StrictnessSelector {

    public static Strictness determineStrictness(Strictness currentStrictness, MockCreationSettings mockSettings, Stubbing stubbing) {
        if (stubbing != null && stubbing.getStrictness() != null) {
            return stubbing.getStrictness();
        }

        if (mockSettings.isLenient()) {
            return Strictness.LENIENT;
        }

        return currentStrictness;
    }
}
