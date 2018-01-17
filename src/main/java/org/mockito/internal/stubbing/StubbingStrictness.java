/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.quality.Strictness;
import org.mockito.stubbing.Stubbing;

import static org.mockito.Mockito.mockingDetails;

public class StubbingStrictness {
    //TODO 792 - this does not seem correct - we don't check the leniency of the stubbing itself, only mock-level settings
    //we should also check if we can pass mock creation settings or mocking details directly to avoid 'mockingDetails()' method overhead
    public static boolean isLenientStubbing(Stubbing stubbing) {
        Strictness mockStrictness = mockingDetails(stubbing.getInvocation().getMock()).getMockCreationSettings().getStrictness();
        return mockStrictness == Strictness.LENIENT;
    }
}
