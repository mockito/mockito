/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

//TODO document exposed interfaces
public interface OngoingStubbing<T> {

    void andReturn(T value);

    void andThrow(Throwable throwable);
}