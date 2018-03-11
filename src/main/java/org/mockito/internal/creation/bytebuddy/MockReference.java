/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.creation.bytebuddy;

import java.io.Serializable;

/**
 * To avoid memory leaks for certain implementations of MockMaker,
 * we need to use weak mock references internally in most cases.
 * See #1313
 */
public interface MockReference<T> extends Serializable {
    T get();
}
