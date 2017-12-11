/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.quality.Strictness;
import org.mockito.stubbing.Stubbing;

import static org.mockito.Mockito.mockingDetails;

public class StubbingStrictness {
    public static boolean isLenientStubbing(Stubbing stubbing) {
        Strictness mockStrictness = mockingDetails(stubbing.getInvocation().getMock()).getMockCreationSettings().getStrictness();
        return mockStrictness == Strictness.LENIENT;
    }
}
