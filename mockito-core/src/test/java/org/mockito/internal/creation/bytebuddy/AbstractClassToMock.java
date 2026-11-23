/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import java.util.function.IntSupplier;

public abstract class AbstractClassToMock implements IntSupplier {
    private final int value;

    public AbstractClassToMock(int value) {
        this.value = value;
    }

    @Override
    public final int getAsInt() {
        return value;
    }
}
