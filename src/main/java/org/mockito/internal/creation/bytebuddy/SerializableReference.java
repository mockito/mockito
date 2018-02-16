/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.creation.bytebuddy;

import java.io.Serializable;

public interface SerializableReference<T> extends Serializable {
    T get();
}
