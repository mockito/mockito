package org.mockito.internal.stubbing;

import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Stubbing;

public class StrictnessSelector {

    public static Strictness determineStrictness(Strictness currentStrictness, MockCreationSettings mockSettings, Stubbing stubbing) {
        if (stubbing != null && stubbing.getStrictness() != null) {
            return stubbing.getStrictness();
        }

        if (mockSettings.getStrictness() != null) {
            return mockSettings.getStrictness();
        }

        return currentStrictness;
    }
}
