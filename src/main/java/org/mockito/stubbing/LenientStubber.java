/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.CheckReturnValue;
import org.mockito.Mockito;
import org.mockito.NotExtensible;

/**
 * Used for declaring optional stubbings with {@link Mockito#lenient()}
 */
@NotExtensible
public interface LenientStubber extends BaseStubber {

    /**
     * Allows declaring the method to stub. See {@link Mockito#when(Object)}.
     * Needed for classic stubbing with when().then()
     */
    @CheckReturnValue
    <T> OngoingStubbing<T> when(T methodCall);
}
