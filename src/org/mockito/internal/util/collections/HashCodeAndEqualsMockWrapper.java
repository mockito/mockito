/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import org.mockito.internal.util.MockUtil;

/**
 * hashCode and equals safe mock wrapper.
 *
 * <p>
 *     It doesn't use the actual mock {@link Object#hashCode} and {@link Object#equals} method as they might
 *     throw an NPE if those method cannot be stubbed <em>even internally</em>.
 * </p>
 *
 * <p>
 *     Instead the strategy is :
 *     <ul>
 *         <li>For hashCode : <strong>use {@link System#identityHashCode}</strong></li>
 *         <li>For equals : <strong>use the object reference equality</strong></li>
 *     </ul>
 * </p>
 *
 * @see HashCodeAndEqualsSafeSet
 */
public class HashCodeAndEqualsMockWrapper {

    private final Object mockInstance;

    public HashCodeAndEqualsMockWrapper(final Object mockInstance) {
        this.mockInstance = mockInstance;
    }

    public Object get() {
        return mockInstance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HashCodeAndEqualsMockWrapper)) {
            return false;
        }

        final HashCodeAndEqualsMockWrapper that = (HashCodeAndEqualsMockWrapper) o;

        return mockInstance == that.mockInstance;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(mockInstance);
    }

    public static HashCodeAndEqualsMockWrapper of(final Object mock) {
        return new HashCodeAndEqualsMockWrapper(mock);
    }

    @Override public String toString() {
        final MockUtil mockUtil = new MockUtil();
        return "HashCodeAndEqualsMockWrapper{" +
                "mockInstance=" + (mockUtil.isMock(mockInstance) ? mockUtil.getMockName(mockInstance) : typeInstanceString()) +
                '}';
    }

    private String typeInstanceString() {
        return mockInstance.getClass().getSimpleName() + "(" + System.identityHashCode(mockInstance) + ")";
    }
}
