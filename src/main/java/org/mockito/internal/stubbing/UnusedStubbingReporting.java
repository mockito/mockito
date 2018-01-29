/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.quality.Strictness;
import org.mockito.stubbing.Stubbing;

/**
 * Helps determining if stubbing should be reported as unused
 */
public class UnusedStubbingReporting {

    /**
     * Decides if the stubbing should be reported as unused.
     * Lenient stubbings are not reported as unused.
     */
    public static boolean shouldBeReported(Stubbing stubbing) {
        return !stubbing.wasUsed() && stubbing.getStrictness() != Strictness.LENIENT;
    }
}
