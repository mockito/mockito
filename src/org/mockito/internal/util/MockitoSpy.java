/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

/**
 * Mark internally a Mockito spy.
 *
 * To be used un conjunction with {@link MockUtil#isMock(Object)} or {@link MockUtil#isSpy(Object)}.
 */
public interface MockitoSpy extends MockitoMock {
}
