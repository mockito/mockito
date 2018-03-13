/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation.mockref;

import java.io.ObjectStreamException;
import java.lang.ref.WeakReference;

/**
 * A weak reference that is converted into a strong reference when serialized.
 * See {@link MockReference}.
 */
public class MockWeakReference<T> implements MockReference<T> {

    private final WeakReference<T> ref;

    public MockWeakReference(T t) {
        this.ref = new WeakReference<T>(t);
    }

    private Object writeReplace() throws ObjectStreamException {
        return new MockStrongReference<T>(get(), true);
    }

    @Override
    public T get() {
        T ref = this.ref.get();

        if (ref == null) {
            throw new IllegalStateException("The mock object was garbage collected. " +
                "This should not happen in normal circumstances when using public API. " +
                "Typically, the test class keeps strong reference to the mock object " +
                "and it prevents getting the mock collected. Mockito internally needs " +
                "to keep weak references to mock objects to avoid memory leaks for " +
                "certain types of MockMaker implementations. If you see this exception " +
                "using Mockito public API, please file a bug. For more information see " +
                "issue #1313.");
        }

        return ref;
    }
}
