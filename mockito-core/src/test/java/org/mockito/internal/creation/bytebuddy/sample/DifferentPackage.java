/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy.sample;

import org.mockito.internal.creation.bytebuddy.InlineDelegateByteBuddyMockMakerTest;

public class DifferentPackage extends InlineDelegateByteBuddyMockMakerTest.SamePackage {

    public final Object p2;

    public DifferentPackage(Object p1, Object p2) {
        super(p1);
        this.p2 = p2;
    }
}
